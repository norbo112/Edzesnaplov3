package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.ViewDataBinding;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityEdzestervNezokeBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmDialogLoadEdzestervLayoutBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmEdzestervTitleDialogLayoutBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmEdzestervTitleLayoutBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.converters.TervModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.service.files.edzesterv.TervFileService;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils.EdzesTervKeszito;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.edzesterv.EdzesTervViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EdzesTervNezokeActivity extends EdzesTervBaseActivity<MvvmActivityEdzestervNezokeBinding> {
    private static final String TAG = "EdzesTervNezokeActivity";

    @Inject
    EdzesTervViewModel edzesTervViewModel;

    @Inject
    TervModelConverter modelConverter;

    @Inject
    EdzesTervKeszito edzesTervKeszito;

    @Inject
    TervFileService tervFileService;
    private static final int FILE_LOAD_RCODE = 1001;

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
            binding.tervElonezetLinearLayout.removeAllViews();
            binding.tervElonezetLinearLayout.invalidate();
            if (edzesTervWithEdzesnaps != null && edzesTervWithEdzesnaps.size() > 0) {
                initElonezet(edzesTervKeszito.makeEdzesterv(edzesTervWithEdzesnaps), binding.tervElonezetLinearLayout, false);
            } else {
                appendInfo();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mvvm_edzesterv_nezoke_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.edzesterv_menu_betoltes) {
            Intent filechooser = new Intent(Intent.ACTION_GET_CONTENT);
            filechooser.setType("*/*");
            filechooser.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/octet-stream", "application/json"});
            filechooser.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(filechooser, FILE_LOAD_RCODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == FILE_LOAD_RCODE && resultCode == RESULT_OK) {
            betoltottTervDialog(data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void betoltottTervDialog(Uri data) {
        tervFileService.futureLoadSaveFile(data).whenComplete((edzesTerv, throwable) -> {
            if(throwable != null) {
                runOnUiThread(() -> Toast.makeText(EdzesTervNezokeActivity.this, "Nem sikerült betölteni a fájlt", Toast.LENGTH_SHORT).show());
                Log.e(TAG, "betoltottTervDialog: ", throwable);
            } else {
                runOnUiThread(() -> {
                    MvvmDialogLoadEdzestervLayoutBinding binding = MvvmDialogLoadEdzestervLayoutBinding.inflate(getLayoutInflater(), null, false);
                    initElonezet(Stream.of(edzesTerv).collect(Collectors.toList()), binding.tervElonezetLinearLayout, true);
                    new AlertDialog.Builder(EdzesTervNezokeActivity.this)
                            .setTitle("Edzésterv rögzítése az adatbázisban?")
                            .setView(binding.getRoot())
                            .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                            .setPositiveButton("mentés", (dialog, which) -> edzesTervViewModel.mentes(edzesTerv).whenComplete((aVoid, throwable1) -> {
                                if(throwable1 != null) {
                                    Toast.makeText(EdzesTervNezokeActivity.this, "Nem sikerült menteni az adatbázisba :(", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "betoltottTervDialog: adat mentés fájlból adatbázisba", throwable1);
                                } else {
                                    Toast.makeText(EdzesTervNezokeActivity.this, "Sikertesen mentve az adatbázisba", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }))
                            .show();
                });
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

    public void initElonezet(List<EdzesTerv> edzesTervs, LinearLayout layout, boolean isDialog) {
        for (EdzesTerv terv : edzesTervs) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams llparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llparam.topMargin = 20;
            llparam.bottomMargin = 20;
            linearLayout.setLayoutParams(llparam);
            linearLayout.setBackgroundResource(R.drawable.custom_edzesterv_hatter);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            List<Edzesnap> edzesnapList = terv.getEdzesnapList();


            if(isDialog) {
                MvvmEdzestervTitleDialogLayoutBinding titleLayoutBinding = MvvmEdzestervTitleDialogLayoutBinding.inflate(LayoutInflater.from(this), null, false);
                titleLayoutBinding.etTitleLabel.setText(terv.getMegnevezes());
                linearLayout.addView(titleLayoutBinding.getRoot());
            } else {
                MvvmEdzestervTitleLayoutBinding titleLayoutBinding = MvvmEdzestervTitleLayoutBinding.inflate(LayoutInflater.from(this), null, false);
                titleLayoutBinding.etBtnMenu.setText(terv.getMegnevezes());
                titleLayoutBinding.setTervId(terv.getTervId());
                titleLayoutBinding.setEdzesterv(terv);
                titleLayoutBinding.setSzerkeszto(new TervSzerkesztoAction(titleLayoutBinding)); //próba
                linearLayout.addView(titleLayoutBinding.getRoot());
            }

            for (Edzesnap edzesnap : edzesnapList) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = 20;
                params.bottomMargin = 20;
                params.leftMargin = 10;

                TextView textView = new TextView(this);
                textView.setLayoutParams(params);
                textView.setText(edzesnap.getEdzesNapLabel());
                textView.setPadding(10, 10, 10, 10);
                textView.setBackgroundResource(R.drawable.custom_et_gyak_hatter);
                linearLayout.addView(textView);

                for (Csoport csoport : edzesnap.getValasztottCsoport()) {
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
                    for (GyakorlatTerv gyakorlatTerv : csoport.getValasztottGyakorlatok()) {
                        TextView textView2 = new TextView(this);
                        textView2.setLayoutParams(params);
                        textView2.setText(gyakorlatTerv.toString());

                        linearLayout.addView(textView2);
                    }
                }
            }

            layout.addView(linearLayout);
        }
    }

    public class TervSzerkesztoAction {
        private MvvmEdzestervTitleLayoutBinding testBinding;

        public TervSzerkesztoAction(MvvmEdzestervTitleLayoutBinding binding) {
            this.testBinding = binding;
        }

        @SuppressLint("RestrictedApi")
        public void popupMenu(EdzesTerv terv) {
            PopupMenu popupMenu = new PopupMenu(EdzesTervNezokeActivity.this, testBinding.etBtnMenu);
            popupMenu.getMenuInflater().inflate(R.menu.edzesterv_nezoke_title_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                popupMenuItemClick(terv, item);
                return true;
            });

            MenuPopupHelper menuPopupHelper = new MenuPopupHelper(EdzesTervNezokeActivity.this, (MenuBuilder) popupMenu.getMenu(), testBinding.etBtnMenu);
            menuPopupHelper.setForceShowIcon(true);
            menuPopupHelper.show();
        }

        private void popupMenuItemClick(EdzesTerv terv, MenuItem item) {
            if(item.getItemId() == R.id.edzesterv_szerk_torol) {
                tervTorlese(terv.getTervId());
            } else if(item.getItemId() == R.id.edzesterv_szerk_edit) {
                tervSzerkesztese(terv);
            } else if(item.getItemId() == R.id.edzesterv_title_menu_ment) {
                tervMentese(terv);
            }
        }

        public void tervTorlese(int tervId) {
            new AlertDialog.Builder(EdzesTervNezokeActivity.this)
                    .setMessage("Biztosan törölni szeretnéd a tervet?")
                    .setPositiveButton("igen", (dialog, which) -> edzesTervViewModel.deleteTerv(tervId).whenComplete((aVoid, throwable) -> {
                        if (throwable != null) {
                            runOnUiThread(() -> Toast.makeText(EdzesTervNezokeActivity.this, "Nem sikerült a törlés!", Toast.LENGTH_SHORT).show());
                            Log.e(TAG, "tervTorlese: törlés nem sikerült", throwable);
                        } else {
                            runOnUiThread(() -> Toast.makeText(EdzesTervNezokeActivity.this, tervId + " A terv törlésre került", Toast.LENGTH_SHORT).show());
                        }
                    }))
                    .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                    .show();

        }

        public void tervMentese(EdzesTerv edzesTerv) {
            tervFileService.futureFileSave(edzesTerv).whenComplete((uri, throwable) -> {
                if(throwable != null) {
                    runOnUiThread(() -> Toast.makeText(EdzesTervNezokeActivity.this, "Nem sikerült betölteni a fájlt", Toast.LENGTH_SHORT).show());
                    return;
                }

                if(uri != null) {
                    runOnUiThread(() -> Toast.makeText(EdzesTervNezokeActivity.this, uri.getEncodedPath()+" fájl mentve!", Toast.LENGTH_SHORT).show());
                }
            });
        }

        public void tervSzerkesztese(EdzesTerv edzesTerv) {
            Intent intent = new Intent(EdzesTervNezokeActivity.this, EdzesTervKeszitoActivity.class);
            intent.putExtra(EdzesTervKeszitoActivity.TERV_SZERKESZTESRE_BETOLTES, edzesTerv);
            startActivity(intent);
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
            finish();
        }
    }
}