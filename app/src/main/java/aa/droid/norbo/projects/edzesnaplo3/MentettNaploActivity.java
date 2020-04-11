package aa.droid.norbo.projects.edzesnaplo3;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import aa.droid.norbo.projects.edzesnaplo3.database.dao.SorozatWithGyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Naplo;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.NaploViewModel;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.providers.NaploContentProvider;
import aa.droid.norbo.projects.edzesnaplo3.rcview.NaploAdapter;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.FileWorkerImpl;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.FileWorkerInterface;

public class MentettNaploActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        SwipeActionAdapter.SwipeActionListener {
    private static final int FILE_LOAD_RCODE = 1;
    private final String TAG = getClass().getSimpleName();

    private ListView listView;
    private RecyclerView rcnaploview;
    private TextView osszsnapisuly;
    private NaploViewModel naploViewModel;
    private ConstraintLayout rootView;

    private SwipeActionAdapter mAdapter;
    private SorozatViewModel sorozatViewModel;
    private FileWorkerInterface<SorozatWithGyakorlat> fileWorkerInterface;
    private List<SorozatWithGyakorlat> menteshezLista;

    private Naplo naplo;
    private Uri jsonFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentettnaplo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mentett Naplók");
        toolbar.setLogo(R.drawable.ic_run);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fileWorkerInterface = new FileWorkerImpl<>(this);

        rcnaploview = findViewById(R.id.rcNaploMegtekinto);
        listView = findViewById(R.id.lvMentettNaploLista);
        osszsnapisuly = findViewById(R.id.tvMOsszsuly);

        rootView = findViewById(R.id.root_mentett_naplo_view);

