package com.example.game_box

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth


class SignUpActivity: AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var mBinding: ActivitySignUpBinding

    private lateinit var firebaseAuth:  FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        firebaseAuth = FirebaseAuth.getInstance() // Initialisation de firebaseAuth
        mBinding.emailEt.onFocusChangeListener = this
        mBinding.passET.onFocusChangeListener = this
        mBinding.confirmPassEt.onFocusChangeListener = this

        mBinding.textView.setOnClickListener {
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }

        mBinding.button.setOnClickListener {

            val email = mBinding.emailEt.text.toString()
            val password = mBinding.passET.text.toString()
            val confirmPass = mBinding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty()) {
                if(password == confirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent = Intent(this,SignInActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this,"Password is not matching",Toast.LENGTH_SHORT).show()
                }


            } else {
                Toast.makeText(this, "Enter your data please !", Toast.LENGTH_SHORT).show()

            }
        }


    }
    private fun validateEmail(shouldVibrateView: Boolean= true):Boolean{
        var errorMessage:String? = null
        val value: String = mBinding.emailEt.text.toString()
        if(value.isEmpty()){

            errorMessage = "Email is required"

        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()){

            errorMessage = "Email adress is invalid !"
        }
        if(errorMessage != null){

            mBinding.emailLayout.apply {
                isErrorEnabled = true
                error = errorMessage
                if(shouldVibrateView){ VibrateView.vibrate(this@SignUpActivity, this)}

            }

        }
        return errorMessage == null

    }
    private fun validatePassword(shouldVibrateView: Boolean= true):Boolean{
        var errorMessage:String? = null
        val value: String = mBinding.passET.text.toString()
        if(value.isEmpty()){

            errorMessage = "Password is required"

        } else if (value.length < 6){

            errorMessage = "Password must be 6 characters Long !"
        }

        if(errorMessage != null){

            mBinding.passwordLayout.apply {
                isErrorEnabled = true
                error = errorMessage
                if(shouldVibrateView){ VibrateView.vibrate(this@SignUpActivity, this)}

            }

        }

        return errorMessage == null
    }

    private fun validateConfirmpassword(shouldVibrateView: Boolean= true):Boolean {
        var errorMessage:String? = null
        val value: String = mBinding.confirmPassEt.text.toString()
        if(value.isEmpty()){

            errorMessage = " Confirm Password is required"

        } else if (value.length < 6){

            errorMessage = "Confirm Password must be 6 characters Long !"
        }
        if(errorMessage != null){

            mBinding.confirmPasswordLayout.apply {
                isErrorEnabled = true
                error = errorMessage
                if(shouldVibrateView){ VibrateView.vibrate(this@SignUpActivity, this)}

            }

        }

        return errorMessage == null
    }

    private fun validatePasswordAndConfirmPassword(shouldVibrateView: Boolean= true):Boolean{
        var errorMessage: String? = null
        val password: String? = mBinding.passET.text.toString()
        val confirmPassword: String? = mBinding.confirmPassEt.text.toString()

        if(password!=confirmPassword){
            errorMessage = "Confirm password doesn't match with password"
        }
        if(errorMessage != null){

            mBinding.confirmPasswordLayout.apply {
                isErrorEnabled = true
                error = errorMessage
               if(shouldVibrateView){ VibrateView.vibrate(this@SignUpActivity, this)}
            }

        }
        return errorMessage == null
    }

    override fun onClick(view: View?) {



    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view!=null){
            when(view.id){

                R.id.emailEt ->{
                    if(hasFocus){
                        if(mBinding.emailLayout.isErrorEnabled){
                            mBinding.emailLayout.isErrorEnabled = false
                        }
                    }else{
                        if(validateEmail()){
                            mBinding.emailLayout.apply {

                                setStartIconDrawable(R.drawable.check_circle_24)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))

                            }
                        }
                    }
                }
                R.id.passET ->{
                    if(hasFocus){
                        if(mBinding.passwordLayout.isErrorEnabled){
                            mBinding.passwordLayout.isErrorEnabled = false
                        }
                    }else{
                        if(validatePassword() && mBinding.confirmPassEt.text!!.isEmpty() && validateConfirmpassword() && validatePasswordAndConfirmPassword() ) {
                            if (mBinding.confirmPasswordLayout.isErrorEnabled){
                                mBinding.confirmPasswordLayout.isErrorEnabled = false
                            }
                            mBinding.confirmPasswordLayout.apply {
                                setStartIconDrawable(R.drawable.check_circle_24)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            }
                        }

                    }
                }
                R.id.confirmPassEt ->{
                    if(hasFocus){
                        if(mBinding.confirmPasswordLayout.isErrorEnabled){
                            mBinding.confirmPasswordLayout.isErrorEnabled = false
                        }
                    }else{
                        if(validateConfirmpassword() && validatePasswordAndConfirmPassword() && validatePassword()){
                            if (mBinding.passwordLayout.isErrorEnabled){
                                mBinding.passwordLayout.isErrorEnabled = false
                            }
                            mBinding.confirmPasswordLayout.apply {

                                setStartIconDrawable(R.drawable.check_circle_24)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))

                            }
                        }
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, event: Int, keyevent: KeyEvent?): Boolean {
        return false
    }

}