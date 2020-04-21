package aa.droid.norbo.projects.edzesnaplo3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.datainterfaces.AdatBeallitoInterface;
import aa.droid.norbo.projects.edzesnaplo3.ui.controller.EdzesFragmentCntImpl;
import aa.droid.norbo.projects.edzesnaplo3.ui.controller.interfaces.EdzesFragmentControllerInterface;

public class Edzes extends Fragment implements View.OnClickListener {
    private static final int SULY_BAR_DIALOG = 1;
    private static final int ISM_BAR_DIALOG = 2;

    private Gyakorlat gyakorlat;
    private List<Sorozat> sorozats;
    private Naplo naplo;
    private String felhasznalonev;

    private ArrayAdapter<Sorozat> listAdapter;

    private EditText etSuly;
    private EditText etIsm;

    private final Handler stopperHandler = new Handler();
    private TextView tvStopper;
    private final MyTimer stopperTimer = new MyTimer();

    private AdatBeallitoInterface adatBeallitoInterface;
    private EdzesFragmentControllerInterface controllerInterface;
    private SorozatViewModel sorozatViewModel;
    private View fragmentView;
    private TextView tvSorozatTitle;
    private CardView cardView;

    //most csak tablet layouthoz
    private SeekBar seekBarSuly, seekBarIsm;
    private Button btnSulyLabel, btnIsmLabel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.adatBeallitoInterface = (AdatBeallitoInterface) context;
        this.felhasznalonev = adatBeallitoInterface.getFelhasznaloNev();
        this.controllerInterface = new EdzesFragmentCntImpl();
        this.sorozatViewModel = new ViewModelProvider(this).get(SorozatViewModel.class);
        naplo = new Naplo(Long.toString(System.currentTimeMillis()), felhasznalonev);
        sorozats = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("naplo", naplo);
        outState.putParcelableArrayList("sorozatok", (ArrayList<? extends Parcelable>) sorozats);
        outState.putSerializable("gyakorlat", gyakorlat);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            naplo = (Naplo) savedInstanceState.getSerializable("naplo");
            gyakorlat = (Gyakorlat) savedInstanceState.getSerializable("gyakorlat");
            sorozats = savedInstanceState.getParcelableArrayList("sorozatok");
        }

        View view = inflater.inflate(R.layout.test_tabbed_edzes_layout, container, false);
        this.fragmentView = view;
        cardView = view.findViewById(R.id.edzes_card_view);
        TextView gyaktitle = view.findViewById(R.id.gyak_title);
        tvStopper = view.findViewById(R.id.tvStopper);

        tvSorozatTitle = view.findViewById(R.id.tvSorozatokTitle);

        etIsm = view.findViewById(R.id.etIsm);
        etSuly = view.findViewById(R.id.etSuly);

        Button btnSorozatAdd = view.findViewById(R.id.btnSorozatAdd);
        btnSorozatAdd.setOnClickListener(this);
        Button btnUjGyakorlat = view.findViewById(R.id.btnEdzesUjGy);
        btnUjGyakorlat.setOnClickListener(this);
        Button btnSave = view.findViewById(R.id.btnEdzesSave);
        btnSave.setOnClickListener(this);

        seekBarSuly = view.findViewById(R.id.seekBarsuly);
        seekBarIsm = view.findViewById(R.id.seekBarism);
        btnSulyLabel = view.findViewById(R.id.tvSulyLabel);
        btnIsmLabel = view.findViewById(R.id.tvIsmLabel);

        if(seekBarSuly != null) {
            seekBarSuly.setOnSeekBarChangeListener(seekListeren);
            seekBarIsm.setOnSeekBarChangeListener(seekListeren);
            btnSulyLabel.setOnClickListener(btnClickListener);
            btnIsmLabel.setOnClickListener(btnClickListener);
        }

        if(gyakorlat != null) {
            gyaktitle.setText(String.format("%s használata", gyakorlat.getMegnevezes()));
        } else {
            gyaktitle.setText("Kérlek válassz egy gyakorlatot");
            gyaktitle.setTextColor(Color.RED);

            controllerInterface.disableButtons(this, view);
        }

        listAdapter = new ArrayAdapter<Sorozat>(getContext(),
                R.layout.sorozat_list_item, sorozats){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Sorozat sorozat = getItem(position);

                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.sorozat_list_item, parent, false);
                }

                ((TextView)convertView.findViewById(R.id.sorozatTextview)).setText(sorozat.toString());
                textKorabbiOsszsuly(convertView.findViewById(R.id.korabbiSSulyTV),
                        sorozat);

                return convertView;
            }
        };
        ListView listView = view.findViewById(R.id.sorozatLista);
        listView.setAdapter(listAdapter);
        listView.setNestedScrollingEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSorozatAdat(listAdapter.getItem(position));
            }
        });

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void textKorabbiOsszsuly(TextView textView, Sorozat sorozat) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return sorozatViewModel.getSorozatKorabbiOsszsuly(sorozat.getGyakorlatid());
            }

            @Override
            protected void onPostExecute(Integer integer) {
                textView.setText(integer != null ? integer+" Kg" : "0");
            }
        }.execute();
    }

    public void setGyakorlat(Gyakorlat gyakorlat) {
        this.gyakorlat = gyakorlat;
        controllerInterface.prepareGyakorlat(this, fragmentView);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSorozatAdd) {
            sorozatHozzaad();
        } else if(v.getId() == R.id.btnEdzesUjGy) {
            naplo.addAllSorozat(sorozats);
            controllerInterface.disableButtons(this, fragmentView);
            clearEdzesView();
            adatBeallitoInterface.adatNaplo(naplo);
            ViewPager tabHost = getActivity().findViewById(R.id.view_pager);
            if(tabHost != null) {
                tabHost.setCurrentItem(0, true);
            }
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
        controllerInterface.prepareGyakorlat(this, fragmentView);

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

        if(seekBarSuly != null) {
            if(seekBarSuly.getProgress() == 0 || seekBarIsm.getProgress() == 0) {
                Toast.makeText(getContext(), "Súly vagy ismétlés 0!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if (TextUtils.isEmpty(etIsm.getText().toString()) ||
                    TextUtils.isEmpty(etSuly.getText().toString())) {
                Toast.makeText(getContext(), "Súly vagy ismétlés üres!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        stopperHandler.removeCallbacks(stopperTimer);
        tvStopper.setText("00:00");
        stopperTimer.setStartTime(System.currentTimeMillis());
        stopperHandler.postDelayed(stopperTimer, 0);

        int suly = seekBarSuly != null ? seekBarSuly.getProgress() : Integer.parseInt(etSuly.getText().toString());
        int ism = seekBarIsm != null ? seekBarIsm.getProgress() : Integer.parseInt(etIsm.getText().toString());

        sorozats.add(new Sorozat(gyakorlat,
                suly,
                ism,
                Long.toString(System.currentTimeMillis()),
                naplo.getNaplodatum()));
        updateSorozatTitle();
        listAdapter.notifyDataSetChanged();

        if(seekBarIsm != null ) seekBarIsm.setProgress(0);
        else etIsm.setText("");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.addtouch);
            cardView.startAnimation(animation);
        } else {
            Toast.makeText(getContext(), "!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSorozatTitle() {
        if(sorozats.size() > 0) {
            int osszes = 0;
            for (int i = 0; i < sorozats.size(); i++) {
                osszes += sorozats.get(i).getSuly() * sorozats.get(i).getIsmetles();
            }
            tvSorozatTitle.setText("Megmozgatott súly: "+osszes+" Kg");
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSorozatAdat(Sorozat sorozatAdat) {
        if(sorozatAdat == null) {
            Toast.makeText(getContext(), "Probléma a sorozat kiválasztása közben", Toast.LENGTH_SHORT).show();
            return;
        }

        final View view = LayoutInflater.from(getContext()).inflate(R.layout.sorozat_szerkeszto, null);
        EditText etSuly = view.findViewById(R.id.sorozatSuly);
        EditText etIsm = view.findViewById(R.id.sorozatIsm);

        etSuly.setText(sorozatAdat.getSuly() + "");
        etIsm.setText(sorozatAdat.getIsmetles() + "");

        new AlertDialog.Builder(getContext())
                .setTitle("Sorozat módosítása")
                .setView(view)
                .setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sorozatAdat.setSuly(Integer.parseInt(etSuly.getText().toString()));
                        sorozatAdat.setIsmetles(Integer.parseInt(etIsm.getText().toString()));
                        listAdapter.notifyDataSetChanged();
                        updateSorozatTitle();
                    }
                })
                .create().show();
    }

    private SeekBar.OnSeekBarChangeListener seekListeren = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(seekBar.getId() == R.id.seekBarsuly) btnSulyLabel.setText(progress+" Kg");

            if(seekBar.getId() == R.id.seekBarism) btnIsmLabel.setText(progress + " x");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private final View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.tvSulyLabel) setbarDialog(SULY_BAR_DIALOG);
            else if(v.getId() == R.id.tvIsmLabel) setbarDialog(ISM_BAR_DIALOG);
        }
    };

    private void setbarDialog(int a) {
        final EditText editText = new EditText(getContext());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(getContext())
                .setTitle(a == SULY_BAR_DIALOG ? "Súly megadása" : "Ismétlés megadása")
                .setMessage("Kérem az adatot")
                .setView(editText)
                .setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(editText.getText().toString())) {
                            Toast.makeText(getContext(), "Kérlek, töltsd ki az értéket", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(a == SULY_BAR_DIALOG) {
                            seekBarSuly.setProgress(Integer.parseInt(editText.getText().toString()));
                        }

                        if(a == ISM_BAR_DIALOG) {
                            seekBarIsm.setProgress(Integer.parseInt(editText.getText().toString()));
                        }
                    }
                }).create().show();
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

    public List<Sorozat> getSorozats() {
        return sorozats;
    }

    public ArrayAdapter<Sorozat> getListAdapter() {
        return listAdapter;
    }

    public Handler getStopperHandler() {
        return stopperHandler;
    }

    public MyTimer getStopperTimer() {
        return stopperTimer;
    }

    public Gyakorlat getGyakorlat() {
        return gyakorlat;
    }
}
