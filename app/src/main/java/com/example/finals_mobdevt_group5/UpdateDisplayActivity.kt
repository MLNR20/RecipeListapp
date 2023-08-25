package com.example.finals_mobdevt_group5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.example.finals_mobdevt_group5.databinding.ActivityDeleteDisplayBinding
import com.example.finals_mobdevt_group5.databinding.ActivityUpdateDisplayBinding
import com.example.finals_mobdevt_group5.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UpdateDisplayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateDisplayBinding
    private lateinit var auth: FirebaseAuth

    private val toolbar by lazy{ binding.toolbar}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        setSupportActionBar(toolbar)
        val viewModel by viewModels<MainViewModel>()
        viewModel.username.observe(this){loadUserName(it)}


        binding.button2.setOnClickListener {
            val intent = Intent(this, RecyclerView::class.java)
            startActivity(intent)
        }
    }

    fun loadUserName(it:String)
    {
        toolbar.subtitle = it
    }

    override fun onStart() {
        super.onStart()
        auth =  FirebaseAuth.getInstance()
        val viewModel by viewModels<MainViewModel>()

        if(auth.currentUser!=null)
        {
            viewModel.username.observe(this){loadUserName(it)}
        }
        else
        {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return  super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logoutbtn)
        {
            val viewModel by viewModels<MainViewModel>()
            viewModel.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "User successfully logged out", Toast.LENGTH_SHORT).show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onPause() {
        super.onPause()
        auth =  FirebaseAuth.getInstance()
        val viewModel by viewModels<MainViewModel>()

        if(auth!=null)
        {
            viewModel.username.observe(this){loadUserName(it)}
        }
        else
        {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}