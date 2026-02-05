package com.ringelrangel.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ringelrangel.app.ui.RingelrangelApp
import com.ringelrangel.app.ui.theme.RingelrangelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RingelrangelTheme {
                RingelrangelApp()
            }
        }
    }
}
