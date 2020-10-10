package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityEdzestervElonezetBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityEdzestervNezokeBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.converters.TervModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.CsoportWithGyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.EdzesTervWithEdzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv.relations.EdzesnapWithCsoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv.GyakorlatTervEntity;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils.EdzesTervViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EdzesTervNezokeActivity extends EdzesTervBaseActivity<MvvmActivityEdzestervNezokeBinding> {
    @Inject
    EdzesTervViewModel edzesTervViewModel;

    @Inject
    TervModelConverter modelConverter;

    public EdzesTervNezokeActivity() {
        super(R.layout.mvvm_activity_edzesterv_nezoke);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.tervToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edzéstervek");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edzesTervViewModel.getEdzestervek().observe(this, edzesTervWithEdzesnaps -> {
            if(edzesTervWithEdzesnaps != null && edzesTervWithEdzesnaps.size() > 0) {
                binding.tervElonezetLinearLayout.removeAllViews();
                initElonezet(makeEdzesterv(edzesTervWithEdzesnaps));
            } else {
                appendInfo();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void appendInfo() {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams llparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(llparam);
        textView.setText("Jelenleg nincsenek edzéstervek rögzítve");
        textView.setTextSize(16);
        binding.tervElonezetLinearLayout.addView(textView);
    }

    private List<EdzesTerv> makeEdzesterv(List<EdzesTervWithEdzesnap> edzesTervWithEdzesnaps) {
        List<EdzesTerv> edzesTervs = new ArrayList<>();

        for (EdzesTervWithEdzesnap edzesTervWithEdzesnap : edzesTervWithEdzesnaps) {
            EdzesTerv edzesTerv = new EdzesTerv(edzesTervWithEdzesnap.edzesTervEntity.getMegnevezes());
            for (EdzesnapWithCsoport eddzesnap: edzesTervWithEdzesnap.edzesnapList) {
                Set<String> csoport = eddzesnap.csoportsWithGyakorlat.stream().map(
                        csoportWithGyakorlatTerv -> csoportWithGyakorlatTerv.csoportEntity.getIzomcsoport()
                ).collect(Collectors.toSet());
                Edzesnap edzesnap = new Edzesnap(eddzesnap.edzesnapEntity.getEdzesNapLabel());
                csoport.forEach(s -> {
                    Csoport csoport1 = new Csoport(s);
                    for(CsoportWithGyakorlatTerv csoportsWithGyakorlat: eddzesnap.csoportsWithGyakorlat) {
                        if(csoportsWithGyakorlat.csoportEntity.getIzomcsoport().equals(s)) {
                            for (GyakorlatTervEntity gy : csoportsWithGyakorlat.gyakorlatTervEntities) {
                                csoport1.addGyakorlat(modelConverter.getFromEntity(gy));
                            }
                        }
                    }
                    edzesnap.addCsoport(csoport1);
                });
                edzesTerv.addEdzesNap(edzesnap);
            }
            edzesTervs.add(edzesTerv);
        }

        return edzesTervs;
    }

    private void initElonezet(List<EdzesTerv> edzesTervs) {
        for(EdzesTerv terv: edzesTervs) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams llparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llparam.topMargin = 20;
            llparam.bottomMargin = 20;
            linearLayout.setLayoutParams(llparam);
            linearLayout.setBackgroundResource(R.drawable.custom_edzesterv_hatter);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            List<Edzesnap> edzesnapList = terv.getEdzesnapList();

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 0;
            params.bottomMargin = 20;

            TextView tt = new TextView(this);
            tt.setLayoutParams(params);
            tt.setText(terv.getMegnevezes());
            tt.setGravity(Gravity.CENTER);
            tt.setBackgroundColor(Color.BLACK);
            tt.setTextColor(Color.WHITE);
            tt.setPadding(20,20,20,20);
            tt.setTextSize(21);

            linearLayout.addView(tt);

            for (Edzesnap edzesnap: edzesnapList) {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = 20;
                params.bottomMargin = 20;
                params.leftMargin = 10;

                TextView textView = new TextView(this);
                textView.setLayoutParams(params);
                textView.setText(edzesnap.getEdzesNapLabel());
                textView.setPadding(10,10,10,10);
                textView.setBackgroundResource(R.drawable.custom_et_gyak_hatter);
//                binding.tervElonezetLinearLayout.setGravity(Gravity.CENTER);
                linearLayout.addView(textView);

                for(Csoport csoport: edzesnap.getValasztottCsoport()) {
                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 20;
                    params.bottomMargin = 20;
                    params.leftMargin = 70;

                    TextView textView1 = new TextView(this);
                    textView1.setLayoutParams(params);
                    textView1.setText(csoport.getIzomcsoport());
                    linearLayout.addView(textView1);

                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 20;
                    params.bottomMargin = 20;
                    params.leftMargin = 100;
                    for(GyakorlatTerv gyakorlatTerv: csoport.getValasztottGyakorlatok()) {
                        TextView textView2 = new TextView(this);
                        textView2.setLayoutParams(params);
                        textView2.setText(gyakorlatTerv.toString());

                        linearLayout.addView(textView2);
                    }
                }
            }

            binding.tervElonezetLinearLayout.addView(linearLayout);
        }
    }
}