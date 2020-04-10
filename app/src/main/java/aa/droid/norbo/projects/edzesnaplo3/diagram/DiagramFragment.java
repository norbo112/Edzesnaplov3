package aa.droid.norbo.projects.edzesnaplo3.diagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.UIUtils;

public class DiagramFragment extends Fragment {
    private static final String TAG = "DiagramFragment";
    private BarChart barChart;
    private NaploViewModel naploViewModel;
    private SorozatViewModel sorozatViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        naploViewModel = new ViewModelProvider(this).get(NaploViewModel.class);
        sorozatViewModel = new ViewModelProvider(this).get(SorozatViewModel.class);
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
                    Toast.makeText(getContext(), "Nincsenek mentett naplók", Toast.LENGTH_SHORT).show();
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
        barDataSet.setColor(getContext().getResources().getColor(R.color.edzesHatter));
        barDataSet.setValueTextSize(14f);
        barChart.setDescription("Megmozgatott súlyok");
        barChart.animateY(3000);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diagram_fragment_layout, container, false);
        barChart = view.findViewById(R.id.barchart);

        loadList();

        return view;
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
