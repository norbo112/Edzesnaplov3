package aa.droid.norbo.projects.edzesnaplo3.rcview;

import android.content.Context;
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
    static class NaploViewHolder extends RecyclerView.ViewHolder {
        TextView tvGyakCim;
        ListView listView;
        TextView megmozgatottSuly;
        NaploViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGyakCim = itemView.findViewById(R.id.tvRcGyakNev);
            listView = itemView.findViewById(R.id.lvRcSorozatLista);
            megmozgatottSuly = itemView.findViewById(R.id.tvMegmozgatottSuly);
        }
    }

    private Context context;
    private List<NaploActivity.GyakorlatWithSorozat> sorozats;

    public NaploAdapter(Context context, List<NaploActivity.GyakorlatWithSorozat> sorozats) {
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
        NaploActivity.GyakorlatWithSorozat gyakorlatWithSorozat = sorozats.get(position);
        holder.tvGyakCim.setText(gyakorlatWithSorozat.getGyakorlat().getMegnevezes());

        ListView listView = holder.listView;
        listView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, gyakorlatWithSorozat.getSorozatList()));

        holder.megmozgatottSuly.setText(String.format(Locale.getDefault(), "%d kg, %d perc alatt",
                gyakorlatWithSorozat.getMegmozgatottSuly(),
                gyakorlatWithSorozat.getElteltido()));
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
