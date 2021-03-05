package com.selcukokc.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtGoToLogin.setOnClickListener {
            onBackPressed()
        }

        bRegister.setOnClickListener {
            if(TextUtils.isEmpty(etMail.text.toString().trim{it <= ' '}) || TextUtils.isEmpty(etPassword.text.toString().trim {it <= ' '})){
                Toast.makeText(this@RegisterActivity,"Please enter e-mail and password",Toast.LENGTH_LONG).show()
            }
            else {

                val email = etMail.text.toString().trim{it <= ' '}
                val password = etPassword.text.toString().trim {it <= ' '}

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener( OnCompleteListener<AuthResult>
                        { task ->

                            if(task.isSuccessful){
                                val firebaseUser : FirebaseUser = task.result!!.user!!
                                Toast.makeText(this@RegisterActivity, "You were registered successfully.",
                                    Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                //we cleaned out back stack
                                intent.putExtra("user_id",firebaseUser.uid)
                                intent.putExtra("email_id",email)
                                startActivity(intent)

                            } else {
                                Toast.makeText(this@RegisterActivity,task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT).show()
                            }


                        }

                    )

            }


        }



    }
}