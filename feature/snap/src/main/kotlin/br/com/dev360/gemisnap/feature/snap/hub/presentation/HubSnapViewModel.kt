package br.com.dev360.gemisnap.feature.snap.hub.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.dev360.gemisnap.core.shared.coroutines.CustomDispatchers
import br.com.dev360.gemisnap.core.shared.extensions.empty
import br.com.dev360.gemisnap.feature.snap.hub.domain.HubSnapContract
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface HubSnapAction {
    data class ImageSelected(val uri: Uri) : HubSnapAction
    data class PromptChanged(val value: String) : HubSnapAction
    data object PrimaryActionClicked : HubSnapAction
    data object ClearRequested : HubSnapAction
}

data class HubSnapState(
    val prompt: String = String.empty(),
    val geminiText: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)

class HubSnapViewModel(
    private val repository: HubSnapContract.Repository,
    private val uiModel: HubSnapContract.UiModel,
    private val dispatchers: CustomDispatchers
) : ViewModel() {
    private val _uiState = MutableStateFlow(HubSnapState())
    val uiState = _uiState.asStateFlow()

    private var analysisJob: Job? = null
    private var selectedImageUri: Uri? = null

    fun onAction(action: HubSnapAction) {
        when (action) {
            is HubSnapAction.ImageSelected -> {
                clearResult()
                selectedImageUri = action.uri
            }

            is HubSnapAction.PromptChanged -> {
                _uiState.update { it.copy(prompt = action.value) }
            }

            HubSnapAction.PrimaryActionClicked -> {
                if (_uiState.value.geminiText.isNullOrBlank()) {
                    analyzeImage()
                } else {
                    clearAll()
                }
            }

            HubSnapAction.ClearRequested -> {
                clearAll()
            }
        }
    }

    fun analyzeImage() {
        val uri = selectedImageUri ?: return
        val prompt = uiModel.getDefaultPrompt(_uiState.value.prompt)

        analysisJob?.cancel()

        analysisJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            val imageResult = runCatching {
                withContext(dispatchers.io()) {
                    uiModel.decodeAndConvertBase64(uri)
                }
            }.mapCatching { base64Image ->
                repository.generateContent(prompt, base64Image)
            }

            imageResult.onSuccess { geminiText ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        geminiText = geminiText,
                        isError = false
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }

    private fun clearResult() {
        _uiState.update {
            it.copy(
                geminiText = null,
                isLoading = false,
                isError = false
            )
        }
    }

    private fun clearAll() {
        analysisJob?.cancel()
        selectedImageUri = null
        _uiState.value = HubSnapState()
    }
}