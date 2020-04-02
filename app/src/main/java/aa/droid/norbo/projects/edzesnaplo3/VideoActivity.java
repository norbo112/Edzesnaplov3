package aa.droid.norbo.projects.edzesnaplo3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class VideoActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    public static final String EXTRA_VIDEO_LINK = "aa.droid.norbo.projects.edzesnaplov3.EXTRA_VIDEO_LINK";
    public static final String EXTRA_VIDEO_POZ = "aa.droid.norbo.projects.edzesnaplov3.EXTRA_VIDEO_POZ";
    private final String TAG = getClass().getSimpleName();
    private final String YT_API_KEY = "AIzaSyDzSqMr9tFI2MvQ_b7BMCQE8Xrw3DWvtOw";
    private String yt_video_link;
    private int yt_video_poz;

    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Videó");
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);

        initIntentExtraData(getIntent());
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //müködik, de az ide hibát jelez
        transaction.add(R.id.ytView, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(YT_API_KEY, this);
    }

    private void initIntentExtraData(Intent intent) {
        yt_video_link = intent.getStringExtra(EXTRA_VIDEO_LINK);
        yt_video_poz = intent.getIntExtra(EXTRA_VIDEO_POZ, 0);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b) {
            youTubePlayer = youTubePlayer;
            youTubePlayer.cueVideo(yt_video_link, yt_video_poz * 1000);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Sajnos hiba történt!", Toast.LENGTH_LONG).show();
    }
}
