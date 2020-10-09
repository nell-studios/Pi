package nellStudios.tech.pi.work

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import nellStudios.tech.pi.repositories.TopicDetailRepository

class DownloadManager @WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repo: TopicDetailRepository
): Worker(appContext, workerParameters){
    override fun doWork(): Result {
//        try {
//            repo.downloadFile()
//        }
        return Result.Success()
    }

}