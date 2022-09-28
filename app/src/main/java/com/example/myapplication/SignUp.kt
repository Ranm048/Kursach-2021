package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.math.BigInteger
import java.util.Random


class SignUp : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    val p = BigInteger("2988348162058574136915891421498819466320163312926952423791023078707")
    val g = BigInteger("2998348162058574136915891421498819466320163312926952432791023078703")
    val x = BigInteger(221,Random())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        edtName = findViewById(R.id.edtname)
        edtEmail = findViewById(R.id.email)
        edtPassword = findViewById(R.id.passwd)
        btnSignup = findViewById(R.id.btnsignup)

        btnSignup.setOnClickListener{
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            signUp(name, email, password)
        }
    }
    private fun signUp(name: String, email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDataBase(name,email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@SignUp, MainMenu::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignUp, "Error signing up", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addUserToDataBase(name: String, email: String, uid: String){
        mDbRef = FirebaseDatabase.getInstance("https://kursach3d-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
        mDbRef.child("user").child(uid).child("SecretKey").setValue(x.toString())
        mDbRef.child("user").child(uid).child("PublicKey").setValue(g.modPow(x, p).toString())
    }
}