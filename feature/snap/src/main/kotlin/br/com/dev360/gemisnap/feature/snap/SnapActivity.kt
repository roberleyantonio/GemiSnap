package br.com.dev360.gemisnap.feature.snap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.dev360.gemisnap.core.sharedui.theme.GemiSnapTheme
import br.com.dev360.gemisnap.feature.snap.hub.presentation.HubSnapScreen

class SnapActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            GemiSnapTheme {
                HubSnapScreen()
            }
        }
    }
}