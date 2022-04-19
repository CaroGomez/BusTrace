package co.edu.udea.compumovil.gr05_20212.bustrace

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var txtName:EditText
    private lateinit var txtLastName:EditText
    private lateinit var txtEmail:EditText
    private lateinit var txtPassword:EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var dbReference:DatabaseReference
    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth
    private lateinit var home: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        MyToolbar().show(this, "Registro", true)

        txtName=findViewById(R.id.getName)
        txtLastName=findViewById(R.id.getLastName)
        txtEmail=findViewById(R.id.getCorreo)
        txtPassword=findViewById(R.id.getConfirmPassword)

        progressBar= findViewById(R.id.progressBar)
        database= FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()

        dbReference=database.reference.child("User")
    }

    fun register(view: View) {
        if (view !=null){
            val hideMe=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        createNewAccount()
    }

    private fun createNewAccount(){
        val name:String=txtName.text.toString()
        val lastName:String=txtLastName.text.toString()
        val email:String=txtEmail.text.toString()
        val password:String=txtPassword.text.toString()

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && txtPassword.length()>=6){
                progressBar.visibility=View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){
                    task->

                    if (task.isComplete){
                        val user:FirebaseUser?=auth.currentUser
                        verifyEmail(user)

                        val userBD= user?.uid?.let { dbReference.child(it) }

                        userBD?.child("Name")?.setValue(name)
                        userBD?.child("Last Name")?.setValue(lastName)
                        val toast = Toast.makeText(this, "Usuario registrado con éxito.", Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                        action()
                    }
                }
        }
        else if(TextUtils.isEmpty(name) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            if (TextUtils.isEmpty(name)){
                txtName.error = "El nombre no puede estar vacio."
            }
            if (TextUtils.isEmpty(lastName)){
                txtLastName.setError("El apellido no puede estar vacio.")
            }
            if (TextUtils.isEmpty(email)){
                txtEmail.setError("El correo no puede estar vacio.")
            }
            if (TextUtils.isEmpty(password)){
                txtPassword.setError("La contraseña no puede estar vacia.")
            }
            val toast = Toast.makeText(this, "Los campos no pueden estar vacios.", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
        }
        else{
            txtPassword.setError("La contraseña debe tener mas de 6 digitos.")
            val toast = Toast.makeText(this, "La contraseña debe tener 6 digitos.", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
        }


    }
    private fun action(){
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun iniciarSesion(view:View){
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun verifyEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task ->
                if (task.isComplete){
                    Toast.makeText(this, "Email Enviado", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this, "Error al enviar el email", Toast.LENGTH_LONG).show()
                }
            }
    }
}