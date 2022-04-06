package co.edu.udea.compumovil.gr05_20212.bustrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var txtEmail: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var home: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        home=findViewById(R.id.home_back)
        txtEmail=findViewById(R.id.txtEmail)
        auth= FirebaseAuth.getInstance()
        progressBar= findViewById(R.id.progressBar)

        home.setOnClickListener(){
            val intent = Intent (this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun send(viex: View){
        val email=txtEmail.text.toString()

        if (!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this){
                    task->

                    if(task.isSuccessful){
                        progressBar.visibility=View.VISIBLE
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    else{
                        Toast.makeText(this, "Error al enviar el email", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}