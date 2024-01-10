package myapplication.WorkoutApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import myapplication.WorkoutApp.databinding.ActivityBmiBinding
import myapplication.WorkoutApp.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private var binding: ActivityRegistrationBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        //connect the layout to this activity
        setContentView(binding?.root)

        binding!!.btnRegister.setOnClickListener{
            val dao = (application as WorkOutApp).db!!.UsersDao()
            signUpUser(dao)

        }
        binding!!.RedirectLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    private fun signUpUser(usersDao: UsersDao) {
        val emailAddress = binding?.EmailAddress?.text.toString()
        val password = binding?.Password?.text.toString()
        val confirmPassword = binding?.confPassword?.text.toString()

        // Check email and password
        if (emailAddress.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Use a coroutine to perform database operations on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            val status: String? = usersDao.getEmailByEmail(emailAddress!!)
            if (status == null) {
                val count: Int? = usersDao.getRecordCount()?.plus(1) ?: 1
                val newUser: UsersEntity = UsersEntity(
                    id = count!!,
                    email = emailAddress!!,
                    password = password!!,
                    bmi = 0
                )

                BranchEvent(BRANCH_STANDARD_EVENT.COMPLETE_REGISTRATION)
                    .setCustomerEventAlias("my_custom_alias")
                    .setTransactionID("tx1234")
                    .setDescription("User created an account")
                    .addCustomDataProperty("registrationID", newUser.id.toString())
                    .logEvent(applicationContext)

                // Insert the new user
                usersDao.insertUser(newUser)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegistrationActivity, "Successfully Signed Up", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegistrationActivity, "Sign Up Failed! Email Address already present", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    }

