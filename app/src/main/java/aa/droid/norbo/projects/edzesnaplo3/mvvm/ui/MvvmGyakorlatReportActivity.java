package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.GyakorlatReportActivityLayoutBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.OsszSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.SorozatUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmGyakorlatReportActivity extends BaseActiviry<GyakorlatReportActivityLayoutBinding>
    implements OnChartValueSelectedListener {
    public static final String EXTRA_GYAK = "aa.droid.norbo.projects.edzesnaplo3.mvvm.EXTRA_GYAK";

    private SimpleDateFormat format = new SimpleDateFormat("MM-dd", Locale.getDefault());

    @Inject
    ModelConverter modelConverter;

    @Inject
    SorozatViewModel sorozatViewModel;

    @Inject
    SorozatUtil sorozatUtil;

    private GyakorlatUI gyakorlatUI;

    public MvvmGyakorlatReportActivity() {
        super(R.layout.gyakorlat_report_activity_layout);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getSerializableExtra(EXTRA_GYAK) != null) {
            gyakorlatUI = modelConverter.fromEntity((Gyakorlat) getIntent().getSerializableExtra(EXTRA_GYAK));
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(gyakorlatUI.getMegnevezes());
            }
        }

        sorozatViewModel.getOsszSorozatByGyakorlat(gyakorlatUI.getId()).observe(this, osszSorozats -> {
            if(osszSorozats != null && osszSorozats.size() > 0) {
                initSorozatLineChart(binding.ismChart, osszSorozats);
                initSorozatOsszsulyLineChart(binding.sulyChart, osszSorozats);
            } else {
                Toast.makeText(this, "Sajnos ehhez a gyakorlathoz nincs sorozat rögzítve", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSorozatLineChart(LineChart ismChart, List<OsszSorozat> sorozats) {
        ismChart.getDescription().setEnabled(false);
        ismChart.setDrawGridBackground(false);
        ismChart.getAxisRight().setEnabled(false);

        ismChart.getAxisLeft().setTextColor(Color.WHITE);

        XAxis xAxis = ismChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setEnabled(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(getDateValueFormatter(sorozats));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        setIsmChartData(ismChart, sorozats);
        ismChart.animateX(1500);
        ismChart.getLegend().setEnabled(false);
    }

    private void initSorozatOsszsulyLineChart(LineChart sulyChart, List<OsszSorozat> sorozats) {
        sulyChart.getDescription().setEnabled(false);
        sulyChart.setDrawGridBackground(false);
        sulyChart.getAxisRight().setEnabled(false);
        sulyChart.getAxisLeft().setTextColor(Color.WHITE);

        XAxis xAxis = sulyChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setEnabled(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(getDateValueFormatter(sorozats));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        setSulyChartData(sulyChart, sorozats);

        sulyChart.animateX(1500);
        sulyChart.getLegend().setEnabled(false);
    }

    private void setSulyChartData(LineChart sulyChart, List<OsszSorozat> sorozats) {
        ArrayList<Entry> entries = getOsszSulyEntries(sorozats);

        LineDataSet set;

        set = getLineDataSet(sulyChart, entries, "Össz súly");

        LineData data = new LineData(set);
        sulyChart.setData(data);
    }

    private void setIsmChartData(LineChart sulyChart, List<OsszSorozat> sorozats) {
        ArrayList<Entry> entries = getIsmetlesEntries(sorozats);

        LineDataSet set;

        set = getLineDataSet(sulyChart, entries, "Ismétlések");

        LineData data = new LineData(set);
        sulyChart.setData(data);
    }

    private IndexAxisValueFormatter getDateValueFormatter(List<OsszSorozat> list) {
        return new IndexAxisValueFormatter(
                list.stream().map(sor -> format.format(new Date(sor.getNaplodatum()))).collect(Collectors.toList())
        );
    }

    private LineDataSet getLineDataSet(LineChart lineChart, ArrayList<Entry> entries, String label) {
        LineDataSet set;
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set.setValues(entries);
            set.notifyDataSetChanged();
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            set = new LineDataSet(entries, label);
            set.setDrawIcons(false);
            set.enableDashedLine(10f, 5f, 0f);
            set.setColor(Color.WHITE);
            set.setCircleColor(Color.GREEN);
            set.setLineWidth(1f);
            set.setCircleRadius(3f);
            set.setCircleHoleRadius(3f);
            set.setFormLineWidth(1f);
            set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set.setFormSize(15.f);
        }
        return set;
    }

    private ArrayList<Entry> getIsmetlesEntries(List<OsszSorozat> lencseList) {
        ArrayList<Entry> entries = new ArrayList<>(lencseList.size());
        for (int i = 0; i < lencseList.size(); i++) {
            entries.add(new Entry(i,
                    lencseList.get(i).getOsszism(),
                    lencseList.get(i)));
        }
        return entries;
    }

    private ArrayList<Entry> getOsszSulyEntries(List<OsszSorozat> lencseList) {
        ArrayList<Entry> entries = new ArrayList<>(lencseList.size());
        for (int i = 0; i < lencseList.size(); i++) {
            entries.add(new Entry(i,
                    lencseList.get(i).getOsszsuly(),
                    lencseList.get(i)));
        }
        return entries;
    }

    @Override
    public void setupCustomActionBar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setLogo(R.drawable.ic_gyakorlatok_kis_logo);
            getSupportActionBar().setTitle(R.string.welcome_gyakorlatok_title);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        sorozatUtil.osszSorozatNezoke(this, (OsszSorozat) e.getData());
    }

    @Override
    public void onNothingSelected() {

    }
}
