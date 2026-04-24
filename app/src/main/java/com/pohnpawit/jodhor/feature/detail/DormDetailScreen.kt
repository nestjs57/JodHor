package com.pohnpawit.jodhor.feature.detail

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.pohnpawit.jodhor.R
import com.pohnpawit.jodhor.core.designsystem.modifier.dashedBorder
import com.pohnpawit.jodhor.core.util.formatThaiPhone
import com.pohnpawit.jodhor.data.model.Dorm
import com.pohnpawit.jodhor.data.model.DormStatus
import com.pohnpawit.jodhor.data.model.PhoneContact
import com.pohnpawit.jodhor.data.model.Photo
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState
import java.io.File

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

    var showAddPhotoSheet by remember { mutableStateOf(false) }
    var showDeleteDormDialog by remember { mutableStateOf(false) }
    var photoPendingDelete by remember { mutableStateOf<Photo?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success -> viewModel.onCameraResult(success) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10),
    ) { uris -> viewModel.onPickedFromGallery(uris) }

    var localPhotos by remember { mutableStateOf(state.photos) }
    LaunchedEffect(state.photos) {
        if (localPhotos.map { it.id } != state.photos.map { it.id }) {
            localPhotos = state.photos
        }
    }

    val lazyGridState = rememberLazyGridState()
    val reorderableState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        val fromId = from.key as? Long ?: return@rememberReorderableLazyGridState
        val toId = to.key as? Long ?: return@rememberReorderableLazyGridState
        val updated = localPhotos.toMutableList()
        val fromIdx = updated.indexOfFirst { it.id == fromId }
        val toIdx = updated.indexOfFirst { it.id == toId }
        if (fromIdx < 0 || toIdx < 0) return@rememberReorderableLazyGridState
        updated.add(toIdx, updated.removeAt(fromIdx))
        localPhotos = updated
        viewModel.reorderPhotos(updated.map { it.id })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = dorm?.name ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (dorm?.isFull == true) TextDecoration.LineThrough else null,
                    )
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
                    if (dorm != null) {
                        IconButton(onClick = viewModel::toggleFull) {
                            Icon(
                                imageVector = Icons.Filled.Block,
                                contentDescription = stringResource(
                                    if (dorm.isFull) R.string.action_unmark_full
                                    else R.string.action_mark_full
                                ),
                                tint = if (dorm.isFull) MaterialTheme.colorScheme.error
                                       else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        IconButton(onClick = viewModel::toggleFavorite) {
                            Icon(
                                imageVector = if (dorm.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = stringResource(
                                    if (dorm.isFavorite) R.string.action_unfavorite else R.string.action_favorite
                                ),
                            )
                        }
                        IconButton(onClick = onEdit) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = stringResource(R.string.action_edit),
                            )
                        }
                        IconButton(onClick = { showDeleteDormDialog = true }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = stringResource(R.string.action_delete),
                            )
                        }
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

        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                StatusChip(status = dorm.status, onClick = viewModel::cycleStatus)
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                SectionHeader(stringResource(R.string.section_photos))
            }
            val effectiveCoverId = dorm.coverPhotoId ?: localPhotos.firstOrNull()?.id
            items(localPhotos, key = { it.id }) { photo ->
                ReorderableItem(reorderableState, key = photo.id) { isDragging ->
                    PhotoTile(
                        photo = photo,
                        isCover = photo.id == effectiveCoverId,
                        isDragging = isDragging,
                        dragHandle = Modifier.longPressDraggableHandle(),
                        onClick = { onOpenPhoto(photo.id) },
                        onDelete = { photoPendingDelete = photo },
                    )
                }
            }
            item(key = "add_photo") {
                AddPhotoTile(onClick = { showAddPhotoSheet = true })
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.height(8.dp))
                SectionHeader(stringResource(R.string.section_details))
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                DetailSection(dorm = dorm, phones = state.phones)
            }
        }
    }

    if (showAddPhotoSheet) {
        AddPhotoSheet(
            onDismiss = { showAddPhotoSheet = false },
            onCamera = {
                showAddPhotoSheet = false
                cameraLauncher.launch(viewModel.prepareCameraCapture())
            },
            onGallery = {
                showAddPhotoSheet = false
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
        )
    }

    if (showDeleteDormDialog) {
        ConfirmDialog(
            title = stringResource(R.string.delete_dorm_title),
            message = stringResource(R.string.delete_dorm_message),
            onDismiss = { showDeleteDormDialog = false },
            onConfirm = {
                showDeleteDormDialog = false
                viewModel.deleteDorm(onBack)
            },
        )
    }

    photoPendingDelete?.let { photo ->
        ConfirmDialog(
            title = stringResource(R.string.delete_photo_title),
            message = stringResource(R.string.delete_photo_message),
            onDismiss = { photoPendingDelete = null },
            onConfirm = {
                photoPendingDelete = null
                viewModel.deletePhoto(photo.id)
            },
        )
    }
}

