package br.com.dev360.gemisnap.feature.snap.hub.presentation

import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import br.com.dev360.gemisnap.core.shared.extensions.toBase64
import br.com.dev360.gemisnap.core.shared.util.BitmapDecoder
import br.com.dev360.gemisnap.feature.snap.BASE64_IMAGE
import br.com.dev360.gemisnap.feature.snap.ERROR_MESSAGE
import br.com.dev360.gemisnap.feature.snap.PROMPT
import br.com.dev360.gemisnap.feature.snap.R
import br.com.dev360.gemisnap.feature.snap.hub.domain.HubSnapContract
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class HubSnapUiModelImplTest {
    private val resources: Resources = mockk()
    private val decoder: BitmapDecoder = mockk()
    private val uri: Uri = mockk()

    private lateinit var uiModel: HubSnapContract.UiModel

    @Before
    fun setup() {
        uiModel = HubSnapUiModelImpl(resources, decoder)
        mockkStatic("br.com.dev360.gemisnap.core.shared.extensions.BitmapExtensionsKt")
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getDefaultPrompt should return custom prompt when it is not blank`() {
        val result = uiModel.getDefaultPrompt(PROMPT)

        assertEquals(PROMPT, result)

        verify(exactly = 0) { resources.getString(any()) }
    }

    @Test
    fun `getDefaultPrompt should return default resource string when prompt is blank`() {
        // Arrange
        val defaultString = "Default prompt from strings"
        every { resources.getString(R.string.default_prompt) } returns defaultString

        // Act
        val result = uiModel.getDefaultPrompt("   ")

        // Assert
        assertEquals(defaultString, result)
        verify(exactly = 1) { resources.getString(R.string.default_prompt) }
    }

    @Test
    fun `decodeAndConvertBase64 should return encoded string and recycle bitmap`() {
        val mockBitmap: Bitmap = mockk(relaxed = true)

        every { decoder.decode(uri) } returns mockBitmap
        every { mockBitmap.toBase64() } returns BASE64_IMAGE

        val result = uiModel.decodeAndConvertBase64(uri)

        assertEquals(BASE64_IMAGE, result)

        verify(exactly = 1) { mockBitmap.recycle() }
    }

    @Test(expected = RuntimeException::class)
    fun `decodeAndConvertBase64 should still recycle bitmap if conversion fails`() {
        val mockBitmap: Bitmap = mockk(relaxed = true)
        every { decoder.decode(uri) } returns mockBitmap
        every { mockBitmap.toBase64() } throws RuntimeException(ERROR_MESSAGE)

        try {
            uiModel.decodeAndConvertBase64(uri)
        } finally {
            verify(exactly = 1) { mockBitmap.recycle() }
        }
    }

}