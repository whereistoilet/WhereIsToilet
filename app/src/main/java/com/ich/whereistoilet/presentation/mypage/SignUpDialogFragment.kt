package com.ich.whereistoilet.presentation.mypage

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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpDialogFragment: DialogFragment() {
    private val auth by lazy { Firebase.auth }

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        val view = requireActivity().layoutInflater.inflate(R.layout.layout_signup, null)
        initialize(view)

        builder.setView(view)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    private fun initialize(view: View){
        emailEditText = view.findViewById(R.id.signupEmailEditText)
        passwordEditText = view.findViewById(R.id.signupPasswordEditText)

        view.findViewById<Button>(R.id.signupButton).setOnClickListener {
            auth.createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(requireContext(), "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show()
                        auth.signOut()
                        dismiss()
                    }else{
                        Toast.makeText(requireContext(), "이미 존재하거나 연결할 수 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}