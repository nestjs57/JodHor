package com.pohnpawit.jodhor.feature.adddorm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pohnpawit.jodhor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDormScreen(
    dormId: Long?,
    onDone: () -> Unit,
    onBack: () -> Unit,
    viewModel: AddDormViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(if (state.isNew) R.string.add_title else R.string.edit_title))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.save(onDone) }, enabled = state.canSave) {
                        Text(stringResource(R.string.action_save))
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                label = { Text(stringResource(R.string.field_name)) },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = state.address,
                onValueChange = viewModel::onAddressChange,
                label = { Text(stringResource(R.string.field_address)) },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = state.priceMonthly,
                onValueChange = viewModel::onPriceChange,
                label = { Text(stringResource(R.string.field_price)) },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = state.contactPhone,
                onValueChange = viewModel::onPhoneChange,
                label = { Text(stringResource(R.string.field_phone)) },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = state.mapUrl,
                onValueChange = viewModel::onMapUrlChange,
                label = { Text(stringResource(R.string.field_map_url)) },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = state.notes,
                onValueChange = viewModel::onNotesChange,
                label = { Text(stringResource(R.string.field_notes)) },
                minLines = 3,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
