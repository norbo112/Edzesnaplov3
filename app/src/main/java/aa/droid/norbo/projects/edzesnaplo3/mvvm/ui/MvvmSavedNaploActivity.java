package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityMentettNaplokBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.service.files.MyFileService;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.rcviews.NaploDetailsRcViewAdapterFactory;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.NaploListUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.WidgetUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.v3.NaploAll;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MvvmSavedNaploActivity extends BaseActiviry<MvvmActivityMentettNaplokBinding> implements NaploListUtil.NaploTorlesInterface {
    private static final String TAG = "MvvmSavedNaploActivity";
    private static final int FILE_LOAD_RCODE = 10001;
    private static final int FILE_V3_LOAD = 2001;

    private Animation naploBeAnim;
    private Animation naploKiAnim;

    @Inject
    NaploViewModel naploViewModel;

    @Inject
    SorozatViewModel sorozatViewModel;

    @Inject
    NaploListUtil naploListUtil;

    @Inject
    DateTimeFormatter dateTimeFormatter;

    @Inject
    MyFileService myFileService;

    @Inject
    NaploDetailsRcViewAdapterFactory adapterFactory;

    @Inject
    ModelConverter modelConverter;

    @Inject
    WidgetUtil widgetUtil;

    public MvvmSavedNaploActivity() {
        super(R.layout.mvvm_activity_mentett_naplok);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.mentettNaplokWarningLabel.setVisibility(View.VISIBLE);

        if(getResources().getBoolean(R.bool.isTablet)) {
            binding.naploDetailsDatumLabel.setText(R.string.mvvm_saved_tablet_info);
        }

        naploBeAnim = AnimationUtils.loadAnimation(this, R.anim.tablet_mentett_naplo_reszletek_view);
        naploKiAnim = AnimationUtils.loadAnimation(this, R.anim.tablet_mentett_naplo_reszletek_clear);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Naplók betöltése");
        progressDialog.show();


        naploViewModel.getNaploWithSorozat().observe(this, naplos -> {
            if(naplos != null && naplos.size() > 0) {
                binding.mentettNaplokWarningLabel.setVisibility(View.GONE);
                binding.mentettNaplokLista.setVisibility(View.VISIBLE);
                binding.mentettNaplokLista.setAdapter(naploListUtil.getListAdapter(naplos));
                binding.mentettNaploDbOsszsuly.setText(String.format(Locale.getDefault(), "[%,d] db mentve, %,d Kg megmozgatott súly",
                        naplos.size(), naplos.stream().mapToInt(np -> getSorozatOsszSuly(np.sorozats)).sum()));
                binding.mentettNaplokLista.setOnItemClickListener((parent, view, position, id) -> {
                    NaploWithSorozat item = (NaploWithSorozat) parent.getAdapter().getItem(position);
                    if(!getResources().getBoolean(R.bool.isTablet)) {
                        Intent intent = new Intent(this, NaploDetailsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(NaploDetailsActivity.EXTRA_NAPLO_DATUM, item.daonaplo.getNaplodatum());
                        startActivity(intent);
                        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
                    } else {
                        loadInfoForTablet(item);
                    }
                });
                runOnUiThread(progressDialog::dismiss);
            } else {
                binding.mentettNaplokWarningLabel.setVisibility(View.VISIBLE);
                binding.mentettNaplokLista.setVisibility(View.GONE);
            }
        });
    }

    private void loadInfoForTablet(NaploWithSorozat item) {
        //Teszt
        binding.naploReszletek.startAnimation(naploKiAnim);

        long naplodatum = Long.parseLong(item.daonaplo.getNaplodatum());
        sorozatViewModel.getForNaplo(naplodatum).observe(this, sorozatWithGyakorlats -> {
            if(sorozatWithGyakorlats != null) {
                binding.naploReszletek.startAnimation(naploBeAnim);
                binding.naploDetailsDatumLabel.setText(dateTimeFormatter.getNaploDatum(naplodatum));
                binding.naploDetailsRcView.setAdapter(adapterFactory.create(sorozatWithGyakorlats));
                binding.naploDetailsRcView.setItemAnimator(new DefaultItemAnimator());
                binding.naploDetailsRcView.setLayoutManager(new LinearLayoutManager(MvvmSavedNaploActivity.this, RecyclerView.HORIZONTAL, false));

                binding.naploDetailsSulyLabel.setText(String.format(Locale.getDefault(), "Összesen %,d Kg megmozgatott súly",
                        sorozatWithGyakorlats.stream().mapToInt(gyak -> gyak.sorozat.getIsmetles() * gyak.sorozat.getSuly()).sum()));
                binding.naploDetailsInfoLabel.setText(String.format(Locale.getDefault(), "Elvégzett gyakorlatok száma [%d] db", getGyakDarabSzam(sorozatWithGyakorlats)));
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
            loadNaploFileFromSD();
        } else if(item.getItemId() == R.id.naplo_details_v3_load) {
            loadV3NaploFromDirectory();
        }
        return super.onContextItemSelected(item);
    }

    private void loadNaploFileFromSD() {
        Intent filechooser = new Intent(Intent.ACTION_GET_CONTENT);
        filechooser.setType("*/*");
        filechooser.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/octet-stream", "application/json"});
        filechooser.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(filechooser, FILE_LOAD_RCODE);
    }

    private void loadV3NaploFromDirectory() {
        myFileService.futureLoadV3saves().whenComplete((naploAllList, throwable) -> {
            if(throwable != null) {
                toast("Nem sikerült betölteni a fájlokat\n"+throwable.getMessage());
                return;
            }

            if(naploAllList != null) {
                for (NaploAll egyNaplo : naploAllList) {
                    naploViewModel.insert(modelConverter.getNaploFromV3(egyNaplo.getNaplo()));
                    List<Sorozat> sorozats = egyNaplo.getSorozatWithGyakorlats()
                            .stream()
                            .map(srs -> modelConverter.getSorozatFromV3(srs.sorozat))
                            .collect(Collectors.toList());
                    sorozatViewModel.insertAll(sorozats);
                }
                toast("Sikeresen betöltve a naplók!");
            }
        });
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
                    naploViewModel.insert(modelConverter.getNewNaplo(naplo.naplo));
                    sorozatViewModel.insertAll(naplo.sorozats.stream().map(sorozat -> modelConverter.getNewSorozat(sorozat)).collect(Collectors.toList()));
                    widgetUtil.updateWidget();
                    toast("Napló betöltve!");
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

    private int getGyakDarabSzam(List<SorozatWithGyakorlat> sorozatWithGyakorlats) {
        return sorozatWithGyakorlats.stream().map(sorozatWithGyakorlat -> sorozatWithGyakorlat.gyakorlat).collect(Collectors.toSet()).size();
    }

    private int getSorozatOsszSuly(List<SorozatWithGyakorlat> sorozatWithGyakorlat) {
        return sorozatWithGyakorlat.stream().mapToInt(srs -> srs.sorozat.getIsmetles() * srs.sorozat.getSuly()).sum();
    }

    private void toast(String msg) {
        runOnUiThread(() -> Toast.makeText(MvvmSavedNaploActivity.this, msg, Toast.LENGTH_SHORT).show());
    }
}
