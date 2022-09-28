package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.math.BigInteger
import java.util.*

class Comunicaion1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comunicaion1)
        val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val p = BigInteger("2988348162058574136915891421498819466320163312926952423791023078707")
        val g = BigInteger("2998348162058574136915891421498819466320163312926952432791023078703")
        var q = BigInteger(221, Random())
        var  mDbRef : DatabaseReference = FirebaseDatabase.getInstance("https://kursach3d-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
        var m : BigInteger
        var m1 : BigInteger
        var a :BigInteger
        var b : BigInteger
        var x = BigInteger(intent.getStringExtra("secret"))
        mDbRef.child("Interaction").child("Verification").child(myUid).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                if (snapshot.child("1").value != null){
                    Toast.makeText(this@Comunicaion1, "Verifying..., ${snapshot.child("1").child("a").value}", Toast.LENGTH_SHORT).show()
                        m = BigInteger(snapshot.child("1").child("a").value.toString())
                        m1 = BigInteger(snapshot.child("1").child("b").value.toString())

                    val h1 :BigInteger = (m1.multiply(g.modPow(q, p))).rem(p)
                    val h2 :BigInteger = h1.modPow(x, p)
                    mDbRef.child("Interaction").child("Verification").child(myUid).child("2").setValue(Verification(h1.toString(),h2.toString()))
                    if (m1 == (m.modPow(h1,p).multiply(g.modPow(h2, p))).rem(p))
                    {
                        mDbRef.child("Interaction").child("Verification").child(myUid).child("4").setValue(Verification(q.toString(),"0"))
                        Toast.makeText(this@Comunicaion1, "Succesfull check", Toast.LENGTH_SHORT).show()
                        mDbRef.child("Interaction").child("Verification").child(myUid).removeEventListener(this)
                        mDbRef.child("Interaction").child("Verification").child(myUid).removeValue()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}