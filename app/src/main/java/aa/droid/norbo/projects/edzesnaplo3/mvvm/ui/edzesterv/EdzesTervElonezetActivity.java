package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.w3c.dom.Text;

import java.util.List;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityEdzestervBelepoBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityEdzestervElonezetBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmAlertTevekenysegElhagyasaBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils.EdzesTervViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.qualifiers.ActivityContext;

@AndroidEntryPoint
public class EdzesTervElonezetActivity extends EdzesTervBaseActivity<MvvmActivityEdzestervElonezetBinding> {
    @Inject
    EdzesTervViewModel edzesTervViewModel;

    public EdzesTervElonezetActivity() {
        super(R.layout.mvvm_activity_edzesterv_elonezet);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.tervToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edzésterv előnézet");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initElonezet(edzesTervViewModel.getEdzesTerv(), binding.tervElonezetLinearLayout, this);

        binding.tervMentesBtn.setOnClickListener(v -> {
            try {
                edzesTervViewModel.mentes().whenComplete((aVoid, throwable) -> {
                    if (throwable != null) {
                        runOnUiThread(() -> Toast.makeText(this, "Hiba történt a mentés közben", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    runOnUiThread(() -> Toast.makeText(this, "Az edzésterv sikeresen mentve", Toast.LENGTH_SHORT).show());
                });
            } catch (NullPointerException e) {
                Toast.makeText(this, "Nincs mit menteni", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initElonezet(EdzesTerv edzesTerv, LinearLayout layout, Context mContext) {
//        EdzesTerv edzesTerv = edzesTervViewModel.getEdzesTerv();
        if(edzesTerv != null) {
            if(!isTablet(mContext)) binding.tervMegnevezes.setText(edzesTerv.getMegnevezes());

            List<Edzesnap> edzesnapList = edzesTerv.getEdzesnapList();

            for (Edzesnap edzesnap: edzesnapList) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = 20;
                params.bottomMargin = 20;
                params.leftMargin = 10;

                TextView textView = new TextView(mContext);
                textView.setLayoutParams(params);
                textView.setText(edzesnap.getEdzesNapLabel());
                textView.setPadding(10,10,10,10);
                textView.setBackgroundResource(R.drawable.custom_et_gyak_hatter);
//                binding.tervElonezetLinearLayout.setGravity(Gravity.CENTER);
                layout.addView(textView);

                for(Csoport csoport: edzesnap.getValasztottCsoport()) {
                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 20;
                    params.bottomMargin = 20;
                    params.leftMargin = 70;

                    TextView textView1 = new TextView(mContext);
                    textView1.setLayoutParams(params);
                    textView1.setText(csoport.getIzomcsoport());
                    layout.addView(textView1);

                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 20;
                    params.bottomMargin = 20;
                    params.leftMargin = 100;
                    for(GyakorlatTerv gyakorlatTerv: csoport.getValasztottGyakorlatok()) {
                        TextView textView2 = new TextView(mContext);
                        textView2.setLayoutParams(params);
                        textView2.setText(gyakorlatTerv.toString());

                        layout.addView(textView2);
                    }
                }
            }
        }
    }

    private boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }
}