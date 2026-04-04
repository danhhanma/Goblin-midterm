package com.example.goblin_midterm.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.goblin_midterm.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun MainApp() {
    // BẠN HÃY ĐỔI EMAIL NÀY THÀNH EMAIL CÁ NHÂN CỦA BẠN ĐỂ LÀM ADMIN NHÉ!
    val ADMIN_EMAIL = "admin@gmail.com" 

    val auth = FirebaseAuth.getInstance()
    var currentUser by remember { mutableStateOf<FirebaseUser?>(auth.currentUser) }

    // Theo dõi trạng thái đăng nhập
    LaunchedEffect(auth) {
        auth.addAuthStateListener { firebaseAuth ->
            currentUser = firebaseAuth.currentUser
        }
    }

    if (currentUser == null) {
        LoginScreen(onAuthSuccess = { user ->
            currentUser = user
        })
    } else {
        // So sánh email hiện tại với ADMIN_EMAIL để cấp quyền
        val isAdmin = currentUser?.email == ADMIN_EMAIL
        val productViewModel: ProductViewModel = viewModel()
        
        ProductScreen(
            viewModel = productViewModel,
            isAdmin = isAdmin,
            onLogout = {
                auth.signOut()
            }
        )
    }
}
