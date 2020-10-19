package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityEdzestervBelepoBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils.EdzesTervViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EdzesTervBelepoActivity extends EdzesTervBaseActivity<MvvmActivityEdzestervBelepoBinding> {
    @Inject
    EdzesTervViewModel edzesTervViewModel;

    public EdzesTervBelepoActivity() {
        super(R.layout.mvvm_activity_edzesterv_belepo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.tervToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edzésnapló v4");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.tervekMegtekintese.setOnClickListener(v -> {
            startActivity(new Intent(this, EdzesTervNezokeActivity.class));
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        });

        binding.tervezoBelepesBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, EdzesTervKeszitoActivity.class));
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        });

        if(edzesTervViewModel.getEdzesTerv() != null)
            edzesTervViewModel.clear();

        edzesTervViewModel.getEdzestervek().observe(this, edzesTervWithEdzesnaps -> {
            if(edzesTervWithEdzesnaps != null && edzesTervWithEdzesnaps.size() > 0) {
                String info = edzesTervWithEdzesnaps.size() + " db mentett terv az adatbázisban";
                binding.mvvmEdzestervInfo.setText(info);
            }
        });
    }
}