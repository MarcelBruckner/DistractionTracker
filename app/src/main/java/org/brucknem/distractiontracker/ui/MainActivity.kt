package org.brucknem.distractiontracker.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import org.brucknem.distractiontracker.R
import org.brucknem.distractiontracker.data.Entry
import org.brucknem.distractiontracker.databinding.ActivityMainBinding
import org.brucknem.distractiontracker.util.InjectorUtils
import org.brucknem.distractiontracker.util.UserProvider
import org.brucknem.distractiontracker.viewmodel.EntriesViewModel

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnEntryClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var user: FirebaseUser
    private lateinit var viewModel: EntriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: started")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = UserProvider.checkUserLoggedIn(this) ?: return
        viewModel =
            ViewModelProvider(this, InjectorUtils.provideFirebaseEntriesViewModelFactory(user)).get(
                EntriesViewModel::class.java
            )
        initializeUI()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.reloadDatabase()
            binding.swipeRefresh.isRefreshing = false
        }

        binding.floatingActionButton.setOnClickListener {
            switchToDetail(-1)
        }
    }

    private fun initializeUI() {
        viewModel.getEntries().observe(this, {
            initRecyclerView(entries = it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reload -> {
                viewModel.reloadDatabase()
                binding.swipeRefresh.isRefreshing = false
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

        initializeUI()
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

    override fun onClick(entryId: Long) {
        Log.d(TAG, "onClick: clicked $entryId")
        switchToDetail(entryId)
    }

    private fun switchToDetail(entryId: Long) {
        val intent: Intent = Intent(this, DetailViewActivity::class.java).apply {
            putExtra("entryId", entryId)
        }
        startActivity(intent)
    }
}