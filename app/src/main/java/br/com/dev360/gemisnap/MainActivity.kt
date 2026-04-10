package br.com.dev360.gemisnap

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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