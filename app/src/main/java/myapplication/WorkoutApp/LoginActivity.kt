package myapplication.WorkoutApp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import myapplication.WorkoutApp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        binding.btnLogin.setOnClickListener {
            val dao = (application as WorkOutApp).db!!.UsersDao()
            loginUser(dao)
        }

        binding.RedirectSignUp.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()

        }


    }

    fun loginUser(usersDao: UsersDao) {
        val emailAddress = binding.EmailAddress.text.toString()
        val password = binding.Password.text.toString()

        if (emailAddress.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        // Use a coroutine to perform database operations on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            val existingEmail = usersDao.getEmailByEmail(emailAddress)
            if (existingEmail == null) {
                // Email not found
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Invalid Email-ID", Toast.LENGTH_LONG).show()
                }
            } else {
                // Email found, check the password
                val storedPassword = usersDao.getPasswordByEmail(emailAddress)
                if (password == storedPassword) {
                    // Password is correct, you can proceed with login logic here
                    val userId = usersDao.getUserIdByEmail(emailAddress)
                    val sessionManager = SessionManager(this@LoginActivity)
                    sessionManager.login(userId!!)

                    BranchEvent(BRANCH_STANDARD_EVENT.LOGIN)
                        .setCustomerEventAlias("my_custom_alias")
                        .setTransactionID("tx1234")
                        .setDescription("User created an account")
                        .addCustomDataProperty("loginID", userId.toString())
                        .logEvent(applicationContext)

                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Password is incorrect
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Invalid Password", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }
}