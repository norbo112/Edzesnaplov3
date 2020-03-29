package aa.droid.norbo.projects.edzesnaplo3.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.Comparator;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.GyakorlatValaszto;
import aa.droid.norbo.projects.edzesnaplo3.R;
import aa.droid.norbo.projects.edzesnaplo3.database.entities.Gyakorlat;
import aa.droid.norbo.projects.edzesnaplo3.database.viewmodels.GyakorlatViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private GyakorlatViewModel gyakorlatViewModel;
    private ListView listView;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        View root = inflater.inflate((index == 1) ? R.layout.tevekenyseg :
                R.layout.test_tabbed_edzes_layout, container,false);
        generateLogic(root, index);
        return root;
    }

    private void generateLogic(View root, int index) {
        if(index == 1) {
            listView = root.findViewById(R.id.gyakrolatListView);
            gyakorlatViewModel = new ViewModelProvider(getActivity()).get(GyakorlatViewModel.class);
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
                    listView.setAdapter(new GyakorlatValaszto.ListItemAdapter(gyakorlats, getContext()));
                }
            });
        }
    }
}