package com.example.finals_mobdevt_group5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.finals_mobdevt_group5.databinding.ActivityDetailsBinding
import com.example.finals_mobdevt_group5.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailsActivity : AppCompatActivity() {

    //binding
    private lateinit var binding: ActivityDetailsBinding

    //firebase
    private lateinit var auth: FirebaseAuth

    private val toolbar by lazy { binding.toolbar }
    private val time by lazy { binding.cooktimeTextView }
    private val deltBtn by lazy { binding.deleteButton }
    private val backBtn by lazy { binding.backButton }
    private val updateBtn by lazy { binding.updateButton }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<MainViewModel>()

        viewModel.username.observe(this) { loadUserName(it) }

        viewModel.retrieveData(intent.getStringExtra("recipeName").toString())
        viewModel.changeTime(this, intent.getStringExtra("cookTime").toString())
        viewModel.time.observe(this) {  changeText(it,binding.cooktimeTextView) }


        setSupportActionBar(toolbar)

        binding.recipeNameTxtDetails.text  = intent.getStringExtra("recipeName")
        binding.ingredientsListingTxt.text = intent.getStringExtra("ingredients")
        binding.contributorTxtDetails.text = intent.getStringExtra("email")
        binding.instructionsListingTxt.text = intent.getStringExtra("instructions")
        Glide.with(this).load(intent.getStringExtra("url")).into(binding.imageView2)

        if (viewModel.userName() != intent.getStringExtra("email").toString())
        {
            deltBtn.isVisible = false
            updateBtn.isVisible = false
        }

        deltBtn.setOnClickListener {
            viewModel.delete(intent.getStringExtra("recipeName").toString(), this)
            val intent = Intent(this, DeleteDisplay::class.java)
            startActivity(intent)
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, RecyclerView::class.java)
            startActivity(intent)
        }


        binding.updateButton.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("recipeName", binding.recipeNameTxtDetails.text.toString())
            intent.putExtra("cookTime", binding.cooktimeTextView.text.toString())
            startActivity(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logoutbtn) {
            val viewModel by viewModels<MainViewModel>()
            viewModel.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "User successfully logged out", Toast.LENGTH_SHORT).show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }



    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val viewModel by viewModels<MainViewModel>()

        if (auth.currentUser != null) {
            viewModel.username.observe(this) { loadUserName(it) }
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        auth = FirebaseAuth.getInstance()
        val viewModel by viewModels<MainViewModel>()

        if (auth != null) {
            viewModel.username.observe(this) { loadUserName(it) }
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun loadUserName(it: String) {
        toolbar.subtitle = it
    }

    fun changeText(it: String, textview: TextView)
    {
        textview.text = it
    }

    fun timeText(it:String)
    {
        time.text= it
    }



}