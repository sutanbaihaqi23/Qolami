package com.example.TugasAkhir.qolami.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun login(email: String, password: String, onComplete: (Boolean) -> Unit) {
       coroutineScope.launch {
           try {
               auth.signInWithEmailAndPassword(email, password).await()
               onComplete(true)
           } catch (e: Exception) {
               Log.e("AuthViewModel", "Login failed: ${e.message}")
               onComplete(false)
           }
       }
   }

    fun getUserUid(): String? {
        return auth.currentUser?.uid ?: throw IllegalStateException("User is not logged in.")
    }



    fun changePassword(oldPassword: String, newPassword: String, onComplete: (Boolean, String?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                val credential = EmailAuthProvider.getCredential(email, oldPassword)
                coroutineScope.launch {
                    try {
                        // Reauthenticate dengan password lama
                        currentUser.reauthenticate(credential).await()
                        // Jika berhasil, ganti password
                        currentUser.updatePassword(newPassword).await()
                        onComplete(true, null) // Ganti password sukses
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Password change failed: ${e.message}")
                        onComplete(false, e.message) // Error saat reauth atau update
                    }
                }
            } else {
                onComplete(false, "User email is null.")
            }
        } else {
            onComplete(false, "User is not logged in.")
        }
    }

    fun register(fullname: String, email: String, password: String, onComplete: (Boolean) -> Unit) {
        coroutineScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                val userId = auth.currentUser?.uid ?: return@launch
                val user = hashMapOf("fullname" to fullname)
                firestore.collection("users").document(userId).set(user).await()
                onComplete(true)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login failed: ${e.message}")
                onComplete(false)
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
    }


    fun getFullname(onComplete: (String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        Log.d("AuthViewModel", "Fetching fullname for UID: $userId")
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val doc = firestore.collection("users").document(userId).get().await()
                val fullname = doc.getString("fullname")
                Log.d("AuthViewModel", "Fullname retrieved: $fullname")
                onComplete(fullname)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error fetching fullname: ${e.message}")
                onComplete(null)
            }
        }
    }
}
