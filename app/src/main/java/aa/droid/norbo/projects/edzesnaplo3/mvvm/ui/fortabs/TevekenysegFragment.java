package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmActivityEdzesBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TevekenysegFragment extends Fragment {
    private MvvmActivityEdzesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MvvmActivityEdzesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
