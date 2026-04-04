package com.example.goblin_midterm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.goblin_midterm.ui.components.CustomInputField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun LoginScreen(onAuthSuccess: (FirebaseUser) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("XÁC THỰC QUẢN TRỊ", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))
        
        CustomInputField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email (vd: admin@gmail.com)"
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        CustomInputField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Mật khẩu"
        )
        
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if(email.isNotEmpty() && password.isNotEmpty()){
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    auth.currentUser?.let { onAuthSuccess(it) }
                                } else {
                                    errorMessage = "Đăng nhập thất bại: ${task.exception?.message}"
                                }
                            }
                    }
                },
                shape = RectangleShape,
                modifier = Modifier.weight(1f)
            ) {
                Text("ĐĂNG NHẬP")
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = {
                    if(email.isNotEmpty() && password.isNotEmpty()){
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    errorMessage = "Đăng ký thành công! Vui lòng Đăng nhập."
                                } else {
                                    errorMessage = "Đăng ký thất bại: ${task.exception?.message}"
                                }
                            }
                    }
                },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.weight(1f)
            ) {
                Text("ĐĂNG KÝ")
            }
        }
    }
}
