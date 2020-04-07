package aa.droid.norbo.projects.edzesnaplo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.NaploUser;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.NaploUserViewModel;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.NaploViewModel;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    public static final String TAROLTNEV = "tarolt.nev";
    public static final String FELHASZNALONEV = "aa.droid.norbo.projects.edzesnaplo3.FELHASZNALONEV";
    public static final String INTENT_DATA_NAPLO = "aa.droid.norbo.projects.edzesnaplo3.INTENT_DATA_NAPLO";
    public static final String INTENT_DATA_GYAKORLAT = "aa.droid.norbo.projects.edzesnaplo3.INTENT_DATA_GYAKORLAT";
    public static final String INTENT_DATA_NEV = "aa.droid.norbo.projects.edzesnaplo3.INTENT_DATA_NEV";
    public static final int EDZESACTIVITY = 1001;
    private final String TAG = getClass().getSimpleName();
    private EditText editText;
    private TextView textView;
    private String nevFromFile;

    private NaploUserViewModel naploUserViewModel;
    private TextView textViewTemp;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edzésnapló v3");
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);

        editText = findViewById(R.id.etWelcomeNev);
        textView = findViewById(R.id.tvWelcomeNev);
        textViewTemp = findViewById(R.id.tvTextTemp);

        naploUserViewModel = new ViewModelProvider(this).get(NaploUserViewModel.class);

        textView.setVisibility(View.GONE);
        textViewTemp.setVisibility(View.GONE);

        checkNevInDB();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tevekenyseg, menu);
        menu.removeItem(R.id.app_bar_search);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_mentett_nezet) {
            startActivity(new Intent(this, MentettNaploActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void startTevekenysegActivity() {
        if(editText.getVisibility() == View.VISIBLE && TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(this, "Kérlek írd be a neved", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editText.getVisibility() == View.VISIBLE) {
            saveFelhasznaloToDB();
        }

        Intent intent = new Intent(this, Tevekenyseg.class);
        intent.putExtra(FELHASZNALONEV, nevFromFile);
        startActivity(intent);
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

    private void saveFelhasznaloToDB() {
        nevFromFile = editText.getText().toString();
        NaploUser naploUser = new NaploUser();
        naploUser.setFelhasznalonev(nevFromFile);
        naploUserViewModel.insert(naploUser);
    }

    public String getNevFromFile(Context context, String filename) {
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

    public void checkNevInDB() {
        naploUserViewModel.getNaploUserLiveData().observe(this, new Observer<NaploUser>() {
            @Override
            public void onChanged(NaploUser naploUser) {
                if(naploUser != null) {
                    nevFromFile = naploUser.getFelhasznalonev();
                    editText.setVisibility(View.GONE);
                    textView.setText(nevFromFile);
                    textView.setVisibility(View.VISIBLE);
                    textViewTemp.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
