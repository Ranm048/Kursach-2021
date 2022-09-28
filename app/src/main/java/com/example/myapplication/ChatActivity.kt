package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var  messageRecyclerView: RecyclerView
    private lateinit var  messageBox: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var  msgAdapter : MessageAdapter
    private lateinit var  msgList: ArrayList<Message>
    private lateinit var  mDbRef : DatabaseReference

    var recieverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val recieveruid = intent.getStringExtra("uid")
        val senderuid = FirebaseAuth.getInstance().currentUser?.uid

        mDbRef = FirebaseDatabase.getInstance("https://kursach3d-default-rtdb.europe-west1.firebasedatabase.app/").getReference()

        senderRoom = recieveruid + senderuid
        recieverRoom = senderuid + recieveruid


        supportActionBar?.title = name

        messageRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messagebox)
        sendBtn = findViewById(R.id.sendbtn)
        msgList = ArrayList()
        msgAdapter = MessageAdapter(this,msgList)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = msgAdapter


        mDbRef.child("Interaction").child(senderRoom!!).child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    msgList.clear()

                    for(postSnapshot in snapshot.children){
                      val message = postSnapshot.getValue(Message::class.java)
                      msgList.add(message!!)
                  }
                    msgAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })



        sendBtn.setOnClickListener{
            val message = messageBox.text.toString()
            val messageObject = Message(message, senderuid)
            mDbRef.child("Interaction").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("Interaction").child(recieverRoom!!).child("messages").push()
                        .setValue(messageObject)

                }



        }
    }
}