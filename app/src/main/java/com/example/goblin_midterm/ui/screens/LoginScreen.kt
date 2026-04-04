package com.example.goblin_midterm.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onAuthSuccess: (FirebaseUser) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) } // Chuyển đổi Đăng nhập / Đăng ký

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon trang trí
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFE3F2FD), shape = RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock Icon",
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tiêu đề
        Text(
            text = if (isLoginMode) "ĐĂNG NHẬP" else "ĐĂNG KÝ TÀI KHOẢN",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        Text(
            text = "Vui lòng nhập thông tin để tiếp tục",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        // Ô nhập Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Địa chỉ Email") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Email") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ô nhập mật khẩu
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = if (errorMessage.contains("thành công")) Color(0xFF4CAF50) else Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Nút chức năng chính
        Button(
            onClick = {
                val trimEmail = email.trim()
                if (trimEmail.isNotEmpty() && password.isNotEmpty()) {
                    if (isLoginMode) {
                        auth.signInWithEmailAndPassword(trimEmail, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    auth.currentUser?.let { onAuthSuccess(it) }
                                } else {
                                    errorMessage = "Đăng nhập thất bại: Xin kiểm tra lại"
                                }
                            }
                    } else {
                        auth.createUserWithEmailAndPassword(trimEmail, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    errorMessage = "Đăng ký thành công! Đang tự động đăng nhập..."
                                    auth.currentUser?.let { onAuthSuccess(it) }
                                } else {
                                    errorMessage = "Đăng ký thất bại: Tài khoản có thể đã tồn tại"
                                }
                            }
                    }
                } else {
                    errorMessage = "Vui lòng điền đầy đủ thông tin"
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = if (isLoginMode) "ĐĂNG NHẬP" else "ĐĂNG KÝ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút chuyển đổi Mode
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isLoginMode) "Chưa có tài khoản? " else "Đã có tài khoản? ",
                color = Color.Gray
            )
            Text(
                text = if (isLoginMode) "Đăng ký ngay" else "Đăng nhập",
                color = Color(0xFF1976D2),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    isLoginMode = !isLoginMode
                    errorMessage = ""
                }
            )
        }
    }
}
