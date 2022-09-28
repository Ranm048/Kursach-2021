package com.example.myapplication

import MsgAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CheckActivity : AppCompatActivity() {

    private lateinit var msgRecyclerView: RecyclerView
    private lateinit var msgList: ArrayList<Message>
    private lateinit var adapter: MsgAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)

        mAuth = FirebaseAuth.getInstance()
        mDbRef =
            FirebaseDatabase.getInstance("https://kursach3d-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()

        msgList = ArrayList()
        adapter = MsgAdapter(this, msgList)

        msgRecyclerView = findViewById(R.id.msgRecyclerView)

        msgRecyclerView.layoutManager = LinearLayoutManager(this)
        msgRecyclerView.adapter = adapter

        mDbRef.child("Interaction").child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    msgList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        msgList.add(message!!)
                    }

                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}