package com.serify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.serify.components.NavigationStack
import com.serify.ui.theme.SerifyTheme
import com.serify.ui.theme.SerifyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SerifyTheme {
                NavigationStack()
            }
        }
    }
}