        naploViewModel = new ViewModelProvider(this).get(NaploViewModel.class);
        naploViewModel.getNaploListLiveData().observe(this, new Observer<List<Naplo>>() {
            @Override
            public void onChanged(List<Naplo> naplos) {
                if(naplos.size() != 0) {
                    ArrayAdapter adapter = new ArrayAdapter<Naplo>(MentettNaploActivity.this, R.layout.mentett_naplo_item_0, naplos) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            if(convertView == null) {
                                convertView =getLayoutInflater().inflate(R.layout.mentett_naplo_item_0, parent, false);
                            }
                            
                            final Naplo selected = naplos.get(position);

                            ((TextView)convertView.findViewById(R.id.tvNaploListaItem)).setText(selected.getNaplodatum());

                            return convertView;
                        }

                        @Override
                        public int getCount() {
                            return naplos.size();
                        }
                    };

                    mAdapter = new SwipeActionAdapter(adapter);
                    listView.setAdapter(mAdapter);
                    mAdapter.setListView(listView);
                    listView.setOnItemClickListener(MentettNaploActivity.this);

                    mAdapter.addBackground(SwipeDirection.DIRECTION_NORMAL_RIGHT, R.layout.swipe_action_delete);
                    mAdapter.addBackground(SwipeDirection.DIRECTION_FAR_LEFT, R.layout.swipe_action_normal);
                    mAdapter.addBackground(SwipeDirection.DIRECTION_FAR_RIGHT, R.layout.swipe_action_delete);
                    mAdapter.addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT, R.layout.swipe_action_normal);
                    mAdapter.addBackground(SwipeDirection.DIRECTION_NEUTRAL, R.layout.swipe_action_normal);
                    mAdapter.setSwipeActionListener(MentettNaploActivity.this);
                } else {
                    changeEmptyTextView();
                }
            }
        });
    }

    private void changeEmptyTextView() {
        rcnaploview.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        osszsnapisuly.setText(R.string.mentett_naplo_empty);

        ConstraintSet cntset = new ConstraintSet();
        cntset.clone(rootView);
        cntset.connect(R.id.tvMOsszsuly, ConstraintSet.TOP, R.id.root_mentett_naplo_view, ConstraintSet.TOP,0);
        cntset.applyTo(rootView);
    }

    private void alertTorles(Naplo selected) {
        new AlertDialog.Builder(this)
                .setTitle("Törlés")
                .setMessage("Biztosan törölni akarod?")
                .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        naploViewModel.delete(selected);
                        sorozatViewModel = new ViewModelProvider(MentettNaploActivity.this)
                                .get(SorozatViewModel.class);
                        sorozatViewModel.delete(selected.getNaplodatum());
                        NaploContentProvider.sendRefreshBroadcast(getApplicationContext());
                        Toast.makeText(MentettNaploActivity.this, "Napló törölve", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Mégse", null)
                .create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mentett_naplok, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_mentett_naplok_alldelete) {
            new AlertDialog.Builder(this)
                    .setTitle("Törlés")
                    .setMessage("Biztos törölni akarod a naplókat?")
                    .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            naploViewModel.deleteAll();
                            SorozatViewModel sorozatViewModel = new ViewModelProvider(
                                    MentettNaploActivity.this
                            )
                                    .get(SorozatViewModel.class);
                            sorozatViewModel.deleteAll();
                            changeEmptyTextView();
                        }
                    })
                    .setNegativeButton("Nem", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();

        } else if(item.getItemId() == R.id.menu_mentett_driveupload) {
            if(naplo != null && menteshezLista != null) {
                jsonFilePath = fileWorkerInterface.makeJsonFile(menteshezLista, "naplo_"+System.currentTimeMillis()+".json");
                Toast.makeText(this, "Napló fájl mentve", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Kérlek válassz egy naplót", Toast.LENGTH_SHORT).show();
            }
        } else if(item.getItemId() == R.id.menu_diagram) {
            startActivity(new Intent(this, DiagramActivity.class));
        } else if(item.getItemId() == R.id.menu_mentett_load) {
            Intent filechooser = new Intent(Intent.ACTION_GET_CONTENT);
            filechooser.setType("*/*");
            filechooser.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(filechooser, FILE_LOAD_RCODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FILE_LOAD_RCODE && resultCode == RESULT_OK) {
            String filename = getFileName(data.getData());
            loadKulsoMentettLista(data.getData());
            Log.i(TAG, "onActivityResult: Fájl kiválasztva: " + filename);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadKulsoMentettLista(Uri data) {
        List<SorozatWithGyakorlat> list;
        try {
            list = fileWorkerInterface.loadJsonFile(getContentResolver().openInputStream(data));
            if(!list.isEmpty()) {
                String naplodatum = list.get(0).sorozat.getNaplodatum();
                naploViewModel.insert(new Naplo(naplodatum, "kulso_forras"));

                for (int i = 0; i < list.size(); i++) {
                    sorozatViewModel.insert(new Sorozat(list.get(i).sorozat));
                }

                Toast.makeText(this, "Lista betöltve és mentve: mérete= " + list.size(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lista betöltve: Üres a lista", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(Uri data) {
        Cursor cursor = getContentResolver().query(data, null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            throw new IllegalArgumentException("Can't obtain file name, cursor is empty");
        }

        cursor.moveToFirst();
        String fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
        cursor.close();
        return fileName;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        naplo = (Naplo) listView.getAdapter().getItem(position);
        if(naplo != null) {
            SorozatViewModel sorozatViewModel = new ViewModelProvider(MentettNaploActivity.this)
                    .get(SorozatViewModel.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                CompletableFuture<LiveData<List<SorozatWithGyakorlat>>> sorozatWithGyakByNaplo =
                        sorozatViewModel.getSorozatWithGyakByNaplo(naplo.getNaplodatum());
                try {
                    sorozatWithGyakByNaplo.get().observe(MentettNaploActivity.this, new Observer<List<SorozatWithGyakorlat>>() {
                        @Override
                        public void onChanged(List<SorozatWithGyakorlat> sorozatWithGyakorlats) {
                            menteshezLista = sorozatWithGyakorlats;
                            List<NaploActivity.GyakorlatWithSorozat> withSorozats = new NaploActivity().doitMentettNaploMegjelenesre(sorozatWithGyakorlats);
                            int napiosszsuly = getNapiOsszSuly(withSorozats);
                            osszsnapisuly.setText(String.format(Locale.getDefault(),"%d Kg napi megmozgatott súly", napiosszsuly));
                            rcnaploview.setAdapter(new NaploAdapter(MentettNaploActivity.this, withSorozats));
                            rcnaploview.setLayoutManager(new LinearLayoutManager(MentettNaploActivity.this));
                            rcnaploview.setItemAnimator(new DefaultItemAnimator());
                        }
                    });
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, "onItemSelected: Naplók betöltése", e);
                }
            } else {
                new AsyncTask<Void, Void, List<SorozatWithGyakorlat>>() {
                    @Override
                    protected List<SorozatWithGyakorlat> doInBackground(Void... voids) {
                        List<SorozatWithGyakorlat> sorozatWithGyakByNaploToList =
                                sorozatViewModel.getSorozatWithGyakByNaploToList(naplo.getNaplodatum());
                        menteshezLista = sorozatWithGyakByNaploToList;

                        return sorozatWithGyakByNaploToList;
                    }

                    @Override
                    protected void onPostExecute(List<SorozatWithGyakorlat> sorozatWithGyakorlats) {
                        List<NaploActivity.GyakorlatWithSorozat> withSorozats = new NaploActivity().doitMentettNaploMegjelenesre(sorozatWithGyakorlats);
                        int napiosszsuly = getNapiOsszSuly(withSorozats);
                        osszsnapisuly.setText(String.format(Locale.getDefault(),"%d Kg napi megmozgatott súly", napiosszsuly));
                        rcnaploview.setAdapter(new NaploAdapter(MentettNaploActivity.this, withSorozats));
                        rcnaploview.setLayoutManager(new LinearLayoutManager(MentettNaploActivity.this));
                        rcnaploview.setItemAnimator(new DefaultItemAnimator());
                    }
                }.execute();
            }
        }
    }

    private int getNapiOsszSuly(List<NaploActivity.GyakorlatWithSorozat> withSorozats) {
        int result = 0;
        for (int i = 0; i < withSorozats.size(); i++) {
            result += withSorozats.get(i).getMegmozgatottSuly();
        }
        return result;
    }

    @Override
    public boolean hasActions(int position, SwipeDirection direction) {
        if(direction.isRight()) return true;
        return false;
    }

    @Override
    public boolean shouldDismiss(int position, SwipeDirection direction) {
        return direction == SwipeDirection.DIRECTION_NORMAL_LEFT;
    }

    @Override
    public void onSwipe(int[] position, SwipeDirection[] direction) {
        for (int i = 0; i < position.length; i++) {
            SwipeDirection swipedirection = direction[i];
            int poz = position[i];

            switch (swipedirection) {
                case DIRECTION_FAR_LEFT:
                    break;
                case DIRECTION_NORMAL_LEFT:
                    break;
                case DIRECTION_FAR_RIGHT:
                case DIRECTION_NORMAL_RIGHT:
                    alertTorles((Naplo) mAdapter.getItem(poz));
                    break;
            }

            mAdapter.notifyDataSetChanged();
        }
    }
}
