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
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class ForgotPasswordFragment : Fragment() {

    lateinit var submitBtn: Button
    lateinit var emailInputLayout: TextInputLayout
    lateinit var emailInputEditText: TextInputEditText

    @Inject
    lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fr_forgot_password, container, false)
        emailInputLayout = v.findViewById(R.id.emailInput)
        emailInputEditText = v.findViewById(R.id.emailInputEditText)
        submitBtn = v.findViewById(R.id.submitNewPasswordBtn)

        submitBtn.setOnClickListener {
            viewModel.sendPasswordResetEmail()
        }

        emailInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.enterEmail(s.toString())
            }
        })

        return v
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as AuthActivity).authComponent.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.emailError.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    AuthErrors.FIELD_EMAIL_IS_EMPTY -> emailInputLayout.error =
                        getString(R.string.enter_your_email)
                    AuthErrors.NOT_CORRECT_EMAIL -> emailInputLayout.error =
                        getString(R.string.email_is_not_correct)
                    AuthErrors.NONE -> emailInputLayout.error = null
                    else -> emailInputEditText.error = getString(R.string.error)
                }
            }
        )

    }
}