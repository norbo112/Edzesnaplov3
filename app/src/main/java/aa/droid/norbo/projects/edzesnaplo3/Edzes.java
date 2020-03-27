package aa.droid.norbo.projects.edzesnaplo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;

public class Edzes extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private Gyakorlat gyakorlat;
    private List<Sorozat> sorozats;
    private Naplo naplo;
    private String felhasznalonev;

    private ArrayAdapter<Sorozat> listAdapter;

    private EditText etSuly;
    private EditText etIsm;
    private ListView listView;

    private final Handler stopperHandler = new Handler();
    private TextView tvStopper;
    private final MyTimer stopperTimer = new MyTimer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edzes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edzés");
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initNaploFromSavedBundle(savedInstanceState);
        initIntentExtraData(getIntent());

        TextView gyaktitle = findViewById(R.id.gyak_title);
        tvStopper = findViewById(R.id.tvStopper);

        etIsm = findViewById(R.id.etIsm);
        etSuly = findViewById(R.id.etSuly);


        if(gyakorlat != null) gyaktitle.setText(String.format("%s használata", gyakorlat.getMegnevezes()));

        sorozats = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, sorozats);
        listView = findViewById(R.id.sorozatLista);
        listView.setAdapter(listAdapter);
        listView.setNestedScrollingEnabled(true);

        Button btnSorozatAdd = findViewById(R.id.btnSorozatAdd);
        btnSorozatAdd.setOnClickListener(this);
        Button btnUjGyakorlat = findViewById(R.id.btnEdzesUjGy);
        btnUjGyakorlat.setOnClickListener(this);
        Button btnSave = findViewById(R.id.btnEdzesSave);
        btnSave.setOnClickListener(this);
    }

    private void initIntentExtraData(Intent intent) {
        felhasznalonev = intent.getStringExtra(MainActivity.INTENT_DATA_NEV);
        gyakorlat = (Gyakorlat) intent.getSerializableExtra(
                MainActivity.INTENT_DATA_GYAKORLAT
        );
        naplo = (Naplo) intent.getSerializableExtra(MainActivity.INTENT_DATA_NAPLO);
        if(naplo == null) naplo = new Naplo(new Date().toString(), felhasznalonev);

        //DEBUG
        Log.i(TAG, "initIntentExtraData: Naplo: "+naplo.getNaplodatum()+"\n"+
                "sorozat méret= "+naplo.getSorozats().size());
    }

    private void initNaploFromSavedBundle(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.getSerializable("naplo") != null) {
            naplo = (Naplo) savedInstanceState.getSerializable("naplo");
        } else {
            naplo = new Naplo(new Date().toString(), felhasznalonev);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSorozatAdd) {
            sorozatHozzaad();
        } else if(v.getId() == R.id.btnEdzesUjGy) {
            naplo.addAllSorozat(sorozats);
            System.out.println("új gyakorlat kezdete ... "+sorozats);
            sorozats.clear();
            Intent resultIntent = new Intent();
            resultIntent.putExtra(MainActivity.INTENT_DATA_NAPLO, naplo);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else if(v.getId() == R.id.btnEdzesSave) {
            saveNaplo();
        }
    }

    private void saveNaplo() {
        naplo.addAllSorozat(sorozats);
        sorozats.clear();
        Intent naplomentes = new Intent(this, NaploActivity.class);
        naplomentes.putExtra(MainActivity.FELHASZNALONEV, felhasznalonev);
        naplomentes.putExtra(MainActivity.INTENT_DATA_NAPLO, naplo);
        startActivity(naplomentes);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        System.out.println("Saved InstanceState");
        outState.putSerializable("nsplo", naplo);
        super.onSaveInstanceState(outState);
    }

    private void sorozatHozzaad() {
        if (TextUtils.isEmpty(etIsm.getText().toString()) ||
            TextUtils.isEmpty(etSuly.getText().toString())) {
            Toast.makeText(this, "Súly vagy ismétlés üres!", Toast.LENGTH_SHORT).show();
            return;
        }

        stopperHandler.removeCallbacks(stopperTimer);
        tvStopper.setText("00:00");
        stopperTimer.setStartTime(System.currentTimeMillis());
        stopperHandler.postDelayed(stopperTimer, 0);

        sorozats.add(new Sorozat(gyakorlat,
                Integer.parseInt(etSuly.getText().toString()),
                Integer.parseInt(etIsm.getText().toString()), new Date().toString(),
                naplo.getNaplodatum()));
        listAdapter.notifyDataSetChanged();
    }

    private class MyTimer implements Runnable {
        long startTime;

        private MyTimer() {
            this.startTime = 0;
        }

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            tvStopper.setText(String.format("%02d:%02d", minutes, seconds));

            stopperHandler.postDelayed(this, 500);
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
    }
}
