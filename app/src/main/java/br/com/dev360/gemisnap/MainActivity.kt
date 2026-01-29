package br.com.dev360.gemisnap

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.dev360.gemisnap.core.sharedui.theme.GemiSnapTheme
import br.com.dev360.gemisnap.core.sharedui.theme.Typography
import br.com.dev360.gemisnap.core.sharedui.theme.dimens
import br.com.dev360.gemisnap.feature.snap.SnapActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            startActivity(Intent(this@MainActivity, SnapActivity::class.java))
        }
    }
}