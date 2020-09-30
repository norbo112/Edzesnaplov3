package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmMentettNaploItemWithDelbuttonBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.toolmodels.NaploWithSorozat;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.SorozatWithGyakorlat;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class NaploListFactory {
    private static final String TAG = "DialogFactory";
    private Context context;
    private DateTimeFormatter timeFormatter;
    private NaploTorlesInterface naploTorlesInterface;

    @Inject
    public NaploListFactory(@ActivityContext Context context, DateTimeFormatter timeFormatter) {
        this.context = context;
        this.timeFormatter = timeFormatter;
        try {
            this.naploTorlesInterface = (NaploTorlesInterface) context;
        } catch (ClassCastException ex) {
            Log.i(TAG, "DialogFactory: Ezt az activity nincs felkészítve a napló törlésére és megtekintésére");
        }
    }

    public interface NaploTorlesInterface {
        void naplotTorol(long naplodatum);
    }

    /**
     * Esetlegesen felhasználom ezt egy activitiben, melyben a naplók lesznek megjelenítve...
     * @param naplos
     * @return
     */
    public ArrayAdapter<NaploWithSorozat> getListAdapter(List<NaploWithSorozat> naplos) {
//    public ArrayAdapter<Naplo> getListAdapter(List<Naplo> naplos) {
        ArrayAdapter<NaploWithSorozat> listAdapter = new ArrayAdapter<NaploWithSorozat>(context, R.layout.mvvm_mentett_naplo_item_with_delbutton, naplos) {
            MvvmMentettNaploItemWithDelbuttonBinding itemBinding;
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.mvvm_mentett_naplo_item_with_delbutton, parent, false);
                    itemBinding = DataBindingUtil.bind(convertView);
                    convertView.setTag(itemBinding);
                } else {
                    itemBinding = (MvvmMentettNaploItemWithDelbuttonBinding) convertView.getTag();
                }

                long naplodatum = Long.parseLong(getItem(position).daonaplo.getNaplodatum());
                itemBinding.mvvmMentettNaploWithDelLabel.setText(timeFormatter.getNaploDatum(naplodatum));
                itemBinding.mvvmMentettNaploIzomcsoportlista.setText(getIzomcsoportLista(getItem(position)));
                itemBinding.imBtnNaploTorol.setOnClickListener(v -> {
                    if(naploTorlesInterface != null) {
                        naploTorlesInterface.naplotTorol(naplodatum);
                    } else {
                        Toast.makeText(context, "Napló törlése nem lehetséges!", Toast.LENGTH_SHORT).show();
                    }
                });
                return itemBinding.getRoot();
            }
        };

        return listAdapter;
    }

    public String getIzomcsoportLista(NaploWithSorozat naploWithSorozats) {
        Set<String> izomcsoportok = new LinkedHashSet<>();
        for (SorozatWithGyakorlat sorozat : naploWithSorozats.sorozats) {
            izomcsoportok.add(sorozat.gyakorlat.getCsoport());
        }
        StringBuilder sb = new StringBuilder();
        izomcsoportok.forEach(csoport -> sb.append(csoport).append(", "));
        return sb.toString().substring(0, sb.toString().lastIndexOf(','));
    }
}
