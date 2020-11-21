package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui

import aa.droid.norbo.projects.edzesnaplo3.R
import aa.droid.norbo.projects.edzesnaplo3.databinding.ActivityCommentBinding
import android.os.Bundle

class CommentActivity : BaseActivity<ActivityCommentBinding>(R.layout.activity_comment) {

    override fun setupCustomActionBar() {
        actionBar?.title = "Audio Comment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}