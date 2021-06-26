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

    private var images: ArrayList<String> = ArrayList()
    private var imageNames: ArrayList<String> = ArrayList()
    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: started")

        initImageBitmaps()
        initRecyclerView()
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

    private fun initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps")

        images.add("https://cdn2.thecatapi.com/images/1u8.jpg")
        images.add("https://cdn2.thecatapi.com/images/5hq.jpg")
        images.add("https://cdn2.thecatapi.com/images/1ql.jpg")
        images.add("https://cdn2.thecatapi.com/images/fdLoQnXHG.jpg")
        images.add("https://cdn2.thecatapi.com/images/9DduryW4F.jpg")
        images.add("https://cdn2.thecatapi.com/images/c2r.jpg")
        images.add("https://cdn2.thecatapi.com/images/MTgxOTM1NA.jpg")

        for (i in 1..images.size) {
            imageNames.add("Cat No. $i")
        }
    }

    private fun initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycler view")

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val recyclerViewAdapter = RecyclerViewAdapter(
            context = this,
            images = this.images,
            imageNames = this.imageNames,
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
            putExtra("image", images[postition])
            putExtra("image_name", imageNames[postition])
        }
        startActivity(intent)
    }

}