package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.rcviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmGyakSorozatRcitemBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatSorozatUI;

public class NaploDetailsRcViewAdapter extends RecyclerView.Adapter<NaploDetailsRcViewAdapter.NaploDetailsViewHolder> {
    private List<GyakorlatSorozatUI> gyakorlatSorozatUIS;
    private Context context;

    public NaploDetailsRcViewAdapter(List<GyakorlatSorozatUI> Sorozat, Context context) {
        this.gyakorlatSorozatUIS = Sorozat;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull NaploDetailsViewHolder holder, int position) {
        holder.bind(gyakorlatSorozatUIS.get(position));
//        holder.binding.rcitemGyakSorozatList
//                .setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
//                        gyakorlatSorozatUIS.get(position).getSorozats()));
    }

    @NonNull
    @Override
    public NaploDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MvvmGyakSorozatRcitemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.mvvm_gyak_sorozat_rcitem, parent, false);
        return new NaploDetailsViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return gyakorlatSorozatUIS.size();
    }

    static class NaploDetailsViewHolder extends RecyclerView.ViewHolder {
        MvvmGyakSorozatRcitemBinding binding;
        public NaploDetailsViewHolder(MvvmGyakSorozatRcitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(GyakorlatSorozatUI sorozat) {
            int osszsuly = sorozat.getSorozats().stream().mapToInt(sor -> sor.getSuly() * sor.getIsmetles()).sum();

            binding.setSorozatesgyakorlat(sorozat);
            binding.rcitemGyakSorozatList
                    .setAdapter(new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_list_item_1, sorozat.getSorozats()));
            binding.info.setText(String.format(Locale.getDefault(), "Megmozgatott súly %,d Kg", osszsuly));
            binding.executePendingBindings();
        }
    }
}
