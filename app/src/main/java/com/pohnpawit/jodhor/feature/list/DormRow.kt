package com.pohnpawit.jodhor.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pohnpawit.jodhor.R
import com.pohnpawit.jodhor.data.model.Dorm
import com.pohnpawit.jodhor.data.model.DormStatus

@Composable
fun DormRow(
    dorm: Dorm,
    onClick: () -> Unit,
    onFavorite: () -> Unit,
    onToggleStatus: () -> Unit,
) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(dorm.name, style = MaterialTheme.typography.titleMedium)
                if (dorm.address.isNotBlank()) {
                    Text(dorm.address, style = MaterialTheme.typography.bodySmall)
                }
                dorm.priceMonthly?.let {
                    Text(
                        stringResource(R.string.price_monthly, it),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            IconButton(onClick = onToggleStatus) {
                val viewed = dorm.status == DormStatus.VIEWED
                Icon(
                    imageVector = if (viewed) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                    contentDescription = stringResource(R.string.action_toggle_viewed),
                )
            }
            IconButton(onClick = onFavorite) {
                Icon(
                    imageVector = if (dorm.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(R.string.action_favorite),
                )
            }
        }
    }
}
