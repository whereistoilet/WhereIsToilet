package com.ich.whereistoilet.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ich.whereistoilet.R


class LoginDialogFragment(private val loginSuccess: () -> Unit): DialogFragment() {
    private val auth by lazy { Firebase.auth }

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        val view = requireActivity().layoutInflater.inflate(R.layout.layout_login, null)
        initialize(view)

        builder.setView(view)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    private fun initialize(view: View){
        emailEditText = view.findViewById(R.id.loginEmailEditText)
        passwordEditText = view.findViewById(R.id.loginPasswordEditText)

        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
            auth.signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(requireContext(), "로그인에 성공하셨습니다", Toast.LENGTH_SHORT).show()
                        loginSuccess()
                        dismiss()
                    }else{
                        Toast.makeText(requireContext(), "로그인에 실패하셨습니다", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}