package aa.droid.norbo.projects.edzesnaplo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.diagram.DiagramFragment;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.UIUtils;

public class DiagramActivity extends AppCompatActivity {

    private NaploViewModel naploViewModel;
    private SorozatViewModel sorozatViewModel;
    private BarChart barChart;

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

        barChart = findViewById(R.id.barchart);

        loadList();
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
//                            Log.i(TAG, "onPostExecute: Diagram adatok betöltve");
//                            Log.i(TAG, "onPostExecute: adatok size: "+naploEsOsszSulies.size());
                            makeChart(naploEsOsszSulies);
                        }
                    }.execute();
                } else {
                    Toast.makeText(DiagramActivity.this, "Nincsenek mentett naplók", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void makeChart(List<NaploEsOsszSuly> data) {
        List<BarEntry> barEntries = new ArrayList<>();
        List<String> barLabels = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            barLabels.add(UIUtils.getNapDatumStr(data.get(i).naplo.getNaplodatum()));
            barEntries.add(new BarEntry(data.get(i).osszsuly, i));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Összsúly");
        BarData barData = new BarData(barLabels, barDataSet);

        barChart.setData(barData);
//        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setColor(getResources().getColor(R.color.edzesHatter));
        barDataSet.setValueTextSize(14f);
        barChart.setDescription("Megmozgatott súlyok");
        barChart.animateY(3000);
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
