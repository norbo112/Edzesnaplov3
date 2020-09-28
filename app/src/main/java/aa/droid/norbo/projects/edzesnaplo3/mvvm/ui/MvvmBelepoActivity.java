package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.MainActivity;
import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityBelepoBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmBelepoActivity extends BaseActiviry<MvvmActivityBelepoBinding> {
    private static final int MY_PERMISSION = 200;
    private static final String[] MANI_PERMS = new String[] {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences naplopref;

    @Inject
    NaploViewModel naploViewModel;

    @Inject
    SorozatViewModel sorozatViewModel;

    public MvvmBelepoActivity() {
        super(R.layout.mvvm_activity_belepo);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar.customToolbar);
        setupCustomActionBar();

        binding.btnBelepes.setEnabled(false);
        naplopref = getSharedPreferences("naplo", MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(this, MANI_PERMS[0])
                == PackageManager.PERMISSION_GRANTED) {

            SharedPreferences.Editor edit = naplopref.edit();
            edit.putBoolean(MainActivity.AUDIO_RECORD_IS, true);
            edit.apply();
        }


        if (ActivityCompat.checkSelfPermission(this, MANI_PERMS[1])
                == PackageManager.PERMISSION_GRANTED) {
            binding.btnBelepes.setEnabled(true);
            binding.btnBelepes.setOnClickListener(v -> startActivity(new Intent(this, TevekenysegActivity.class)));
        } else {
            ActivityCompat.requestPermissions(this, MANI_PERMS, MY_PERMISSION);
            binding.btnBelepes.setEnabled(false);
        }

        naploViewModel.getNaploList().observe(this, naplos -> {
            if(naplos != null) {
                binding.belepoInfoDetails.setText("Eddig ["+naplos.size()+" db] napló lett rögzítve");
            }
        });
    }

    @Override
    public void setupCustomActionBar() {
        if (getSupportActionBar() != null) {
            binding.toolbar.naploDetails.setOnClickListener(v -> {
                naploViewModel.getNaploList().observe(this, naplos -> {
                    if(naplos != null) {
                        ArrayAdapter<Naplo> listAdapter = new ArrayAdapter<>(MvvmBelepoActivity.this, android.R.layout.simple_list_item_1, naplos);
                        new AlertDialog.Builder(this)
                                .setTitle("Mentett naplók")
                                .setAdapter(listAdapter, (dialog, which) -> {
                                    Naplo naplo = listAdapter.getItem(which);
                                    if (naplo != null) {
                                        Intent intent = new Intent(this, NaploDetailsActivity.class);
                                        intent.putExtra(NaploDetailsActivity.EXTRA_NAPLO_DATUM, naplo.getNaplodatum());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(this, "Nem lehet megtekinteni a naplót :(", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setPositiveButton("ok", (dialog, which) -> dialog.dismiss())
                                .show();
                    } else {
                        new AlertDialog.Builder(this)
                                .setMessage("Nincsenek még mentve adatok")
                                .show();
                    }
                });
            });

            binding.toolbar.moreOptions.setOnClickListener(this::showMoreOptionsPopupMenu);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION:
                boolean audiorecordon = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                SharedPreferences.Editor edit = naplopref.edit();
                edit.putBoolean(MainActivity.AUDIO_RECORD_IS, audiorecordon);
                edit.apply();

                if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    binding.warning.setText(R.string.mvvm_permissions_warning);
                }
                break;
        }
    }
}
