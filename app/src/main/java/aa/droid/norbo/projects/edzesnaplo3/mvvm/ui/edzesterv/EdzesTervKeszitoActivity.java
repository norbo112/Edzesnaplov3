package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityEdzestervKeszitoBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.GyakorlatTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.GyakorlatViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EdzesTervKeszitoActivity extends EdzesTervBaseActivity<MvvmActivityEdzestervKeszitoBinding> {
    @Inject
    GyakorlatViewModel gyakorlatViewModel;

    private MyTouchListener myTouchListener = new MyTouchListener();

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

        izomcsoportok = new ArrayList<>();
        gyakorlatTervs = new ArrayList<>();
        csoportokGyakorlatai = new ArrayList<>();

        initIzomcsoportok();

        binding.imGyakorlatokValasztasa.setOnClickListener(view -> {
            gyakorlatTervs.clear();
            SparseBooleanArray checkedItemPositions = binding.csoportokGyakorlatai.getCheckedItemPositions();
            for (int i=0; i<checkedItemPositions.size(); i++) {
                if(checkedItemPositions.get(i)) {
                    Gyakorlat item = csoportGyakorlataiAdapter.getItem(i);
                    gyakorlatTervs.add(new GyakorlatTerv(item.getMegnevezes()));
                }
            }
            gyakorlatTervArrayAdapter.notifyDataSetInvalidated();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initIzomcsoportok() {
        valasztottCsoportAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, izomcsoportok);
        binding.valasztottCsoportokList.setAdapter(valasztottCsoportAdapter);
        binding.valasztottCsoportokList.setOnDragListener(new MyDragListener());

        for (String csoport: getResources().getStringArray(R.array.csoportokForTervezo)) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setText(csoport);
            textView.setClickable(true);
            textView.setOnTouchListener(myTouchListener);
            binding.izomcsoportokLl.addView(textView);
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("Izomcsoportok", ((TextView)view).getText().toString());
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
//                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private final class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
//                case DragEvent.ACTION_DRAG_STARTED:
//                    Log.i(TAG, "onDrag: Drag started");
//                    break;
//                case DragEvent.ACTION_DRAG_ENTERED:
//                    Log.i(TAG, "onDrag: Drag Entered");
//                    break;
//                case DragEvent.ACTION_DRAG_EXITED:
//                    Log.i(TAG, "onDrag: Drag Exited");
//                    break;
//                case DragEvent.ACTION_DRAG_ENDED:
//                    Log.i(TAG, "onDrag: Drag ended");
//                    break;
                case DragEvent.ACTION_DROP:
                    String izomcsoport = event.getClipData().getItemAt(0).getText().toString();
                    izomcsoportok.add(izomcsoport);
                    valasztottCsoportAdapter.notifyDataSetInvalidated();
                    initCsoportokGyakorlatai(izomcsoportok);
                    break;

            }
            return true;
        }
    }

    private void initCsoportokGyakorlatai(List<String> izomcsoportok) {
        gyakorlatTervArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gyakorlatTervs);
        binding.valasztottGyakorlatokTerv.setAdapter(gyakorlatTervArrayAdapter);

        gyakorlatViewModel.getGyakorlatByCsoport(izomcsoportok).observe(this, gyakorlats -> {
            if(gyakorlats != null && gyakorlats.size() > 0) {
                csoportokGyakorlatai.clear();
                if (binding.csoportokGyakorlatai.getAdapter() == null) {
                    csoportGyakorlataiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, csoportokGyakorlatai);
                    binding.csoportokGyakorlatai.setAdapter(csoportGyakorlataiAdapter);
                }

                csoportokGyakorlatai.addAll(gyakorlats);
                csoportGyakorlataiAdapter.notifyDataSetInvalidated();


            }
        });

    }
}