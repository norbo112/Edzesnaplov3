package aa.droid.norbo.projects.edzesnaplo3.jelenlegimunka;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.SorozatViewModel;
import aa.droid.norbo.projects.edzesnaplo3.uiutils.DateTimeFormatter;

public class BovitettKorabbiEdzesFragmant extends Fragment {
    private ListView listView;
    private List<Sorozat> sorozats;
    private TextView status;
    private ArrayAdapter<Sorozat> adapter;
    private Context mContext;

    public BovitettKorabbiEdzesFragmant() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.sorozats = new ArrayList<>();
        this.adapter = getSorozatAdapter();
    }

    private ArrayAdapter<Sorozat> getSorozatAdapter() {
        return new ArrayAdapter<Sorozat>(mContext,R.layout.bovitett_sorozat_item, sorozats) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Sorozat item = getItem(position);
                if(convertView == null)
                    convertView = getLayoutInflater().inflate(R.layout.bovitett_sorozat_item, parent, false);

                if(item != null) {
                    ((TextView) convertView.findViewById(R.id.tvSorozatDatum)).setText(DateTimeFormatter.getDate(item.getNaplodatum()));
                    ((TextView) convertView.findViewById(R.id.tvSorozatAdat)).setText(item.toString());
                }

                return convertView;
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            sorozats = savedInstanceState.getParcelableArrayList("sorozatok");
            adapter = getSorozatAdapter();
        }
        View view =  inflater.inflate(R.layout.activity_bovitett_korabbi_edzes_fragmane, container, false);
        status = view.findViewById(R.id.bovitett_status);
        listView = view.findViewById(R.id.bovitett_korabbi_listview);
        listView.setAdapter(adapter);

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    public void setListViewAdapter(Gyakorlat gyakorlat) {
        SorozatViewModel viewModel = new ViewModelProvider(this)
                .get(SorozatViewModel.class);

        new AsyncTask<Void, Void, List<Sorozat>>() {
            @Override
            protected List<Sorozat> doInBackground(Void... voids) {
                return viewModel.getallByGyakorlat(gyakorlat.getId());
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onPostExecute(List<Sorozat> sorozats) {
                if(sorozats != null && sorozats.size() > 0) {
                    BovitettKorabbiEdzesFragmant.this.sorozats.clear();
                    BovitettKorabbiEdzesFragmant.this.sorozats.addAll(sorozats);
                    adapter.notifyDataSetChanged();
                    status.setText(countNapok(sorozats)+" napon át");
                } else {
                    BovitettKorabbiEdzesFragmant.this.sorozats.clear();
                    adapter.notifyDataSetChanged();
                    status.setText("Még nem használtad ezt a gyakorlatot");
                }
            }
        }.execute();

    }

    private int countNapok(List<Sorozat> sorozats) {
        Set<String> mentettnaplok = new HashSet<>();
        for (Sorozat s: sorozats) {
            mentettnaplok.add(s.getNaplodatum());
        }

        return  mentettnaplok.size();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("sorozatok", (ArrayList<? extends Parcelable>) sorozats);
        super.onSaveInstanceState(outState);
    }
}
