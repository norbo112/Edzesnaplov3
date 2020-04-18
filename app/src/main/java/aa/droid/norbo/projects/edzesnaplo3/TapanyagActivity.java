package aa.droid.norbo.projects.edzesnaplo3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.TapanyagDatabase;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.tapanyag.Elelmiszer;
import aa.droid.norbo.projects.edzesnaplo3.rcview.TapanyagAdapter;

import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.TapanyagViewModel;

public class TapanyagActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String TIPUS_SZURES_LABEL = "Típus szűrés...";
    private TapanyagViewModel tapanyagViewModel;

    private List<Elelmiszer> elelmiszerList;
    private ListView taplista;

    private Spinner spinElelemTipus;
    private TextView tvElelemTipusDarab;
    private List<String> tapFajtaLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapanyag);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Tápanyagtábla");
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinElelemTipus = findViewById(R.id.elelemFajta);
        tvElelemTipusDarab = findViewById(R.id.tvFajtaDarab);
        spinElelemTipus.setOnItemSelectedListener(this);

        tapFajtaLista = new ArrayList<>();
        tapFajtaLista.add(TIPUS_SZURES_LABEL);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(TapanyagActivity.this, android.R.layout.simple_spinner_item,
                tapFajtaLista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinElelemTipus.setAdapter(adapter);

        taplista = findViewById(R.id.taplista);
        taplista.setAdapter(new TapanyagAdapter(this, new ArrayList<>()));
        taplista.setNestedScrollingEnabled(true);
        tapanyagViewModel = new ViewModelProvider(this)
                .get(TapanyagViewModel.class);
        loadList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tevekenyseg, menu);
        menu.removeItem(R.id.menu_tapanyag);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextChange(String newText) {
                if(((TapanyagAdapter)taplista.getAdapter()).getFilter_is_tipus() != 0) {
                    ((TapanyagAdapter) taplista.getAdapter()).setFilter_is_tipus(0);
                    spinElelemTipus.setSelection(0);
                }

                ((TapanyagAdapter) taplista.getAdapter()).getFilter().filter(newText,
                        new Filter.FilterListener() {
                            @Override
                            public void onFilterComplete(int count) {
                                tvElelemTipusDarab.setText(count+" db");
                            }
                        });
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_mentett_nezet) {
            startActivity(new Intent(this, MentettNaploActivity.class));
        } else if(item.getItemId() == R.id.menu_diagram) {
            startActivity(new Intent(this, DiagramActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadList() {
        tapanyagViewModel.getLiveData().observe(this, new Observer<List<Elelmiszer>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<Elelmiszer> elelmiszers) {
                if(elelmiszers != null && elelmiszers.size() > 0) {
                    elelmiszerList = elelmiszers;
                    initElelmiszerViews(elelmiszers);
                    tvElelemTipusDarab.setText(elelmiszers.size()+" db");
                } else {
                    Toast.makeText(TapanyagActivity.this, "Nincsenek rögzített élelmiszerek! :(",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        tapanyagViewModel.getElelemTipus().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if (strings != null && strings.size() > 0) {
                    tapFajtaLista.addAll(strings);
                } else {
                    Toast.makeText(TapanyagActivity.this, "MNincsenek választható típusok", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initElelmiszerViews(List<Elelmiszer> elelmiszers) {
        if(taplista.getAdapter() != null) {
            ((TapanyagAdapter) taplista.getAdapter()).setElelmiszers(elelmiszers);
        } else {
            taplista.setAdapter(new TapanyagAdapter(this, elelmiszers));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TapanyagAdapter)taplista.getAdapter()).setFilter_is_tipus(1);
        Filter filter = ((TapanyagAdapter) taplista.getAdapter()).getFilter();
        filter.filter(spinElelemTipus.getSelectedItem().equals(TIPUS_SZURES_LABEL) ? "" : spinElelemTipus.getSelectedItem().toString(),
                new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        tvElelemTipusDarab.setText(count+" db");
                    }
                });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
