package aa.droid.norbo.projects.edzesnaplo3.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListItemService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListItemViewFactory(this.getApplicationContext(), intent);
    }
}
