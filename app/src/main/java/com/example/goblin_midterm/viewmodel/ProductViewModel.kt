package com.example.goblin_midterm.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.goblin_midterm.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel : ViewModel() {

    // Khai báo các MutableState theo yêu cầu
    var productName = mutableStateOf("")
    var productType = mutableStateOf("")
    var price = mutableStateOf("")
    var fileUriString = mutableStateOf("")
    var fileName = mutableStateOf("")

    private val db = FirebaseFirestore.getInstance()
    private val productRef = db.collection("Products")

    // State flow chứa danh sách Load realtime từ Database Firebase
    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> get() = _productList

    // Hàm lắng nghe realtime cho danh sách sản phẩm
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

    // Hàm thêm sản phẩm
    fun addProduct() {
        val priceValue = price.value.toDoubleOrNull() ?: 0.0
        val data = hashMapOf(
            "productName" to productName.value,
            "productType" to productType.value,
            "price" to priceValue,
            "file" to fileUriString.value
        )
        
        productRef.add(data).addOnSuccessListener {
            clearInput()
        }
    }

    // Hàm cập nhật sản phẩm theo id
    fun updateProduct(id: String) {
        val priceValue = price.value.toDoubleOrNull() ?: 0.0
        val data = hashMapOf(
            "productName" to productName.value,
            "productType" to productType.value,
            "price" to priceValue,
            "file" to fileUriString.value
        )
        productRef.document(id).update(data as Map<String, Any>).addOnSuccessListener {
            clearInput()
        }
    }

    // Hàm xóa sản phẩm
    fun deleteProduct(id: String) {
        productRef.document(id).delete()
    }

    // Hàm phụ trợ để reset giá trị trong các input sau khi thực hiện
    private fun clearInput() {
        productName.value = ""
        productType.value = ""
        price.value = ""
        fileUriString.value = ""
        fileName.value = ""
    }
}
