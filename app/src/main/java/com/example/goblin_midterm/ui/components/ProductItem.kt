package com.example.goblin_midterm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.goblin_midterm.model.Product

@Composable
fun ProductItem(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RectangleShape)
            .padding(4.dp), // Padding nhỏ bên trong theo yêu cầu
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cột 1 (Trái): Hình vuông 60dp, dùng Coil load ảnh
        AsyncImage(
            model = product.file,
            contentDescription = "Hình sản phẩm",
            modifier = Modifier
                .size(60.dp)
                .background(Color.LightGray) // Ảnh nền phòng trường hợp chưa có file
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Cột 2 (Giữa): Column chứa 3 dòng chữ, các dòng khít nhau
        Column(
            modifier = Modifier.weight(1f)
        ) {
            val smallFontSize = 12.sp

            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = smallFontSize)) {
                    append("Tên sp: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = smallFontSize)) {
                    append(product.productName)
                }
            })

            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = smallFontSize)) {
                    append("Giá sp: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = smallFontSize)) {
                    // Xử lý hiển thị số dạng chuỗi, bỏ phần thập phân .0
                    val priceValue = if (product.price % 1 == 0.0) {
                        product.price.toLong().toString()
                    } else {
                        product.price.toString()
                    }
                    append(priceValue)
                }
            })

            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = smallFontSize)) {
                    append("Loại sp: ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = smallFontSize)) {
                    append(product.productType)
                }
            })
        }

        // Cột 3 (Phải): Đường kẻ dọc ngang Column (Divider) + Nút sửa xóa
        Row(
            modifier = Modifier.height(60.dp), // Giữ bằng height của image để line dọc thẳng đẹp
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Đường kẻ dọc ngăn cách
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(60.dp)
                    .background(Color.LightGray)
            )
            
            // Cột chứa 2 nút hình chữ nhật không bo tròn
            Column(
                modifier = Modifier
                    .width(40.dp)
                    .height(60.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Nút trên (Sửa)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable { onEdit() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Sửa",
                        tint = Color(0xFFF0B305), // Icon cây bút màu vàng giống ảnh
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Đường kẻ ngang ngăn cách giữa 2 nút (để trông vuông vức giống bảng)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )

                // Nút dưới (Xóa)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable { onDelete() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Xóa",
                        tint = Color.Red, // Icon thùng rác màu đỏ
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
