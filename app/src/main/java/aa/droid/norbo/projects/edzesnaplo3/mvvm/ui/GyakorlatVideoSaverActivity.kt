package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui

import aa.droid.norbo.projects.edzesnaplo3.R
import aa.droid.norbo.projects.edzesnaplo3.databinding.ActivityGyakorlatVideoSaverBinding
import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.GyakorlatUI
import aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.Gyakorlat
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.listadapters.GyakorlatItemAdapter
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils.ModelConverter
import aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.viewmodels.GyakorlatViewModel
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.stream.Collectors
import javax.inject.Inject

class GyakorlatVideoSaverActivity : AppCompatActivity() {
    private val TAG = "GyakorlatVideoSaver"

    @Inject lateinit var gyakorlatViewModel: GyakorlatViewModel
    @Inject lateinit var modelConverter: ModelConverter

    private lateinit var binding: ActivityGyakorlatVideoSaverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGyakorlatVideoSaverBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = resources.getString(R.string.app_name_v4)

        val link = intent.extras?.getString(Intent.EXTRA_TEXT)
        if(link != null)
            sharedVideoLinkSave(link)

        setContentView(binding.root)
    }

    fun sharedVideoLinkSave(link: String) {
        gyakorlatViewModel.gyakorlatForVideoLinkEdit.whenComplete { mutableList: MutableList<Gyakorlat>, throwable: Throwable ->
            if(throwable == null) {
                if(mutableList.isNotEmpty()) {
                    binding.gyakorlatVideoLink.text.append(link.subSequence(link.lastIndexOf('/') + 1, link.length).toString())
                    val gyaklista = mutableList.stream().map { m -> modelConverter.fromEntity(m) }
                            .collect(Collectors.toList())

                    binding.gyakSpinner.adapter = GyakorlatItemAdapter(gyaklista, this)
                    binding.spinIzomcsop.adapter = getIzomcsoportSpinnerAdapter(gyaklista)

                    binding.spinIzomcsop.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val gyakAdapter = binding.gyakSpinner.adapter as GyakorlatItemAdapter
                            val filter = gyakAdapter.filter
                            if (filter != null) {
                                var constraint = parent?.adapter?.getItem(position).toString()
                                if (constraint.equals("Válassz...")) constraint = ""
                                filter.filter(constraint)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {;
                        }
                    }

                    binding.gyakorlatVideoBtnMentesMegse.setOnClickListener { finish() }
                    binding.gyakorlatVideoBtnMentes.setOnClickListener {
                        val gyakorlatUI = binding.gyakSpinner.selectedItem as GyakorlatUI
                        if (gyakorlatUI.videolink != null && gyakorlatUI.videolink.length > 1) {
                            Toast.makeText(this, "Ehhez a gyakorlathoz már van mentve videó link!", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        if (!TextUtils.isEmpty(binding.gyakorlatVideoLink.text.toString()) && binding.gyakorlatVideoLink.text.toString().length > 5) {
                            gyakorlatUI.videolink = binding.gyakorlatVideoLink.text.toString()
                        } else {
                            Toast.makeText(this, "Videó azonosító hiányzik", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        if (!TextUtils.isEmpty(binding.gyakorlatVideoLinkPoz.text.toString())) {
                            gyakorlatUI.videostartpoz = binding.gyakorlatVideoLinkPoz.text.toString()
                        } else {
                            gyakorlatUI.videostartpoz = "0"
                        }

                        gyakorlatViewModel.update(modelConverter.fromUI(gyakorlatUI))
                        Toast.makeText(this, "Gyakorlat Videó mentve!", Toast.LENGTH_SHORT).show()
                        binding.gyakorlatVideoBtnMentes.isEnabled = false
                    }
                } else {
                    Toast.makeText(this, "Nincsenek gyakorlatok rögzítve", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Hiba lépett fel!", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "sharedVideoLinkSave: hiba", throwable)
            }

        }
    }

    fun getIzomcsoportSpinnerAdapter(gyaklista: List<GyakorlatUI>) : ArrayAdapter<String> {
        var lista = arrayListOf<String>("Válassz...")
        lista.addAll(gyaklista.stream().map { m -> m::getCsoport.toString() }
                .distinct().collect(Collectors.toList()))

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lista)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        return arrayAdapter
    }
}