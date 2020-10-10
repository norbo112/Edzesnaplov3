package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.edzesterv.utils;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.edzesterv.EdzesTervRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.EdzesTerv;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;

@Singleton
public class EdzesTervViewModel extends ViewModel {
    private EdzesTervRepository edzesTervRepository;
    private EdzesTerv edzesTerv;

    @Inject
    public EdzesTervViewModel(EdzesTervRepository edzesTervRepository) {
        this.edzesTervRepository = edzesTervRepository;
    }

    public void createEdzesTerv(String title) {
        this.edzesTerv = new EdzesTerv(title);
    }

    public boolean addEdzesnapForEdzesTerv(Edzesnap edzesnap) {
        if(edzesTerv.getEdzesnapList().contains(edzesnap)) {
            return false;
        }
        edzesTerv.addEdzesNap(edzesnap);
        return true;
    }

    public EdzesTerv getEdzesTerv() {
        return edzesTerv;
    }

    public void clear() {
        this.edzesTerv = null;
    }
}
