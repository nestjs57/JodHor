package com.pohnpawit.jodhor.feature.list

import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DormListScreen(
    onAddDorm: () -> Unit,
    onOpenDorm: (Long) -> Unit,
    viewModel: DormListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddDorm) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.action_add_dorm))
            }
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            FilterRow(
                selected = state.filter,
                onSelect = viewModel::setFilter,
            )

            if (state.dorms.isEmpty() && !state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.list_empty))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.dorms, key = { it.id }) { dorm ->
                        DormRow(
                            dorm = dorm,
                            onClick = { onOpenDorm(dorm.id) },
                            onFavorite = { viewModel.toggleFavorite(dorm.id, !dorm.isFavorite) },
                            onToggleStatus = { viewModel.toggleStatus(dorm.id, dorm.status) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    selected: DormListUiState.Filter,
    onSelect: (DormListUiState.Filter) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DormListUiState.Filter.entries.forEach { option ->
            FilterChip(
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(stringResource(option.labelRes)) },
            )
        }
    }
}

@get:StringRes
private val DormListUiState.Filter.labelRes: Int
    get() = when (this) {
        DormListUiState.Filter.ALL -> R.string.filter_all
        DormListUiState.Filter.PLANNED -> R.string.filter_planned
        DormListUiState.Filter.VIEWED -> R.string.filter_viewed
        DormListUiState.Filter.FAVORITES -> R.string.filter_favorites
    }
