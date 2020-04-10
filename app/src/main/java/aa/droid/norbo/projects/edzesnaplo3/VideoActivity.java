package aa.droid.norbo.projects.edzesnaplo3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;

public class VideoActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    public static final String EXTRA_GYAKORLAT = "aa.droid.norbo.projects.edzesnaplov3.EXTRA_GYAKORLAT";
    private final String TAG = getClass().getSimpleName();
    private final String YT_API_KEY = "AIzaSyDzSqMr9tFI2MvQ_b7BMCQE8Xrw3DWvtOw";
    private Gyakorlat gyakorlat;

    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private YouTubePlayer youTubePlayer;
    private Integer videopoz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        initIntentExtraData(getIntent());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(gyakorlat.getMegnevezes());
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //müködik, de az ide hibát jelez
        transaction.add(R.id.ytView, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(YT_API_KEY, this);
    }

    private void initIntentExtraData(Intent intent) {
        gyakorlat = (Gyakorlat) intent.getSerializableExtra(EXTRA_GYAKORLAT);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b) {
            youTubePlayer = youTubePlayer;
            youTubePlayer.cueVideo(gyakorlat.getVideolink(), (videopoz != null) ? videopoz : gyakorlat.getVideostartpoz() * 1000);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Sajnos hiba történt!", Toast.LENGTH_LONG).show();
    }
}
