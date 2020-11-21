package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.comment

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AudioComment @Inject constructor(@ActivityContext val context: Context) {
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null

    fun recordComment(filename: String) {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder!!.setOutputFile(filename)
        mediaRecorder!!.prepare()
        mediaRecorder!!.start()
    }

    fun stopRecord() {
        mediaRecorder!!.stop()
        mediaRecorder!!.release()
    }

    fun startPlaying(filename: String) {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(filename)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()
    }

    fun stopPlaying() {
        if(mediaPlayer != null)
            mediaPlayer?.release()
        mediaPlayer = null
    }
}