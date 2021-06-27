package org.brucknem.distractiontracker.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.brucknem.distractiontracker.R
import org.brucknem.distractiontracker.data.Entry
import org.brucknem.distractiontracker.databinding.ActivityMainBinding
import org.brucknem.distractiontracker.util.InjectorUtils
import org.brucknem.distractiontracker.viewmodel.EntriesViewModel
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnClickListener {

    private var entryIds: ArrayList<String> = ArrayList()
    private var entries: ArrayList<Entry> = ArrayList()
    private val db = Firebase.firestore

    //    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: started")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI()

//        swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
//        swipeRefresh.setOnRefreshListener {
//            fetchEntries()
//        }
//
//        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
//            val addEntryIntent = Intent(this, AddEntryActivity::class.java)
//            startActivity(addEntryIntent)
//        }
    }

    private fun initializeUI() {
        val viewModel = ViewModelProvider(this, InjectorUtils.provideEntriesViewModelFactory()).get(
            EntriesViewModel::class.java
        )

        viewModel.getEntries().observe(this, { entries ->
            initRecyclerView(entries)
        })
//        binding.buttonAddQuote.setOnClickListener {
//            val quote =
//                Quote(binding.editTextQuote.text.toString(), binding.editTextAuthor.text.toString())
//            viewModel.addQuote(quote)
//            binding.editTextQuote.setText("")
//            binding.editTextAuthor.setText("")
//        }
    }

    private fun fetchEntries() {
        val user = checkUserLoggedIn() ?: return

//        swipeRefresh.isRefreshing = true

        db.collection("users").document(user.uid)
            .collection("entries").get()
            .addOnSuccessListener { result ->
                val resultMap: MutableMap<Long, Pair<String, Entry>> = mutableMapOf()
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val entry = Entry(document.data as Map<String, Any>)
                    resultMap[entry.datetime] = Pair(document.id, entry)
                }

                entries.clear()
                entryIds.clear()
                for (entry in resultMap.toSortedMap().asIterable().reversed()) {
                    entryIds.add(entry.value.first)
                    entries.add(entry.value.second)
                }

                initRecyclerView(entries)
//                swipeRefresh.isRefreshing = false
                Toast.makeText(this, "Successfully fetched entries", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting entries", exception)
//                swipeRefresh.isRefreshing = false
                Toast.makeText(this, "Error fetching entries", Toast.LENGTH_LONG).show()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reload -> {
                fetchEntries()
                true
            }
            R.id.settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onStart() {
        super.onStart()

//        fetchEntries()
    }

    private fun checkUserLoggedIn(): FirebaseUser? {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
        }
        return user
    }

    private fun initRecyclerView(entries: List<Entry>) {
        Log.d(TAG, "initRecyclerView: init recycler view")

        val recyclerView: RecyclerView = binding.recyclerView
        val recyclerViewAdapter = RecyclerViewAdapter(
            context = this,
            entries = entries,
            onClickListener = this
        )
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onClick(position: Int) {
        Log.d(TAG, "onClick: clicked $position")

        val intent: Intent = Intent(this, DetailViewActivity::class.java).apply {
            putExtra("entryPosition", position)
        }
        startActivity(intent)
    }
}