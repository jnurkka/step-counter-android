package com.cliniserve.stepcounter

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.content.Context;
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.lang.Math.sqrt

class MainActivity : Activity(), SensorEventListener {
  private var mSensorManager : SensorManager ?= null
  private var mAccelerometer : Sensor ?= null
    val sensorFilteredList = mutableListOf(0.0)

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      Log.d("StepCounter", "Initiated app")

      mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
      mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  }

  override fun onSensorChanged(event: SensorEvent?) {
      if (event != null) {
          Log.d("rawX", event.values[0].toString())
          var mag : Double = calcMagnitude(event.values[0].toDouble(), event.values[1].toDouble(), event.values[2].toDouble())
          Log.d("rawMag", mag.toString())

          var filteredVal : Double = filter(sensorFilteredList.last(), mag)
          sensorFilteredList.add(filteredVal)
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
