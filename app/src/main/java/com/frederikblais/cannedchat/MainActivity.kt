package com.frederikblais.cannedchat

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // DB
        val db = Firebase.firestore

        // set onclick listener
        findViewById<android.widget.Button>(R.id.testButton).setOnClickListener {
            getConnections()
        }
    }

    private fun getConnections() {

        // start timer
        val startTime = System.currentTimeMillis()

        val connections = hashMapOf(
            // get current time
            "time" to startTime,
            "code" to 200,
        )

        val db = Firebase.firestore
        db.collection("connections")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        // push to db
        db.collection("connections").document(startTime.toString())
            .set(connections)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        // end timer
        val endTime = System.currentTimeMillis()
        val totalTime = endTime - startTime

        // add time to connections
        connections["time"] = totalTime

        // push toast with totaltime
        Toast.makeText(this, "Total time: $totalTime", Toast.LENGTH_SHORT).show()
    }
}