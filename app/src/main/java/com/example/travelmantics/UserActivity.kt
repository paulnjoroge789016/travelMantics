package com.example.travelmantics

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebStorage
import android.widget.NumberPicker
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    lateinit var travelDeal: TravelDeal
    lateinit var travelDeals: ArrayList<TravelDeal>
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var firebaseRef: DatabaseReference
    lateinit var firebaseEventListener: ChildEventListener
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseAuthListener: FirebaseAuth.AuthStateListener
    lateinit var recyclerView: RecyclerView
    public var isAdmin = false
    lateinit var currrentUser: String
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        if(!isAdmin) {
            myFab.visibility = GONE
        }
        travelDeals = ArrayList()
        recyclerView = findViewById<RecyclerView>(R.id.myRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDatabase.getReference().child("TravelDeals")
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuthListener = object : FirebaseAuth.AuthStateListener {

            override fun onAuthStateChanged(p0: FirebaseAuth) {
                if(p0.currentUser == null) {
                    val providers = arrayListOf(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    )

                    // Create and launch sign-in intent
                    val RC_SIGN_IN = 123
                    startActivityForResult(
                        AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                        RC_SIGN_IN
                    )
                }
                else{
                    currrentUser = firebaseAuth.uid.toString()
                    checkAdmin(currrentUser)
                }

                Toast.makeText(this@UserActivity, "Welcome Back", Toast.LENGTH_SHORT).show()
            }
        }
        currrentUser = firebaseAuth.uid.toString()
        checkAdmin(currrentUser)
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
        travelDeal = TravelDeal()
        travelDeals = ArrayList<TravelDeal>()
        firebaseEventListener = object: ValueEventListener,ChildEventListener{
            override fun onDataChange(p0: DataSnapshot) {
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                   travelDeal = p0.getValue(TravelDeal::class.java)!!
                   travelDeals.add(travelDeal)
                Log.d("this@paul", "data fetched")
                recyclerView.adapter = recyclerViewAdapter(travelDeals, this@UserActivity)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        firebaseRef.addChildEventListener(firebaseEventListener)
        val fab: FloatingActionButton = findViewById<FloatingActionButton>(R.id.myFab)
            myFab.setOnClickListener {
                startActivity(Intent(this, AdminActivity::class.java))
            }
    }

    private fun checkAdmin(currentUser: String) {
        isAdmin = false
        val dbReference =  firebaseDatabase.getReference().child("administrators")
        dbReference.addChildEventListener(object: ValueEventListener,ChildEventListener{
            override fun onDataChange(p0: DataSnapshot) {
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            @SuppressLint("RestrictedApi")
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if(p0.key == currentUser) {
                    isAdmin = true
                    Toast.makeText(this@UserActivity, "You are an admin", Toast.LENGTH_SHORT).show()
                    myFab.visibility = VISIBLE
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val  menuInflaterMenu: MenuInflater = MenuInflater(this)
        menuInflaterMenu.inflate(R.menu.logout, menu)
        return true
    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        if(!isAdmin) {
            myFab.visibility = GONE
        }
        recyclerView.adapter = recyclerViewAdapter(travelDeals, this@UserActivity)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.logout -> {
            AuthUI.getInstance().signOut(this).addOnCompleteListener {
                Toast.makeText(this, "The session has ended", Toast.LENGTH_SHORT).show()
                firebaseRef.addChildEventListener(firebaseEventListener)
            }
                firebaseRef.removeEventListener(firebaseEventListener)
                return true
        }

        }
        return true
    }


}
