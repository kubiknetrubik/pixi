package com.example.vk

import android.app.Application
import com.example.vk.data.MainDb

class App: Application() {
    val database by lazy { MainDb.createDataBase(this) }
}