package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityBelepoBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.utils.AdatFeltoltes;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.NaploListFactory;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmBelepoActivity extends BaseActiviry<MvvmActivityBelepoBinding> {
    public static final String AUDIO_RECORD_IS = "aa.droid.norbo.projects.edzesnaplo3.AUDIO_RECORD_IS";
    private static final int MY_PERMISSION = 200;
    private static final String[] MANI_PERMS = new String[] {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences naplopref;

    @Inject
    AdatFeltoltes adatFeltoltes;

    @Inject
    NaploViewModel naploViewModel;

    @Inject
    NaploListFactory naploListFactory;

    @Inject
    SorozatViewModel sorozatViewModel;

    public MvvmBelepoActivity() {
        super(R.layout.mvvm_activity_belepo);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adatFeltoltes.gyakorlatAdatFeltoltes();

        binding.btnBelepes.setEnabled(false);
        naplopref = getSharedPreferences("naplo", MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(this, MANI_PERMS[0])
                == PackageManager.PERMISSION_GRANTED) {

            SharedPreferences.Editor edit = naplopref.edit();
            edit.putBoolean(AUDIO_RECORD_IS, true);
            edit.apply();
        }


        if (ActivityCompat.checkSelfPermission(this, MANI_PERMS[1])
                == PackageManager.PERMISSION_GRANTED) {
            binding.btnBelepes.setEnabled(true);
            binding.btnBelepes.setOnClickListener(v -> {
                startActivity(new Intent(this, TevekenysegActivity.class));
                overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
            });
        } else {
            ActivityCompat.requestPermissions(this, MANI_PERMS, MY_PERMISSION);
            binding.btnBelepes.setEnabled(false);
        }

        naploViewModel.getNaploList().observe(this, naplos -> {
            if(naplos != null) {
                binding.belepoInfoDetails.setText("Eddig ["+naplos.size()+" db] napló lett rögzítve");
            }
        });

        binding.btnNaplok.setOnClickListener(v -> {
            startActivity(new Intent(this, MvvmSavedNaploActivity.class));
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        });
    }

    @Override
    protected PopupMenu showMoreOptionsPopupMenu(View view) {
        PopupMenu popupMenu = super.showMoreOptionsPopupMenu(view);
        popupMenu.getMenu().removeItem(R.id.tevekenyseg_gyakorlat_view);
        return popupMenu;
    }

    @Override
    public void setupCustomActionBar() {
        setSupportActionBar(binding.toolbar.customToolbar);
        if (getSupportActionBar() != null) {
            binding.toolbar.naploDetails.setVisibility(View.GONE);
            binding.toolbar.moreOptions.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION:
                boolean audiorecordon = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                SharedPreferences.Editor edit = naplopref.edit();
                edit.putBoolean(AUDIO_RECORD_IS, audiorecordon);
                edit.apply();

                if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    binding.warning.setVisibility(View.VISIBLE);
                    binding.warning.setText(R.string.mvvm_permissions_warning);
                }
                break;
        }
    }
}
