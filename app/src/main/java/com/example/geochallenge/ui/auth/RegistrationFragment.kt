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
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fr_registration.*
import javax.inject.Inject

class RegistrationFragment : Fragment() {

    private lateinit var passwordInputEditText: TextInputEditText
    private lateinit var emailInputEditText: TextInputEditText
    private lateinit var usernameInputEditText: TextInputEditText
    private lateinit var registrationBtn: Button

    @Inject
    lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fr_registration, container, false)

        registrationBtn = v.findViewById(R.id.registrationBtn)

        passwordInputEditText = v.findViewById(R.id.passwordInputEditText)
        emailInputEditText = v.findViewById(R.id.emailInputEditText)
        usernameInputEditText = v.findViewById(R.id.usernameInputEditText)

        passwordInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.enterPassword(s.toString())
            }
        })
        emailInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.enterEmail(s.toString())
            }
        })

        usernameInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.enterUsername(s.toString())
            }
        })

        registrationBtn.setOnClickListener {
            viewModel.registration()
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
                    AuthErrors.FIELD_PASSWORD_IS_EMPTY -> passwordInput.error =
                        getString(R.string.enter_your_password)
                    AuthErrors.SHORT_PASSWORD -> passwordInput.error =
                        getString(R.string.short_password_warning)
                    AuthErrors.NONE -> passwordInput.error = null
                    else -> showErrorMessage(it)
                }
            }
        )

        viewModel.emailError.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    AuthErrors.FIELD_EMAIL_IS_EMPTY -> emailInput.error =
                        getString(R.string.enter_your_email)
                    AuthErrors.NOT_CORRECT_EMAIL -> emailInput.error =
                        getString(R.string.email_is_not_correct)
                    AuthErrors.EMAIL_ALREADY_IN_USE -> emailInput.error =
                        getString(R.string.email_already_in_use_warning)
                    AuthErrors.NONE -> emailInput.error = null
                    else -> showErrorMessage(it)
                }
            }
        )

        viewModel.usernameError.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    AuthErrors.USERNAME_ALREADY_IN_USE ->
                        usernameInput.error =
                            getString(
                                R.string.username_already_in_use_warning,
                                usernameInputEditText.text
                            )
                    AuthErrors.FIELD_USERNAME_IS_EMPTY ->
                        usernameInput.error = getString(R.string.enter_your_username)
                    AuthErrors.NONE -> usernameInput.error = null
                    else -> showErrorMessage(it)
                }
            }
        )
    }

    private fun showErrorMessage(error: AuthErrors) {
        (activity as? AuthActivity)?.showAuthError(error)
    }
}