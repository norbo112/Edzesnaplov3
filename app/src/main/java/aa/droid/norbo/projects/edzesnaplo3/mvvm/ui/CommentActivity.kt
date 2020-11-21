package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui

import aa.droid.norbo.projects.edzesnaplo3.R
import aa.droid.norbo.projects.edzesnaplo3.databinding.ActivityCommentBinding
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.comment.AudioComment
import android.content.Intent
import android.os.Bundle
import android.view.View

class CommentActivity : BaseActivity<ActivityCommentBinding>(R.layout.activity_comment) {
    private var audioComment: AudioComment? = null
    private var felvetelVan: Boolean = false
    private var playAudio: Boolean = false

    override fun setupCustomActionBar() {
        setSupportActionBar( binding.toolbar)
        supportActionBar?.title = "Audio Comment"
        supportActionBar?.setIcon(R.drawable.ic_headset)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle: Bundle? = intent.extras
        val filename = bundle!!.getString("extra_file_name")
        playAudio = bundle.getBoolean("audio_play_on", false)
        audioComment = AudioComment(this)

        binding.filename.text = filename

        if(!playAudio) {
            binding.btnStartPlay.visibility = View.GONE
            binding.btnStartRecord.setOnClickListener {
                felvetelVan = true
                if (filename != null) {
                    audioComment!!.recordComment(filename)
                }
                enableButtons()
            }

            binding.btnStopRecord.setOnClickListener {
                felvetelVan = false
                audioComment!!.stopRecord()
                enableButtons()
            }
        } else {
            binding.btnStartPlay.visibility = View.VISIBLE
            binding.btnStartRecord.visibility = View.GONE
            binding.btnStopRecord.visibility = View.GONE

            binding.btnStartPlay.setOnClickListener {
                if (filename != null) {
                    audioComment!!.startPlaying(filename)
                }
            }
        }
    }

    override fun onBackPressed() {
        val intentReply = Intent()
        if(audioComment?.file != null) {
            intentReply.putExtra("extra_file_name", audioComment?.file?.absolutePath)
            setResult(RESULT_OK, intentReply)
        } else {
            setResult(RESULT_CANCELED)
        }
        finish()
        super.onBackPressed()
    }

    private fun enableButtons() {
        if(felvetelVan) {
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
