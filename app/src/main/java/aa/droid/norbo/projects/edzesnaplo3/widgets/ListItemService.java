package aa.droid.norbo.projects.edzesnaplo3.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

import javax.inject.Inject;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.api.NaploRepository;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter;
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.NaploListUtil;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListItemService extends RemoteViewsService {
    @Inject
    NaploRepository naploRepository;

    @Inject
    ModelConverter modelConverter;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListItemViewFactory(this.getApplicationContext(), intent, naploRepository, modelConverter);
    }
}
