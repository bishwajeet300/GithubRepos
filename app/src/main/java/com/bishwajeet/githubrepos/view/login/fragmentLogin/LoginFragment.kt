package com.bishwajeet.githubrepos.view.login.fragmentLogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bishwajeet.githubrepos.BuildConfig
import com.bishwajeet.githubrepos.GitHubRepoApp
import com.bishwajeet.githubrepos.R
import com.bishwajeet.githubrepos.model.AppUser
import com.bishwajeet.githubrepos.model.CipherTextWrapper
import com.bishwajeet.githubrepos.view.login.fragmentLogin.helper.LoginFormState
import com.bishwajeet.githubrepos.view.login.fragmentLogin.helper.NotificationWorker
import com.bishwajeet.githubrepos.view.login.fragmentLogin.helper.biometric.BiometricPromptUtils
import com.bishwajeet.githubrepos.view.login.fragmentLogin.helper.biometric.CryptographyManager
import com.bishwajeet.githubrepos.view.repo.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginFragment : Fragment() {

    private val _tag = "LoginFragment"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<LoginViewModel> {
        viewModelFactory
    }

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var cryptographyManager: CryptographyManager
    private lateinit var encryptedServerToken: CipherTextWrapper
    private lateinit var binding: View


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as GitHubRepoApp).applicationComponent.loginComponent()
            .create()
            .inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflate(R.layout.fragment_login, container, false)

        return binding
    }


    override fun onResume() {
        super.onResume()

        cryptographyManager =
            CryptographyManager()
        val cipherTextWrapper = cryptographyManager.getCipherTextWrapperFromSharedPrefs(
            requireContext().applicationContext,
            BuildConfig.SHARED_PREFS_FILENAME,
            BuildConfig.CIPHERTEXT_WRAPPER,
            Context.MODE_PRIVATE
        )

        if (cipherTextWrapper != null) {
            encryptedServerToken = cipherTextWrapper
            try {
                showBiometricPromptForDecryption()
            } catch (e: Exception) {
                Log.e(_tag, e.message.plus(""))
            }
        } else {
            viewModel.loginWithPasswordFormState.observe(viewLifecycleOwner, Observer { it ->
                val loginState = it ?: return@Observer
                when (loginState) {
                    is LoginFormState.SuccessfulLoginFormState -> binding.login.isEnabled =
                        loginState.isDataValid
                    is LoginFormState.FailedLoginFormState -> {
                        loginState.usernameError?.let { binding.username.error = getString(it) }
                        loginState.passwordError?.let { binding.password.error = getString(it) }
                    }
                }
            })

            binding.username.afterTextChanged {
                viewModel.onLoginDataChanged(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                )
            }

            binding.password.apply {
                afterTextChanged {
                    viewModel.onLoginDataChanged(
                        binding.username.text.toString(),
                        binding.password.text.toString()
                    )
                }

                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE ->
                            loginWithPassword(
                                binding.username.text.toString(),
                                binding.password.text.toString()
                            )
                    }
                    false
                }
            }
        }

        binding.login.setOnClickListener {
            loginWithPassword(username.text.toString(), password.text.toString())
        }
    }


    private fun showBiometricPromptForDecryption() {
        val canAuthenticate = BiometricManager.from(requireContext()).canAuthenticate()
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val secretKeyName = getString(R.string.secret_key_name)
            val cipher = cryptographyManager.getInitializedCipherForDecryption(
                secretKeyName,
                encryptedServerToken.initializationVector
            )
            biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(
                    requireActivity(),
                    ::decryptServerTokenFromStorage
                )
            val promptInfo = BiometricPromptUtils.createPromptInfo(requireActivity())
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }


    private fun showBiometricPromptForEncryption() {
        val canAuthenticate = BiometricManager.from(requireContext()).canAuthenticate()
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val secretKeyName = getString(R.string.secret_key_name)
            cryptographyManager =
                CryptographyManager()
            val cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName)
            val biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(
                    requireActivity(),
                    ::encryptAndStoreServerToken
                )
            val promptInfo = BiometricPromptUtils.createPromptInfo(requireActivity())
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }


    private fun encryptAndStoreServerToken(authResult: BiometricPrompt.AuthenticationResult) {
        authResult.cryptoObject?.cipher?.apply {
            AppUser.token?.let { token ->
                Log.d(_tag, "The token from server is $token")
                val encryptedServerToken = cryptographyManager.encryptData(token, this)
                cryptographyManager.persistCipherTextWrapperToSharedPrefs(
                    requireContext().applicationContext,
                    encryptedServerToken,
                    BuildConfig.SHARED_PREFS_FILENAME,
                    BuildConfig.CIPHERTEXT_WRAPPER,
                    Context.MODE_PRIVATE
                )
            }
        }

        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }


    private fun decryptServerTokenFromStorage(authResult: BiometricPrompt.AuthenticationResult) {
        authResult.cryptoObject?.cipher?.apply {
            val plainText = cryptographyManager.decryptData(encryptedServerToken.ciphertext, this)
            AppUser.token = plainText

            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }
    }


    private fun loginWithPassword(username: String, password: String) {
        val succeeded = viewModel.login(username, password)
        if (succeeded) {
            initNotificationWorker()
            if (use_biometrics.isChecked) {
                showBiometricPromptForEncryption()
            } else {
                Log.i(
                    _tag,
                    "You successfully signed up using password as : user " + "${AppUser.username} woth token ${AppUser.token}"
                )
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
        }
    }


    private fun AppCompatEditText.afterTextChanged(task: () -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                task()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })
    }


    private fun initNotificationWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val notificationWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(10, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(requireContext()).enqueue(notificationWorkRequest)
    }
}