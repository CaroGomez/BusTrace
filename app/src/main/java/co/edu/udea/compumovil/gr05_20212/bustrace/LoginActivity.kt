package co.edu.udea.compumovil.gr05_20212.bustrace

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var txtUser: EditText
    private lateinit var txtPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtUser=findViewById(R.id.email)
        txtPassword=findViewById(R.id.password)
        progressBar= findViewById(R.id.progressBar)
        auth= FirebaseAuth.getInstance()

        var consultas = Consultas()
        GlobalScope.launch(Dispatchers.IO) {
            var routes = consultas.getRoutes()
            for (i in routes) {
                Log.i("Route", i.id)
                Log.i("Route", i.name)
            }

            Log.i("RouteById", consultas.getRoutebyId("1").name)
        }



    }

    fun forgotPassword(view:View){
        startActivity(Intent(this, ForgotPassActivity::class.java))
    }
    fun register(view:View){
        startActivity(Intent(this, RegisterActivity::class.java))
    }
    fun login(view:View){
        loginUser()
    }

    private fun loginUser(){
        val user:String=txtUser.text.toString()
        val password:String=txtPassword.text.toString()

        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)){
            progressBar.visibility=View.VISIBLE
            auth.signInWithEmailAndPassword(user, password)
                .addOnCompleteListener(this){
                    task->
                    if (task.isSuccessful){
                        action()
                    }
                    else{
                        val toast = Toast.makeText(this, "Error en la autenticación, por favor intente de nuevo.", Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                    }
                }
        }
        else{
            val toast = Toast.makeText(this, "Los campos no pueden estar vacios.", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
            if (txtUser.length()==0){
                txtUser.setError("El correo no puede estar vacio.")
            }
            if(txtPassword.length()==0){
                txtPassword.setError("La contraseña no puede estar vacia.")
            }
        }
    }

    private fun action(){
        startActivity(Intent(this, user_menu::class.java))
    }
}