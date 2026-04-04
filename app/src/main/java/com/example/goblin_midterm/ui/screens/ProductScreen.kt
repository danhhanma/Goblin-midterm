package com.example.goblin_midterm.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goblin_midterm.ui.components.ProductItem
import com.example.goblin_midterm.viewmodel.ProductViewModel

@Composable
fun ProductScreen(viewModel: ProductViewModel) {
    val context = LocalContext.current
    val productList by viewModel.productList.collectAsState()

    // Bắt đầu load sản phẩm real-time
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    // Trình chọn ảnh từ máy hiển thị
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.fileUriString.value = it.toString()
            var name = "image_selected.jpg"
            val cursor = context.contentResolver.query(it, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = cursor.getString(nameIndex)
                }
                cursor.close()
            }
            viewModel.fileName.value = name
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Dữ liệu sản phẩm",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3 ô TextField bọc Basic cho giống viền Form cũ nhất
        CustomBasicTextField(
            value = viewModel.productName.value,
            onValueChange = { viewModel.productName.value = it },
            placeholder = "Ap phong nam"
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        CustomBasicTextField(
            value = viewModel.productType.value,
            onValueChange = { viewModel.productType.value = it },
            placeholder = "Thoi trang nu"
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        CustomBasicTextField(
            value = viewModel.price.value,
            onValueChange = { viewModel.price.value = it },
            placeholder = "300000"
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Hàng chọn file
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RectangleShape)
                .clickable { launcher.launch("image/*") }
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = "Chọn file",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            val displayFileName = if (viewModel.fileName.value.isEmpty()) "pexels-blaque-x-...jpg" else viewModel.fileName.value
            Text(
                text = displayFileName,
                fontSize = 12.sp,
                color = if (viewModel.fileName.value.isEmpty()) Color.LightGray else Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút THÊM xanh nhạt Android 
        Button(
            onClick = {
                viewModel.addProduct()
            },
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3) 
            ),
            contentPadding = PaddingValues(vertical = 12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "THÊM SẢN PHẨM",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Danh sách sản phẩm:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // List sản phẩm map với component
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(productList) { product ->
                ProductItem(
                    product = product,
                    onEdit = {
                        // Logic điền thông tin fill ngược lại lên ô Text khi bấm sửa (Cơ bản)
                        viewModel.productName.value = product.productName
                        viewModel.productType.value = product.productType
                        viewModel.price.value = if (product.price % 1 == 0.0) product.price.toLong().toString() else product.price.toString()
                        viewModel.fileUriString.value = product.file
                    },
                    onDelete = {
                        viewModel.deleteProduct(product.id)
                    }
                )
            }
        }
    }
}

@Composable
fun CustomBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF90CAF9), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 10.dp)
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
                innerTextField() 
            }
        }
    )
}
