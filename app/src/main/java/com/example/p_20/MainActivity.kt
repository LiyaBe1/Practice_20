package com.example.p_20
import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var gyroscope: Sensor? = null
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var gyroscopeDataTextView: TextView
    private var isRunning = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        gyroscopeDataTextView = findViewById(R.id.gyroscopeDataTextView)
    }

    fun startButtonClick(view: View) {
        isRunning = true
        startButton.isEnabled = false
        stopButton.isEnabled = true
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopButtonClick(view: View) {
        isRunning = false
        startButton.isEnabled = true
        stopButton.isEnabled = false
        sensorManager.unregisterListener(this, gyroscope)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Не используется
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            runOnUiThread {
                gyroscopeDataTextView.text = "X: $x\nY: $y\nZ: $z"
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (isRunning) {
            sensorManager.unregisterListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isRunning) {
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}