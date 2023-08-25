package com.example.finals_mobdevt_group5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import com.example.finals_mobdevt_group5.databinding.ActivityLoginBinding
import com.example.finals_mobdevt_group5.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth.signOut()

        val viewModel by viewModels<MainViewModel>()
        viewModel.signOut()
        viewModel.username.observe(this){loadData(it)}


        binding.textView2.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            try
            {
                warningClear()
                if(binding.emailText.text.toString()!=null && binding.passwordText.text.toString()!=null)
                {
                    if(viewModel.signIn(binding.emailText.text.toString(), binding.passwordText.text.toString(), this))
                    {
                        val intent = Intent(this, RecyclerView::class.java)
                        startActivity(intent)
                    }

                }
            }
            catch (e:Exception)
            {
                displayWarningUserName(binding.emailText.text.toString(), binding.passwordText.text.toString())
            }

        }

    }

     fun displayWarningUserName(username:String, password:String)
    {
         if(username.isNullOrEmpty() && password.isNullOrEmpty())
      {
        binding.warningTextViewUserName.text= "Username must not be empty"
        binding.warningTextViewUserName2.text= "Password must not be empty"

         }
       else if(username.isNullOrEmpty())
        {
            binding.warningTextViewUserName.text= "Username must not be empty"

        }
        else if(password.isNullOrEmpty())
        {
            binding.warningTextViewUserName2.text= "Password must not be empty"

        }


    }

    fun loadData(it:String){
        binding.emailText.setText(it)
    }

    fun warningClear()
    {
        binding.warningTextViewUserName.text= ""
        binding.warningTextViewUserName2.text= ""
    }

}