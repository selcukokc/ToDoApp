package com.selcukokc.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etMail
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_register.*


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnRegister.setOnClickListener(View.OnClickListener {
            intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        })

        btnLogin.setOnClickListener {
            if(TextUtils.isEmpty(etMail.text.toString().trim{it <= ' '}) || TextUtils.isEmpty(etPassword.text.toString().trim {it <= ' '})){
                Toast.makeText(this@LoginActivity,"Please enter e-mail and password", Toast.LENGTH_LONG).show()
            }
            else {

                val email = etMail.text.toString().trim{it <= ' '}
                val password = etPassword.text.toString().trim {it <= ' '}

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener( OnCompleteListener<AuthResult>
                    { task ->

                        if(task.isSuccessful){
                            val firebaseUser : FirebaseUser = task.result!!.user!!
                            Toast.makeText(this@LoginActivity, "You are logged in successfully.",
                                Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            //we cleaned out back stack
                            startActivity(intent)

                        } else {
                            Toast.makeText(this@LoginActivity,task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT).show()
                        }


                    }

                    )

            }


        }

    }
}