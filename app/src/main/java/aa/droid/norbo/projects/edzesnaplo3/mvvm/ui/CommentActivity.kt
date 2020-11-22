package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui

import aa.droid.norbo.projects.edzesnaplo3.R
import aa.droid.norbo.projects.edzesnaplo3.databinding.ActivityCommentBinding
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.comment.AudioComment
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import java.io.File

class CommentActivity : BaseActivity<ActivityCommentBinding>(R.layout.activity_comment) {
    private var audioComment: AudioComment? = null
    private var felvetelVan: Boolean = false
    private var playAudio: Boolean = false
    private var filename: String = ""
    private var naplodatumStr: String = ""

    override fun setupCustomActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Audio Comment"
        supportActionBar?.setIcon(R.drawable.ic_headset)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle: Bundle? = intent.extras
        filename = bundle!!.getString("extra_file_name", "")
        naplodatumStr = filename
        audioComment = AudioComment(this)

        binding.filename.text = filename

        if (filename.isNotEmpty() && audioComment!!.isPlayable(filename)) {
            binding.btnStartRecord.isEnabled = false
            binding.btnDeleteRecord.isEnabled = true
        }

        binding.btnStartRecord.setOnClickListener {
            felvetelVan = true
            audioComment!!.recordComment(binding.filename.text.toString())
            enableButtons()
        }

        binding.btnStopRecord.setOnClickListener {
            felvetelVan = false
            audioComment!!.stopRecord()
            if (audioComment?.file != null) {
                binding.filename.text = audioComment!!.file!!.absolutePath
                filename = audioComment!!.file!!.absolutePath
                binding.btnDeleteRecord.isEnabled = true
            }
            enableButtons()
        }

        binding.btnStartPlay.setOnClickListener {
            if (filename.isNotEmpty()) {
                if (audioComment!!.isPlayable(filename)) {
                    binding.progressBar.visibility = View.VISIBLE
                    audioComment!!.startPlaying(filename, MediaPlayer.OnCompletionListener { binding.progressBar.visibility = View.GONE })
                } else {
                    Toast.makeText(this, "Nem létezik a fájl", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnDeleteRecord.setOnClickListener {
            if (audioComment?.deleteComment(filename)!!) {
                binding.filename.text = naplodatumStr.subSequence(naplodatumStr.lastIndexOf('/')+1, naplodatumStr.length)
                binding.btnDeleteRecord.isEnabled = false
                binding.btnStartRecord.isEnabled = true
            }
        }

    }

    override fun onBackPressed() {
        val intentReply = Intent()
        if (audioComment!!.isPlayable(filename)) {
            intentReply.putExtra("extra_file_name", filename)
            setResult(RESULT_OK, intentReply)
        } else {
            setResult(RESULT_CANCELED)
        }
        finish()
        super.onBackPressed()
    }

    private fun enableButtons() {
        if (felvetelVan) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnStartRecord.compoundDrawables[0].setTint(resources.getColor(R.color.recoff))
            binding.btnStartRecord.isEnabled = false
            binding.btnStopRecord.compoundDrawables[0].setTint(resources.getColor(R.color.recon))
            binding.btnStopRecord.isEnabled = true
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnStartRecord.compoundDrawables[0].setTint(resources.getColor(R.color.recon))
            binding.btnStartRecord.isEnabled = true
            binding.btnStopRecord.compoundDrawables[0].setTint(resources.getColor(R.color.recoff))
            binding.btnStopRecord.isEnabled = false
        }
    }
}
