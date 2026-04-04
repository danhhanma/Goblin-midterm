package com.example.goblin_midterm.model

import com.google.firebase.firestore.Exclude

data class Product(
    @get:Exclude var id: String = "",
    var tenSanPham: String = "",
    var loaiSanPham: String = "",
    var gia: Long = 0L,
    var file: String = ""
)
