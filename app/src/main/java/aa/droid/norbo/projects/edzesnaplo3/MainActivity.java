package aa.droid.norbo.projects.edzesnaplo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

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
import aa.droid.norbo.projects.edzesnaplo3.logutils.Lgr;
import aa.droid.norbo.projects.edzesnaplo3.onlytest.NaplodbTabla;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.MainAdatTolto;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Logger lgr;
    public static final String FELHASZNALONEV = "aa.droid.norbo.projects.edzesnaplo3.FELHASZNALONEV";
    public static final String INTENT_DATA_NAPLO = "aa.droid.norbo.projects.edzesnaplo3.INTENT_DATA_NAPLO";
    public static final String INTENT_DATA_NEV = "aa.droid.norbo.projects.edzesnaplo3.INTENT_DATA_NEV";
    public static final String AUDIO_RECORD_IS = "aa.droid.norbo.projects.edzesnaplo3.AUDIO_RECORD_IS";

    private EditText editText;
    private TextView textView;
    private String nevFromFile;

    private NaploUserViewModel naploUserViewModel;
    private TextView textViewTemp;
    private boolean felhasznaloOn = false;
    private SharedPreferences naplopref;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        naplopref = getSharedPreferences("naplo", MODE_PRIVATE);
        lgr = Lgr.getLogger("MainActivity");

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

        Button btnBelep = findViewById(R.id.welcome_belep);
        btnBelep.setOnClickListener(this);
        TextView textViewNaplokSzama = findViewById(R.id.tvMainRogzitettNaplok);
        TextView textViewMegmozgatottSuly = findViewById(R.id.tvMainMegmozgatottSulyok);

        try {
            checkNevInDB();

            MainAdatTolto mainAdatTolto = new MainAdatTolto(this);
            textViewNaplokSzama.setText(mainAdatTolto.getNaploCntint()+" db");
            textViewMegmozgatottSuly.setText(mainAdatTolto.getOsszSuly()+" KG");
        } catch (SQLiteException e) {
            if(lgr != null) lgr.log(Level.ERROR, "Adatbázis hiba",e);
            else
                Log.e(TAG, "onCreate: adatbázis hiba", e);
        }
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
        } else if(item.getItemId() == R.id.menu_diagram) {
            startActivity(new Intent(this, DiagramActivity.class));
        } else if(item.getItemId() == R.id.menu_tapanyag) {
            startActivity(new Intent(this, TapanyagActivity.class));
        } else if (item.getItemId() == R.id.menu_db_test) {
            startActivity(new Intent(this, NaplodbTabla.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void startTevekenysegActivity() {
        if(editText.getVisibility() == View.VISIBLE && TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(this, "Kérlek írd be a neved", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editText.getVisibility() == View.VISIBLE && !felhasznaloOn) {
            saveFelhasznaloToDB();
        }

        Intent intent = new Intent(this, Tevekenyseg.class);
        intent.putExtra(FELHASZNALONEV, nevFromFile);
        startActivity(intent);
    }

    private void saveFelhasznaloToDB() {
        nevFromFile = editText.getText().toString();
        NaploUser naploUser = new NaploUser();
        naploUser.setFelhasznalonev(nevFromFile);
        naploUserViewModel.insert(naploUser);
    }

    public void checkNevInDB() {
        naploUserViewModel.getNaploUserLiveData().observe(this, new Observer<NaploUser>() {
            @Override
            public void onChanged(NaploUser naploUser) {
                if (naploUser != null) {
                    nevFromFile = naploUser.getFelhasznalonev();
                    editText.setVisibility(View.GONE);
                    textView.setText(nevFromFile);
                    textView.setVisibility(View.VISIBLE);
                    textViewTemp.setVisibility(View.VISIBLE);
                    felhasznaloOn = true;
                }
            }
        });
    }
}
