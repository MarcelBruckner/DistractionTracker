package org.brucknem.distractiontracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnClickListener {

    private var entryIds: ArrayList<String> = ArrayList()
    private var entries: ArrayList<Entry> = ArrayList()
    private val db = Firebase.firestore
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: started")

        swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        swipeRefresh.setOnRefreshListener {
            fetchEntries()
        }

        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val addEntryIntent = Intent(this, AddEntryActivity::class.java)
            startActivity(addEntryIntent)
        }
    }

    private fun fetchEntries() {
        val user = checkUserLoggedIn() ?: return

        swipeRefresh.isRefreshing = true

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

                initRecyclerView()
                swipeRefresh.isRefreshing = false
                Toast.makeText(this, "Successfully fetched entries", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting entries", exception)
                swipeRefresh.isRefreshing = false
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

        fetchEntries()
    }

    private fun checkUserLoggedIn(): FirebaseUser? {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
        }
        return user
    }

    private fun initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycler view")

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
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

    override fun onClick(postition: Int) {
        Log.d(TAG, "onClick: clicked $postition")

        val intent: Intent = Intent(this, DetailViewActivity::class.java).apply {
            putExtra("entry", entries[postition])
            putExtra("entryId", entryIds[postition])
        }
        startActivity(intent)
    }
}