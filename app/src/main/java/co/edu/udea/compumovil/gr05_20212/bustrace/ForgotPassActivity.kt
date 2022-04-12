package co.edu.udea.compumovil.gr05_20212.bustrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
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

    fun send(view: View) {
        val email=txtEmail.text.toString()

        if (!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this){
                    task->
                    if(task.isSuccessful){
                        progressBar.visibility=View.VISIBLE
                        val toast = Toast.makeText(this, "Email de recuperación enviado con éxito. Por favor reestablezca la contraseña en su correo.", Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    else{
                        Toast.makeText(this, "Email no registrado en la base de datos.", Toast.LENGTH_LONG).show()
                    }
                }
        }
        else{
            txtEmail.setError("El correo no puede estar vacio.")
            val toast = Toast.makeText(this, "Los campos no pueden estar vacios.", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
        }
    }
}