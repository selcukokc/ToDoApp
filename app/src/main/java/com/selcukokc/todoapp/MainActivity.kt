package com.selcukokc.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    lateinit var todoAdapter: TodoAdapter
    lateinit var todoList: ArrayList<Todo>
    lateinit var fStore: FirebaseFirestore
    lateinit var fAuth: FirebaseAuth
    lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this@MainActivity)


        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        userID = fAuth.currentUser.uid


        loadData()


        btnAdd.setOnClickListener {
            val title = txtTitle.text.toString()
            val description = txtDesc.text.toString()
            insertData(title,description)
            txtTitle.setText("")
            txtDesc.setText("")
        }



    }

    fun loadData(){
        todoList = ArrayList<Todo>()
        fStore.collection("users").document(userID).collection("todo").get().addOnCompleteListener(
                OnCompleteListener {
                    for(doc:DocumentSnapshot in it.getResult()!!){
                        var todo = Todo(doc.getString("title")!!, doc.getString("description")!!,doc.id)
                        todoList.add(todo)
                    }
                   todoAdapter = TodoAdapter(this@MainActivity,todoList)
                    rv.setAdapter(todoAdapter)

                }).addOnFailureListener(OnFailureListener {
            Toast.makeText(this@MainActivity,it.message,Toast.LENGTH_SHORT).show()
        })

    }

    fun insertData(title: String, description: String){

        val id = UUID.randomUUID().toString()
        val documentReferance = fStore.collection("users").document(userID)
                .collection("todo").document(id)
        val info: MutableMap<String, Any> = HashMap()
        info["title"] = title
        info["description"] = description
        documentReferance.set(info).addOnSuccessListener(OnSuccessListener {
            loadData()
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemLogout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java) )
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }
}