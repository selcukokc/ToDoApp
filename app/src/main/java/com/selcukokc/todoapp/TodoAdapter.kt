package com.selcukokc.todoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TodoAdapter (private val mContext: Context, private val todoList: List<Todo>) : RecyclerView.Adapter<TodoAdapter.CardViewHolder>() {



    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var cardView: CardView
        var txtTitleCard: TextView
        var txtDescCard: TextView
        var deleteButton: ImageButton

        init{
            cardView=view.findViewById(R.id.cardView)
            txtTitleCard=view.findViewById(R.id.txtTitleCard)
            txtDescCard=view.findViewById(R.id.txtDescCard)
            deleteButton=view.findViewById(R.id.deleteButton)
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_design,null,false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val todoObj = todoList[position]
        var fStore: FirebaseFirestore
        var fAuth: FirebaseAuth

        holder.txtTitleCard.setText(todoObj.title)
        holder.txtDescCard.setText(todoObj.description)

        holder.deleteButton.setOnClickListener(View.OnClickListener {

            fAuth = FirebaseAuth.getInstance()
            fStore = FirebaseFirestore.getInstance()
            val userID=fAuth.currentUser.uid

            fStore.collection("users").document(userID).collection("todo").document(todoList.get(position).id)
                    .delete().addOnSuccessListener(OnSuccessListener {
                        (mContext as MainActivity).recreate()  //in order to refresh
                    })


        })

        holder.cardView.setOnClickListener(View.OnClickListener {

            val alertdesign = (mContext as MainActivity).layoutInflater.inflate(R.layout.alertview_design,null)
            val editTextTitle = alertdesign.findViewById(R.id.alertTitle) as EditText
            val editTextDescription = alertdesign.findViewById(R.id.alertDescription) as EditText

            val av = AlertDialog.Builder(mContext)
            av.setTitle("EDIT")
            av.setView(alertdesign)
            editTextTitle.setText(holder.txtTitleCard.text.toString())
            editTextDescription.setText(holder.txtDescCard.text.toString())

            av.setPositiveButton("Save"){DialogInterface, i->
                fAuth = FirebaseAuth.getInstance()
                fStore = FirebaseFirestore.getInstance()
                var strTitle = editTextTitle.text.toString()
                var strDesc = editTextDescription.text.toString()
                val userID=fAuth.currentUser.uid
                fStore.collection("users").document(userID).collection("todo").document(todoList.get(position).id)
                        .update("title",strTitle,"description",strDesc).addOnSuccessListener(OnSuccessListener {
                            (mContext as MainActivity).recreate()
                        })

            }

            av.setNegativeButton("Cancel"){DialogInterface, i->


            }

            av.create().show()
        })



    }




}