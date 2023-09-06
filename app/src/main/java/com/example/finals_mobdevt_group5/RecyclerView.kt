package com.example.finals_mobdevt_group5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finals_mobdevt_group5.adapter.RecipeAdapter
import com.example.finals_mobdevt_group5.databinding.ActivityRecyclerViewBinding
import com.example.finals_mobdevt_group5.model.Recipe
import com.example.finals_mobdevt_group5.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class RecyclerView : AppCompatActivity() {

    //binding
    private lateinit var binding:ActivityRecyclerViewBinding

    //firebase
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    private lateinit var recipeQuery:ArrayList<Recipe>

    //views
    private val recyclerView by lazy{ binding.recyclerView}
    private val toolbar by lazy{ binding.toolbar2}
    private val bottomNavigation by lazy{binding.bottomNavigation}
    private  lateinit var recipeList: ArrayList<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth =  FirebaseAuth.getInstance()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        loadDb()

        setSupportActionBar(toolbar)


        val viewModel by viewModels<MainViewModel>()
        viewModel.username.observe(this){loadUserName(it)}


        bottomNavigation.setSelectedItemId(R.id.homeBtn)
        bottomNavigation.setOnItemSelectedListener {
            onNavigationItemSelected(it)
        }


        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                recipeQuery.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    recipeList.forEach{
                        if(it.recipeName.toString().toLowerCase(Locale.getDefault()).contains(searchText)){
                            recipeQuery.add(it)
                        }
                    }

                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                else{
                    recipeQuery.clear()
                    recipeQuery.addAll(recipeList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })


    }


    private fun loadDb()
    {
        db = FirebaseFirestore.getInstance()
        recipeList = arrayListOf()
        recipeQuery = arrayListOf()

        db.collection("finalsSample").get().addOnSuccessListener {
            if(!it.isEmpty){
                for(data in it.documents){
                    val recipe: Recipe? = data.toObject(Recipe::class.java)
                    if(recipe!=null){
                        recipeList.add(recipe)
                    }
                }
            }

            recipeQuery.addAll(recipeList)
            var adapter = RecipeAdapter(recipeQuery, this@RecyclerView)
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener(object: RecipeAdapter.onItemClickListener{
                override fun onItemClick(position: Int) {
                    val intent = Intent(this@RecyclerView, DetailsActivity::class.java)

                    intent.putExtra("recipeName", recipeList[position].recipeName)
                    intent.putExtra("cookTime", recipeList[position].cookTime)
                    intent.putExtra("email", recipeList[position].email)
                    intent.putExtra("ingredients", recipeList[position].ingredient)
                    intent.putExtra("instructions", recipeList[position].instructions)
                    intent.putExtra("url", recipeList[position].url)

                    startActivity(intent)

                }
            })

        }


    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return  super.onCreateOptionsMenu(menu)

    }


    fun onNavigationItemSelected(item: MenuItem): Boolean{
        if(item.itemId==R.id.addBtn)
        {
            val intent = Intent(this, AddActivity::class.java)
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


    override fun onStart() {
        super.onStart()
        auth =  FirebaseAuth.getInstance()
        bottomNavigation.setSelectedItemId(R.id.homeBtn)
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
        loadDb()
        bottomNavigation.setSelectedItemId(R.id.homeBtn)
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