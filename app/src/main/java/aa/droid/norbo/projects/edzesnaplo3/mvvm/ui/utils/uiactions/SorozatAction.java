package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.uiactions;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.fortabs.SorozatDisplay;

public class SorozatAction {
    private final ActionsInterface anInterface;

    public SorozatAction(ActionsInterface anInterface) {
        this.anInterface = anInterface;
    }

    public void increaseSorozatSuly() {
        SorozatDisplay sorozatUI = anInterface.getSorozatUI();
        int suly = Integer.parseInt(sorozatUI.getSuly());
        if (anInterface.getSwitchCompat() != null && anInterface.getSwitchCompat().isChecked())
            suly += 10;
        else
            suly += 2;
        anInterface.getEtSulyView().setText(Integer.toString(suly));
    }

    public void decreaseSorozatSuly() {
        SorozatDisplay sorozatUI = anInterface.getSorozatUI();
        int suly = Integer.parseInt(sorozatUI.getSuly());
        if(anInterface.getSwitchCompat() != null && anInterface.getSwitchCompat().isChecked())
            suly -= 10;
        else
            suly -= 2;
        if(suly < 0) suly = 0;
        anInterface.getEtSulyView().setText(Integer.toString(suly));
    }

    public void increaseSorozatIsm() {
        SorozatDisplay sorozatUI = anInterface.getSorozatUI();
        int ism = Integer.parseInt(sorozatUI.getIsm());
        if(anInterface.getSwitchCompat() != null && anInterface.getSwitchCompat().isChecked())
            ism += 10;
        else
            ism += 1;
        anInterface.getEtIsmView().setText(Integer.toString(ism));
    }

    public void decreaseSorozatIsm() {
        SorozatDisplay sorozatUI = anInterface.getSorozatUI();
        int ism = Integer.parseInt(sorozatUI.getIsm());
        if(anInterface.getSwitchCompat()!= null && anInterface.getSwitchCompat().isChecked())
            ism -= 10;
        else
            ism -= 1;
        if(ism < 0) ism = 0;
        anInterface.getEtIsmView().setText(Integer.toString(ism));
    }
}
