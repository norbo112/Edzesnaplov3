package aa.droid.norbo.projects.edzesnaplo3.rcview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import aa.droid.norbo.projects.edzesnaplo3.NaploActivity;
import aa.droid.norbo.projects.edzesnaplo3.R;

public class NaploAdapter extends RecyclerView.Adapter<NaploAdapter.NaploViewHolder> {
    private static final String TAG = "NaploAdapter";

    static class NaploViewHolder extends RecyclerView.ViewHolder {
        TextView tvGyakCim;
        ListView listView;
        TextView megmozgatottSuly;
        TextView sorozat_szam;
        NaploViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGyakCim = itemView.findViewById(R.id.tvRcGyakNev);
            listView = itemView.findViewById(R.id.lvRcSorozatLista);
            megmozgatottSuly = itemView.findViewById(R.id.tvMegmozgatottSuly);
            sorozat_szam = itemView.findViewById(R.id.sorozat_id);
        }
    }

    private Context context;
    private List<NaploActivity.RCViewGyakSorozat> sorozats;

    public NaploAdapter(Context context, List<NaploActivity.RCViewGyakSorozat> sorozats) {
        this.context = context;
        this.sorozats = sorozats;
    }

    @NonNull
    @Override
    public NaploViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View naploviewholder = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rc_naplo_sorozat_item, parent, false);
        return new NaploViewHolder(naploviewholder);
    }

    @Override
    public void onBindViewHolder(@NonNull NaploViewHolder holder, int position) {
        NaploActivity.RCViewGyakSorozat gyakorlatWithSorozat = sorozats.get(position);
        holder.tvGyakCim.setText(gyakorlatWithSorozat.getGyakorlat().getMegnevezes());
        holder.sorozat_szam.setText(""+(position+1));
        ListView listView = holder.listView;
        listView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, gyakorlatWithSorozat.getSorozatList()));

        long elteltido;
        try {
            elteltido = gyakorlatWithSorozat.getElteltido();
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "onBindViewHolder: ", e);
            elteltido = -1;
        }

        holder.megmozgatottSuly.setText(String.format(Locale.getDefault(), "%d kg, %d perc alatt",
                gyakorlatWithSorozat.getMegmozgatottSuly(),
                elteltido));
    }

    @Override
    public int getItemCount() {
        return sorozats.size();
    }

    public void clear() {
        sorozats.clear();
        notifyDataSetChanged();
    }

}
