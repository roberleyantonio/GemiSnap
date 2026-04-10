package br.com.dev360.gemisnap.feature.snap.hub.presentation.components

import android.R
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import br.com.dev360.gemisnap.core.sharedui.theme.GemiSnapTheme
import org.junit.Rule
import org.junit.Test

class ImageCardPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar"
    )

    @Test
    fun shouldRenderEmptyState() {
        paparazzi.snapshot {
            GemiSnapTheme {
                ImageCard(
                    model = null,
                    isLoading = false,
                    onClick = {}
                )
            }
        }
    }

    @Test
    fun shouldRenderImageState() {
        paparazzi.snapshot {
            GemiSnapTheme {
                ImageCard(
                    model = R.drawable.ic_menu_gallery,
                    isLoading = false,
                    onClick = {}
                )
            }
        }
    }

    @Test
    fun shouldRenderLoadingState() {
        paparazzi.snapshot {
            GemiSnapTheme {
                ImageCard(
                    model = R.drawable.ic_menu_gallery,
                    isLoading = true,
                    onClick = {}
                )
            }
        }
    }
}