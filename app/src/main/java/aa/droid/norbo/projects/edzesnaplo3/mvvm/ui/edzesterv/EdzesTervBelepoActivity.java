package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityEdzestervBelepoBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EdzesTervBelepoActivity extends EdzesTervBaseActivity<MvvmActivityEdzestervBelepoBinding> {
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

        binding.tervekMegtekintese.setOnClickListener(v -> Toast.makeText(this, "Fejlesztés alatt", Toast.LENGTH_SHORT).show());

        binding.tervezoBelepesBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, EdzesTervKeszitoActivity.class));
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        });
    }
}