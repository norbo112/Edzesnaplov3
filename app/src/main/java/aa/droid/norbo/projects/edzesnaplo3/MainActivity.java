package aa.droid.norbo.projects.edzesnaplo3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    private static final String TAROLTNEV = "tarolt.nev";
    private final String TAG = getClass().getSimpleName();
    private EditText editText;
    private TextView textView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edzésnapló v3");
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);

        editText = findViewById(R.id.etWelcomeNev);
        textView = findViewById(R.id.tvWelcomeNev);
        final String nevFromFile = getNevFromFile(this, TAROLTNEV);
        if(nevFromFile != null) {
            editText.setVisibility(View.INVISIBLE);
            textView.setText(nevFromFile+" naplója");
        } else {
            textView.setVisibility(View.INVISIBLE);
        }

        Button btnBelep = findViewById(R.id.welcome_belep);
        btnBelep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.welcome_belep :
                startTevekenysegActivity();
                break;
        }
    }

    private void startTevekenysegActivity() {
        if(editText.getVisibility() == View.VISIBLE && TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(this, "Kérlek írd be a neved", Toast.LENGTH_SHORT).show();
            return;
        }

        saveFelhasznaloNev(editText.getText().toString());
        startActivity(new Intent(this, Tevekenyseg.class));
    }

    private void saveFelhasznaloNev(String felhasznalpnev) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(TAROLTNEV, MODE_PRIVATE)
            ));
            writer.write(felhasznalpnev);
        } catch (Exception ex) {
            Log.e(TAG, "saveFelhasznaloNev: Sikertelen fájl mentés", ex);
        } finally {
            try {
                if(writer!=null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getNevFromFile(Context context, String filename) {
        BufferedReader reader = null;
        String nev = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
            nev = reader.readLine();
        } catch (FileNotFoundException e) {
            Log.i(TAG, "getNevFromFile: ["+filename+"] féjl nem található");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getNevFromFile: "+filename+" olvasás hiba", e);
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return nev;
    }

    public String getFelhasznaloNev(Context context) {
        return getNevFromFile(context, TAROLTNEV);
    }
}
