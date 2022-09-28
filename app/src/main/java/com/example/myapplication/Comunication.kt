package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import java.math.BigInteger
import java.util.*

class Comunication : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comunication)
        val p = BigInteger("2988348162058574136915891421498819466320163312926952423791023078707")
        val g = BigInteger("2998348162058574136915891421498819466320163312926952432791023078703")
        var  mDbRef : DatabaseReference = FirebaseDatabase.getInstance("https://kursach3d-default-rtdb.europe-west1.firebasedatabase.app/").getReference()

        val senderId = intent.getStringExtra("senderId")
        val m = BigInteger(intent.getStringExtra("message"))
        Toast.makeText(this, "Check ${m} by ${senderId}", Toast.LENGTH_SHORT).show()
        var a = BigInteger(221, Random())
        var b = BigInteger(221,Random())
        var m1 = (m.modPow(a, p).multiply(g.modPow(b,p))).mod(p)
        var h1 = BigInteger("1")
        var h2 : BigInteger
        var q : BigInteger
        var verif = Verification(m.toString(),(m.modPow(a, p).multiply(g.modPow(b,p))).mod(p).toString())
        mDbRef.child("Interaction").child("Verification").child(senderId.toString()).child("1").setValue(verif)
        mDbRef.child("Interaction").child("Verification").child(senderId.toString()).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Toast.makeText(this@Comunication, "Есть контакт", Toast.LENGTH_SHORT).show()
                var s1 = snapshot.child("1").getValue(Verification::class.java)
                if (s1 != null){
                h1 = BigInteger(s1.a)
                h2 = BigInteger(s1.b)
                }
                if (snapshot.child("2").value == null) mDbRef.child("Interaction").child("Verification")
                    .child(senderId.toString()).child("3")
                    .setValue(Verification(a.toString(), b.toString()))
                if (snapshot.child("4").value != null){var s2 = snapshot.child("4").getValue(Verification::class.java)
                if (s2 != null)q = BigInteger(s2.a)
                    if (h1 == ((m.modPow(a, p).multiply(g.modPow(b, p))).rem(p))) {
                    Toast.makeText(this@Comunication,
                        "End of communication",
                        Toast.LENGTH_SHORT).show()

                    mDbRef.child("Interaction").child("Verification").child(senderId.toString()).removeValue()
                    mDbRef.child("Interaction").child("Verification")
                        .child(senderId.toString())
                        .removeEventListener(this)
                 }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })


    }
}