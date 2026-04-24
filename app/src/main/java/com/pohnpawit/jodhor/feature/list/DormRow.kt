package com.pohnpawit.jodhor.feature.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pohnpawit.jodhor.R
import com.pohnpawit.jodhor.data.model.DormPreview
import com.pohnpawit.jodhor.data.model.DormStatus
import com.pohnpawit.jodhor.data.model.Photo
import java.io.File

@Composable
fun DormRow(
    preview: DormPreview,
    onClick: () -> Unit,
    onToggleViewed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dorm = preview.dorm
    val cover = preview.photos.firstOrNull()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        if (cover != null) {
            CoverPhoto(photo = cover)
            Spacer(Modifier.height(12.dp))
        }
        Row(
            modifier = Modifier.padding(horizontal = 2.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dorm.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (dorm.isFull) TextDecoration.LineThrough else null,
                        color = if (dorm.isFull) MaterialTheme.colorScheme.onSurfaceVariant
                                else MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    if (dorm.isFull) {
                        Spacer(Modifier.width(8.dp))
                        FullBadge()
                    }
                }
                dorm.priceMonthly?.let {
                    Text(
                        text = stringResource(R.string.price_monthly, it),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            IconButton(
                onClick = onToggleViewed,
                modifier = Modifier.offset(y = (-8).dp),
            ) {
                val viewed = dorm.status == DormStatus.VIEWED
                Icon(
                    imageVector = if (viewed) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                    contentDescription = stringResource(R.string.action_toggle_viewed),
                    tint = if (viewed) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

@Composable
private fun CoverPhoto(photo: Photo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f)
            .clip(RoundedCornerShape(14.dp)),
    ) {
        AsyncImage(
            model = File(photo.filePath),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEFEFEF)),
        )
    }
}

@Composable
private fun FullBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(horizontal = 6.dp, vertical = 2.dp),
    ) {
        Text(
            text = stringResource(R.string.badge_full),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onErrorContainer,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
