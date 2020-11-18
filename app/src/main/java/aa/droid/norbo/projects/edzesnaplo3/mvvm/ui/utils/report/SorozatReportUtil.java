package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmKorabbiSorozatItemBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.OsszSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.NaploDetailsActivity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.SorozatUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.model.GyakorlatSorozatElteltIdo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.widgets.NaploGyakOsszsuly;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class SorozatReportUtil {
    private Context context;
    private SorozatUtil sorozatUtil;
    private DateTimeFormatter formatter;

    private Activity activity;

    @Inject
    public SorozatReportUtil(@ActivityContext Context context,
                             DateTimeFormatter formatter, SorozatUtil sorozatUtil) {
        this.context = context;
        this.formatter = formatter;
        this.sorozatUtil = sorozatUtil;
    }

    public void initSorozatReportCharts(Activity activity, List<OsszSorozat> osszSorozats, List<Sorozat> sorozats, LineChart sulyChart, LineChart elteltidoChart) {
        this.activity = activity;
        initOsszSulyEsIsmChart(sulyChart, osszSorozats);
        initElteltIdoChart(elteltidoChart, sorozatUtil.getEleltIdoList(sorozats));
    }

    private void initElteltIdoChart(LineChart elteltIdoChart, List<GyakorlatSorozatElteltIdo> gyakorlatSorozatElteltIdos) {
        elteltIdoChart.getDescription().setEnabled(false);
        elteltIdoChart.setDrawGridBackground(false);
        elteltIdoChart.getAxisRight().setEnabled(false);
        elteltIdoChart.getAxisLeft().setTextColor(Color.WHITE);
        elteltIdoChart.setOnChartValueSelectedListener(chartListener);

        XAxis xAxis = elteltIdoChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setEnabled(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(getEleltIdoDateValueFormatter(gyakorlatSorozatElteltIdos));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        setEleltIdoData(elteltIdoChart, gyakorlatSorozatElteltIdos);
        setLegends(elteltIdoChart, new String[]{"Eltelt idő"}, new int[]{Color.CYAN});

        elteltIdoChart.animateX(1500);
    }

    private void initOsszSulyEsIsmChart(LineChart sulyChart, List<OsszSorozat> sorozats) {
        sulyChart.getDescription().setEnabled(false);
        sulyChart.setDrawGridBackground(false);
        sulyChart.getAxisRight().setEnabled(false);
        sulyChart.getAxisLeft().setTextColor(Color.WHITE);
        sulyChart.setOnChartValueSelectedListener(chartListener);

        XAxis xAxis = sulyChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setEnabled(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(getDateValueFormatter(sorozats));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        setOsszSulyEsIsmChartData(sulyChart, sorozats);

        sulyChart.animateX(1500);
    }

    private void setOsszSulyEsIsmChartData(LineChart sulyChart, List<OsszSorozat> sorozats) {
        ArrayList<Entry> entries = getOsszSulyEntries(sorozats);
        ArrayList<Entry> entriesIsm = getIsmetlesEntries(sorozats);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(getLineDataSet(sulyChart, entries, "Össz súly", Color.RED));
        sets.add(getLineDataSet(sulyChart, entriesIsm, "Ismétlés", Color.GREEN));
        setLegends(sulyChart, new String[]{"Össz súly", "Ismétlés"}, new int[]{Color.RED, Color.GREEN});

        LineData data = new LineData(sets);
        data.setValueTextColor(Color.WHITE);
        data.setValueFormatter(defaultValueFormatter);
        sulyChart.setData(data);
    }

//    private void setOsszIsmChartData(LineChart sulyChart, List<OsszSorozat> sorozats) {
//        ArrayList<Entry> entriesIsm = getIsmetlesEntries(sorozats);
//
//        ArrayList<ILineDataSet> sets = new ArrayList<>();
//        sets.add(getLineDataSet(sulyChart, entriesIsm, "Ismétlés", Color.GREEN));
//        setLegends(sulyChart, new String[]{"Ismétlés"}, new int[]{Color.GREEN});
//
//        LineData data = new LineData(sets);
//        data.setValueTextColor(Color.WHITE);
//        data.setValueFormatter(defaultValueFormatter);
//        sulyChart.setData(data);
//    }

    private void setLegends(LineChart sulyChart, String[] labels, int[] colors) {
        Legend legend = sulyChart.getLegend();
        legend.setEnabled(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextColor(Color.WHITE);

        List<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < labels.length; i++) {
            legendEntries.add(getLegendEntry(labels[i], colors[i]));
        }
        legend.setCustom(legendEntries);
    }

    private LegendEntry getLegendEntry(String label, int color) {
        return new LegendEntry(label, Legend.LegendForm.CIRCLE, 10f, 10f,
                null, color);
    }

    private void setEleltIdoData(LineChart elteltIdoChart, List<GyakorlatSorozatElteltIdo> elteltIdos) {
        ArrayList<Entry> entries = getEleltIdoEntries(elteltIdos);
        LineDataSet lineDataSet = getLineDataSet(elteltIdoChart, entries, "Eltelt idő", Color.CYAN);
        LineData data = new LineData(lineDataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueFormatter(defaultValueFormatter);
        elteltIdoChart.setData(data);
    }

    private IndexAxisValueFormatter getDateValueFormatter(List<OsszSorozat> list) {
        return new IndexAxisValueFormatter(
                list.stream().map(sor -> formatter.getNaploShortDatum(sor.getNaplodatum())).collect(Collectors.toList())
        );
    }

    private IndexAxisValueFormatter getEleltIdoDateValueFormatter(List<GyakorlatSorozatElteltIdo> list) {
        return new IndexAxisValueFormatter(
                list.stream().map(sor -> formatter.getNaploShortDatum(sor.getNaploDatum())).collect(Collectors.toList())
        );
    }

    private DefaultValueFormatter defaultValueFormatter = new DefaultValueFormatter(0) {
        @Override
        public String getFormattedValue(float value) {
            return String.format(Locale.getDefault(), "%.0f", value);
        }
    };

    private LineDataSet getLineDataSet(LineChart lineChart, ArrayList<Entry> entries, String label, int color) {
        LineDataSet set;
//        if (lineChart.getData() != null &&
//                lineChart.getData().getDataSetCount() > 0) {
//            set = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
//            set.setValues(entries);
//            set.notifyDataSetChanged();
//            lineChart.getData().notifyDataChanged();
//            lineChart.notifyDataSetChanged();
//        } else {
        set = new LineDataSet(entries, label);
        set.setDrawIcons(false);
        set.enableDashedLine(10f, 5f, 0f);
        set.setColor(Color.LTGRAY);
        set.setCircleColor(color);
        set.setLineWidth(1f);
        set.setCircleRadius(3f);
        set.setCircleHoleRadius(3f);
        set.setFormLineWidth(1f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);
//        }
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

    private OnChartValueSelectedListener chartListener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {
            if (e.getData() instanceof OsszSorozat) {
                sorozatUtil.osszSorozatNezoke(activity, (OsszSorozat) e.getData(),
                        Long.toString(((OsszSorozat) e.getData()).getNaplodatum()), reportInterface);
            } else if (e.getData() instanceof GyakorlatSorozatElteltIdo) {
                String elteltIdoStr = String.format(Locale.getDefault(), "Eltelt idő: %d perc",
                        ((GyakorlatSorozatElteltIdo) e.getData()).getElteltIdo());
                MvvmKorabbiSorozatItemBinding binding = MvvmKorabbiSorozatItemBinding.inflate(activity.getLayoutInflater());
                binding.korabbiSorozatDatumLabel.setText(
                        sorozatUtil.getFormatter().getNaploDatum(((GyakorlatSorozatElteltIdo) e.getData()).getNaploDatum()));
                binding.korabbiSorozatLista.setText(elteltIdoStr);
                new AlertDialog.Builder(context)
                        .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                        .setView(binding.getRoot())
                        .show();
            }
        }

        @Override
        public void onNothingSelected() {

        }
    };

    SorozatUtil.SorozatUtilReportInterface reportInterface = new SorozatUtil.SorozatUtilReportInterface() {
        @Override
        public void viewNaploFromReport(String naplodatum) {
            Intent intent = new Intent(context, NaploDetailsActivity.class);
            intent.putExtra(NaploDetailsActivity.EXTRA_NAPLO_DATUM, naplodatum);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        }
    };
}
