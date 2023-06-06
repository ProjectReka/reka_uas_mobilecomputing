package com.example.recyclerviewreka

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerviewreka.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth



class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        textdaftar()

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener(){
            val email: String = binding.edtEmail.text.toString().trim()
            val password: String = binding.edtPassword.text.toString().trim()

            if (email.isEmpty()){
                binding.edtEmail.error = "Masukan Email"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6){
                binding.edtPassword.error = "Masukan Password"
                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() {
            if (it.isSuccessful) {
                Intent(this, MainActivity::class.java).also { intent ->
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
            else {
                Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            Intent(this, MainActivity::class.java).also { intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun textdaftar() {
        binding.txtDaftar.setOnClickListener{
            startActivity(Intent( this, RegisterActivity::class.java))
        }
    }
}