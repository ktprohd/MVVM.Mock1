package com.example.mvvmmock1.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mvvmmock1.R
import com.example.mvvmmock1.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth


    companion object {
        private const val RC_SIGN_IN = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //------------------- clickable string --------------------//

        clickableText()
        binding.txtSignup.movementMethod = LinkMovementMethod.getInstance()

        //-----------------------forgotpass-----------------------//
        binding.forgotpass.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_section, ForgotPassFragment())
            transaction?.commit() }

        //------------------------login---------------------------//
        binding.btnLogin1.setOnClickListener {
            checkUser()
             }

        //------------------------loginGG---------------------------//
        auth = Firebase.auth
        binding.btnLogin2.setOnClickListener {
            signIn()
        }

    }

    fun clickableText() {
        val spannable: CharSequence = getText(R.string.sign_up)
        val spannableString = SpannableString(spannable)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragment_section, SignupFragment())
                transaction?.commit()
            }
        }
            spannableString.setSpan(clickableSpan, (spannable.length - 7), spannable.length, 0)
            binding.txtSignup.text = spannableString
        }

    fun checkUser() {
        val username = binding.edtusername.text.toString().trim()
        val password = binding.edtpassword.text.toString().trim()
        if (username.isEmpty()) {
            binding.edtusername.setError("Can't be null")
        }
        if (password.isEmpty()) {
            binding.edtpassword.setError("Can't be null")
        } else {
            val auth = FirebaseAuth.getInstance()
            // Đăng nhập người dùng bằng email và mật khẩu
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Đăng nhập thành công
                        val user: FirebaseUser? = auth.currentUser
                        if (user != null) {
                            // Người dùng đã đăng nhập
                            // Thực hiện các hành động cần thiết sau khi đăng nhập thành công
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.fragment_section, StartFragment())
                            transaction?.commit()
                             }
                    } else {
                        // Đăng nhập thất bại
                        // Xử lý lỗi hoặc hiển thị thông báo lỗi cho người dùng
                        Toast.makeText(requireActivity(), "Login Failed", Toast.LENGTH_SHORT).show() }
                }
        }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(requireActivity(), "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragment_section, StartFragment())
                    transaction?.commit()

                } else {
                    Toast.makeText(requireActivity(), "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

