package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class CryptoActivity : AppCompatActivity() {
    private lateinit var BroadcastBtn : Button
    private lateinit var GetTestedBtn : Button
    private lateinit var Viewdb : Button
    private lateinit var  messageBox: EditText
    private lateinit var  mDbRef : DatabaseReference
    private var  myUid : String? = null
    private val p = BigInteger("2988348162058574136915891421498819466320163312926952423791023078707")
    //val g = BigInteger("2998348162058574136915891421498819466320163312926952432791023078703")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypto)
        BroadcastBtn = findViewById(R.id.btnBroadcast)
        GetTestedBtn = findViewById(R.id.btnGetTested)
        Viewdb = findViewById(R.id.btnViewdb)
        mDbRef = FirebaseDatabase.getInstance("https://kursach3d-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
        messageBox = findViewById(R.id.message)
        myUid = FirebaseAuth.getInstance().currentUser?.uid
        //Toast.makeText(applicationContext, "$x", Toast.LENGTH_SHORT).show()
        mDbRef.child("Notify")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(postSnapshot in snapshot.children){
                        val msg = postSnapshot.getValue(Message::class.java)
                        if (msg != null){
                            Toast.makeText(applicationContext, "Новое сообщение : ${msg.message.toString()}", Toast.LENGTH_SHORT).show()
                            signAndPost(msg.message.toString())
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        GetTestedBtn.setOnClickListener{
            mDbRef.child("user").child(myUid.toString()).child("SecretKey").get().addOnSuccessListener {
                val intent = Intent(this@CryptoActivity, Comunicaion1::class.java)
                intent.putExtra("secret", it.value.toString())
                startActivity(intent)
            }
        }


        BroadcastBtn.setOnClickListener{
            val message = messageBox.text.toString()
            if (message != "") {
                val name = FirebaseAuth.getInstance().currentUser?.uid
                val messageObject = Message(message, name)
                mDbRef.child("Notify").push().setValue(Message(message, name))
                mDbRef.child("Notify").removeValue()
                mDbRef.child("Interaction").child("messages").push().setValue(messageObject)
            } else Toast.makeText(applicationContext, "Введите сообщение", Toast.LENGTH_SHORT).show()
        }

        Viewdb.setOnClickListener{
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
        }



    }


    private fun signAndPost(messageToSign: String){
        mDbRef.child("user").child(myUid.toString()).child("SecretKey").get().addOnSuccessListener {
            val x = it.value.toString().toBigInteger()
            val md = MessageDigest.getInstance("MD5")
            val hashInBytes = md.digest(messageToSign.toByteArray(StandardCharsets.UTF_8))
            val sb = StringBuilder()
            for (b in hashInBytes) {
                sb.append(String.format("%02x", b))
            }
            val m = BigInteger(Math.abs(sb.toString().hashCode()).toString())
            val messageObject = Message(m.modPow(x,p).toString(), myUid.toString(), m.toString(), true)
            mDbRef.child("Interaction").child("messages").push().setValue(messageObject)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@CryptoActivity, login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}