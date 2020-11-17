package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.GyakorlatReportActivityLayoutBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.OsszSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.SorozatUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.report.SorozatReportUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmGyakorlatReportActivity extends BaseActiviry<GyakorlatReportActivityLayoutBinding> {
    private static final String TAG = "MvvmGyakorlatReportActi";
    public static final String EXTRA_GYAK = "aa.droid.norbo.projects.edzesnaplo3.mvvm.EXTRA_GYAK";
    public static final String EXTRA_OSSZ_SOR = "aa.droid.norbo.projects.edzesnaplo3.mvvm.EXTRA_OSSZ_SOR";
    public static final String EXTRA_SOROZAT = "aa.droid.norbo.projects.edzesnaplo3.mvvm.EXTRA_SOROZAT";

    @Inject
    ModelConverter modelConverter;

    @Inject
    SorozatReportUtil sorozatReportUtil;

//    private GyakorlatUI gyakorlatUI;

    public MvvmGyakorlatReportActivity() {
        super(R.layout.gyakorlat_report_activity_layout);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getSerializableExtra(EXTRA_GYAK) != null) {
            String gyakorlat = getIntent().getStringExtra(EXTRA_GYAK);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(gyakorlat);
            }
        }

        List<OsszSorozat> osszSorozats = (List<OsszSorozat>) getIntent().getSerializableExtra(EXTRA_OSSZ_SOR);
        List<Sorozat> sorozats = (List<Sorozat>) getIntent().getSerializableExtra(EXTRA_SOROZAT);

        sorozatReportUtil.initSorozatReportCharts(this, osszSorozats, sorozats,
                binding.osszsulyEsIsmChart,
                binding.elteltIdoChart);
    }


    @Override
    public void setupCustomActionBar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setLogo(R.drawable.ic_gyakorlatok_kis_logo);
            getSupportActionBar().setTitle(R.string.welcome_gyakorlatok_title);
        }
    }
}
