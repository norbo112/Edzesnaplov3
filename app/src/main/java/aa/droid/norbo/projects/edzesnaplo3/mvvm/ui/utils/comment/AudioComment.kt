package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.comment

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.widget.Toast
import java.io.File
import java.io.IOException

class AudioComment (private val context: Context) {
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null
    var file: File? = null

    fun recordComment(filename: String) {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder!!.setOutputFile(getFile(filename))
        try {
            mediaRecorder!!.prepare()
        } catch (e: IOException) {
            Toast.makeText(context, "Hiba történt a rögzítés közben :(", Toast.LENGTH_SHORT).show()
        }
        mediaRecorder!!.start()
    }

    private fun getFile(filename: String): File {
        file = File(context.getExternalFilesDir(null), filename)
        return file as File
    }

    fun stopRecord() {
        mediaRecorder!!.stop()
        mediaRecorder!!.release()
    }

    fun startPlaying(filename: String, whencomplete: MediaPlayer.OnCompletionListener) {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnCompletionListener(whencomplete)
        try {
            mediaPlayer!!.setDataSource(filename)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
        } catch (e: IOException) {
            Toast.makeText(context, "Hiba történt a lejátszás közben :(", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteComment(filename: String): Boolean {
        val file = File(filename)
        this.file = null
        return file.delete()
    }

    fun isPlayable(filename: String) : Boolean {
        return File(filename).exists()
    }
}