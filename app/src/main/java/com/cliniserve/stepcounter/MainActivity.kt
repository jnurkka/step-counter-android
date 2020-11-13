package com.cliniserve.stepcounter

import android.os.Bundle
import android.app.Activity;

class MainActivity() {

val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)


}