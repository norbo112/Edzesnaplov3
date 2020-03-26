package aa.droid.norbo.projects.edzesnaplo3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.GyakorlatViewModel;

public class Tevekenyseg extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private GyakorlatViewModel gyakorlatViewModel;
    private ListView listView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tevekenyseg);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Tevékenység");
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);

        TextView tvTestNev = findViewById(R.id.tvTestNev);

        final String felhasznaloNev = new MainActivity().getFelhasznaloNev(this);
        if(felhasznaloNev != null) {
            tvTestNev.setText(felhasznaloNev+" naplója");
        }

        listView = findViewById(R.id.gyakrolatListView);
        listView.setNestedScrollingEnabled(true);
        listView.startNestedScroll(View.OVER_SCROLL_ALWAYS);

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
                listView.setOnItemClickListener(Tevekenyseg.this);
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

    private void createGyakorlatDialog(Gyakorlat gyakorlat) {
        //gyakorlat a szerkesztéshez majd
        final View customView = LayoutInflater.from(this).inflate(R.layout.gyakorlatdialog, null);
        EditText megnevezes = customView.findViewById(R.id.etGyakDialogNev);
        EditText izomcsoport = customView.findViewById(R.id.etGyakDialogCsoport);
        EditText leiras = customView.findViewById(R.id.etGyakDialogLeiras);
        EditText videolink = customView.findViewById(R.id.etGyakDialogVideolink);
        EditText videostartpoz = customView.findViewById(R.id.etGyakDialogVideoStartPoz);

        new AlertDialog.Builder(this)
                .setTitle("Gyakorlat adatai")
                .setView(customView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(megnevezes.getText().toString()) || TextUtils.isEmpty(izomcsoport.getText().toString())) {
                            Toast.makeText(Tevekenyseg.this, "Izomcsoport, megnevezés kötelező megadni", Toast.LENGTH_LONG).show();
                            return;
                        }
                        
                        int videopoz = (TextUtils.isEmpty(videostartpoz.getText().toString())) ? 0 :
                                Integer.parseInt(videostartpoz.getText().toString());
                        
                        gyakorlatViewModel.insert(new Gyakorlat(izomcsoport.getText().toString(),
                                megnevezes.getText().toString(),
                                leiras.getText().toString(),
                                videolink.getText().toString(), videopoz));
                        Toast.makeText(Tevekenyseg.this, "Gyakorlat felvéve a listára", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Gyakorlat gy = (Gyakorlat) listView.getAdapter().getItem(position);
        String gystr = gy.getMegnevezes()+"\n"+gy.getCsoport()+" izomcsoport\n"+gy.getLeiras();
        new AlertDialog.Builder(this)
                .setTitle("Gyakorlat adatai")
                .setMessage(gystr)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
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
                convertView = LayoutInflater.from(Tevekenyseg.this).inflate(R.layout.gyakorlat_list_item, parent, false);
            }
            Gyakorlat gy = (Gyakorlat) getItem(position);
            final TextView nev = convertView.findViewById(R.id.gyak_neve);
            nev.setText(gy.getMegnevezes());
            final TextView csoport = convertView.findViewById(R.id.gyakizomcsoport);
            csoport.setText(gy.getCsoport());
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
