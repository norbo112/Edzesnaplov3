package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class BaseActiviry<T extends ViewDataBinding> extends AppCompatActivity {
    protected T binding;
    private int layoutResource;

    public BaseActiviry(int layoutResource) {
        this.layoutResource = layoutResource;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), layoutResource, null, false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(binding.getRoot());
    }
}
