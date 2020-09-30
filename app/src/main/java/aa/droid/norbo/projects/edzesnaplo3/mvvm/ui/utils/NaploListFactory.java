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

import java.util.List;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmMentettNaploItemWithDelbuttonBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Naplo;
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
    public ArrayAdapter<Naplo> getListAdapter(List<Naplo> naplos) {
        ArrayAdapter<Naplo> listAdapter = new ArrayAdapter<Naplo>(context, R.layout.mvvm_mentett_naplo_item_with_delbutton, naplos) {
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

                long naplodatum = getItem(position).getNaplodatum();
                itemBinding.mvvmMentettNaploWithDelLabel.setText(timeFormatter.getNaploDatum(naplodatum));
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
}
