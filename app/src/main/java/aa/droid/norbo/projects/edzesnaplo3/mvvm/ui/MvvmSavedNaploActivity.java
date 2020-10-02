package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityMentettNaplokBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.service.files.MyFileService;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.NaploListFactory;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmSavedNaploActivity extends BaseActiviry<MvvmActivityMentettNaplokBinding> implements NaploListFactory.NaploTorlesInterface {
    private static final String TAG = "MvvmSavedNaploActivity";
    private static final int FILE_LOAD_RCODE = 10001;
    private static final int FILE_V3_LOAD = 2001;
    @Inject
    NaploViewModel naploViewModel;

    @Inject
    SorozatViewModel sorozatViewModel;

    @Inject
    NaploListFactory naploListFactory;

    @Inject
    DateTimeFormatter dateTimeFormatter;

    @Inject
    MyFileService myFileService;

    public MvvmSavedNaploActivity() {
        super(R.layout.mvvm_activity_mentett_naplok);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.mentettNaplokWarningLabel.setVisibility(View.VISIBLE);

        naploViewModel.getNaploWithSorozat().observe(this, naplos -> {
            if(naplos != null && naplos.size() > 0) {
                binding.mentettNaplokWarningLabel.setVisibility(View.GONE);
                binding.mentettNaplokLista.setVisibility(View.VISIBLE);
                binding.mentettNaplokLista.setAdapter(naploListFactory.getListAdapter(naplos));
                binding.mentettNaplokLista.setOnItemClickListener((parent, view, position, id) -> {
                    NaploWithSorozat item = (NaploWithSorozat) parent.getAdapter().getItem(position);
                    Intent intent = new Intent(this, NaploDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(NaploDetailsActivity.EXTRA_NAPLO_DATUM, item.daonaplo.getNaplodatum());
                    startActivity(intent);
                    overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
                });
            } else {
                binding.mentettNaplokWarningLabel.setVisibility(View.VISIBLE);
                binding.mentettNaplokLista.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void setupCustomActionBar() {
        setSupportActionBar(binding.toolbar.customToolbar);
        if(getSupportActionBar() != null) {
            binding.toolbar.naploDetails.setVisibility(View.GONE);
            binding.toolbar.moreOptions.setOnClickListener(this::showMoreOptionsPopupMenu);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected PopupMenu showMoreOptionsPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.mvvm_naplo_details_menu, popupMenu.getMenu());
        popupMenu.getMenu().removeItem(R.id.naplo_details_save_to_file);

        popupMenu.setOnMenuItemClickListener(item -> {
            onContextItemSelected(item);
            return true;
        });

        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this, (MenuBuilder) popupMenu.getMenu(), view);
        menuPopupHelper.setForceShowIcon(true);
        menuPopupHelper.show();
        return popupMenu;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.naplo_details_load_from_file) {
            loadNaploFileFromSD(FILE_LOAD_RCODE);
        } else if(item.getItemId() == R.id.naplo_details_v3_load) {
            loadNaploFileFromSD(FILE_V3_LOAD);
        }
        return super.onContextItemSelected(item);
    }

    private void loadNaploFileFromSD(int code) {
        Intent filechooser = new Intent(Intent.ACTION_GET_CONTENT);
        filechooser.setType("*/*");
        filechooser.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/octet-stream", "application/json"});
        filechooser.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(filechooser, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == FILE_LOAD_RCODE && resultCode == RESULT_OK) {
            myFileService.futureLoadSaveFile(data.getData()).whenComplete((naplo, throwable) -> {
                if(throwable != null) {
                    toast("Nem sikerült betölteni a fájlt\n"+throwable.getMessage());
                    return;
                }

                if(naplo != null) {
                    naploViewModel.insert(naplo.naplo);
                    sorozatViewModel.insertAll(naplo.sorozats);
                    toast("Napló betöltve!");
                }
            });
        } else if(requestCode == FILE_V3_LOAD && resultCode == RESULT_OK) {
            myFileService.futureLoadV3saves(data.getData()).whenComplete((naploAll, throwable) -> {
                if(throwable != null) {
                    toast("Nem sikerült betölteni a fájlt\n"+throwable.getMessage());
                    return;
                }

                if(naploAll != null) {
                    naploViewModel.insert(getNaploFromV3(naploAll.getNaplo()));
                    List<Sorozat> sorozats = naploAll.getSorozatWithGyakorlats()
                            .stream()
                            .map(srs -> getSorozatFromV3(srs.sorozat))
                            .collect(Collectors.toList());
                    sorozatViewModel.insertAll(sorozats);
                    toast("Sikeresen betöltve a napló!");
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void naplotTorol(long naplodatum) {
        new AlertDialog.Builder(this)
                .setTitle("Törlés?")
                .setMessage(dateTimeFormatter.getNaploDatum(naplodatum)+" valóban törölni akarod?")
                .setPositiveButton("ok", (dialog, which) -> {
                    naploViewModel.deleteNaplo(naplodatum);
                    sorozatViewModel.deleteSorozat(naplodatum);
                    Toast.makeText(this, "Sikeresen törölve a napló!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void naplotMent(long naplodatum) {
        myFileService.futureFileSave(naplodatum).whenComplete((uri, throwable) -> {
            if (throwable == null) {
                toast("Fájl mentve: " + uri.toString());
            } else {
                toast("Hiba a mentés végrehajtása közben");
                Log.e(TAG, "naplotMent: ", throwable);
            }
        });
    }

    private void toast(String msg) {
        runOnUiThread(() -> Toast.makeText(MvvmSavedNaploActivity.this, msg, Toast.LENGTH_SHORT).show());
    }

    private Naplo getNaploFromV3(aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo naplo) {
        return new Naplo(Long.parseLong(naplo.getNaplodatum()), "kulso_forras", naplo.getCommentFilePath());
    }

    private Sorozat getSorozatFromV3(aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat sorozat) {
        return new Sorozat(sorozat.getGyakorlatid(), sorozat.getSuly(), sorozat.getIsmetles(),
                Long.parseLong(sorozat.getIsmidopont()),
                Long.parseLong(sorozat.getNaplodatum()));
    }
}
