package com.example.geochallenge.ui.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.geochallenge.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class LoginFragment : Fragment() {

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailInputEditText: TextInputEditText
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var passwordInputEditText: TextInputEditText
    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button
    private lateinit var forgotPasswordBtn: Button
    private lateinit var signInBtn: SignInButton

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var viewModel: AuthViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fr_login, container, false)
        emailInputLayout = v.findViewById(R.id.emailInput)
        emailInputEditText = v.findViewById(R.id.emailInputEditText)
        passwordInputLayout = v.findViewById(R.id.passwordInput)
        passwordInputEditText = v.findViewById(R.id.passwordInputEditText)

        loginBtn = v.findViewById(R.id.loginBtn)
        registerBtn = v.findViewById(R.id.registerBtn)
        forgotPasswordBtn = v.findViewById(R.id.forgotPasswordBtn)
        signInBtn = v.findViewById(R.id.sign_in_button)

        signInBtn.setOnClickListener {
            signIn()
        }

        forgotPasswordBtn.setOnClickListener {
            (activity as AuthActivity).showForgotPasswordScreen()
        }

        emailInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.enterEmail(s.toString())
            }
        })

        passwordInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.enterPassword(s.toString())
            }
        })
        loginBtn.setOnClickListener {
            viewModel.login()
        }

        registerBtn.setOnClickListener {
            (context as AuthActivity).showRegistrationScreen()
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as AuthActivity).authComponent.inject(this)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.passwordError.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    AuthErrors.FIELD_PASSWORD_IS_EMPTY -> passwordInputLayout.error =
                        getString(R.string.enter_your_password)
                    AuthErrors.SHORT_PASSWORD -> passwordInputLayout.error =
                        getString(R.string.short_password_warning)
                    AuthErrors.WRONG_PASSWORD -> passwordInputLayout.error =
                        getString(R.string.password_is_not_correct)
                    AuthErrors.NONE -> passwordInputLayout.error = null
                    else -> showErrorMessage(it)
                }
            }
        )
        viewModel.authError.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    AuthErrors.INVALID_USER -> emailInputLayout.error =
                        getString(R.string.invalid_user_warning)
                    AuthErrors.TO_MANY_REQUESTS -> emailInputLayout.error =
                        getString(R.string.to_many_requests_warning)
                    AuthErrors.NONE -> emailInputLayout.error = null
                    else -> showErrorMessage(it)
                }
            }
        )

        viewModel.emailError.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    AuthErrors.FIELD_EMAIL_IS_EMPTY -> emailInputLayout.error =
                        getString(R.string.enter_your_email)
                    AuthErrors.NOT_CORRECT_EMAIL -> emailInputLayout.error =
                        getString(R.string.email_is_not_correct)
                    AuthErrors.EMAIL_ALREADY_IN_USE -> emailInputLayout.error =
                        getString(R.string.email_already_in_use_warning)
                    AuthErrors.NONE -> emailInputLayout.error = null
                    else -> showErrorMessage(it)
                }
            }
        )

    }

    private fun showErrorMessage(error: AuthErrors) {
        (activity as? AuthActivity)?.showAuthError(error)
    }

    private fun signIn() {
        val intent = googleSignInClient.signInIntent
        activity?.startActivityForResult(intent, AuthActivity.RC_SIGN_IN)
    }
}