package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.model.GyakorlatSorozatElteltIdo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmGyakorlatReportActivity extends BaseActiviry<GyakorlatReportActivityLayoutBinding>
    implements OnChartValueSelectedListener, SorozatUtil.SorozatUtilReportInterface {
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
                initOsszsulyEsIsmetlesChart(binding.osszsulyesismChart, osszSorozats);
            } else {
                Toast.makeText(this, "Sajnos ehhez a gyakorlathoz nincs sorozat rögzítve", Toast.LENGTH_SHORT).show();
            }
        });

        sorozatViewModel.getSorozatByGyakorlat(gyakorlatUI.getId()).observe(this, sorozats -> {
            if(sorozats != null && sorozats.size() > 0) {
                initElteltIdoChart(binding.elteltIdoChart, sorozatUtil.getEleltIdoList(sorozats));
            }
        });
    }

    private void initElteltIdoChart(LineChart elteltIdoChart, List<GyakorlatSorozatElteltIdo> gyakorlatSorozatElteltIdos) {
        elteltIdoChart.getDescription().setEnabled(false);
        elteltIdoChart.setDrawGridBackground(false);
        elteltIdoChart.getAxisRight().setEnabled(false);
        elteltIdoChart.getAxisLeft().setTextColor(Color.WHITE);
        elteltIdoChart.setOnChartValueSelectedListener(this);

        XAxis xAxis = elteltIdoChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setEnabled(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(getEleltIdoDateValueFormatter(gyakorlatSorozatElteltIdos));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        setEleltIdoData(elteltIdoChart, gyakorlatSorozatElteltIdos);

        elteltIdoChart.animateX(1500);
        elteltIdoChart.getLegend().setEnabled(false);
    }

    private void initOsszsulyEsIsmetlesChart(LineChart sulyChart, List<OsszSorozat> sorozats) {
        sulyChart.getDescription().setEnabled(false);
        sulyChart.setDrawGridBackground(false);
        sulyChart.getAxisRight().setEnabled(false);
        sulyChart.getAxisLeft().setTextColor(Color.WHITE);
        sulyChart.setOnChartValueSelectedListener(this);

        XAxis xAxis = sulyChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setEnabled(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(getDateValueFormatter(sorozats));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        setSulyEsIsmetlesData(sulyChart, sorozats);

        sulyChart.animateX(1500);
        sulyChart.getLegend().setEnabled(false);
    }

    private void setSulyEsIsmetlesData(LineChart sulyChart, List<OsszSorozat> sorozats) {
        ArrayList<Entry> entries = getOsszSulyEntries(sorozats);
        ArrayList<Entry> entriesIsm = getIsmetlesEntries(sorozats);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(getLineDataSet(sulyChart, entries, "Össz súly", Color.RED));
        sets.add(getLineDataSet(sulyChart, entriesIsm, "Ismétlés", Color.GREEN));

        LineData data = new LineData(sets);
        sulyChart.setData(data);
    }

    private void setEleltIdoData(LineChart elteltIdoChart, List<GyakorlatSorozatElteltIdo> elteltIdos) {
        ArrayList<Entry> entries = getEleltIdoEntries(elteltIdos);
        LineDataSet lineDataSet = new LineDataSet(entries, "Elelt idő");
        elteltIdoChart.setData(new LineData(lineDataSet));
    }

    private IndexAxisValueFormatter getDateValueFormatter(List<OsszSorozat> list) {
        return new IndexAxisValueFormatter(
                list.stream().map(sor -> format.format(new Date(sor.getNaplodatum()))).collect(Collectors.toList())
        );
    }

    private IndexAxisValueFormatter getEleltIdoDateValueFormatter(List<GyakorlatSorozatElteltIdo> list) {
        return new IndexAxisValueFormatter(
                list.stream().map(sor -> format.format(new Date(sor.getNaploDatum()))).collect(Collectors.toList())
        );
    }

    private LineDataSet getLineDataSet(LineChart lineChart, ArrayList<Entry> entries, String label, int color) {
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
            set.setCircleColor(color);
            set.setLineWidth(1f);
            set.setCircleRadius(3f);
            set.setCircleHoleRadius(3f);
            set.setFormLineWidth(1f);
            set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set.setFormSize(15.f);
        }
        return set;
    }

    private ArrayList<Entry> getEleltIdoEntries(List<GyakorlatSorozatElteltIdo> elteltIdos) {
        ArrayList<Entry> entries = new ArrayList<>(elteltIdos.size());
        for (int i = 0; i < elteltIdos.size(); i++) {
            entries.add(new Entry(i,
                    elteltIdos.get(i).getElteltIdo(), elteltIdos.get(i)));
        }
        return entries;
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
    public void viewNaploFromReport(String naplodatum) {
        Intent intent = new Intent(this, NaploDetailsActivity.class);
        intent.putExtra(NaploDetailsActivity.EXTRA_NAPLO_DATUM, naplodatum);
        startActivity(intent);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
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
        if(e.getData() instanceof OsszSorozat) {
            sorozatUtil.osszSorozatNezoke(this, (OsszSorozat) e.getData(),
                    Long.toString(((OsszSorozat) e.getData()).getNaplodatum()), this);
        } else if(e.getData() instanceof GyakorlatSorozatElteltIdo) {
            String elteltIdoStr = String.format(Locale.getDefault(), "Eltelt idő: %d perc",
                    ((GyakorlatSorozatElteltIdo) e.getData()).getElteltIdo());
            new AlertDialog.Builder(this)
                    .setTitle(sorozatUtil.getFormatter().getNaploDatum(((GyakorlatSorozatElteltIdo)e.getData()).getNaploDatum()))
                    .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                    .setMessage(elteltIdoStr)
                    .show();
        }
    }

    @Override
    public void onNothingSelected() {

    }
}
