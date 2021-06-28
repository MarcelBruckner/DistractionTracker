package org.brucknem.distractiontracker.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.brucknem.distractiontracker.util.UserManager


class FirebaseDao : DatabaseAccessObject() {

    private val db = Firebase.firestore

    init {
        reloadDatabase()
    }

    override fun reloadDatabase() {
        fetchEntries()
    }

    private fun fetchEntries() {
        val user = UserManager.getCurrentUser() ?: return

        db.collection("users").document(user.uid)
            .collection("entries").get()
            .addOnSuccessListener { result ->
                entryList.clear()
                result.forEach {
                    Log.d(TAG, "${it.id} => ${it.data}")
                    entryList.add(Entry(it.data as Map<String, Any>))
                }
                entryList.sortBy { it.datetime }
                entryList.reverse()
                refresh()
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting entries", exception)
            }
    }

    private fun uploadEntry(entry: Entry): Boolean {
        val user = UserManager.getCurrentUser() ?: return false

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
        return true
    }

    override fun addEntry(entry: Entry) {
        super.addEntry(entry)
        uploadEntry(entry)
    }

    override fun updateEntry(entry: Entry) {
        super.updateEntry(entry)
        uploadEntry(entry)
    }

    override fun deleteEntry(entryId: Long) {
        val user = UserManager.getCurrentUser() ?: return
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