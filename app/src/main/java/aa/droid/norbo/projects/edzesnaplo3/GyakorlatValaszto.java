package aa.droid.norbo.projects.edzesnaplo3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.GyakorlatViewModel;

public class GyakorlatValaszto extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private GyakorlatViewModel gyakorlatViewModel;
    private ListView listView;
    private String felhasznaloNev;
    private Naplo naplo;
    private TextView tvTestNev;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tevekenyseg);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Gyakorlatok");
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTestNev = findViewById(R.id.tvTestNev);

        felhasznaloNev = getIntent().getStringExtra(MainActivity.FELHASZNALONEV);
        if(felhasznaloNev != null) {
            tvTestNev.setText(felhasznaloNev +" naplója");
        }

        listView = findViewById(R.id.gyakrolatListView);
        listView.setNestedScrollingEnabled(true);
        listView.startNestedScroll(View.OVER_SCROLL_ALWAYS);
        registerForContextMenu(listView);

        gyakorlatViewModel = new ViewModelProvider(this).get(GyakorlatViewModel.class);
        gyakorlatViewModel.getGyListLiveData().observe(this, new Observer<List<Gyakorlat>>() {
            @Override
            public void onChanged(List<Gyakorlat> gyakorlats) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    gyakorlats.sort(new Comparator<Gyakorlat>() {
                        @Override
                        public int compare(Gyakorlat o1, Gyakorlat o2) {
                            return o1.getCsoport().compareTo(o2.getCsoport());
                        }
                    });
                }
                listView.setAdapter(new ListItemAdapter(gyakorlats));
                listView.setOnItemClickListener(GyakorlatValaszto.this);
            }
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGyakorlatDialog(null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        felhasznaloNev = new MainActivity().getNevFromFileOut(this, MainActivity.TAROLTNEV);
        tvTestNev.setText(felhasznaloNev+" naplója");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tevekenyseg, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((ListItemAdapter)listView.getAdapter()).getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_mentett_nezet) {
            startActivity(new Intent(this, MentettNaploActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == MainActivity.EDZESACTIVITY && resultCode == RESULT_OK) {
            if(data != null)
                naplo = (Naplo) data.getSerializableExtra(MainActivity.INTENT_DATA_NAPLO);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createGyakorlatDialog(Gyakorlat gyakorlat) {
        final View customView = LayoutInflater.from(this).inflate(R.layout.gyakorlatdialog, null);
        EditText megnevezes = customView.findViewById(R.id.etGyakDialogNev);
        AppCompatSpinner izomcsoport = customView.findViewById(R.id.etGyakDialogCsoport);
        EditText leiras = customView.findViewById(R.id.etGyakDialogLeiras);
        EditText videolink = customView.findViewById(R.id.etGyakDialogVideolink);
        EditText videostartpoz = customView.findViewById(R.id.etGyakDialogVideoStartPoz);

        String title = (gyakorlat != null) ? gyakorlat.getMegnevezes()+" szerkesztése" : "Új gyakorlat felvétele";

        String[] izomccsoportResource = getResources().getStringArray(R.array.izomcsoportok);

        if(gyakorlat != null) {
            int szerkeszIndex = 0;
            for (int i=0; i<izomccsoportResource.length; i++) {
                if(izomccsoportResource[i].equals(gyakorlat.getCsoport())) {
                    szerkeszIndex = i;
                    break;
                }
            }
            megnevezes.setText(gyakorlat.getMegnevezes());
            izomcsoport.setSelection(szerkeszIndex);
            leiras.setText(gyakorlat.getLeiras());
            videolink.setText(gyakorlat.getVideolink());
            videostartpoz.setText(""+gyakorlat.getVideostartpoz());
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(customView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(megnevezes.getText().toString()) || izomcsoport.getSelectedItem().toString().equals("Kérlek, válassz...")) {
                            Toast.makeText(GyakorlatValaszto.this, "Izomcsoport, megnevezés kötelező megadni", Toast.LENGTH_LONG).show();
                            return;
                        }
                        
                        int videopoz = (TextUtils.isEmpty(videostartpoz.getText().toString())) ? 0 :
                                Integer.parseInt(videostartpoz.getText().toString());
                        if(gyakorlat == null) {
                            gyakorlatViewModel.insert(new Gyakorlat(izomcsoport.getSelectedItem().toString(),
                                    megnevezes.getText().toString(),
                                    leiras.getText().toString(),
                                    videolink.getText().toString(), videopoz));
                            Toast.makeText(GyakorlatValaszto.this, "Gyakorlat felvéve a listára", Toast.LENGTH_SHORT).show();
                        } else {
                            gyakorlat.setMegnevezes(megnevezes.getText().toString());
                            gyakorlat.setCsoport(izomcsoport.getSelectedItem().toString());
                            gyakorlat.setLeiras(leiras.getText().toString());
                            gyakorlat.setVideolink(videolink.getText().toString());
                            gyakorlat.setVideostartpoz(videopoz);
                            gyakorlatViewModel.update(gyakorlat);
                            Toast.makeText(GyakorlatValaszto.this, "Gyakorlat szerkesztve", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void showAlertGyakTorles(Gyakorlat gyakorlat) {
        new AlertDialog.Builder(this)
                .setMessage("Biztos törölni akarod?")
                .setTitle(gyakorlat.getMegnevezes()+" törlése")
                .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gyakorlatViewModel.delete(gyakorlat);
                    }
                })
                .setNegativeButton("Nem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Gyakorlat gy = (Gyakorlat) listView.getAdapter().getItem(position);
        Intent edzes = new Intent(this, Edzes.class);
        edzes.putExtra(MainActivity.INTENT_DATA_GYAKORLAT, gy);
        edzes.putExtra(MainActivity.INTENT_DATA_NEV, felhasznaloNev);
        edzes.putExtra(MainActivity.INTENT_DATA_NAPLO, naplo);
        startActivityForResult(edzes, MainActivity.EDZESACTIVITY);
    }

    int kijeloltGyakPoz;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.gyak_szerkeszto_menu, menu);
        AdapterView.AdapterContextMenuInfo mi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        if(v == findViewById(R.id.gyakrolatListView)) {
            kijeloltGyakPoz = mi.position;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gyakszerk :
                createGyakorlatDialog((Gyakorlat) listView.getAdapter().getItem(kijeloltGyakPoz));
                break;
            case R.id.gyaktorol :
                showAlertGyakTorles((Gyakorlat) listView.getAdapter().getItem(kijeloltGyakPoz));
                break;
        }
        return super.onContextItemSelected(item);
    }

    private class ListItemAdapter extends BaseAdapter implements Filterable {
        private List<Gyakorlat> gyakorlats;
        private List<Gyakorlat> gyakorlatsFull;
        private CsoportFilter csoportFilter;

        public ListItemAdapter(List<Gyakorlat> gyakorlats) {
            this.gyakorlats = gyakorlats;
            this.gyakorlatsFull = gyakorlats;
        }

        @Override
        public int getCount() {
            return gyakorlats.size();
        }

        @Override
        public Object getItem(int position) {
            return gyakorlats.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Filter getFilter() {
            if(csoportFilter == null)
                csoportFilter = new CsoportFilter();
            return csoportFilter;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(GyakorlatValaszto.this).inflate(R.layout.gyakorlat_list_item, parent, false);
            }
            Gyakorlat gy = (Gyakorlat) getItem(position);
            final TextView nev = convertView.findViewById(R.id.gyak_neve);
            nev.setText(gy.getMegnevezes());
            final TextView csoport = convertView.findViewById(R.id.gyakizomcsoport);
            csoport.setText(gy.getCsoport()+" ["+gy.getId()+"]");
            return convertView;
        }

        class CsoportFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if(constraint != null && constraint.length() > 0) {
                    List<Gyakorlat> filterList = new ArrayList<>();
                    for(Gyakorlat rssItem: gyakorlatsFull) {
                        if(rssItem.getCsoport().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filterList.add(rssItem);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = gyakorlatsFull.size();
                    results.values = gyakorlatsFull;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count == 0) {
                    notifyDataSetInvalidated();
                } else {
                    gyakorlats = (List<Gyakorlat>) results.values;
                    notifyDataSetChanged();
                }
            }
        }
    }
}
