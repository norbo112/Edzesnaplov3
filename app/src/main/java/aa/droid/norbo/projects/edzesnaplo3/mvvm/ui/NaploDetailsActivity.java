package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmNaploDetailsActivityBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.service.files.MyFileService;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.rcviews.NaploDetailsRcViewAdapterFactory;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.DateTimeFormatter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.NaploListUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.WidgetUtil;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.SorozatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NaploDetailsActivity extends BaseActivity<MvvmNaploDetailsActivityBinding> implements NaploListUtil.NaploTorlesInterface {
    private static final String TAG = "NaploDetailsActivity";
    public static final String EXTRA_NAPLO_DATUM = "aa.droid.norbo.projects.edzesnaplo3.v4.EXTRA_NAPLO_DATUM";
    public static final String EXTRA_NAPLO_COMMENT = "aa.droid.norbo.projects.edzesnaplo3.v4.EXTRA_NAPLO_COMMENT";

    @Inject
    NaploDetailsRcViewAdapterFactory adapterFactory;

    @Inject
    SorozatViewModel sorozatViewModel;

    @Inject
    DateTimeFormatter dateTimeFormatter;

    @Inject
    NaploViewModel naploViewModel;

    @Inject
    MyFileService myFileService;

    @Inject
    WidgetUtil widgetUtil;

    private Long naploDatum;
    private String naploComment;

    public NaploDetailsActivity() {
        super(R.layout.mvvm_naplo_details_activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        naploDatum = Long.parseLong(
                Objects.requireNonNull(getIntent().getStringExtra(EXTRA_NAPLO_DATUM), "Nem lett átadva a megfelelő adat!"));

        naploComment = getIntent().getStringExtra(EXTRA_NAPLO_COMMENT);

        if(naploDatum != 0) {
            setupRcViewWithDate(naploDatum);
        }
    }

    private void setupRcViewWithDate(long naploDatum) {
        sorozatViewModel.getForNaplo(naploDatum).observe(this, sorozatWithGyakorlats -> {
            if(sorozatWithGyakorlats != null) {
                binding.naploDetailsDatumLabel.setText(dateTimeFormatter.getNaploDatum(naploDatum));
                binding.naploDetailsRcView.setAdapter(adapterFactory.create(sorozatWithGyakorlats));
                binding.naploDetailsRcView.setItemAnimator(new DefaultItemAnimator());
                binding.naploDetailsRcView.setLayoutManager(new LinearLayoutManager(NaploDetailsActivity.this, RecyclerView.HORIZONTAL, false));

                binding.naploDetailsSulyLabel.setText(String.format(Locale.getDefault(), "Összesen %,d Kg megmozgatott súly",
                        sorozatWithGyakorlats.stream().mapToInt(gyak -> gyak.sorozat.getIsmetles() * gyak.sorozat.getSuly()).sum()));
                binding.naploDetailsInfoLabel.setText(String.format(Locale.getDefault(), "Elvégzett gyakorlatok száma [%d] db", getGyakDarabSzam(sorozatWithGyakorlats)));
            }
        });
    }

    private int getGyakDarabSzam(List<SorozatWithGyakorlat> sorozatWithGyakorlats) {
        return sorozatWithGyakorlats.stream().map(sorozatWithGyakorlat -> sorozatWithGyakorlat.gyakorlat).collect(Collectors.toSet()).size();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected PopupMenu showMoreOptionsPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.mvvm_naplo_details_edit, popupMenu.getMenu());

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
    public void setupCustomActionBar() {
        setSupportActionBar(binding.toolbar.customToolbar);
        if(getSupportActionBar() != null) {
            binding.toolbar.naploDetails.setVisibility(View.GONE);
            binding.toolbar.moreOptions.setOnClickListener(this::showMoreOptionsPopupMenu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.naplo_details_save) {
            naplotMent(naploDatum);
        } else if(item.getItemId() == R.id.naplo_details_delete) {
            uiNaplotTorol(naploDatum);
        } else if(item.getItemId() == R.id.naplo_details_comment) {
            naploViewModel.getNaploByNaploDatum(naploDatum).observe(this, naplo -> {
                if(naplo != null && naplo.getCommentFilePath() != null && naplo.getCommentFilePath().length() > 0) {
                    Intent commentActivity = new Intent(this, CommentActivity.class);
                    commentActivity.putExtra("audio_play_on", true);
                    commentActivity.putExtra("extra_file_name", naplo.getCommentFilePath());
                    startActivity(commentActivity);
                    overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
                } else {
                    Toast.makeText(this, "Nincs audio comment rögzítve!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return super.onContextItemSelected(item);
    }

    private void uiNaplotTorol(Long naploDatum) {
        if(naploDatum != null && naploDatum != 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Törés")
                    .setMessage("Biztosan törölni szeretnéd a naplót?")
                    .setPositiveButton("ok", (dialog, which) -> {
                        naplotTorol(naploDatum);
                        onBackPressed();
                        finish();
                    })
                    .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

    @Override
    public void naplotTorol(long naplodatum) {
        naploViewModel.deleteNaplo(naplodatum);
        sorozatViewModel.deleteSorozat(naplodatum);

        if(naploComment != null) {
            File f = new File(naploComment);
            if (f.exists()) {
                f.delete();
            }
        }

        widgetUtil.updateWidget();
        Toast.makeText(this, "Napló törölve", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void naplotMent(long naplodatum) {
        myFileService.futureFileSave(naplodatum).whenComplete((uri, throwable) -> {
            if (throwable == null) {
                runOnUiThread(() -> Toast.makeText(this, "Fájl mentve: " + uri.toString(), Toast.LENGTH_SHORT).show());
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Hiba a mentés végrehajtása közben", Toast.LENGTH_SHORT).show());
                Log.e(TAG, "naplotMent: ", throwable);
            }
        });
    }
}
