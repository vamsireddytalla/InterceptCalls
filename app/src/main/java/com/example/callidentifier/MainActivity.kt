package com.example.callidentifier

import android.Manifest
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.callidentifier.adapters.MyAdapter
import com.example.callidentifier.databinding.ActivityMainBinding
import com.example.callidentifier.network.ApiClient
import com.example.callidentifier.pojo.Data
import com.example.callidentifier.pojo.NetworkResModel
import com.example.callidentifier.services.MyWorker
import com.example.callidentifier.services.SamJobService
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.b1.setOnClickListener {
            requestPermissions()
            runBlocking {
                callNetworkOperation()
            }
        }

        val litstItems:List<Triple<String,String,String>> = getCallLogs(this.contentResolver)
        val myAdapter = MyAdapter(litstItems){sampleData->
            Toast.makeText(this, sampleData.first, Toast.LENGTH_SHORT).show()
            apiCall(sampleData.first)
        }
        binding.rc1.adapter = myAdapter

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInitialDelay(120, TimeUnit.SECONDS)
            .build()

        //enque the work
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)


//        val currentTimeMillis = System.currentTimeMillis()
//        val twoMinutesInMillis = 2 * 60 * 1000 // 2 minutes in milliseconds
//
//        val delay = twoMinutesInMillis - (currentTimeMillis % twoMinutesInMillis)
//
//        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
//        val componentName = ComponentName(this, SamJobService::class.java)
//        val jobInfo = JobInfo.Builder(18, componentName)
//            .setMinimumLatency(delay) // Delay until the next 2-minute mark
//            .setRequiresCharging(true)
//            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//            .build()
//
//
//        jobScheduler.schedule(jobInfo)



    }

    private fun requestPermissions() {

        val permissionsToRequest = mutableListOf<String>()

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CALL_LOG)!=PackageManager.PERMISSION_GRANTED){
            permissionsToRequest.add(Manifest.permission.READ_CALL_LOG)
        }

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissionsToRequest.add(Manifest.permission.READ_PHONE_STATE)
        }

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED){
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.MANAGE_OWN_CALLS)!=PackageManager.PERMISSION_GRANTED){
//            permissionsToRequest.add(Manifest.permission.MANAGE_OWN_CALLS)
//        }

        if(permissionsToRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(this,permissionsToRequest.toTypedArray(),REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    println("Permissions granted")
                } else {
                    // Permission denied
                    println("Permission denied")
                }
            }
        }
    }

     fun callNetworkOperation() {
        if (binding.et1.text.trim().length >= 10) {
          apiCall(binding.et1.text.toString())
        } else {
            Snackbar.make(binding.root, "Enter 10 digit indian number", Snackbar.LENGTH_LONG).show()
        }
    }

    fun apiCall(data:String){
        try {
            ApiClient.apiService.searchNoDetails(q = data).enqueue(object :Callback<NetworkResModel>{
                override fun onResponse(call: Call<NetworkResModel>, response: retrofit2.Response<NetworkResModel>) {
                    if (response.isSuccessful){
                        binding.result.text = response.body()?.let {
                            var res = ""
                            val obj: Data = it.data[0]
                            res = obj.name+"\n"+obj.phones[0].carrier+"\n"+obj.addresses[0].city+"\n"+obj.addresses[0].address
                            res
                        }
                    }else{
                        Snackbar.make(binding.root, response.message(), Snackbar.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<NetworkResModel>, t: Throwable) {
                    Snackbar.make(binding.root, t.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            })
        } catch (e: Exception) {
            Snackbar.make(binding.root, e.message.toString(), Snackbar.LENGTH_LONG).show()
        }
    }


     fun getCallLogs(contentResolver:ContentResolver) : List<Triple<String,String,String>>
     {
         val callLogsList = mutableListOf<Triple<String,String,String>>()

         val projection = arrayOf(CallLog.Calls.NUMBER,CallLog.Calls.CACHED_NAME,CallLog.Calls.DATE)
         val selection = "${CallLog.Calls.NUMBER} = ?"
         val selectionArgs = arrayOf("+91yourNo")
         val sortOrder = "${CallLog.Calls.DATE} DESC"

         val cursor = contentResolver.query(
             CallLog.Calls.CONTENT_URI,
             projection,
             null,
             null,
             sortOrder
         )

         println(cursor.toString())

         cursor?.use {
             println(it.toString())
             val indexNoId = it.getColumnIndex(CallLog.Calls.NUMBER)
             val indexNameId = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
             val dateIndexId = it.getColumnIndex(CallLog.Calls.DATE)

             while (it.moveToNext()){
                 val no = it.getString(indexNoId)
                 val name = it.getString(indexNameId) ?: ""
                 val date = it.getString(dateIndexId)

                 callLogsList.add(Triple(no,name,date))

             }

         }
         return callLogsList

     }

}