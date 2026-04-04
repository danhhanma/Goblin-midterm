package com.example.goblin_midterm.model

import com.google.firebase.firestore.Exclude

data class Product(
    @get:Exclude var id: String = "",
    var productName: String = "",
    var productType: String = "",
    var price: Double = 0.0,
    var file: String = "" 
)
