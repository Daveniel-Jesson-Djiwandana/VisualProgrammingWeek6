package com.davenieljessondjiwandana.assignmentweek6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import android.graphics.Color as AndroidColor
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.davenieljessondjiwandana.assignmentweek6.navigation.AppNavHost
import com.davenieljessondjiwandana.assignmentweek6.ui.theme.AssignmentWeek6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT)
        )
        setContent {
            AssignmentWeek6Theme {
                AppNavHost()
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AssignmentWeek6Theme {
        AppNavHost()
    }
}