package com.pohnpawit.jodhor.feature.list

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pohnpawit.jodhor.R
import com.pohnpawit.jodhor.core.designsystem.modifier.dashedBorder
import com.pohnpawit.jodhor.data.model.DormPreview
import com.pohnpawit.jodhor.data.model.DormStatus
import com.pohnpawit.jodhor.data.model.Photo
import java.io.File

private const val MAX_PREVIEW_SLOTS = 5

@Composable
fun DormRow(
    preview: DormPreview,
    onClick: () -> Unit,
    onFavorite: () -> Unit,
    onCycleStatus: () -> Unit,
) {
    val dorm = preview.dorm
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = dorm.name,
                            style = MaterialTheme.typography.titleMedium,
                            textDecoration = if (dorm.isFull) TextDecoration.LineThrough else null,
                            color = if (dorm.isFull) MaterialTheme.colorScheme.onSurfaceVariant
                                    else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f, fill = false),
                        )
                        if (dorm.isFull) {
                            Spacer(Modifier.size(6.dp))
                            FullBadge()
                        }
                    }
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
                IconButton(onClick = onCycleStatus) {
                    Icon(
                        imageVector = dorm.status.icon,
                        contentDescription = stringResource(R.string.action_cycle_status),
                        tint = dorm.status.tint(),
                    )
                }
                IconButton(onClick = onFavorite) {
                    Icon(
                        imageVector = if (dorm.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = stringResource(
                            if (dorm.isFavorite) R.string.action_unfavorite else R.string.action_favorite
                        ),
                    )
                }
            }

            PhotoStrip(photos = preview.photos)
        }
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

private val DormStatus.icon: ImageVector
    get() = when (this) {
        DormStatus.NOT_CONTACTED -> Icons.Outlined.Circle
        DormStatus.CONTACTED -> Icons.Filled.Phone
        DormStatus.VIEWED -> Icons.Filled.CheckCircle
    }

@Composable
private fun DormStatus.tint(): Color = when (this) {
    DormStatus.NOT_CONTACTED -> MaterialTheme.colorScheme.outline
    DormStatus.CONTACTED -> MaterialTheme.colorScheme.tertiary
    DormStatus.VIEWED -> MaterialTheme.colorScheme.primary
}

@get:StringRes
val DormStatus.labelRes: Int
    get() = when (this) {
        DormStatus.NOT_CONTACTED -> R.string.status_not_contacted
        DormStatus.CONTACTED -> R.string.status_contacted
        DormStatus.VIEWED -> R.string.status_viewed
    }

private sealed interface PreviewSlot {
    data class PhotoSlot(val photo: Photo) : PreviewSlot
    data object AddSlot : PreviewSlot
    data class MoreSlot(val extraCount: Int) : PreviewSlot
}

private fun previewSlots(photos: List<Photo>): List<PreviewSlot> = when {
    photos.isEmpty() -> listOf(PreviewSlot.AddSlot)
    photos.size < MAX_PREVIEW_SLOTS ->
        photos.map { PreviewSlot.PhotoSlot(it) } + PreviewSlot.AddSlot
    photos.size == MAX_PREVIEW_SLOTS ->
        photos.map { PreviewSlot.PhotoSlot(it) }
    else -> buildList {
        photos.take(MAX_PREVIEW_SLOTS - 1).forEach { add(PreviewSlot.PhotoSlot(it)) }
        add(PreviewSlot.MoreSlot(photos.size - (MAX_PREVIEW_SLOTS - 1)))
    }
}

@Composable
private fun PhotoStrip(photos: List<Photo>) {
    val slots = previewSlots(photos)
    val last = slots.last()
    val lastPhoto = if (last is PreviewSlot.MoreSlot) photos[MAX_PREVIEW_SLOTS - 1] else null

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        slots.forEach { slot ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
            ) {
                when (slot) {
                    is PreviewSlot.PhotoSlot -> PreviewPhoto(slot.photo)
                    PreviewSlot.AddSlot -> PreviewAdd()
                    is PreviewSlot.MoreSlot -> PreviewMore(slot.extraCount, lastPhoto)
                }
            }
        }
        repeat(MAX_PREVIEW_SLOTS - slots.size) {
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun PreviewPhoto(photo: Photo) {
    AsyncImage(
        model = File(photo.filePath),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    )
}

@Composable
private fun PreviewAdd() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .dashedBorder(
                color = MaterialTheme.colorScheme.outline,
                cornerRadius = 8.dp,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_photo),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun PreviewMore(extraCount: Int, basePhoto: Photo?) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (basePhoto != null) {
            AsyncImage(
                model = File(basePhoto.filePath),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "+$extraCount",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
