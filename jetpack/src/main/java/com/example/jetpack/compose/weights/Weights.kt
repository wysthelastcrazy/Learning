package com.example.jetpack.compose.weights

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 *@author wangyasheng
 *@date 2023/10/12
 */
@Preview
@Composable
fun HelloContent(){
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello!",
            modifier = Modifier.padding(bottom = 8.dp, top = 0.dp, start = 0.dp, end = 0.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Text(text = "Name")
            })
    }
}