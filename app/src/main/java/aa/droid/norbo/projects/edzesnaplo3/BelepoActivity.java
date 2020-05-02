package aa.droid.norbo.projects.edzesnaplo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import aa.droid.norbo.projects.edzesnaplo3.logutils.Lgr;

public class BelepoActivity extends AppCompatActivity {

    private static final int MY_PERMISSION = 200;
    private static final String[] MANI_PERMS = new String[] {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences naplopref;

    private TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_belepo);

        warning = findViewById(R.id.warning);
        naplopref = getSharedPreferences("naplo", MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(this, MANI_PERMS[0])
                == PackageManager.PERMISSION_GRANTED) {

            SharedPreferences.Editor edit = naplopref.edit();
            edit.putBoolean(MainActivity.AUDIO_RECORD_IS, true);
            edit.apply();
        }


        if (ActivityCompat.checkSelfPermission(this, MANI_PERMS[1])
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            ActivityCompat.requestPermissions(this, MANI_PERMS, MY_PERMISSION);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION:
                boolean audiorecordon = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                SharedPreferences.Editor edit = naplopref.edit();
                edit.putBoolean(MainActivity.AUDIO_RECORD_IS, audiorecordon);
                edit.apply();

                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    warning.setText("A médiatartalmak elérését mindenképpen elkell fogadnod, különben nem tudod használni az alkalmazást!");
                }
                break;
        }
    }
}
