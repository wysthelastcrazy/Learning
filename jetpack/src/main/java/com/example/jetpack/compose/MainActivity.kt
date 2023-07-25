package com.example.jetpack.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.ui.theme.LearningTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch {

        }
        setContent {
            LearningTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Column() {
        var number by remember {
            mutableStateOf(0)
        }
        Counter(number = number) {
            number++
        }
        LazyColumn(){
            items(items = getData()){
                    data -> ItemView(title = data)
            }
        }
    }

}
//计数器
@Composable
fun Counter(number: Int, onClickValue: () -> Unit){
    Column() {
        Text(text = "当前数值：$number")
        Button(onClick = onClickValue ) {
            Text(text = "add")
        }
    }
}
@Composable
fun ItemView(title : String){
    var expanded by remember{
        mutableStateOf(false)
    }
    Row(modifier = Modifier
        .background(Color.Red)
        .padding(10.dp)
    ) {
        Text(text = title, fontSize = 16.sp, color = Color.White, modifier = Modifier.weight(1f))
        Button(onClick = { 
            expanded = !expanded
        }) {
            Text(text = if (!expanded) "查看详情" else "收起", color = Color.White)
        }
    }
    if (expanded){
        Text(text = "我是详情哈哈哈哈")
    }
}
//获取数据
fun getData():List<String>{
    return List(20){
        "Compose课程第${it.add()}课，快来学习吧～"
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LearningTheme {
        Greeting("Android")
    }
}
//扩展函数
fun Int.add(): Int{
    return this + 1
}