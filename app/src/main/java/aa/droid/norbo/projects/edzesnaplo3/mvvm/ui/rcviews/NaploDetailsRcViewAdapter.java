package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.rcviews;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmGyakSorozatRcitemBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatSorozatUI;

public class NaploDetailsRcViewAdapter extends RecyclerView.Adapter<NaploDetailsRcViewAdapter.NaploDetailsViewHolder> {
    private List<GyakorlatSorozatUI> gyakorlatSorozatUIS;
    private Context context;

    public NaploDetailsRcViewAdapter(List<GyakorlatSorozatUI> sorozat, Context context) {
        this.gyakorlatSorozatUIS = sorozat;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull NaploDetailsViewHolder holder, int position) {
        holder.bind(gyakorlatSorozatUIS.get(position));
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
            long el = elteltIdo(sorozat);
            binding.info.append(String.format(Locale.getDefault(), "\n %d óra és %d perc alatt elvégezve", el/60, el%60));
            binding.executePendingBindings();
        }

        private long elteltIdo(GyakorlatSorozatUI gyakorlatSorozatUI) {
            Duration between = Duration.between(Instant.ofEpochMilli(gyakorlatSorozatUI.getSorozats().get(0).getIsmidopont()),
                    Instant.ofEpochMilli(gyakorlatSorozatUI.getSorozats().get(gyakorlatSorozatUI.getSorozats().size() - 1).getIsmidopont()));
            return between.toMinutes();
        }
    }
}
