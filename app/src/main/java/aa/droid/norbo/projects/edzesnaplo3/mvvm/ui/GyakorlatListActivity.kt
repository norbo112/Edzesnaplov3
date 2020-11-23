package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui

import aa.droid.norbo.projects.edzesnaplo3.databinding.ActivityGyakorlatListBinding
import aa.droid.norbo.projects.edzesnaplo3.widgets.withhilt.EdzesnaploWidget
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter

class GyakorlatListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGyakorlatListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGyakorlatListBinding.inflate(layoutInflater)

        val gyakorlatAdatok = intent.extras?.get(EdzesnaploWidget.ADATOK_NAPLO) as String

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
            gyakorlatAdatok.split(","))
        binding.listView.adapter = adapter

        setContentView(binding.root)
    }
}