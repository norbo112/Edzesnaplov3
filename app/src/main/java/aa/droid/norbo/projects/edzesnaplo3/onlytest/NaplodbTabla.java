package aa.droid.norbo.projects.edzesnaplo3.onlytest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Sorozat;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.SorozatViewModel;

public class NaplodbTabla extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naplodb_tabla);

        ListView listView = findViewById(R.id.lvSorozatTabla);
        List<Sorozat> sorozats = new ArrayList<>();
        SorozatViewModel sorozatViewModel = new ViewModelProvider(this)
                .get(SorozatViewModel.class);
        ArrayAdapter<Sorozat> adapter = new ArrayAdapter<Sorozat>(this, R.layout.db_sorozat_item, sorozats) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Sorozat s = getItem(position);
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.db_sorozat_item, parent, false);
                }

                ((TextView)convertView.findViewById(R.id.db_sorozat_id)).setText(s.getId()+"");
                ((TextView)convertView.findViewById(R.id.db_sorozat_gyakid)).setText(s.getGyakorlatid()+"");
                ((TextView)convertView.findViewById(R.id.db_sorozat_suly)).setText(s.getSuly()+"");
                ((TextView)convertView.findViewById(R.id.db_sorozat_ism)).setText(s.getIsmetles()+"");
                ((TextView)convertView.findViewById(R.id.db_sorozaT_ismido)).setText(s.getIsmidopont());
                ((TextView)convertView.findViewById(R.id.db_sorozat_naplod)).setText(s.getNaplodatum());

                return convertView;
            }
        };

        sorozatViewModel.getAllSorozat().observe(this, new Observer<List<Sorozat>>() {
            @Override
            public void onChanged(List<Sorozat> sorozats1) {
                sorozats.addAll(sorozats1);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
