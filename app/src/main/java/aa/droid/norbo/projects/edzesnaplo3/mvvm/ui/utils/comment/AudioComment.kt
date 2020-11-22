package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.comment

import aa.droid.norbo.projects.edzesnaplo3.R
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.CommentActivity
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.NaploDetailsActivity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.io.File
import java.io.IOException
import javax.inject.Inject

@ActivityScoped
class AudioComment @Inject constructor(@ActivityContext val context: Context) {
    private val TAG: String? = "AudioComment"
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
            mediaRecorder!!.start()
        } catch (e: IOException) {
            Toast.makeText(context, "Hiba történt a rögzítés közben :(", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            Log.e(TAG, "recordComment: hiba történt", e)
        }
    }

    private fun getFile(filename: String): File {
        var _file: String = ""
        if (filename.startsWith("/storage/")) {
            _file = filename.subSequence(filename.lastIndexOf('/') + 1, filename.length).toString()
        } else {
            _file = filename
        }

        file = File(context.getExternalFilesDir(null), _file)
        if (!file!!.exists())
            file!!.createNewFile()
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

    fun isPlayable(filename: String): Boolean {
        return File(filename).exists()
    }

    fun askNewCommentDialog(activity: Activity, newFileName: String) {
        val builder = AlertDialog.Builder(context).apply {
            setTitle("Audio Comment")
            setMessage("Nincs hanganyag rögzítve, szeretnél újat?")
            setPositiveButton("igen") { dialog, which ->
                val intent = Intent(context, CommentActivity::class.java).apply {
                    putExtra(NaploDetailsActivity.EXTRA_NEW_COMMENT, true)
                    putExtra(NaploDetailsActivity.EXTRA_FILE_NAME, newFileName)
                }
                activity.startActivityForResult(intent, NaploDetailsActivity.COMMENT)
                activity.overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity)
            }
            setNeutralButton("mégse") { dialog, which ->
                dialog.dismiss()
            }
        }
        builder.show()
    }

    fun gotoCommentDialog(activity: Activity, commentFilePath: String, naploDatum: Long) {
        val intent = Intent(context, CommentActivity::class.java).apply {
            putExtra(NaploDetailsActivity.EXTRA_NAPLO_DATUM, naploDatum)
            putExtra(NaploDetailsActivity.EXTRA_ONLY_PLAY, true)
            putExtra(NaploDetailsActivity.EXTRA_FILE_NAME, commentFilePath)
        }
        activity.startActivityForResult(intent, NaploDetailsActivity.COMMENT)
        activity.overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity)
    }
}