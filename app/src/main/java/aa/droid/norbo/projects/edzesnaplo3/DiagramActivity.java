package aa.droid.norbo.projects.edzesnaplo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.SelectionDetail;

import java.nio.file.NotLinkException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import aa.droid.norbo.projects.edzesnaplo3.adapters.SpinnerAdapters;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatGyakorlatNevvel;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlatAndNaplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.GyakorlatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.widgets.NaploGyakOsszsuly;

public class DiagramActivity extends AppCompatActivity implements OnChartValueSelectedListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "DiagramActivity";
    private NaploViewModel naploViewModel;
    private SorozatViewModel sorozatViewModel;
    private BarChart barChart;
    private ArrayAdapter adapter;
    private AppCompatSpinner spinner;
    private List<NaploEsOsszSuly> alldiagram;
    private TextView diagramSubTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Diagram");
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        naploViewModel = new ViewModelProvider(this).get(NaploViewModel.class);
        sorozatViewModel = new ViewModelProvider(this).get(SorozatViewModel.class);
        diagramSubTitle = findViewById(R.id.diagramSubTitle);
        spinner = findViewById(R.id.spGyakorlats);
        spinner.setOnItemSelectedListener(this);

        barChart = findViewById(R.id.barchart);
        barChart.setMaxVisibleValueCount(60);
        barChart.setOnChartValueSelectedListener(this);

        loadList();
    }

    private void initSpinner() {
        sorozatViewModel.getSorozatListLiveData().observe(this, new Observer<List<SorozatWithGyakorlat>>() {
            @Override
            public void onChanged(List<SorozatWithGyakorlat> sorozatGyakorlatNevvels) {
                if(sorozatGyakorlatNevvels != null) {
                    adapter = new SpinnerAdapters(DiagramActivity.this)
                            .getSpinnerAdapter(sorozatGyakorlatNevvels);
                    spinner.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tevekenyseg, menu);
        menu.removeItem(R.id.app_bar_search);
        menu.removeItem(R.id.menu_diagram);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_mentett_nezet) {
            startActivity(new Intent(this, MentettNaploActivity.class));
        }else if(item.getItemId() == R.id.menu_tapanyag) {
            startActivity(new Intent(this, TapanyagActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadList() {
        naploViewModel.getNaploListLiveData().observe(this, new Observer<List<Naplo>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onChanged(List<Naplo> naplos) {
                if(naplos != null && naplos.size() != 0) {
                    new AsyncTask<Void, Void, List<NaploEsOsszSuly>>() {
                        @Override
                        protected List<NaploEsOsszSuly> doInBackground(Void... voids) {
                            List<NaploEsOsszSuly> list = new ArrayList<>();
                            NaploEsOsszSuly n;
                            for (int i = 0; i < naplos.size(); i++) {
                                n = new NaploEsOsszSuly();
                                n.setNaplo(naplos.get(i));
                                n.setOsszsuly(sorozatViewModel.getOsszSulyByNaplo(naplos.get(i).getNaplodatum()));
                                list.add(n);
                            }
                            return list;
                        }

                        @Override
                        protected void onPostExecute(List<NaploEsOsszSuly> naploEsOsszSulies) {
                            alldiagram = naploEsOsszSulies;
                            makeChart(alldiagram);
                            initSpinner();
                            subtitleText("Edzések adatai");
                        }
                    }.execute();
                } else {
                    Toast.makeText(DiagramActivity.this, "Nincsenek mentett naplók", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void subtitleText(String message) {
        diagramSubTitle.setText(message);
    }

    private void makeChart(List<NaploEsOsszSuly> data) {
        List<BarEntry> barEntries = new ArrayList<>();
        List<String> barLabels = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            barLabels.add(DateTimeFormatter.getDate(data.get(i).naplo.getNaplodatum()));
            BarEntry entry = new BarEntry(data.get(i).osszsuly, i);
            entry.setData(data.get(i));
            barEntries.add(entry);
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Összsúly");
        BarData barData = new BarData(barLabels, barDataSet);

        barChart.setData(barData);
        barDataSet.setColor(getResources().getColor(R.color.edzesHatter));
        barDataSet.setValueTextSize(14f);

        barChart.setDescription("Megmozgatott súlyok");
        barChart.animateY(2000);
    }

    private void makeChartGyakorlat(List<Sorozat> data) {
        List<BarEntry> barEntries = new ArrayList<>();
        List<String> barLabels = new ArrayList<>();

        Set<String> naplodatumok = new HashSet<>();
        for(Sorozat s: data) {
            naplodatumok.add(s.getNaplodatum());
        }

        Iterator<String> dit = naplodatumok.iterator();
        int index = 0;
        while(dit.hasNext()) {
            String naplodatum = dit.next();
            barLabels.add(DateTimeFormatter.getDate(naplodatum));
            float suly = 0;
            for (int i = 0; i < data.size(); i++) {
                if(naplodatum.equals(data.get(i).getNaplodatum())) {
                    suly += data.get(i).getSuly() * data.get(i).getIsmetles();
                }
            }
            barEntries.add(new BarEntry(suly, index++));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Gyakorlat összsúly (kg)");
        BarData barData = new BarData(barLabels, barDataSet);

        barChart.setData(barData);
        barDataSet.setColor(getResources().getColor(R.color.edzesHatter));
        barDataSet.setValueTextSize(14f);

        barChart.setDescription("Megmozgatott súlyok");
        barChart.animateY(2000);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if(e == null) return;

        try {
            NaploEsOsszSuly n = ((NaploEsOsszSuly) e.getData());
            new AlertDialog.Builder(this)
                    .setTitle("Edzésnap adatok")
                    .setMessage(n.naplo.getFelhasznalonev() + "\n" +
                            DateTimeFormatter.getNaploListaDatum(n.naplo.getNaplodatum()) + "\n" +
                            n.osszsuly + " KG")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }
                    ).show();
        } catch (NullPointerException error) {
            Log.i(TAG, "onValueSelected ");
        }
    }

    @Override
    public void onNothingSelected() {

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final Gyakorlat item = (Gyakorlat) adapter.getItem(position);
        if(item != null) {
            if(item.getId() == -1) {
                makeChart(alldiagram);
                subtitleText("Edzések adatai");
            } else {
                new AsyncTask<Void, Void, List<Sorozat>>() {
                    @Override
                    protected List<Sorozat> doInBackground(Void... voids) {
                        return sorozatViewModel.getallByGyakorlat(item.getId());
                    }

                    @Override
                    protected void onPostExecute(List<Sorozat> sorozats) {
                        makeChartGyakorlat(sorozats);
                        subtitleText(item.getMegnevezes()+" adatai");
                    }
                }.execute();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class NaploEsOsszSuly {
        private Naplo naplo;
        private int osszsuly;

        public NaploEsOsszSuly() {
        }

        public NaploEsOsszSuly(Naplo naplo, int osszsuly) {
            this.naplo = naplo;
            this.osszsuly = osszsuly;
        }

        public Naplo getNaplo() {
            return naplo;
        }

        public void setNaplo(Naplo naplo) {
            this.naplo = naplo;
        }

        public int getOsszsuly() {
            return osszsuly;
        }

        public void setOsszsuly(int osszsuly) {
            this.osszsuly = osszsuly;
        }
    }
}
