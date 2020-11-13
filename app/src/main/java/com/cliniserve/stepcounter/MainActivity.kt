package com.cliniserve.stepcounter

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.view.View
import android.widget.EditText
import java.lang.Math.sqrt
import java.util.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.jjoe64.graphview.GraphView


class MainActivity :  AppCompatActivity(), SensorEventListener {
  private var mSensorManager : SensorManager ?= null
  private var mAccelerometer : Sensor ?= null
    val sensorFilteredList = mutableListOf(Pair(0.0, Calendar.getInstance().timeInMillis))

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main);

      Log.d("StepCounter", "Initiated app")

      mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
      mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  }

  override fun onSensorChanged(event: SensorEvent?, callback) {
      if (event != null) {

          //Get time for timestamp
          val currentTime: Long = Calendar.getInstance().timeInMillis

          // Calc magnitude from 3 axis
          var mag : Double = calcMagnitude(event.values[0].toDouble(), event.values[1].toDouble(), event.values[2].toDouble()) - 9.81
          // Filter magnitude
          var filteredVal : Double = filter(sensorFilteredList.last().first, mag)
          // add result to list
          sensorFilteredList.add(Pair(filteredVal , currentTime))

          val textView: TextView = findViewById(R.id.textView) as TextView
          textView.setText(filteredVal.toString())

          val GraphView: GraphView = findViewById(R.id.graph) as GraphView



          Log.d("filteredMag", sensorFilteredList.last().toString())
      }
  }

  override fun onStart() {
    super.onStart()
    Log.d("StepCounter", "Started app")
  }

  override fun onResume() {
    super.onResume()
    Log.d("StepCounter", "Resumed app")
    mSensorManager!!.registerListener(this,mAccelerometer,
            SensorManager.SENSOR_DELAY_GAME)
  }

  override fun onPause() {
      super.onPause()
      Log.d("StepCounter", "Paused app")
      mSensorManager!!.unregisterListener(this)
  }

  override fun onStop() {
    super.onStop()
    Log.d("StepCounter", "Stopped app")
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.d("StepCounter", "Destroyed app")
  }
}

fun calcMagnitude(x : Double, y : Double, z: Double) : Double {
    var Magnitude : Double = sqrt (x * x + y * y + z * z)
    return Magnitude
}

fun filter(m_old: Double , m_new : Double) : Double {
    var ReturnVal : Double = 0.9 * m_old + 0.1 * m_new
    return ReturnVal
}
