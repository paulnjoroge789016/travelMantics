package com.example.travelmantics

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_admin.*
class AdminActivity : AppCompatActivity() {
     private lateinit var firebaseDatabase : FirebaseDatabase
     private lateinit var firebaseRef: DatabaseReference
     private lateinit var firebasseStorage: FirebaseStorage
     lateinit var storageRef: StorageReference
     lateinit var deal: TravelDeal
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val intent = intent
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDatabase.reference.child("TravelDeals")
        firebasseStorage = FirebaseStorage.getInstance()
        storageRef = firebasseStorage.reference.child("travel_deals_images")
        deal= TravelDeal()
        if(intent.extras != null) {
            val travelDeal  = intent.getParcelableExtra<TravelDeal>("deal")
            townEditText.setText(travelDeal.town)
            costEditText.setText(travelDeal.cost)
            descrioptionEdtText.setText(travelDeal.description)
            val imgUrl: String = travelDeal.imageUrl
            Picasso.get().load(imgUrl).into(dealImageView)
        }
            selectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/jpeg")
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                startActivityForResult(Intent.createChooser( intent, "insert picture"), 60)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val  menuInflaterMenu = MenuInflater(this)
        menuInflaterMenu.inflate(R.menu.mymenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.save -> {
               deal.town = townEditText.text.toString()
                deal.cost=costEditText.text.toString()
                deal.description = descrioptionEdtText.text.toString()
                firebaseRef.push().setValue(deal)
                Toast.makeText(this, "First message sent to firebase", Toast.LENGTH_SHORT).show()
                cleanText()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun cleanText() {
        townEditText.setText("")
        costEditText.setText("")
        descrioptionEdtText.setText("")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if(requestCode == 60 && resultCode == RESULT_OK)
         {
             val imageUri = data!!.data
             Picasso.get().load(imageUri).into(dealImageView)
             val storage = storageRef.child(imageUri.lastPathSegment)
             storage.putFile(imageUri).addOnSuccessListener(object:OnSuccessListener<UploadTask.TaskSnapshot> {
                 override fun onSuccess(taskSnapshot:UploadTask.TaskSnapshot) {
                     storage.getDownloadUrl().addOnSuccessListener(object:OnSuccessListener<Uri> {
                         override fun onSuccess(uri:Uri) {
                          deal.imageUrl = uri.toString()
                         }
                     })
                 }
             })

         }
     }

 }
