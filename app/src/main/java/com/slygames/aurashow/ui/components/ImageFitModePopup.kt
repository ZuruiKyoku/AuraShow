package com.slygames.aurashow.ui.components

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.slygames.aurashow.R
import com.slygames.aurashow.util.FitModeNames

@Composable
fun ImageFitModePopup(
    currentFitMode: FitModeNames,
    onApply: (FitModeNames) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedFitMode by remember { mutableStateOf(currentFitMode) }
    var visible by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = { visible = false }) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + scaleIn(initialScale = 0.9f),
            exit = fadeOut() + scaleOut(targetScale = 0.9f),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight()
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.clickable(enabled = false) {}
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Select Image Fit Mode", style = MaterialTheme.typography.titleMedium)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    ) {
                        AsyncImage(
                            model = Uri.parse("android.resource://com.slygames.aurashow/${R.drawable.sample_preview}"),
                            contentDescription = "Preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = selectedFitMode.scale
                        )
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        userScrollEnabled = false
                    ) {
                        items(FitModeNames.values()) { mode ->
                            FitModeButton(
                                label = mode.label,
                                icon = when (mode) {
                                    FitModeNames.Fit -> Icons.Default.Fullscreen
                                    FitModeNames.Crop -> Icons.Default.Crop
                                    FitModeNames.FillBounds -> Icons.Default.FitScreen
                                    FitModeNames.Inside -> Icons.Default.PhotoSizeSelectSmall
                                    FitModeNames.None -> Icons.Default.CenterFocusWeak
                                },
                                mode = mode,
                                selected = selectedFitMode,
                                onSelect = { selectedFitMode = it }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { visible = false }) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            onApply(selectedFitMode)
                            visible = false
                        }) {
                            Text("Apply")
                        }
                    }
                }
            }
        }

        // Wait for animation to finish before invoking dismiss
        if (!visible) {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(300)
                onDismiss()
            }
        }
    }
}

@Composable
private fun FitModeButton(
    label: String,
    icon: ImageVector,
    mode: FitModeNames,
    selected: FitModeNames,
    onSelect: (FitModeNames) -> Unit
) {
    val isSelected = selected == mode
    OutlinedButton(
        onClick = { onSelect(mode) },
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = label)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}