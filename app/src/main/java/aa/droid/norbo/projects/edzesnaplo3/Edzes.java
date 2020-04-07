package aa.droid.norbo.projects.edzesnaplo3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.datainterfaces.AdatBeallitoInterface;

public class Edzes extends Fragment implements View.OnClickListener {
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
    private TextView gyaktitle;

    private AdatBeallitoInterface adatBeallitoInterface;
    private Button btnSorozatAdd;
    private Button btnUjGyakorlat;
    private Button btnSave;
    private TextView tvSorozatTitle;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.adatBeallitoInterface = (AdatBeallitoInterface) context;
        this.felhasznalonev = adatBeallitoInterface.getFelhasznaloNev();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        naplo = new Naplo(new Date().toString(), felhasznalonev);
        sorozats = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_tabbed_edzes_layout, container, false);

        gyaktitle = view.findViewById(R.id.gyak_title);
        tvStopper = view.findViewById(R.id.tvStopper);

        tvSorozatTitle = view.findViewById(R.id.tvSorozatokTitle);

        etIsm = view.findViewById(R.id.etIsm);
        etSuly = view.findViewById(R.id.etSuly);

        btnSorozatAdd = view.findViewById(R.id.btnSorozatAdd);
        btnSorozatAdd.setOnClickListener(this);
        btnUjGyakorlat = view.findViewById(R.id.btnEdzesUjGy);
        btnUjGyakorlat.setOnClickListener(this);
        btnSave = view.findViewById(R.id.btnEdzesSave);
        btnSave.setOnClickListener(this);


        if(gyakorlat != null) {
            gyaktitle.setText(String.format("%s használata", gyakorlat.getMegnevezes()));
        } else {
            gyaktitle.setText("Kérlek válassz egy gyakorlatot");
            gyaktitle.setTextColor(Color.RED);

            etIsm.setEnabled(false);
            etSuly.setEnabled((false));
            btnSave.setEnabled(false);
            btnSorozatAdd.setEnabled(false);
            btnUjGyakorlat.setEnabled(false);
        }

        listAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, sorozats);
        listView = view.findViewById(R.id.sorozatLista);
        listView.setAdapter(listAdapter);
        listView.setNestedScrollingEnabled(true);

        return view;
    }

    public void setGyakorlat(Gyakorlat gyakorlat) {
        this.gyakorlat = gyakorlat;
        enabledButtons();
        sorozats.clear();
        listAdapter.notifyDataSetChanged();
        stopperHandler.removeCallbacks(stopperTimer);
        etSuly.setText("");
        etIsm.setText("");
        tvStopper.setText("00:00");
        gyaktitle.setText(gyakorlat.getMegnevezes()+" használata");
    }

    private void disableButtons() {
        gyaktitle.setText("Kérlek válassz egy gyakorlatot");
        gyaktitle.setTextColor(Color.RED);
        etIsm.setEnabled(false);
        etSuly.setEnabled(false);
        btnSave.setEnabled(false);
        btnSorozatAdd.setEnabled(false);
        btnUjGyakorlat.setEnabled(false);

    }

    private void enabledButtons() {
        gyaktitle.setText(String.format("%s használata", gyakorlat.getMegnevezes()));
        gyaktitle.setTextColor(Color.WHITE);

        etIsm.setEnabled(true);
        etSuly.setEnabled((true));
        btnSave.setEnabled(true);
        btnSorozatAdd.setEnabled(true);
        btnUjGyakorlat.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSorozatAdd) {
            sorozatHozzaad();
        } else if(v.getId() == R.id.btnEdzesUjGy) {
            naplo.addAllSorozat(sorozats);
            disableButtons();
            ViewPager tabHost = getActivity().findViewById(R.id.view_pager);
            if(tabHost != null) {
                tabHost.setCurrentItem(0, true);
            } else {
                clearEdzesView();
            }

            adatBeallitoInterface.adatNaplo(naplo);
        } else if(v.getId() == R.id.btnEdzesSave) {
            saveNaplo();
        }
    }

    private void clearEdzesView() {
        sorozats.clear();
        stopperHandler.removeCallbacks(stopperTimer);
        listAdapter.notifyDataSetChanged();
        tvStopper.setText(R.string.stopper_kijelzo);
    }

    private void saveNaplo() {
        naplo.addAllSorozat(sorozats);

        sorozats.clear();
        listAdapter.notifyDataSetChanged();
        stopperHandler.removeCallbacks(stopperTimer);
        etSuly.setText("");
        etIsm.setText("");
        tvStopper.setText("00:00");

        Intent naplomentes = new Intent(getContext(), NaploActivity.class);
        naplomentes.putExtra(MainActivity.FELHASZNALONEV, felhasznalonev);
        naplomentes.putExtra(MainActivity.INTENT_DATA_NAPLO, naplo);
        startActivity(naplomentes);
    }

    private void sorozatHozzaad() {
        if(gyakorlat == null) {
            Toast.makeText(getContext(), "Válassz egy gyakorlatot!", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(etIsm.getText().toString()) ||
            TextUtils.isEmpty(etSuly.getText().toString())) {
            Toast.makeText(getContext(), "Súly vagy ismétlés üres!", Toast.LENGTH_SHORT).show();
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
        updateSorozatTitle();
        listAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "!", Toast.LENGTH_SHORT).show();
    }

    private void updateSorozatTitle() {
        if(sorozats.size() > 0) {
            int osszes = 0;
            for (int i = 0; i < sorozats.size(); i++) {
                osszes += sorozats.get(i).getSuly() * sorozats.get(i).getIsmetles();
            }
            tvSorozatTitle.setText("Megmozgatott sőly: "+osszes+" Kg");
        }
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