@Composable
private fun StatusChip(status: DormStatus, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        leadingIcon = { Icon(status.icon, contentDescription = null) },
        label = { Text(stringResource(status.labelRes)) },
        colors = AssistChipDefaults.assistChipColors(
            leadingIconContentColor = status.tint(),
        ),
    )
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun AddPhotoTile(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .dashedBorder(
                color = MaterialTheme.colorScheme.outline,
                cornerRadius = 12.dp,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_photo),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun PhotoTile(
    photo: Photo,
    isCover: Boolean,
    isDragging: Boolean,
    dragHandle: Modifier,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val scale by animateFloatAsState(if (isDragging) 1.05f else 1f, label = "tileScale")
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .then(dragHandle),
    ) {
        AsyncImage(
            model = File(photo.filePath),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().clickable(onClick = onClick),
        )
        if (isCover) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.92f))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(
                    text = stringResource(R.string.badge_cover),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(onClick = onDelete),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.delete_photo),
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@Composable
private fun DetailSection(dorm: Dorm, phones: List<PhoneContact>) {
    val context = LocalContext.current
    Column {
        if (dorm.address.isNotBlank()) {
            DetailRow(
                leading = { Icon(Icons.Filled.LocationOn, null) },
                title = stringResource(R.string.field_address),
                value = dorm.address,
                onClick = if (dorm.mapUrl.isBlank()) {
                    {
                        val geo = "geo:0,0?q=${Uri.encode(dorm.address)}".toUri()
                        context.startActivity(Intent(Intent.ACTION_VIEW, geo))
                    }
                } else null,
            )
        }
        dorm.priceMonthly?.let {
            DetailRow(
                leading = { Icon(Icons.Filled.Payments, null) },
                title = stringResource(R.string.field_price),
                value = stringResource(R.string.price_monthly, it),
            )
        }
        dorm.securityDeposit?.let {
            DetailRow(
                leading = { Icon(Icons.Filled.Payments, null) },
                title = stringResource(R.string.field_deposit),
                value = stringResource(R.string.amount_baht, it),
            )
        }
        dorm.advancePayment?.let {
            DetailRow(
                leading = { Icon(Icons.Filled.Payments, null) },
                title = stringResource(R.string.field_advance),
                value = stringResource(R.string.amount_baht, it),
            )
        }
        dorm.contractYears?.let {
            DetailRow(
                leading = { Icon(Icons.Filled.CalendarMonth, null) },
                title = stringResource(R.string.field_contract_years),
                value = stringResource(R.string.contract_years_value, it),
            )
        }
        phones.forEach { phone ->
            val raw = phone.number.filter(Char::isDigit)
            DetailRow(
                leading = { Icon(Icons.Filled.Phone, null) },
                title = phone.note.ifBlank { stringResource(R.string.field_phone_number) },
                value = formatThaiPhone(phone.number),
                onClick = {
                    context.startActivity(
                        Intent(Intent.ACTION_DIAL, "tel:$raw".toUri())
                    )
                },
            )
        }
        if (dorm.mapUrl.isNotBlank()) {
            MapLinkRow(
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, dorm.mapUrl.toUri()))
                },
            )
        }
        if (dorm.notes.isNotBlank()) {
            DetailRow(
                leading = { Icon(Icons.AutoMirrored.Filled.Notes, null) },
                title = stringResource(R.string.field_notes),
                value = dorm.notes,
            )
        }
    }
}

@Composable
private fun DetailRow(
    leading: @Composable () -> Unit,
    title: String,
    value: String,
    onClick: (() -> Unit)? = null,
) {
    Column {
        ListItem(
            leadingContent = leading,
            headlineContent = { Text(title, style = MaterialTheme.typography.labelMedium) },
            supportingContent = { Text(value, style = MaterialTheme.typography.bodyLarge) },
            modifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
        )
        HorizontalDivider()
    }
}

@Composable
private fun MapLinkRow(onClick: () -> Unit) {
    Column {
        ListItem(
            leadingContent = {
                Icon(Icons.Filled.Map, null, tint = MaterialTheme.colorScheme.primary)
            },
            headlineContent = {
                Text(
                    stringResource(R.string.action_open_in_maps),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            modifier = Modifier.clickable(onClick = onClick),
        )
        HorizontalDivider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPhotoSheet(
    onDismiss: () -> Unit,
    onCamera: () -> Unit,
    onGallery: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(Modifier.padding(bottom = 16.dp)) {
            Text(
                text = stringResource(R.string.sheet_add_photo_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.PhotoCamera, null) },
                headlineContent = { Text(stringResource(R.string.action_camera)) },
                modifier = Modifier.clickable(onClick = onCamera),
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.PhotoLibrary, null) },
                headlineContent = { Text(stringResource(R.string.action_gallery)) },
                modifier = Modifier.clickable(onClick = onGallery),
            )
        }
    }
}

@Composable
private fun ConfirmDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.action_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        },
    )
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

private val DormStatus.labelRes: Int
    get() = when (this) {
        DormStatus.NOT_CONTACTED -> R.string.status_not_contacted
        DormStatus.CONTACTED -> R.string.status_contacted
        DormStatus.VIEWED -> R.string.status_viewed
    }
