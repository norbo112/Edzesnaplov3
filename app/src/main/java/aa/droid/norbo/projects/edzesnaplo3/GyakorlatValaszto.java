package aa.droid.norbo.projects.edzesnaplo3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
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
import aa.droid.norbo.projects.edzesnaplo3.database.entities.NaploUser;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.GyakorlatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.NaploUserViewModel;
import aa.droid.norbo.projects.edzesnaplo3.datainterfaces.AdatBeallitoInterface;

public class GyakorlatValaszto extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "GyakorlatValaszto";
    private GyakorlatViewModel gyakorlatViewModel;
    private ListView listView;
    private String felhasznaloNev;
    private Naplo naplo;

    private AdatBeallitoInterface beallitoInterface;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        beallitoInterface = (AdatBeallitoInterface) context;
        felhasznaloNev = beallitoInterface.getFelhasznaloNev();
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tevekenyseg, container, false);
        TextView tvTestNev = view.findViewById(R.id.tvTestNev);

        NaploUserViewModel naploUserViewModel = new ViewModelProvider(this)
                .get(NaploUserViewModel.class);

        naploUserViewModel.getNaploUserLiveData().observe(this, new Observer<NaploUser>() {
            @Override
            public void onChanged(NaploUser naploUser) {
                if(naploUser != null) {
                    felhasznaloNev = naploUser.getFelhasznalonev();
                    tvTestNev.setText(felhasznaloNev + " naplója");
                }
            }
        });

        listView = view.findViewById(R.id.gyakrolatListView);
        listView.setNestedScrollingEnabled(true);
        listView.startNestedScroll(View.OVER_SCROLL_ALWAYS);
        registerForContextMenu(listView);

        gyakorlatViewModel = new ViewModelProvider(this).get(GyakorlatViewModel.class);
        gyakorlatViewModel.getGyListLiveData().observe(getActivity(), new Observer<List<Gyakorlat>>() {
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
                listView.setAdapter(new ListItemAdapter(gyakorlats, getContext()));
                listView.setOnItemClickListener(GyakorlatValaszto.this);
            }
        });

        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGyakorlatDialog(null);
            }
        });

        SearchView searchView = view.findViewById(R.id.gyak_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    ((GyakorlatValaszto.ListItemAdapter) listView.getAdapter()).getFilter().filter(newText);
                } catch (NullPointerException ex) {
                    Log.e(TAG, "onQueryTextChange: Valamiért null van itt amikor elforgatom a kijelzőt", ex);
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void createGyakorlatDialog(Gyakorlat gyakorlat) {
        final View customView = LayoutInflater.from(getContext()).inflate(R.layout.gyakorlatdialog, null);
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

        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setView(customView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(megnevezes.getText().toString()) || izomcsoport.getSelectedItem().toString().equals("Kérlek, válassz...")) {
                            Toast.makeText(getContext(), "Izomcsoport, megnevezés kötelező megadni", Toast.LENGTH_LONG).show();
                            return;
                        }
                        
                        int videopoz = (TextUtils.isEmpty(videostartpoz.getText().toString())) ? 0 :
                                Integer.parseInt(videostartpoz.getText().toString());
                        if(gyakorlat == null) {
                            gyakorlatViewModel.insert(new Gyakorlat(izomcsoport.getSelectedItem().toString(),
                                    megnevezes.getText().toString(),
                                    leiras.getText().toString(),
                                    videolink.getText().toString(), videopoz));
                            Toast.makeText(getContext(), "Gyakorlat felvéve a listára", Toast.LENGTH_SHORT).show();
                        } else {
                            gyakorlat.setMegnevezes(megnevezes.getText().toString());
                            gyakorlat.setCsoport(izomcsoport.getSelectedItem().toString());
                            gyakorlat.setLeiras(leiras.getText().toString());
                            gyakorlat.setVideolink(videolink.getText().toString());
                            gyakorlat.setVideostartpoz(videopoz);
                            gyakorlatViewModel.update(gyakorlat);
                            Toast.makeText(getContext(), "Gyakorlat szerkesztve", Toast.LENGTH_SHORT).show();
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
        new AlertDialog.Builder(getContext())
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
        beallitoInterface.adatGyakorlat(gy);
        ViewPager viewById = getActivity().findViewById(R.id.view_pager);
        if(viewById != null) viewById.setCurrentItem(1, true);
    }

    int kijeloltGyakPoz;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.gyak_szerkeszto_menu, menu);
        AdapterView.AdapterContextMenuInfo mi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        if(v.getId() == R.id.gyakrolatListView) {
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
            case R.id.gyakszerk_menu_video :
                Gyakorlat gyakorlat = ((Gyakorlat) listView.getAdapter().getItem(kijeloltGyakPoz));
                if(gyakorlat != null && gyakorlat.getVideolink().length() > 0) {
                    Intent videointent = new Intent(getContext(), VideoActivity.class);
                    videointent.putExtra(VideoActivity.EXTRA_GYAKORLAT, gyakorlat);
                    startActivity(videointent);
                } else {
                    Toast.makeText(getContext(), "Nincs video!", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onContextItemSelected(item);
    }

    public static class ListItemAdapter extends BaseAdapter implements Filterable {
        private List<Gyakorlat> gyakorlats;
        private List<Gyakorlat> gyakorlatsFull;
        private CsoportFilter csoportFilter;
        private Context context;

        public ListItemAdapter(List<Gyakorlat> gyakorlats, Context context) {
            this.gyakorlats = gyakorlats;
            this.gyakorlatsFull = gyakorlats;
            this.context = context;
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

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.gyakorlat_list_item, parent, false);
            }
            Gyakorlat gy = (Gyakorlat) getItem(position);
            final TextView nev = convertView.findViewById(R.id.gyak_neve);
            nev.setText(gy.getMegnevezes());
            final TextView csoport = convertView.findViewById(R.id.gyakizomcsoport);
            csoport.setText(gy.getCsoport()+" "+(gy.getVideolink() != null && gy.getVideolink().length() > 0 ? "[Videó!]" : ""));
            return convertView;
        }

        class CsoportFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if(constraint != null && constraint.length() > 0) {
                    List<Gyakorlat> filterList = new ArrayList<>();
                    for(Gyakorlat rssItem: gyakorlatsFull) {
                        if(rssItem.getCsoport().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            rssItem.getMegnevezes().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
