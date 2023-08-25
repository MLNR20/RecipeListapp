package com.example.finals_mobdevt_group5

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.finals_mobdevt_group5.databinding.ActivityUpdateBinding
import com.example.finals_mobdevt_group5.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth


class UpdateActivity : AppCompatActivity() {


    private lateinit var binding: ActivityUpdateBinding
    private lateinit var auth: FirebaseAuth

    private val toolbar by lazy { binding.toolbar3 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel by viewModels<MainViewModel>()

        viewModel.username.observe(this){loadUserName(it)}
        setSupportActionBar(toolbar)
        viewModel.retrieveData(intent.getStringExtra("recipeName").toString())
        val recipeName = findViewById<View>(R.id.recipeNameText) as EditText
        val cookTime = findViewById<View>(R.id.cookTimeTextUpdate) as EditText
        val shortDescription = findViewById<View>(R.id.shortDescriptionILUPTxtBox) as EditText
        val ingredients = findViewById<View>(R.id.IngredientsTxtBoxUpdate) as EditText
        val instructions = findViewById<View>(R.id.InstructionsTxtBoxUpdate) as EditText
        val url = findViewById<View>(R.id.FoodImageTxtBoxUpdate) as EditText

        viewModel.recipeName.observe(this){recipeName.setText(it)}
        cookTime.setText(intent.getStringExtra("cookTime"))
        viewModel.sd.observe(this){shortDescription.setText(it)}
        viewModel.ingredient.observe(this){ingredients.setText(it)}
        viewModel.instruction.observe(this){instructions.setText(it)}
        viewModel.url.observe(this){url.setText(it)}

        binding.button2.setOnClickListener {


            if(!recipeName.text.toString().isNullOrEmpty() && !cookTime.text.toString().isNullOrEmpty()
                && !ingredients.text.toString().isNullOrEmpty() && !instructions.text.toString().isNullOrEmpty()
                && !shortDescription.text.toString().isNullOrEmpty() && !url.text.toString().isNullOrEmpty())
            {
                viewModel.update(intent.getStringExtra("recipeName").toString(),
                    recipeName.text.toString(),
                    cookTime.text.toString(),
                    ingredients.text.toString(),
                    instructions.text.toString(),
                    shortDescription.text.toString(),
                    url.text.toString(),
                    this)
                val intent = Intent(this, UpdateDisplayActivity::class.java)
                startActivity(intent)
            }
            else Toast.makeText(this,"All fields must be filled!", Toast.LENGTH_SHORT).show()



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
    fun loadUserName(it:String)
    {
        toolbar.subtitle = it
    }


}