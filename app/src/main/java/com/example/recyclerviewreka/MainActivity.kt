package com.example.recyclerviewreka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewreka.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {

    private lateinit var wisataRecyclerview: RecyclerView
    private lateinit var wisataList: MutableList<Image>
    private lateinit var wisataAdapter: ImageAdapter
    private lateinit var binding: ActivityMainBinding

    private var mStorage: FirebaseStorage? = null
    private var mDatabaseRef: DatabaseReference? = null
    private var mDBListener: ValueEventListener? = null

    private lateinit var auth: FirebaseAuth
    private var gridLayoutManager : GridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        wisataRecyclerview = findViewById(R.id.imageRecyclerView)
        gridLayoutManager = GridLayoutManager(applicationContext, 3,
            LinearLayoutManager.VERTICAL, false)
        wisataList = ArrayList()
        wisataAdapter = ImageAdapter(this@MainActivity,wisataList)
        wisataRecyclerview.layoutManager = gridLayoutManager
        wisataRecyclerview.setHasFixedSize(true)
        wisataRecyclerview.adapter = wisataAdapter

        mStorage = FirebaseStorage.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("wisata")
        mDBListener = mDatabaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                wisataList.clear()
                if (snapshot.exists()) {
                    for (teacherSnapshot in snapshot.children) {
                        val upload = teacherSnapshot.getValue(Image::class.java)
                        upload!!.key = teacherSnapshot.key
                        wisataList.add(upload)
                    }
                    wisataAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                Intent(this, LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        }
        return true
    }
}