package com.example.recyclerviewreka

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerviewreka.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        txtregisterlistener()

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener{


            val email: String = binding.edtEmail.text.toString().trim()
            val password: String = binding.edtPass.text.toString().trim()
            val Config : String = binding.edtConfigPass.text.toString().trim()


            if (email.isEmpty()){
                binding.edtEmail.error = "Masukan Email"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }



            if (password.isEmpty() || password.length <6 ) {
                binding.edtPass.error = "Masukan Password Yang Lebih Dari 6 karakter"
                binding.edtPass.requestFocus()
                return@setOnClickListener
            }

            if (password != Config)  {
                binding.edtConfigPass.error = "Password tidak sama"
                binding.edtConfigPass.requestFocus()
                return@setOnClickListener

            }



            registerUser(email, password)

        }

    }
    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Intent(this, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
            else {
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            Intent(this, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }


    private fun txtregisterlistener() {
        binding.txtLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}