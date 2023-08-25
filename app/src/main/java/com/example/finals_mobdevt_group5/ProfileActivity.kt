package com.example.finals_mobdevt_group5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.example.finals_mobdevt_group5.databinding.ActivityProfileBinding
import com.example.finals_mobdevt_group5.databinding.ActivityRecyclerViewBinding
import com.example.finals_mobdevt_group5.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    //binding
    private lateinit var binding: ActivityProfileBinding

    //firebase
    private lateinit var auth: FirebaseAuth

    private val bottomNavigation by lazy{binding.bottomNavigation}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<MainViewModel>()
        viewModel.username.observe(this){loadUserName(it)}


        bottomNavigation.setSelectedItemId(R.id.profileBtn)
        bottomNavigation.setOnItemSelectedListener { onNavigationItemSelected(it) }

        binding.button2.setOnClickListener {
            viewModel.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    fun loadUserName(it:String)
    {
        binding.recipeNameTxtDetails.setText(it)
    }

    fun onNavigationItemSelected(item: MenuItem): Boolean{
        if(item.itemId==R.id.homeBtn)
        {
            val intent = Intent(this, RecyclerView::class.java)
            startActivity(intent)
            return true
        }
        else if(item.itemId==R.id.addBtn)
        {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
            return true
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        bottomNavigation.setSelectedItemId(R.id.profileBtn)
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

    override fun onPause() {
        super.onPause()
        bottomNavigation.setSelectedItemId(R.id.profileBtn)
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