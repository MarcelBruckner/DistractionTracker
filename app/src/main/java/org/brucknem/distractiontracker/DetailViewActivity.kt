package org.brucknem.distractiontracker

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: called")

        setContentView(R.layout.activity_detail_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getIncomingIntent()
    }

    private fun getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents")
        if(intent.hasExtra("image") && intent.hasExtra("image_name")){
            Log.d(TAG, "getIncomingIntent: found intent extras")

            val image = intent.getStringExtra("image")
            val imageName = intent.getStringExtra("image_name")

            image?.let { imageName?.let { it1 -> setImage(image = it, imageName = it1) } }

        }
    }

    private fun setImage(image: String, imageName: String){
        Log.d(TAG, "setImage: setting $imageName - $image")

        findViewById<TextView>(R.id.image_description).text = imageName

        Glide.with(this)
            .asBitmap()
            .load(image)
            .into(findViewById(R.id.detail_image))
    }

    companion object {
        private const val TAG = "DetailView"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}