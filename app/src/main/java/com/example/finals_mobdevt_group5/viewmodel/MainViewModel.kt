package com.example.finals_mobdevt_group5.viewmodel

import android.app.Activity
import android.media.MediaPlayer.OnCompletionListener
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finals_mobdevt_group5.model.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainViewModel: ViewModel() {
    private var auth: FirebaseAuth
    private var db = Firebase.firestore

    private val _username = MutableLiveData<String>("")
    private val _password = MutableLiveData<String>("")
    private val _time = MutableLiveData<String>("1 minute")
    private val _email = MutableLiveData<String>("")
    private val _recipeName = MutableLiveData<String>()
    private val _ingredient = MutableLiveData<String>()
    private val _instructions = MutableLiveData<String>()
    private val _sd = MutableLiveData<String>()
    private val _checkCorrect = MutableLiveData<Boolean>()
    private val _url = MutableLiveData<String>()

    val username: LiveData<String> = _username
    val time: LiveData<String> = _time
    val recipeName: LiveData<String> = _recipeName
    val ingredient: LiveData<String> = _ingredient
    val instruction: LiveData<String> = _instructions
    val url : LiveData<String> = _url
    val sd : LiveData<String> = _sd

     fun signIn(email: String, pass: String, login: Activity): Boolean {
        _username.value = email
        _password.value = pass
         auth = Firebase.auth
        auth.signInWithEmailAndPassword(_username.value!!, _password.value!!).
        addOnCompleteListener(login) {
            if (it.isSuccessful) _checkCorrect!!.value = true
            else {
                Toast.makeText(login, "Invalid user credentials", Toast.LENGTH_SHORT).show()
                _checkCorrect!!.value = false
            }
        }

        if (auth?.currentUser!! != null) {
            _checkCorrect!!.value = true
            Toast.makeText(login, "User successfully logged in.", Toast.LENGTH_SHORT).show()
        }
        return _checkCorrect.value!!
    }

    init{

        auth = Firebase.auth
        if(auth?.currentUser !=null){
            _username.value = userName()
            _email.value = userName()
        }
        else ""

    }


    fun signOut() {
        auth = Firebase.auth
        auth.signOut()
    }


    fun register(email: String, pass: String, confirm: String, register: Activity) {

        auth = Firebase.auth


        if (pass != confirm) {
            Toast.makeText(
                register,
                "Confirm password and password does not match",
                Toast.LENGTH_SHORT
            ).show()

        } else if (!email.contains("gmail.com") && !email.contains("email.com")) {
            Toast.makeText(register, "Invalid user email", Toast.LENGTH_SHORT).show()

        } else if (pass.length < 6) {
            Toast.makeText(register, "Password must be six characters long", Toast.LENGTH_SHORT)
                .show()

        } else {
            auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(register) {
                Toast.makeText(register, "Account successfully created", Toast.LENGTH_SHORT).show()
            }

            auth.signOut()
        }

    }

    fun addActivity(recipeName:String, email: String, cookTime:String, ingredient: String, instruction:String, description:String,
                    url:String, activity: Activity){

        if(!recipeName.isNullOrEmpty() && !cookTime.isNullOrEmpty() && !ingredient.isNullOrEmpty() && !instruction.isNullOrEmpty() && !description.isNullOrEmpty()
            &&  !url.isNullOrEmpty())
        {
            var recipeMap = Recipe(recipeName,cookTime,email, ingredient, instruction, description, url)
            db.collection("finalsSample").document().set(recipeMap).addOnSuccessListener {
                Toast.makeText(activity,"Successfully added", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun userName(): String {
        auth = Firebase.auth
        return auth.currentUser?.email.toString()
    }


    fun delete(name: String, activity: Activity){

        db.collection("finalsSample").whereEqualTo("recipeName", name).get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val documentId = document.id
                    db.collection("finalsSample").document(documentId).delete()
                    Toast.makeText(activity,"Recipe successfully deleted", Toast.LENGTH_SHORT).show()
                }
            }

    }

    fun update(email:String, recipeName:String, cookTime:String, ingredient: String, instruction:String, description:String, url: String, activity: Activity){

        var recipeMap = mapOf(
            "recipeName" to recipeName,
            "cookTime" to cookTime,
            "ingredient" to ingredient,
            "instructions" to instruction,
            "shortdescription" to description,
            "url" to url
        )


        if(!recipeName.isNullOrEmpty() && !cookTime.isNullOrEmpty() && !ingredient.isNullOrEmpty()  && !instruction.isNullOrEmpty() &&
                !description.isNullOrEmpty() && !ingredient.isNullOrEmpty() && !url.isNullOrEmpty())
        {
            db.collection("finalsSample").whereEqualTo("recipeName", email).get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val documentId = document.id
                        db.collection("finalsSample").document(documentId).update(recipeMap)
                        Toast.makeText(activity,"Recipe successfully updated", Toast.LENGTH_SHORT).show()
                    }
                }
        }




    }

    fun changeTime(activity: Activity, name: String)
    {
        _time.value = name
        if(name!!.length == 1 || name!!.length ==2)
        {
            _time.value  = name+ " minutes"
        }
        else if(name!!.length == 4 ||name!!.length ==5)
        {
            val split=name.split(":").toTypedArray()
            _time.value = split[0] + " hours, " + split[1] + " minutes"
        }
    }

    fun retrieveData(name:String)
    {
        db.collection("finalsSample").whereEqualTo("recipeName", name).get().addOnCompleteListener()
        {
            if (it.isSuccessful) {
                val querySnapshot = it.result
                for (document in querySnapshot!!) {
                    val value = document.getString("recipeName")
                    val change = document.getString("ingredient")
                    val inst = document.getString("instructions")
                    val email = document.getString("email")
                    _recipeName!!.value = value
                    _ingredient!!.value= change
                    _instructions!!.value = inst
                    _email!!.value = email
                    _sd!!.value = document.getString("shortdescription")
                    _url!!.value = document.getString("url")
                }
            }
        }
    }

}