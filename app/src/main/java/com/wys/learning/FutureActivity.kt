package com.wys.learning

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class FutureActivity : AppCompatActivity() {
    companion object{
        const val TAG = "FutureActivity"
    }
    private val executor: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_future)
        Log.d(TAG,"onCreate ++++++++++++")
        val future = calculate(3)
        Log.d(TAG,"onCreate result : ${future.get()}")
        Log.d(TAG,"onCreate before calculate ++++++++++++")
    }

    private fun calculate(input: Int):Future<Int>{
        return executor.submit(Callable {
//            Log.i(TAG,"calculating input : $input")
//            Thread.sleep(1000L)
            input*input
        })
    }
}