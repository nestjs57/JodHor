package com.pohnpawit.jodhor.feature.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pohnpawit.jodhor.R
import com.pohnpawit.jodhor.data.model.DormStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DormDetailScreen(
    dormId: Long,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onOpenPhoto: (Long) -> Unit,
    viewModel: DormDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val dorm = state.dorm

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(dorm?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = stringResource(R.string.action_edit),
                        )
                    }
                },
            )
        },
    ) { padding ->
        if (dorm == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                if (!state.isLoading) Text(stringResource(R.string.detail_not_found))
            }
            return@Scaffold
        }
        Column(Modifier.fillMaxWidth().padding(padding).padding(16.dp)) {
            Text(stringResource(R.string.status_label, stringResource(dorm.status.labelRes)))
            if (dorm.address.isNotBlank()) Text(dorm.address)
            dorm.priceMonthly?.let { Text(stringResource(R.string.price_monthly, it)) }
            if (dorm.notes.isNotBlank()) Text(dorm.notes)
        }
    }
}

@get:StringRes
private val DormStatus.labelRes: Int
    get() = when (this) {
        DormStatus.PLANNED -> R.string.status_planned
        DormStatus.VIEWED -> R.string.status_viewed
    }
