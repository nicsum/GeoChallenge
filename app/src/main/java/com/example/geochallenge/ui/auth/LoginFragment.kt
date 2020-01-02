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

    lateinit var emailInputLayout: TextInputLayout
    lateinit var emailInputEditText: TextInputEditText
    lateinit var passwordInputLayout: TextInputLayout
    lateinit var passwordInputEditText: TextInputEditText
    lateinit var loginBtn: Button
    lateinit var registerBtn: Button
    lateinit var forgotPasswordBtn: Button
    lateinit var signInBtn: SignInButton

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
            this,
            Observer {
                when (it) {
                    AuthErrors.FIELD_PASSWORD_IS_EMPTY -> passwordInputLayout.error =
                        "Введите пароль"
                    AuthErrors.SHORT_PASSWORD -> passwordInputLayout.error =
                        "Длинна пароля должна быть не меньше 6 знаков"
                    AuthErrors.WRONG_PASSWORD -> passwordInputLayout.error = "Неверный пароль"
                    AuthErrors.NONE -> passwordInputLayout.error = null
                }
            }
        )
        viewModel.authError.observe(
            this,
            Observer {
                when (it) {
                    AuthErrors.INVALID_USER -> emailInputLayout.error =
                        "Пользователь с этой почтой не зарегистрирован"
                    AuthErrors.TO_MANY_REQUESTS -> emailInputLayout.error =
                        "Сервер посчитал вас подозрительным. Попробуйте позже"
                    AuthErrors.NONE -> emailInputLayout.error = null
                }
            }
        )

        viewModel.emailError.observe(
            this,
            Observer {
                when (it) {
                    AuthErrors.FIELD_EMAIL_IS_EMPTY -> emailInputLayout.error = "Введите свою почту"
                    AuthErrors.NOT_CORRECT_EMAIL -> emailInputLayout.error = "Почта введена неверно"
                    AuthErrors.EMAIL_ALREADY_IN_USE -> emailInputLayout.error =
                        "Введенная почта уже занята"
                    AuthErrors.NONE -> emailInputLayout.error = null
                }
            }
        )

    }

    private fun signIn() {
        val intent = googleSignInClient.signInIntent
        activity?.startActivityForResult(intent, AuthActivity.RC_SIGN_IN)
    }
}