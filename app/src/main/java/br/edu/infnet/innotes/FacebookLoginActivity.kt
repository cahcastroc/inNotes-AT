//package br.edu.infnet.innotes
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import com.facebook.AccessToken
//import com.facebook.CallbackManager
//import com.facebook.FacebookCallback
//import com.facebook.FacebookException
//import com.facebook.login.LoginResult
//import com.facebook.login.widget.LoginButton
//import com.google.firebase.auth.FacebookAuthProvider
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.ktx.app
//
//class FacebookLoginActivity : Activity() {
//
//    // [START declare_auth]
//     lateinit var appAuth: FirebaseAuth
//    private lateinit var auth: FirebaseAuth
//    // [END declare_auth]
//
//    private lateinit var callbackManager: CallbackManager
//    private lateinit var buttonFacebookLogin: LoginButton
//
//
//    public override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//
//        appAuth = FirebaseAuth.getInstance()
//
//        callbackManager = CallbackManager.Factory.create()
//
//        buttonFacebookLogin.setReadPermissions("email", "public_profile")
//        buttonFacebookLogin.registerCallback(callbackManager, object :
//            FacebookCallback<LoginResult> {
//            override fun onSuccess(loginResult: LoginResult) {
//                Log.d(TAG, "facebook:onSuccess:$loginResult")
//                handleFacebookAccessToken(loginResult.accessToken)
//
//            }
//
//            override fun onCancel() {
//                Log.d(TAG, "facebook:onCancel")
//            }
//
//            override fun onError(error: FacebookException) {
//                Log.d(TAG, "facebook:onError", error)
//            }
//        })
//
//    }
//
//
//    public override fun onStart() {
//        super.onStart()
//
//        val currentUser = appAuth.currentUser
//        updateUI(currentUser)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//
//        callbackManager.onActivityResult(requestCode, resultCode, data)
//    }
//
//
//
//    private fun handleFacebookAccessToken(token: AccessToken) {
//        Log.d(TAG, "handleFacebookAccessToken:$token")
//
//        val credential = FacebookAuthProvider.getCredential(token.token)
//        appAuth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCredential:success")
//                    val user = appAuth.currentUser
//                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
//                }
//            }
//    }
//    // [END auth_with_facebook]
//
//    private fun updateUI(user: FirebaseUser?) {
//
//        val intent = Intent(this, AppActivity::class.java)
//        startActivity(intent)
//
//    }
//
//    companion object {
//        private const val TAG = "FacebookLogin"
//    }
//}
//
