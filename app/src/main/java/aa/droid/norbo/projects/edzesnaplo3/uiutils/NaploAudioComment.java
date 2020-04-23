package aa.droid.norbo.projects.edzesnaplo3.uiutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import aa.droid.norbo.projects.edzesnaplo3.MainActivity;
import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;

public class NaploAudioComment {
    private static final String LOG_TAG = NaploAudioComment.class.getSimpleName();
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String filename;
    private Context context;
    private TextView animaltView;
    private Animation animation;
    private SharedPreferences preferences;

    public NaploAudioComment(Context mContext, String filename, TextView mAnimaltView) {
        this.context = mContext;
        this.filename = filename;
        this.animaltView = mAnimaltView;
        this.animation = AnimationUtils.loadAnimation(mContext, R.anim.addtouch);
        this.preferences = context.getSharedPreferences("naplo", Context.MODE_PRIVATE);
    }

    public void startRecord() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(filename);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "startRecord: ", e);
            Toast.makeText(context, "Hiba a rögzítés közben! :(", Toast.LENGTH_SHORT).show();
        }

        mediaRecorder.start();
        animaltView.setVisibility(View.VISIBLE);
        animaltView.startAnimation(animation);
    }

    public void stopRecord() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        animaltView.clearAnimation();
        animaltView.setVisibility(View.GONE);
    }

    public void startPlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filename);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "startPlaying: ", e);
            Toast.makeText(context, "Hiba a lejátszás közben! :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void relase() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    public boolean checkRecording(Naplo mNaplo) {
        if(!preferences.getBoolean(MainActivity.AUDIO_RECORD_IS, false)) {
            Toast.makeText(context, "Audio rögzítésre nem jogosult", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mNaplo == null) {
            Toast.makeText(context, "Nincs napló amire rögzítenék", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
