package org.brucknem.distractiontracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.brucknem.distractiontracker.databinding.ActivitySettingsBinding
import org.brucknem.distractiontracker.util.InjectorUtils
import org.brucknem.distractiontracker.util.UserManager
import org.brucknem.distractiontracker.viewmodel.EntriesViewModel

class SettingsActivity : AppCompatActivity(), UserManager.OnLoggedOutListener {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signOut.setOnClickListener {
            UserManager.logout(this, this)
        }

        binding.deleteAccount.setOnClickListener {
            UserManager.deleteAccount(this, this)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onLoggedOut() {
        val viewModel =
            ViewModelProvider(this, InjectorUtils.provideFirebaseEntriesViewModelFactory()).get(
                EntriesViewModel::class.java
            )
        viewModel.clear()
        finish()
    }

}