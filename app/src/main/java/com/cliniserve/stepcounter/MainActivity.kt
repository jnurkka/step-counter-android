package com.cliniserve.stepcounter

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.DataPointInterface
import com.jjoe64.graphview.series.LineGraphSeries
import java.lang.Math.sqrt
import java.util.*




class MainActivity :  AppCompatActivity(), SensorEventListener {
  private var mSensorManager : SensorManager ?= null
  private var mAccelerometer : Sensor ?= null
    var sensorFilteredList = mutableListOf(Pair(0.0, Calendar.getInstance().time))
    var isPeak : Boolean = false
    var stepCount = mutableListOf(Calendar.getInstance().time)
    var mSeries2 = LineGraphSeries<DataPointInterface>()
    var loopCounter : Int = 0


  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main);

      Log.d("StepCounter", "Initiated app")
      mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
      mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  }

    fun serachStep()
    {
        // Function to identify single steps
        if(sensorFilteredList.last().first > 0.5 || sensorFilteredList.last().first < -0.5)
        {
            if(isPeak == false)
            {
                isPeak = true
                stepCount.add(Calendar.getInstance().time)
                // HERE STEP EVENT

                val textView: TextView = findViewById(R.id.textView) as TextView
                textView.setText(stepCount.size.toString())
            }
        }
        else if ((Calendar.getInstance().time.time - stepCount.last().time) > 300)
        {
            isPeak = false
        }
    }

  override fun onSensorChanged(event: SensorEvent?) {
      if (event != null) {

          //Get time for timestamp
          val currentTime: Date = Calendar.getInstance().time
          // Calc magnitude from 3 axis
          var mag : Double = calcMagnitude(event.values[0].toDouble(), event.values[1].toDouble(), event.values[2].toDouble()) - 9.81
          // Filter magnitude
          var filteredVal : Double = filter(sensorFilteredList.last().first, mag)
          // add result to list
          sensorFilteredList.add(Pair(filteredVal , currentTime))
          //Keep list clean
          if(sensorFilteredList.size > 1000)
          {
              sensorFilteredList.removeFirst()
          }

          // Plot Graph
          loopCounter = loopCounter +1
          if(loopCounter == 3) {
              val GraphView: GraphView = findViewById(R.id.graph) as GraphView
              GraphView.getViewport().setXAxisBoundsManual(true);
              GraphView.getViewport().setMinX(currentTime.time.toDouble() - 20000);
              GraphView.getViewport().setMaxX(currentTime.time.toDouble());
              mSeries2.appendData(DataPoint(currentTime, filteredVal.toDouble()), true, 100)
              GraphView.removeAllSeries()
              GraphView.addSeries(mSeries2)
              loopCounter = 0
          }

          serachStep()

      }

  }



  override fun onStart() {
    super.onStart()
    Log.d("StepCounter", "Started app")
      mSensorManager!!.registerListener(this,mAccelerometer,
          SensorManager.SENSOR_DELAY_NORMAL)
  }

  override fun onResume() {
    super.onResume()
    Log.d("StepCounter", "Resumed app")
    //mSensorManager!!.registerListener(this,mAccelerometer,
           // SensorManager.SENSOR_DELAY_NORMAL)
  }

  override fun onPause() {
      super.onPause()
      Log.d("StepCounter", "Paused app")
      //mSensorManager!!.unregisterListener(this)
  }

  override fun onStop() {
    super.onStop()
    Log.d("StepCounter", "Stopped app")
     // mSensorManager!!.unregisterListener(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.d("StepCounter", "Destroyed app")
  }

}


fun calcMagnitude(x : Double, y : Double, z: Double) : Double {
    //Calculates the magnitude of a x,y & z vector
    var Magnitude : Double = sqrt (x * x + y * y + z * z)
    return Magnitude
}

fun filter(m_old: Double , m_new : Double) : Double {
    //Low Pass filter
    var ReturnVal : Double = 0.8 * m_old + 0.2 * m_new
    return ReturnVal
}


