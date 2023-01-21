package com.example.mymediatube.ext

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mymediatube.App

val Fragment.activityCompat: AppCompatActivity get() = activity as AppCompatActivity

val Fragment.app: App get() = requireActivity().application as App