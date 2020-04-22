package aa.droid.norbo.projects.edzesnaplo3.uiutils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class NaploAudioComment {
    private static final String LOG_TAG = NaploAudioComment.class.getSimpleName();
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String filename;
    private Context context;
    private boolean recordis;
    private boolean playing;

    public NaploAudioComment(Context mContext, String filename) {
        this.context = mContext;
        this.filename = filename;
    }

    public void startRecord() {
        if(!recordis) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(filename);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }

            mediaRecorder.start();
        } else {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public void startPlaying() {
        if(!playing) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(filename);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }
        } else {
            mediaPlayer.release();
            mediaPlayer = null;
        }
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isRecordis() {
        return recordis;
    }

    public void setRecordis(boolean recordis) {
        this.recordis = recordis;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
