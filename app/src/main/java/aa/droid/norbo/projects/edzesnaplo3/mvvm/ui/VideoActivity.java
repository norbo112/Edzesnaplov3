package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmVideoActivityBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.VideoUtils;

public class VideoActivity extends BaseActiviry<MvvmVideoActivityBinding> implements YouTubePlayer.OnInitializedListener {
    public static final String EXTRA_GYAKORLAT = "aa.droid.norbo.projects.edzesnaplov3.EXTRA_GYAKORLAT";
    private Gyakorlat gyakorlat;

    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private YouTubePlayer player;
    private Integer videopoz;

    public VideoActivity() {
        super(R.layout.mvvm_video_activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        initIntentExtraData(getIntent());

        binding.toolbar.setTitle(gyakorlat.getMegnevezes());
        binding.toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(binding.toolbar);

        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.ytView, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(VideoUtils.YT_API_KEY, this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("videopoz", player != null ? player.getCurrentTimeMillis() : 0);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        videopoz = savedInstanceState.getInt("videopoz", 0);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initIntentExtraData(Intent intent) {
        gyakorlat = (Gyakorlat) intent.getSerializableExtra(EXTRA_GYAKORLAT);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b) {
            player = youTubePlayer;
            player.cueVideo(gyakorlat.getVideolink(), (videopoz != null) ? videopoz : gyakorlat.getVideostartpoz() * 1000);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Sajnos hiba történt!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setupCustomActionBar() {
    }
}
