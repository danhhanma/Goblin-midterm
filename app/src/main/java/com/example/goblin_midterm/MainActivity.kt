package com.example.goblin_midterm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.goblin_midterm.ui.screens.MainApp
import com.example.goblin_midterm.ui.theme.GoblinmidtermTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoblinmidtermTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Khởi động MainApp ở đây (nó tự quản lý padding hoặc bạn có thể pass padding vào tuỳ layout, 
                    // nhưng MainApp hiện đang dùng Modifier.fillMaxSize() là okay)
                    MainApp()
                }
            }
        }
    }
}