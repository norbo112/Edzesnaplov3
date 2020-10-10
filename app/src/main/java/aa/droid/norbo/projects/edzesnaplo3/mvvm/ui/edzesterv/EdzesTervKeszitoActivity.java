package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityEdzestervKeszitoBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmEdzestervHanyszorhanySzerkesztoBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmSorozatSzerkesztoBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.GyakorlatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EdzesTervKeszitoActivity extends EdzesTervBaseActivity<MvvmActivityEdzestervKeszitoBinding> {
    @Inject
    GyakorlatViewModel gyakorlatViewModel;

    private MyTouchListener myTouchListener = new MyTouchListener();
    private MyDragListener dragListener = new MyDragListener();

    private List<String> izomcsoportok;
    private ArrayAdapter<String> valasztottCsoportAdapter;
    private ArrayAdapter<GyakorlatTerv> gyakorlatTervArrayAdapter;
    private List<GyakorlatTerv> gyakorlatTervs;
    private List<Gyakorlat> csoportokGyakorlatai;
    private ArrayAdapter<Gyakorlat> csoportGyakorlataiAdapter;


    public EdzesTervKeszitoActivity() {
        super(R.layout.mvvm_activity_edzesterv_keszito);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.tervToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edzésnapló v4");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initAdapters();
        initIzomcsoportok();

        binding.clearValasztottGyakorlatok.setOnClickListener(v -> {
            gyakorlatTervs.clear();
            gyakorlatTervArrayAdapter.notifyDataSetChanged();
        });

        binding.valasztottGyakorlatokTerv.setOnItemClickListener((parent, view, position, id) -> {
            GyakorlatTerv gyakorlatTerv = (GyakorlatTerv) parent.getAdapter().getItem(position);
            dialogSorozatSzerkeszto(gyakorlatTerv);
        });
    }

    private void initAdapters() {
        izomcsoportok = new ArrayList<>();
        gyakorlatTervs = new ArrayList<>();
        csoportokGyakorlatai = new ArrayList<>();

        gyakorlatTervArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gyakorlatTervs);
        binding.valasztottGyakorlatokTerv.setAdapter(gyakorlatTervArrayAdapter);

//        csoportGyakorlataiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, csoportokGyakorlatai);
//        binding.csoportokGyakorlatai.setAdapter(csoportGyakorlataiAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initIzomcsoportok() {
        valasztottCsoportAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, izomcsoportok);
        binding.valasztottCsoportokList.setAdapter(valasztottCsoportAdapter);

        binding.valasztottCsoportokList.setOnDragListener(dragListener);
        binding.valasztottCsoportokList.setOnItemClickListener(removeCsoportItemListener);

        for (String csoport: getResources().getStringArray(R.array.csoportokForTervezo)) {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 20;
            params.topMargin = 20;

            textView.setLayoutParams(params);
            textView.setText(csoport);
            textView.setClickable(true);
            textView.setOnTouchListener(myTouchListener);
            binding.izomcsoportokLl.addView(textView);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCsoportokGyakorlataiLinearLayout(List<Gyakorlat> gyakorlats) {
        binding.csoportokGyakorlatai.removeAllViews();
        binding.csoportokGyakorlatai.invalidate();
        for (Gyakorlat gyakorlat: gyakorlats) {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 20;
            params.topMargin = 20;
            params.gravity = Gravity.CENTER_HORIZONTAL;

            textView.setLayoutParams(params);
            textView.setText(gyakorlat.getMegnevezes());
            textView.setClickable(true);
            textView.setOnTouchListener(myTouchListener);
            binding.csoportokGyakorlatai.addView(textView);
        }

        binding.valasztottGyakorlatokTerv.setOnDragListener(dragListener);
    }

    private AdapterView.OnItemClickListener removeCsoportItemListener = (parent, view, position, id) -> {
        binding.csoportokGyakorlatai.removeAllViews();
        binding.csoportokGyakorlatai.invalidate();

        izomcsoportok.remove(position);
        valasztottCsoportAdapter.notifyDataSetChanged();
        initCsoportokGyakorlatai(izomcsoportok);
    };

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("adatok", ((TextView)view).getText().toString());
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }

    private final class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (v.getId() == R.id.valasztottCsoportokList && event.getAction() == DragEvent.ACTION_DROP) {
                String izomcsoport = event.getClipData().getItemAt(0).getText().toString();
                if(!isInCsoport(izomcsoport)) {
                    izomcsoportok.add(izomcsoport);
                }
                valasztottCsoportAdapter.notifyDataSetInvalidated();
                initCsoportokGyakorlatai(izomcsoportok);
            } else if(v.getId() == R.id.valasztottGyakorlatokTerv && event.getAction() == DragEvent.ACTION_DROP) {
                String gyaknev = event.getClipData().getItemAt(0).getText().toString();
                gyakorlatTervs.add(new GyakorlatTerv(gyaknev));
                gyakorlatTervArrayAdapter.notifyDataSetChanged();
            }
            return true;
        }
    }

    private boolean isInCsoport(String izomcsoport) {
        for(String csoport: izomcsoportok) {
            if(csoport.equals(izomcsoport)) {
                return true;
            }
        }
        return false;
    }

    private void initCsoportokGyakorlatai(List<String> izomcsoportok) {
        gyakorlatViewModel.getGyakorlatByCsoport(izomcsoportok).observe(this, gyakorlats -> {
            if(gyakorlats != null && gyakorlats.size() > 0) {
                initCsoportokGyakorlataiLinearLayout(gyakorlats);
            }
        });
    }

    private void dialogSorozatSzerkeszto(GyakorlatTerv gyakorlatTerv) {
        MvvmEdzestervHanyszorhanySzerkesztoBinding binding = MvvmEdzestervHanyszorhanySzerkesztoBinding.inflate(LayoutInflater.from(this),
                null, false);
        binding.setSorozat(new SorozatAdatUI());
        new AlertDialog.Builder(this)
                .setTitle("Adat felvétele")
                .setView(binding.getRoot())
                .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("felvétel", (dialog, which) -> {
                    SorozatAdatUI sorozat = binding.getSorozat();
                    gyakorlatTerv.addSorozatSzam(Integer.parseInt(sorozat.getSorozat()));
                    gyakorlatTerv.addIsmetlesSzam(Integer.parseInt(sorozat.getIsmetles()));
                    gyakorlatTervArrayAdapter.notifyDataSetChanged();
                })
                .show();
    }

    public class SorozatAdatUI {
        String sorozat;
        String ismetles;

        public SorozatAdatUI() {
        }

        public String getSorozat() {
            return sorozat;
        }

        public void setSorozat(String sorozat) {
            this.sorozat = sorozat;
        }

        public String getIsmetles() {
            return ismetles;
        }

        public void setIsmetles(String ismetles) {
            this.ismetles = ismetles;
        }
    }
}