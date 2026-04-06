package com.example.goblin_midterm.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.goblin_midterm.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel : ViewModel() {

    var tenSanPham = mutableStateOf("")
    var loaiSanPham = mutableStateOf("")
    var gia = mutableStateOf("")
    var fileUriString = mutableStateOf("")
    var fileName = mutableStateOf("")
    var editingId = mutableStateOf<String?>(null) // Để biết đang Thêm hay Sửa

    private val db = FirebaseFirestore.getInstance()
    private val productRef = db.collection("Products")

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> get() = _productList

    fun loadProducts() {
        productRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = ArrayList<Product>()
                for (doc in snapshot.documents) {
                    val product = doc.toObject(Product::class.java)
                    if (product != null) {
                        product.id = doc.id
                        list.add(product)
                    }
                }
                _productList.value = list
            }
        }
    }

    // Xử lý cả Thêm và Sửa dựa vào editingId
    fun saveProduct() {
        val price = gia.value.toLongOrNull() ?: 0L
        val data = hashMapOf(
            "tenSanPham" to tenSanPham.value,
            "loaiSanPham" to loaiSanPham.value,
            "gia" to price,
            "file" to fileUriString.value
        )
        
        if (editingId.value == null) {
            productRef.add(data).addOnSuccessListener {
                clearInput()
            }
        } else {
            productRef.document(editingId.value!!).update(data as Map<String, Any>).addOnSuccessListener {
                clearInput()
            }
        }
    }

    // Đổ data lên form
    fun editProduct(product: Product) {
        editingId.value = product.id
        tenSanPham.value = product.tenSanPham
        loaiSanPham.value = product.loaiSanPham
        gia.value = product.gia.toString()
        fileUriString.value = product.file
        fileName.value = "ảnh đính kèm..."
    }

    fun deleteProduct(id: String) {
        productRef.document(id).delete()
    }

    fun clearInput() {
        tenSanPham.value = ""
        loaiSanPham.value = ""
        gia.value = ""
        fileUriString.value = ""
        fileName.value = ""
        editingId.value = null
    }
}
