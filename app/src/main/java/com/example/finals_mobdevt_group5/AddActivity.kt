package com.example.finals_mobdevt_group5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.example.finals_mobdevt_group5.databinding.ActivityAddBinding
import com.example.finals_mobdevt_group5.databinding.ActivityRecyclerViewBinding
import com.example.finals_mobdevt_group5.model.Recipe
import com.example.finals_mobdevt_group5.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddActivity : AppCompatActivity() {


    //binding
    private lateinit var binding: ActivityAddBinding

    //auth
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    //view
    private val toolbar by lazy{ binding.toolbar}
    private val bottomNavigation by lazy{binding.bottomNavigation}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewModel by viewModels<MainViewModel>()

        db = FirebaseFirestore.getInstance()
        auth =  FirebaseAuth.getInstance()

        viewModel.username.observe(this){loadUserName(it)}

        setSupportActionBar(toolbar)


        bottomNavigation.setSelectedItemId(R.id.addBtn)
        bottomNavigation.setOnItemSelectedListener { onNavigationItemSelected(it) }


        binding.button2.setOnClickListener {

            if(!binding.recipeText.text.toString().isNullOrEmpty() && !binding.cookTxtBox.text.toString().isNullOrEmpty()
                && !binding.InstructionsTxtBox.text.toString().isNullOrEmpty() && !binding.shortDescriptionTxtBox.text.toString().isNullOrEmpty()
                && !binding.FoodImageTxtBox.text.toString().isNullOrEmpty())
            {
                viewModel.addActivity(binding.recipeText.text.toString(), toolbar.subtitle.toString(),
                    binding.cookTxtBox.text.toString(), binding.IngredientsTxtBox.text.toString(),
                    binding.InstructionsTxtBox.text.toString(),
                    binding.shortDescriptionTxtBox.text.toString(),
                    binding.FoodImageTxtBox.text.toString(),
                    this)
                val intent = Intent(this, RecyclerView::class.java)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this,"Fill in all textboxes", Toast.LENGTH_SHORT).show()
            }


        }
    }

    fun loadUserName(it:String)
    {
        toolbar.subtitle = it
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return  super.onCreateOptionsMenu(menu)

    }

    fun onNavigationItemSelected(item: MenuItem): Boolean{
        if(item.itemId==R.id.homeBtn)
        {
            val intent = Intent(this, RecyclerView::class.java)
            startActivity(intent)
            return true
        }
        else if(item.itemId==R.id.profileBtn)
        {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            return true
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        bottomNavigation.setSelectedItemId(R.id.addBtn)
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
        bottomNavigation.setSelectedItemId(R.id.addBtn)
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