package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityEdzestervElonezetBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmEdzestervHanyszorhanySzerkesztoBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils.SorozatAdatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.edzesterv.EdzesTervViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EdzesTervElonezetActivity extends EdzesTervBaseActivity<MvvmActivityEdzestervElonezetBinding> {
    @Inject
    EdzesTervViewModel edzesTervViewModel;

    private Context context;

    public EdzesTervElonezetActivity() {
        super(R.layout.mvvm_activity_edzesterv_elonezet);
    }

    public EdzesTervElonezetActivity(Context context, EdzesTervViewModel edzesTervViewModel) {
        super(R.layout.mvvm_activity_edzesterv_elonezet);
        this.edzesTervViewModel = edzesTervViewModel;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.tervToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edzésterv előnézet");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edzesTervViewModel.getEdzesTervLiveData().observe(this, edzesTerv -> {
            binding.tervElonezetLinearLayout.removeAllViews();
            binding.tervElonezetLinearLayout.invalidate();
            if(edzesTerv != null) {
                initElonezet(edzesTerv, binding.tervElonezetLinearLayout, this);
            } else {
                addMessageTextView("Nincs megjeleníthető elem", binding.tervElonezetLinearLayout);
            }
        });

        binding.tervMentesBtn.setOnClickListener(v -> {
            try {
                edzesTervViewModel.mentes().whenComplete((aVoid, throwable) -> {
                    if (throwable != null) {
                        runOnUiThread(() -> Toast.makeText(this, "Hiba történt a mentés közben", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    runOnUiThread(() -> Toast.makeText(this, "Az edzésterv sikeresen mentve", Toast.LENGTH_SHORT).show());
                    startActivity(new Intent(this, EdzesTervBelepoActivity.class));
                    overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
                    finish();
                });
            } catch (NullPointerException e) {
                Toast.makeText(this, "Nincs mit menteni", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMessageTextView(String message, LinearLayout tervElonezetLinearLayout) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 20;
        params.bottomMargin = 20;
        params.leftMargin = 10;

        TextView textView = new TextView(this);
        textView.setLayoutParams(params);
        textView.setText(message);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextColor(Color.RED);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        tervElonezetLinearLayout.addView(textView);
    }

    public void initElonezet(EdzesTerv edzesTerv, LinearLayout layout, Context mContext) {
        this.context = mContext;
        if(edzesTerv != null) {
            if(!isTablet(mContext)) binding.tervMegnevezes.setText(edzesTerv.getMegnevezes());

            List<Edzesnap> edzesnapList = edzesTerv.getEdzesnapList();

            edzesnapList.sort((o1, o2) -> Integer.compare(Integer.parseInt(o1.getEdzesNapLabel().substring(0, 1)),
                    Integer.parseInt(o2.getEdzesNapLabel().substring(0, 1))));

            for (Edzesnap edzesnap: edzesnapList) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = 20;
                params.bottomMargin = 20;
                params.leftMargin = 10;

                TextView textView = new TextView(mContext);
                textView.setTag(edzesnap);
                textView.setOnLongClickListener(tervAdatokListener);
                textView.setLayoutParams(params);
                textView.setText(edzesnap.getEdzesNapLabel());
                textView.setPadding(10,10,10,10);
                textView.setBackgroundResource(R.drawable.custom_et_gyak_hatter);
                layout.addView(textView);

                for(Csoport csoport: edzesnap.getValasztottCsoport()) {
                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 20;
                    params.bottomMargin = 20;
                    params.leftMargin = 70;

                    TextView textView1 = new TextView(mContext);
                    textView1.setTag(csoport);
                    textView1.setLayoutParams(params);
                    textView1.setText(csoport.getIzomcsoport());
                    textView1.setOnLongClickListener(tervAdatokListener);
                    layout.addView(textView1);

                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 20;
                    params.bottomMargin = 20;
                    params.leftMargin = 100;
                    for(GyakorlatTerv gyakorlatTerv: csoport.getValasztottGyakorlatok()) {
                        TextView textView2 = new TextView(mContext);
                        textView2.setTag(gyakorlatTerv);
                        textView2.setLayoutParams(params);
                        textView2.setText(gyakorlatTerv.toString());
                        textView2.setOnLongClickListener(tervAdatokListener);
                        layout.addView(textView2);
                    }
                }
            }
        }
    }

    private boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    private Object kijeloltTervAdat;

    @SuppressLint("RestrictedApi")
    private View.OnLongClickListener tervAdatokListener = v -> {

        kijeloltTervAdat = v.getTag();

        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.getMenuInflater().inflate(R.menu.edzesterv_nezoke_edit_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            onContextItemSelected(item);
            return true;
        });

        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(context, (MenuBuilder) popupMenu.getMenu(), v);
        menuPopupHelper.setForceShowIcon(true);
        menuPopupHelper.show();
        return true;
    };

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.edzesterv_szerk_torol) {
            adatTorol(kijeloltTervAdat);
        } else if(item.getItemId() == R.id.edzesterv_szerk_edit) {
            adatSzerkeszt(kijeloltTervAdat);
        }
        return super.onContextItemSelected(item);
    }

    private void adatSzerkeszt(Object kijeloltTervAdat) {
        if(kijeloltTervAdat == null) {
            Toast.makeText(context, "Sajnos nem jo a kijelölés", Toast.LENGTH_SHORT).show();
            return;
        }

        if(kijeloltTervAdat instanceof Csoport || kijeloltTervAdat instanceof Edzesnap) {
            Toast.makeText(context, "Csak gyakorlatot szerkeszése lehetséges!", Toast.LENGTH_SHORT).show();
            return;
        }

        dialogSorozatSzerkeszto((GyakorlatTerv) kijeloltTervAdat);
    }

    private void adatTorol(Object kijeloltTervAdat) {
        if(kijeloltTervAdat == null) {
            Toast.makeText(context, "Nincs mit törölni", Toast.LENGTH_SHORT).show();
            return;
        }

        if(kijeloltTervAdat instanceof GyakorlatTerv) {
            edzesTervViewModel.deleteGyakorlatTerv((GyakorlatTerv) kijeloltTervAdat);
        } else if(kijeloltTervAdat instanceof Csoport) {
            edzesTervViewModel.deleteCsoport((Csoport) kijeloltTervAdat);
        } else if(kijeloltTervAdat instanceof Edzesnap) {
            edzesTervViewModel.deleteEdzesnap((Edzesnap) kijeloltTervAdat);
        }
    }

    private void dialogSorozatSzerkeszto(GyakorlatTerv gyakorlatTerv) {
        String regPattern = "^([1-9][0-9]*,)*[1-9][0-9]*$";
        MvvmEdzestervHanyszorhanySzerkesztoBinding binding = MvvmEdzestervHanyszorhanySzerkesztoBinding.inflate(LayoutInflater.from(context),
                null, false);
        binding.setSorozat(new SorozatAdatUI());
        binding.sorozatIsm.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        binding.sorozatSuly.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(context)
                .setTitle(gyakorlatTerv.getMegnevezes()+" szerkesztése")
                .setMessage("Sorozat csere\nVesszővel elválasztva a sor. és ismétlések megadása\n Pl 1,2,2 és 15,10,10")
                .setView(binding.getRoot())
                .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("felvétel", (dialog, which) -> {
                    SorozatAdatUI sorozat = binding.getSorozat();
                    if(sorozat.getIsmetles() == null || sorozat.getSorozat() == null) {
                        Toast.makeText(context, "Kérlek töltsd ki az adatokat", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if(!sorozat.getSorozat().matches(regPattern) || !sorozat.getIsmetles().matches(regPattern)) {
                        Toast.makeText(context, "sor: 1,2,2 ismétlés: 8,12,12 adat lehetséges", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        List<Integer> s = Stream.of(sorozat.getSorozat().split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                        List<Integer> ism = Stream.of(sorozat.getIsmetles().split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                        gyakorlatTerv.setIsmetlesSzam(ism);
                        gyakorlatTerv.setSorozatSzam(s);
                        edzesTervViewModel.notifyEdzesTerv();
                    }
                })
                .show();
    }
}