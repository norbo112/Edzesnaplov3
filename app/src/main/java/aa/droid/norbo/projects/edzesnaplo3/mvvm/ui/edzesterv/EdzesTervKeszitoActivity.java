package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityEdzestervKeszitoBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmAlertTevekenysegElhagyasaBinding;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmEdzestervHanyszorhanySzerkesztoBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Csoport;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils.EdzesTervViewModel;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils.GyakorlatAdatTextView;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.GyakorlatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EdzesTervKeszitoActivity extends EdzesTervBaseActivity<MvvmActivityEdzestervKeszitoBinding> {
    @Inject
    GyakorlatViewModel gyakorlatViewModel;

    @Inject
    EdzesTervViewModel edzesTervViewModel;

    private MyTouchListener myTouchListener = new MyTouchListener();
    private MyDragListener dragListener = new MyDragListener();

    private List<String> izomcsoportok;
    private ArrayAdapter<String> valasztottCsoportAdapter;
    private ArrayAdapter<GyakorlatTerv> gyakorlatTervArrayAdapter;
    private List<GyakorlatTerv> gyakorlatTervs;
    private List<Gyakorlat> csoportokGyakorlatai;

    public EdzesTervKeszitoActivity() {
        super(R.layout.mvvm_activity_edzesterv_keszito);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.tervToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edzésterv készítő");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initAdapters();
        initIzomcsoportok();
        initEdzesnap();

        if(edzesTervViewModel.getEdzesTerv() != null) {
            binding.btnEdzesTervElnevezes.setText(edzesTervViewModel.getEdzesTerv().getMegnevezes());
        }

        binding.clearValasztottGyakorlatok.setOnClickListener(v -> {
            gyakorlatTervs.clear();
            gyakorlatTervArrayAdapter.notifyDataSetChanged();
        });

        binding.valasztottGyakorlatokTerv.setOnItemClickListener((parent, view, position, id) -> {
            GyakorlatTerv gyakorlatTerv = (GyakorlatTerv) parent.getAdapter().getItem(position);
            dialogSorozatSzerkeszto(gyakorlatTerv);
        });

        binding.btnPihenotRogzit.setOnClickListener(v -> {
            if(!isTervGood()) {
                Toast.makeText(this, "Kérlek add meg az edzésterv címét", Toast.LENGTH_SHORT).show();
                return;
            }

            String edzesnap = binding.edzesnapSpinner.getSelectedItem().toString();

            if(edzesTervViewModel.isEdzesnapInEdzesterv(edzesnap)) {
                Toast.makeText(this, "Az edzésnap már rögzítve lett", Toast.LENGTH_SHORT).show();
                return;
            }

            edzesTervViewModel.addEdzesnapForEdzesTerv(new Edzesnap(edzesnap+" pihenőnap"));
            clearItems();
            Toast.makeText(this, "Pihenőnap r9gzítve", Toast.LENGTH_SHORT).show();
        });

        binding.btnEdzesnapElonezet.setOnClickListener(v -> {
            startActivity(new Intent(this, EdzesTervElonezetActivity.class));
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        });

        binding.btnEdzesnapFelvetele.setOnClickListener(v -> {
            if(!isTervGood()) {
                Toast.makeText(this, "Kérlek add meg az edzésterv címét", Toast.LENGTH_SHORT).show();
                return;
            }

            if(gyakorlatTervs.size() == 0) {
                Toast.makeText(this, "Nem választottál gyakorlatokat az edzésnaphoz", Toast.LENGTH_SHORT).show();
                return;
            }

            Edzesnap edzesnap = new Edzesnap(binding.edzesnapSpinner.getSelectedItem().toString());
            for(String csoport: izomcsoportok) {
                Csoport tervCsoport = new Csoport(csoport);
                for(GyakorlatTerv gyakorlatTerv: gyakorlatTervs) {
                    if(gyakorlatTerv.getIzomcsoportNev().equals(csoport)) {
                        tervCsoport.addGyakorlat(gyakorlatTerv);
                    }
                }
                edzesnap.addCsoport(tervCsoport);
            }

            if (edzesTervViewModel.addEdzesnapForEdzesTerv(edzesnap)) {
                Toast.makeText(this, "Edzésnap rögzítve", Toast.LENGTH_SHORT).show();
                clearItems();
//                startActivity(new Intent(this, EdzesTervElonezetActivity.class));
//                overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("Ezt a napot már rögzítetted, válassz másikat, vagy mentéshez nyisd meg az előnézetet")
                        .show();
            }
        });

        binding.btnEdzesTervElnevezes.setOnClickListener(v -> {
            EditText textView = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle("Edzésterv elnevezése")
                    .setView(textView)
                    .setPositiveButton("ok", (dialog, which) -> {
                        if(!TextUtils.isEmpty(textView.getText().toString())) {
                            binding.btnEdzesTervElnevezes.setText(textView.getText().toString());
                        }
                    })
                    .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void initEdzesnap() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.edzesnapok));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.edzesnapSpinner.setAdapter(spinnerAdapter);
        binding.edzesnapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearItems();
                binding.btnEdzesnapFelvetele.setEnabled(true);
                binding.valasztottEdzesnap.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.btnEdzesnapFelvetele.setEnabled(false);
            }
        });
    }

    private void clearItems() {
        izomcsoportok.clear();
        gyakorlatTervs.clear();
        csoportokGyakorlatai.clear();

        binding.csoportokGyakorlatai.removeAllViews();
        binding.csoportokGyakorlatai.invalidate();

        valasztottCsoportAdapter.notifyDataSetChanged();
        gyakorlatTervArrayAdapter.notifyDataSetChanged();
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

        String[] izomcsoportArrayFromRes = getResources().getStringArray(R.array.csoportokForTervezo);
        binding.izomcsoportokLl.setColumnCount(4);
        binding.izomcsoportokLl.setRowCount((izomcsoportArrayFromRes.length / 4) + 1);

        for (String csoport: izomcsoportArrayFromRes) {
            TextView textView = new TextView(this);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 20;
            params.topMargin = 20;
            params.rightMargin = 20;
            params.leftMargin = 20;

            textView.setLayoutParams(params);
            textView.setPadding(10,10,10,10);
            textView.setBackgroundResource(R.drawable.custom_et_gyak_hatter);
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

        binding.csoportokGyakorlatai.setColumnCount(2);
        binding.csoportokGyakorlatai.setRowCount((gyakorlats.size() / 2) + 1);

        for (Gyakorlat gyakorlat: gyakorlats) {
            GyakorlatAdatTextView textView = new GyakorlatAdatTextView(this, gyakorlat);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 20;
            params.topMargin = 20;
            params.rightMargin = 20;
            params.leftMargin = 20;

            textView.setLayoutParams(params);
            textView.setText(gyakorlat.getMegnevezes());
            textView.setClickable(true);
            textView.setTextSize(12);
            textView.setPadding(10,10,10,10);
            textView.setBackgroundResource(R.drawable.custom_et_gyak_hatter);
//            textView.setOnTouchListener(gyakorlatValasztoTouchListener);
            textView.setOnLongClickListener(gyakorlatLongClick);
            binding.csoportokGyakorlatai.addView(textView);
        }

        binding.valasztottGyakorlatokTerv.setOnDragListener(dragListener);
    }

    private View.OnLongClickListener gyakorlatLongClick = v -> {
        GyakorlatAdatTextView v1 = (GyakorlatAdatTextView) v;
        GyakorlatTerv e = new GyakorlatTerv(v1.getGyakorlat().getMegnevezes(), v1.getGyakorlat().getCsoport());
        if(!gyakorlatTervs.contains(e)) {
            gyakorlatTervs.add(e);
            gyakorlatTervArrayAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Gyakorlat hozzáadva a tervhez!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ezt a gyakorlatot már hozzáadtad", Toast.LENGTH_SHORT).show();
        }
        return true;
    };

    private AdapterView.OnItemClickListener removeCsoportItemListener = (parent, view, position, id) -> {
        binding.csoportokGyakorlatai.removeAllViews();
        binding.csoportokGyakorlatai.invalidate();

        izomcsoportok.remove(position);
        valasztottCsoportAdapter.notifyDataSetChanged();
        initCsoportokGyakorlatai(izomcsoportok);
    };

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener gyakorlatValasztoTouchListener = (v, event) -> {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Intent dataIntent = new Intent();
            dataIntent.putExtra("gyakorlatAdat", ((GyakorlatAdatTextView)v).getGyakorlat());
            ClipData data = ClipData.newIntent("gyakorlatAdat", dataIntent);

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                    v);
            v.startDragAndDrop(data, shadowBuilder, v, 0);
            return true;
        } else {
            return false;
        }
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
                Intent intent = event.getClipData().getItemAt(0).getIntent();
                if(intent.getExtras().get("gyakorlatAdat") != null) {
                    Gyakorlat gyakorlat = (Gyakorlat) intent.getExtras().get("gyakorlatAdat");
                    gyakorlatTervs.add(new GyakorlatTerv(gyakorlat.getMegnevezes(), gyakorlat.getCsoport()));
                    gyakorlatTervArrayAdapter.notifyDataSetChanged();
                }
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

    @Override
    public void onBackPressed() {
        MvvmAlertTevekenysegElhagyasaBinding viewBinding = MvvmAlertTevekenysegElhagyasaBinding.inflate(LayoutInflater.from(this), null, false);
        viewBinding.alertMessage.setText(R.string.mvvm_edzesterv_keszito_warning_backpressed);
        //alert
        new AlertDialog.Builder(this)
                .setTitle("Figyelem")
                .setView(viewBinding.getRoot())
                .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Visszalép", (dialog, which) -> {
                    edzesTervViewModel.clear();
                    super.onBackPressed();
                })
                .show();
    }

    private boolean isTervGood() {
        if(TextUtils.isEmpty(binding.btnEdzesTervElnevezes.getText().toString()) ||
            binding.btnEdzesTervElnevezes.getText().toString().equals("elnevezés megadása")) {
            return false;
        }

        if(edzesTervViewModel.getEdzesTerv() == null) {
            edzesTervViewModel.createEdzesTerv(binding.btnEdzesTervElnevezes.getText().toString());
        }

        return true;
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