package com.bishwajeet.githubrepos.view.login.fragmentLogin

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bishwajeet.githubrepos.R
import com.bishwajeet.githubrepos.model.AppUser
import com.bishwajeet.githubrepos.view.login.fragmentLogin.helper.LoginFormState
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginWithPasswordFormState: LiveData<LoginFormState> = _loginForm

    fun onLoginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value =
                LoginFormState.FailedLoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value =
                LoginFormState.FailedLoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState.SuccessfulLoginFormState(isDataValid = true)
        }
    }


    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }


    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }


    fun login(username: String, password: String): Boolean {
        if (isUserNameValid(username) && isPasswordValid(password)) {
            // Normally this method would asynchronously send this to your server and your sever
            // would return a token. For high sensitivity apps such as banking, you would keep that
            // token in transient memory similar to my SampleAppUser object. This way the user
            // must login each time they start the app.
            // In this sample, we don't call a server. Instead we use a fake token that we set
            // right here:

            AppUser.username = username
            AppUser.token = java.util.UUID.randomUUID().toString()
            return true
        }
        return false
    }
}