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
import org.brucknem.distractiontracker.R
import org.brucknem.distractiontracker.data.Entry
import org.brucknem.distractiontracker.databinding.ActivityMainBinding
import org.brucknem.distractiontracker.util.InjectorUtils
import org.brucknem.distractiontracker.util.UserManager
import org.brucknem.distractiontracker.viewmodel.EntriesViewModel

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnEntryClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: EntriesViewModel

    private lateinit var loginIntent: Intent
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    private val loginLauncher = registerForActivityResult(
        UserManager.LoggedInResultContract()
    ) {
        viewModel.reloadDatabase()
        this.loginIfNecessary()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: started")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginIntent = Intent(this, SignInActivity::class.java)

        viewModel =
            ViewModelProvider(this, InjectorUtils.provideFirebaseEntriesViewModelFactory()).get(
                EntriesViewModel::class.java
            )
        binding.swipeRefresh.setOnRefreshListener {
            reloadDatabase()
        }

        binding.floatingActionButton.setOnClickListener {
            switchToDetail(-1)
        }

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
                reloadDatabase()
                true
            }
            R.id.settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                true
            }
            R.id.show_all -> {
                recyclerViewAdapter.setEntriesToShow(RecyclerViewAdapter.EntriesToShow.ALL)
                true
            }
            R.id.show_internal_triggers -> {
                recyclerViewAdapter.setEntriesToShow(RecyclerViewAdapter.EntriesToShow.INTERNAL_TRIGGER)
                true
            }
            R.id.show_external_triggers -> {
                recyclerViewAdapter.setEntriesToShow(RecyclerViewAdapter.EntriesToShow.EXTERNAL_TRIGGER)
                true
            }
            R.id.show_planning_problems -> {
                recyclerViewAdapter.setEntriesToShow(RecyclerViewAdapter.EntriesToShow.PLANNING_PROBLEM)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun reloadDatabase() {
        if (loginIfNecessary()) {
            return
        }
        viewModel.reloadDatabase()
        binding.swipeRefresh.isRefreshing = false
    }

    private fun loginIfNecessary(): Boolean {
        if (UserManager.getCurrentUser() == null) {
            loginLauncher.launch(loginIntent)
            return true
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        loginIfNecessary()
    }


    private fun initRecyclerView(
        entries: List<Entry>,
    ) {
        Log.d(TAG, "initRecyclerView: init recycler view")

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerViewAdapter = RecyclerViewAdapter(
            context = this,
            entries = entries,
            entriesToShow = RecyclerViewAdapter.EntriesToShow.ALL,
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