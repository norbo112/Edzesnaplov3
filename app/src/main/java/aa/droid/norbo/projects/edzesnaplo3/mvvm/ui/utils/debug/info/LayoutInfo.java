package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.debug.info;

import android.util.Log;
import android.view.ViewGroup;

public class LayoutInfo {
    private static final String TAG = "LayoutInfo";
    public void layoutChildIndexis(ViewGroup viewGroup) {
        if(viewGroup.getChildCount() != 0) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                Log.i(TAG, "layoutChildIndexis: id=" + viewGroup.getChildAt(i).getId() + " index=" + i);
            }
        } else {
            Log.i(TAG, "layoutChildIndexis: nincsenek elemei a layoutnak");
        }
    }
}
