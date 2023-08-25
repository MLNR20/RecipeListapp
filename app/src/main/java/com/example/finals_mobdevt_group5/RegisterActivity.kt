package com.example.finals_mobdevt_group5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import com.example.finals_mobdevt_group5.databinding.ActivityLoginBinding
import com.example.finals_mobdevt_group5.databinding.ActivityRecyclerViewBinding
import com.example.finals_mobdevt_group5.databinding.ActivityRegisterBinding
import com.example.finals_mobdevt_group5.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val viewModel by viewModels<MainViewModel>()


        binding.button.setOnClickListener {

            try{
                viewModel.register(binding.emailText.text.toString(),binding.passwordText.text.toString(), binding.confirmpasswordText.text.toString(), this)
                binding.emailText.setText("")
                binding.passwordText.setText("")
                binding.confirmpasswordText.setText("")
            }
            catch (e:Exception){ }
        }


        binding.textView2.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}