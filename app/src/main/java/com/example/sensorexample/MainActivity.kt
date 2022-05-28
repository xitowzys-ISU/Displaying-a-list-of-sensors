package com.example.sensorexample

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.sensorexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var sm: SensorManager

    val humanSensors = listOf(34, 31, 21)
    val envSensors = listOf(13, 5, 2, 14, 6, 12)
    val positionSensors = listOf(1, 35, 11, 15, 20, 9, 4, 16, 36, 30, 8, 17, 29, 19, 18, 10)


    private val sensorsList = mutableListOf<String>()
    lateinit var groupArray: List<*>

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        sm = getSystemService(SENSOR_SERVICE) as SensorManager
        groupArray = listOf(*resources.getStringArray(R.array.sensor_types_array))
        createAdapter()
        sensorListOnItemSelectedListener()
    }

    /**
     * Initializes the adapter of grouped services
     */
    private fun createAdapter() {
        ArrayAdapter.createFromResource(
            this,
            R.array.sensor_types_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }

    }

    private fun sensorListOnItemSelectedListener() {
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sensorsList.clear()

                val sensors = when (binding.spinner.adapter.getItem(position).toString()) {
                    groupArray[0] -> envSensors
                    groupArray[1] -> positionSensors
                    groupArray[2] -> humanSensors
                    else -> listOf()
                }

                sm.getSensorList(Sensor.TYPE_ALL).forEach {
                    if (it.type in sensors) {
                        sensorsList.add(it.name)
                    }
                }
                binding.sensorsTextView.text = sensorsList.joinToString("\n")

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}