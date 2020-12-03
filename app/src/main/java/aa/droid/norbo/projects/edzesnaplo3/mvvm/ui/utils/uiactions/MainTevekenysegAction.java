package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.uiactions;

import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;

import aa.droid.norbo.projects.edzesnaplo3.databinding.MvvmEdzesNezetBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.SorozatDisplay;

/**
 * Fő gyakorlat nézeten lévő gombok vezérlése, külön osztály, csak hogy egységes legyen, vagyis
 * ne legyen minden kód egy helyen
 */
public class MainTevekenysegAction implements ActionsInterface {
    private final MvvmEdzesNezetBinding binding;

    public MainTevekenysegAction(MvvmEdzesNezetBinding binding) {
        this.binding = binding;
    }

    @Override
    public GyakorlatUI getGyakorlatUI() {
        return null;
    }

    @Override
    public SwitchCompat getSwitchCompat() {
        return binding.plusz10switch;
    }

    @Override
    public SorozatDisplay getSorozatUI() {
        return binding.getSorozatUI();
    }

    @Override
    public TextView getEtSulyView() {
        return binding.etSuly;
    }

    @Override
    public TextView getEtIsmView() {
        return binding.etIsm;
    }
}
