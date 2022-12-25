package com.example.mymediatube.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

val Fragment.activityCompat: AppCompatActivity get() = activity as AppCompatActivity