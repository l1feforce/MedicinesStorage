package ru.spbstu.gusev.medicinesstorage.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.databinding.FragmentAccountBinding
import ru.spbstu.gusev.medicinesstorage.extensions.showErrorSnackbar
import ru.spbstu.gusev.medicinesstorage.utils.livedata.EventObserver

class AccountFragment : Fragment() {
    companion object {
        const val RC_SIGN_IN = 1
    }

    val viewModel: AccountViewModel by viewModel()
    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.calculateStatistics()

        viewModel.onSignInEvent.observe(viewLifecycleOwner, EventObserver {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build()

            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        })
        viewModel.onSignOutEvent.observe(viewLifecycleOwner, EventObserver {
            Firebase.auth.signOut()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build()

            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            googleSignInClient.signOut()
            viewModel.currentUser.value = null
        })
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = Firebase.auth.currentUser
                    user?.let {
                        viewModel.currentUser.value = it
                        viewModel.insertMedicinesToRemote()
                    }
                } else {
                    showErrorSnackbar(requireView(), task.exception?.localizedMessage ?: "Error")
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                showErrorSnackbar(requireView(), R.string.account_google_sign_in_error)
            }
        }
    }
}