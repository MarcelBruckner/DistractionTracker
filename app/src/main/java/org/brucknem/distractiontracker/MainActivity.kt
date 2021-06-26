package org.brucknem.distractiontracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnClickListener {

    private var entries: ArrayList<Entry> = ArrayList()
    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: started")

        mockEntries()
        initRecyclerView()
    }

    private fun mockEntries() {
        for (i in 1..5) {
            entries.add(Entry(12345, "yeet", "good", true, "Yes", "More yeet"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                true
            }
            R.id.add_entry -> {
                val addEntryIntent = Intent(this, AddEntryActivity::class.java)
                startActivity(addEntryIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
        }
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
//            putExtra("entry", entries[postition])
        }
        startActivity(intent)
    }

}