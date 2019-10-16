package com.company.kshukogramm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.company.kshukogramm.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {
    private val TAG = "EditProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Log.d(TAG,"onCreate")

        close_image.setOnClickListener {
            finish()
        }

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    Log.d(TAG, "connected")
                } else {
                    Log.d(TAG, "not connected")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Listener was cancelled")
            }
        })

        var database = FirebaseDatabase.getInstance().reference
        database.child("users")
            .child(user!!.uid)
            .addListenerForSingleValueEvent(
            object: ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    val user = data.getValue(User::class.java)
                    name_input.setText(user!!.name, TextView.BufferType.EDITABLE)
                    username_input.setText(user.username, TextView.BufferType.EDITABLE)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ", error.toException())
                }
            })

    }
}
