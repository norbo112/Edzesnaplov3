package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.uiactions;

import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import aa.droid.norbo.projects.edzesnaplo3.databinding.SzuperszettSorozatRogzitoLayoutBinding;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.SorozatDisplay;

/**
 * Amikor felveszünk egy gyakorlatot, akkor egy új View-t adok különféle elemekkel egy linearlayout
 * -hoz, és ezeket az elemeket kell külön vezérelnem, súly növelése, csökkentése stb
 */
public class SorozatRogzitoAction implements ActionsInterface {
    private final SzuperszettSorozatRogzitoLayoutBinding binding;

    public SorozatRogzitoAction(SzuperszettSorozatRogzitoLayoutBinding binding) {
        this.binding = binding;
    }

    @Override
    public GyakorlatUI getGyakorlatUI() {
        return binding.getGyakorlatUI();
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
