package com.example.callidentifier.services

import android.content.Context
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MyWorker(val context: Context,workerParameters: WorkerParameters) : Worker(context,workerParameters)
{
    override fun doWork(): Result
    {
        runBlocking {
            sample()
        }
        return Result.success()
    }

    suspend fun sample(){
        withContext(Dispatchers.Main){
            println("One time work request success")
            Toast.makeText(context.applicationContext, "Background task", Toast.LENGTH_SHORT).show()
        }
    }


}