package com.wys.learning.utils
import android.os.Handler
import android.os.Looper
import com.example.commonlib.log.LogUtil
import com.wys.learning.Consumer
import java.util.*
import java.util.concurrent.*

object Rx {
    private const val TAG = "Rx"
    private const val MAX_THREAD_COUNT = 20
    private val executorMap = HashMap<String, ScheduledExecutorService?>()
    private val threadPool =
        Executors.newScheduledThreadPool(MAX_THREAD_COUNT, object : ThreadFactory {
            var count = 0
            override fun newThread(r: Runnable?): Thread {
                return Thread(r, "LearningRxThreadPool-${count++}")
            }
        })
    private val handler = Handler(Looper.getMainLooper())

    fun postIO(runnable: Runnable) {
        threadPool.submit { catchAll(runnable) }
    }

    fun runIO(runnable: Runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            catchAll(runnable)
            return
        }
        postIO(runnable)
    }

    fun delayIO(delay: Long, runnable: Runnable) {
        threadPool.schedule({ catchAll(runnable) }, delay, TimeUnit.MILLISECONDS)
    }

    fun postUI(runnable: Runnable) {
        handler.post { catchAll(runnable) }
    }

    fun runUI(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            catchAll(runnable)
            return
        }
        postUI(runnable)
    }

    fun delayUI(delay: Long, runnable: Runnable) {
        handler.postDelayed({ catchAll(runnable) }, delay)
    }

    fun postTag(tag: String, runnable: Runnable) {
        val executor = getExecutor(tag)
        executor.submit { catchAll(runnable) }
    }

    fun runTag(tag: String, runnable: Runnable) {
        if (Thread.currentThread().name == tag) {
            catchAll(runnable)
            return
        }
        postTag(tag, runnable)
    }

    fun delayTag(tag: String, delay: Long, runnable: Runnable) {
        val executor = getExecutor(tag)
        executor.schedule({ catchAll(runnable) }, delay, TimeUnit.MILLISECONDS)
    }

    @Synchronized
    private fun getExecutor(tag: String): ScheduledExecutorService {
        var executor = executorMap[tag]
        if (executor == null) {
            executor = Executors.newSingleThreadScheduledExecutor { r: Runnable? -> Thread(r, tag) }
            executorMap[tag] = executor
        }
        return executor!!
    }

    fun shutdownTag(tag: String) {
        executorMap[tag]?.shutdown()
        executorMap.remove(tag)
    }

    fun shutdownTagNow(tag: String) {
        executorMap[tag]?.shutdownNow()
        executorMap.remove(tag)
    }

    fun shutdownAllTagNow() {
        for (service in executorMap.values) {
            service?.shutdownNow()
        }
        executorMap.clear()
    }

    fun <T> runTag2UI(tag: String, callable: Callable<T?>, consumer: Consumer<T?>) {
        runTag(tag) {
            val t: T? = catchAll(callable)
            postUI { consumer.accept(t) }
        }
    }

    fun <T> postTag2UI(tag: String, callable: Callable<T?>, consumer: Consumer<T?>) {
        postTag(tag) {
            val t: T? = catchAll(callable)
            postUI { consumer.accept(t) }
        }
    }

    fun <T> delayTag2UI(
        tag: String, delay: Long, callable: Callable<T?>, consumer:
        Consumer<T?>
    ) {
        delayTag(tag, delay) {
            val t: T? = catchAll(callable)
            postUI { consumer.accept(t) }
        }
    }

    fun <T> runUI2Tag(tag: String, callable: Callable<T?>, consumer: Consumer<T?>) {
        runUI {
            val t: T? = catchAll(callable)
            postTag(tag) { consumer.accept(t) }
        }
    }

    fun <T> postUI2Tag(tag: String, callable: Callable<T?>, consumer: Consumer<T?>) {
        postUI {
            val t: T? = catchAll(callable)
            postTag(tag) { consumer.accept(t) }
        }
    }

    fun <T> delayUI2Tag(
        tag: String,
        delay: Long,
        callable: Callable<T?>,
        consumer: Consumer<T?>
    ) {
        delayUI(delay) {
            val t: T? = catchAll(callable)
            postTag(tag) { consumer.accept(t) }
        }
    }

    fun <T> runIO2UI(callable: Callable<T?>, consumer: Consumer<T?>) {
        runIO {
            val t: T? = catchAll(callable)
            postUI { consumer.accept(t) }
        }
    }

    fun <T> postIO2UI(callable: Callable<T?>, consumer: Consumer<T?>) {
        postIO {
            val t: T? = catchAll(callable)
            postUI { consumer.accept(t) }
        }
    }

    fun <T> delayIO2UI(delay: Long, callable: Callable<T?>, consumer: Consumer<T?>) {
        delayIO(delay) {
            val t: T? = catchAll(callable)
            postUI { consumer.accept(t) }
        }
    }

    fun <T> runUI2IO(callable: Callable<T?>, consumer: Consumer<T?>) {
        runUI {
            val t: T? = catchAll(callable)
            postIO { consumer.accept(t) }
        }
    }

    fun <T> postUI2IO(callable: Callable<T?>, consumer: Consumer<T?>) {
        postUI {
            val t: T? = catchAll(callable)
            postIO { consumer.accept(t) }
        }
    }

    fun <T> delayUI2IO(delay: Long, callable: Callable<T?>, consumer: Consumer<T?>) {
        delayUI(delay) {
            val t: T? = catchAll(callable)
            postIO { consumer.accept(t) }
        }
    }

    fun <T> catchAll(callable: Callable<T?>): T? {
        try {
            return callable.call()
        } catch (e: Throwable) {
            log(e)
        }
        return null
    }

    /**
     * 按顺序执行，前面代码崩溃不影响后面代码执行, 返回是否全部成功执行
     */
    fun catchAll(vararg runnables: Runnable): Boolean {
        var b = true
        for (runnable in runnables) {
            try {
                runnable.run()
            } catch (e: Throwable) {
                b = false
                log(e)
            }
        }
        return b
    }

    private fun log(throwable: Throwable) {
        LogUtil.e(TAG, throwable.javaClass.toString(), throwable)
    }
}