package org.brucknem.distractiontracker.data

import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.brucknem.distractiontracker.R
import org.brucknem.distractiontracker.ui.DetailViewActivity
import org.brucknem.distractiontracker.ui.MainActivity


class FirebaseDao(
    private var user: FirebaseUser
) : DatabaseAccessObject() {

    private val db = Firebase.firestore

    init {
        fetchEntries()
        refresh()
    }

    private fun fetchEntries() {
        db.collection("users").document(user.uid)
            .collection("entries").get()
            .addOnSuccessListener { result ->
                entryList.clear()
                result.forEach {
                    Log.d(TAG, "${it.id} => ${it.data}")
                    entryList.add(Entry(it.data as Map<String, Any>))
                }
                refresh()
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting entries", exception)
            }
    }

    override fun addEntry(entry: Entry) {
        super.addEntry(entry)

        db.collection("users").document(user.uid)
            .collection("entries").document(entry.id.toString()).set(entry)
            .addOnSuccessListener {
                Log.d(
                    TAG, "Successfully added $entry with ID: ${entry.id}"
                )
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding $entry", e)
            }
    }

    override fun deleteEntry(entryId: Long) {
        super.deleteEntry(entryId)

        db.collection("users").document(user.uid)
            .collection("entries").document(entryId.toString())
            .delete()
            .addOnSuccessListener {
                Log.d(
                    TAG, "Successfully deleted $entryId"
                )
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting $entryId", e)
            }
    }

    companion object {
        private const val TAG = "FirebaseDao"
    }
}