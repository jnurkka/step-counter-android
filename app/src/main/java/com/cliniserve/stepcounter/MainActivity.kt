package com.cliniserve.stepcounter

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.content.Context;

class MainActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d("StepCounter", "Initiated app")
  }

  override fun onStart() {
    super.onStart()
    Log.d("StepCounter", "Started app")
  }

  override fun onResume() {
    super.onResume()
    Log.d("StepCounter", "Resumed app")
  }

  override fun onPause() {
    super.onPause()
    Log.d("StepCounter", "Paused app")
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