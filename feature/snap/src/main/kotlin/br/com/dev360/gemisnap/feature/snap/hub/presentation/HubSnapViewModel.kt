package br.com.dev360.gemisnap.feature.snap.hub.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.dev360.gemisnap.core.shared.coroutines.CustomDispatchers
import br.com.dev360.gemisnap.feature.snap.hub.domain.HubSnapContract
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface HubSnapAction {
    data class ImageSelected(val uri: Uri) : HubSnapAction
    data class PrimaryActionClicked(val prompt: String) : HubSnapAction
    data object RetryRequested : HubSnapAction
    data object ClearRequested : HubSnapAction
}

data class HubSnapState(
    val geminiText: String? = null,
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HubSnapViewModel(
    private val repository: HubSnapContract.Repository,
    private val uiModel: HubSnapContract.UiModel,
    private val dispatchers: CustomDispatchers
) : ViewModel() {
    private var lastPrompt: String = ""

    private val _uiState = MutableStateFlow(HubSnapState())
    val uiState = _uiState.asStateFlow()

    private var analysisJob: Job? = null

    fun onAction(action: HubSnapAction) {
        when (action) {
            is HubSnapAction.ImageSelected -> {
                clearResult()
                _uiState.update {
                    it.copy(
                        selectedImageUri = action.uri,
                    )
                }
            }
            is HubSnapAction.PrimaryActionClicked -> {
                if (_uiState.value.geminiText.isNullOrBlank()) {
                    lastPrompt = action.prompt
                    analyzeImage()
                } else {
                    clearAll()
                }
            }
            HubSnapAction.RetryRequested -> analyzeImage()
            HubSnapAction.ClearRequested -> clearAll()
        }
    }

    fun analyzeImage() {
        val uri = _uiState.value.selectedImageUri ?: return

        analysisJob?.cancel()

        analysisJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val base64Image = withContext(dispatchers.io()) {
                runCatching { uiModel.decodeAndConvertBase64(uri) }.getOrNull()
            }

            if (base64Image == null) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = uiModel.getDecodeImageErrorMessage())
                }
                return@launch
            }

            repository.generateContent(lastPrompt, base64Image)
                .withSuccessAndError({ geminiText ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            geminiText = geminiText,
                            errorMessage = null
                        )
                    }
                }, { failure ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = uiModel.getErrorMessage(failure)
                        )
                    }
                })
        }
    }

    fun closeFeedbackError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun clearResult() {
        _uiState.update {
            it.copy(
                geminiText = null,
                isLoading = false,
                errorMessage = null
            )
        }
    }

    private fun clearAll() {
        analysisJob?.cancel()
        lastPrompt = ""
        _uiState.value = HubSnapState()
    }
}