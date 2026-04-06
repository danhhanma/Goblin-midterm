package com.example.goblin_midterm.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goblin_midterm.ui.components.CustomInputField
import com.example.goblin_midterm.ui.components.ProductItem
import com.example.goblin_midterm.viewmodel.ProductViewModel

@Composable
fun ProductScreen(viewModel: ProductViewModel, isAdmin: Boolean, onLogout: () -> Unit) {
    val context = LocalContext.current
    val productList by viewModel.productList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

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
        
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Dữ liệu sản phẩm",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onLogout,
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text("ĐĂNG XUẤT", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isAdmin) {
            CustomInputField(
                value = viewModel.tenSanPham.value,
                onValueChange = { viewModel.tenSanPham.value = it },
                placeholder = "Ap phong nam"
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            CustomInputField(
                value = viewModel.loaiSanPham.value,
                onValueChange = { viewModel.loaiSanPham.value = it },
                placeholder = "Thoi trang nu"
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            CustomInputField(
                value = viewModel.gia.value,
                onValueChange = { viewModel.gia.value = it },
                placeholder = "300000"
            )
            Spacer(modifier = Modifier.height(8.dp))

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
                val displayFileName = if (viewModel.fileName.value.isEmpty()) "pexels-blaque...jpg" else viewModel.fileName.value
                Text(
                    text = displayFileName,
                    fontSize = 12.sp,
                    color = if (viewModel.fileName.value.isEmpty()) Color.LightGray else Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveProduct()
                },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue 
                ),
                contentPadding = PaddingValues(vertical = 12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (viewModel.editingId.value == null) "THÊM SẢN PHẨM" else "CẬP NHẬT",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Text(
                text = "Bạn đang xem dưới tư cách Khách (Chỉ xem).",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 16.dp)
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

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(productList) { product ->
                ProductItem(
                    product = product,
                    isAdmin = isAdmin,
                    onEdit = {
                        if (isAdmin) viewModel.editProduct(product)
                    },
                    onDelete = {
                        if (isAdmin) viewModel.deleteProduct(product.id)
                    }
                )
            }
        }
    }
}
