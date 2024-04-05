package com.example.callidentifier.services

import android.app.job.JobParameters
import android.app.job.JobService

class SamJobService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        println("job started")
        return  false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        println("job stopped")
        return false
    }
}