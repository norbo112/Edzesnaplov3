package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.GyakorlatReportActivityLayoutBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.naplo.SorozatUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.report.SorozatReportUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmGyakorlatReportActivity extends BaseActiviry<GyakorlatReportActivityLayoutBinding> {
    public static final String EXTRA_GYAK = "aa.droid.norbo.projects.edzesnaplo3.mvvm.EXTRA_GYAK";

    private SimpleDateFormat format = new SimpleDateFormat("MM-dd", Locale.getDefault());

    @Inject
    ModelConverter modelConverter;

    @Inject
    SorozatViewModel sorozatViewModel;

    @Inject
    SorozatUtil sorozatUtil;

    @Inject
    SorozatReportUtil sorozatReportUtil;

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

        sorozatReportUtil.initSorozatReportCharts(this, gyakorlatUI.getId(),
                binding.osszsulyChart,
                binding.elteltIdoChart,
                binding.osszismChart);
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
