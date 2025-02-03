package com.example.TugasAkhir.qolami.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _fullname = MutableLiveData<String?>()
    val fullname: LiveData<String?> get() = _fullname

    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        // Validasi email dan password
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onComplete(false, "Email tidak valid.")
            return
        }
        if (password.length < 8) {
            onComplete(false, "Password minimal 8 karakter.")
            return
        }

        coroutineScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                onComplete(true, null) // Login berhasil
            } catch (e: FirebaseAuthInvalidUserException) {
                // Email tidak ditemukan
                onComplete(false, "Email atau password \nyang dimasukkan salah.")
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                // Password salah
                onComplete(false, "Email atau password \nyang dimasukkan salah.")
            } catch (e: FirebaseNetworkException) {
                // Masalah koneksi internet
                onComplete(false, "Tidak dapat terhubung ke server. \nPeriksa koneksi Anda.")
            } catch (e: Exception) {
                // Error umum lainnya
                onComplete(false, "Terjadi kesalahan, \ncoba lagi nanti.")
            }
        }
    }




    fun getUserUid(): String? {
        return auth.currentUser?.uid ?: throw IllegalStateException("User is not logged in.")
    }

    fun fetchFullname() {
        val userId = auth.currentUser?.uid ?: return
        Log.d("AuthViewModel", "Fetching fullname for UID: $userId")
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val doc = firestore.collection("users").document(userId).get().await()
                val fullname = doc.getString("fullname")
                Log.d("AuthViewModel", "Fullname retrieved: $fullname")
                _fullname.postValue(fullname) // Update LiveData
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error fetching fullname: ${e.message}")
                _fullname.postValue(null) // Update LiveData dengan null jika error
            }
        }
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

    fun register(fullname: String, email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        coroutineScope.launch {
            try {
                // Cek apakah email sudah terdaftar di Firestore (opsional)
                val querySnapshot = firestore.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    onComplete(false, "Email sudah terdaftar.")
                    return@launch
                }

                // Buat akun baru di Firebase Auth
                auth.createUserWithEmailAndPassword(email, password).await()
                val userId = auth.currentUser?.uid ?: throw Exception("User ID not found.")

                // Simpan data pengguna di Firestore
                val user = hashMapOf(
                    "fullname" to fullname,
                    "email" to email // Simpan email di Firestore untuk pengecekan di masa depan
                )
                firestore.collection("users").document(userId).set(user).await()

                onComplete(true, null) // Registrasi berhasil
            } catch (e: FirebaseAuthUserCollisionException) {
                Log.e("AuthViewModel", "Email already in use: ${e.message}")
                onComplete(false, "Email sudah terdaftar. Silakan login atau gunakan email lain.")
            } catch (e: FirebaseNetworkException) {
                Log.e("AuthViewModel", "Network error: ${e.message}")
                onComplete(false, "Koneksi internet bermasalah.")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Register failed: ${e.message}")
                onComplete(false, "Terjadi kesalahan, coba lagi nanti.")
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
