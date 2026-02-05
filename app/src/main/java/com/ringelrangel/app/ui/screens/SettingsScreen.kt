@file:OptIn(ExperimentalMaterial3Api::class)

package com.ringelrangel.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ringelrangel.app.BuildConfig
import com.ringelrangel.app.update.UpdateRepository
import com.ringelrangel.app.update.UpdateStatus
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val updateRepository = remember { UpdateRepository(context) }
    val status by updateRepository.status.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 24.dp, top = 48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Zurück",
                    tint = Color(0xFF666666)
                )
            }
        }

        Text(
            text = "EINSTELLUNGEN",
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF555555),
            letterSpacing = 3.sp,
            modifier = Modifier.padding(start = 24.dp, top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Version info
        SectionLabel("APP")
        InfoRow("Version", BuildConfig.VERSION_NAME)
        InfoRow("Build", "${BuildConfig.VERSION_BUILD}")
        InfoRow("Package", BuildConfig.APPLICATION_ID)

        Spacer(modifier = Modifier.height(28.dp))
        Divider(color = Color(0xFF1A1A1A), modifier = Modifier.padding(horizontal = 24.dp))
        Spacer(modifier = Modifier.height(28.dp))

        // Update section
        SectionLabel("UPDATES")
        Spacer(modifier = Modifier.height(12.dp))

        // Check button row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Auf Updates prüfen",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            if (status is UpdateStatus.Checking) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 1.5.dp,
                    color = Color(0xFF666666)
                )
            } else if (status is UpdateStatus.Idle || status is UpdateStatus.UpToDate || status is UpdateStatus.Error) {
                TextButton(
                    onClick = {
                        scope.launch { updateRepository.checkForUpdates() }
                    }
                ) {
                    Text(
                        text = "PRÜFEN",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Status content
        when (val currentStatus = status) {
            is UpdateStatus.UpToDate -> {
                Text(
                    text = "Aktuelle Version",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF555555),
                    modifier = Modifier.padding(start = 24.dp, top = 8.dp)
                )
            }

            is UpdateStatus.UpdateAvailable -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Version ${currentStatus.info.versionName} verfügbar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    if (currentStatus.info.releaseNotes.isNotBlank()) {
                        Text(
                            text = currentStatus.info.releaseNotes,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF666666),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    if (currentStatus.info.fileSize > 0) {
                        Text(
                            text = formatFileSize(currentStatus.info.fileSize),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF444444),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(
                        onClick = {
                            scope.launch { updateRepository.downloadUpdate(currentStatus.info) }
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "HERUNTERLADEN",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            is UpdateStatus.Downloading -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    LinearProgressIndicator(
                        progress = currentStatus.progress / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp),
                        color = Color.White,
                        trackColor = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "${currentStatus.progress}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF555555),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            is UpdateStatus.ReadyToInstall -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Download abgeschlossen",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = { updateRepository.installUpdate(currentStatus.apkFile) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "INSTALLIEREN",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            is UpdateStatus.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = currentStatus.message,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = {
                            scope.launch { updateRepository.checkForUpdates() }
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "ERNEUT VERSUCHEN",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            else -> {}
        }

        Spacer(modifier = Modifier.height(28.dp))
        Divider(color = Color(0xFF1A1A1A), modifier = Modifier.padding(horizontal = 24.dp))
        Spacer(modifier = Modifier.height(28.dp))

        // GitHub
        SectionLabel("LINKS")
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = BuildConfig.GITHUB_REPO,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666)
            )
            TextButton(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/${BuildConfig.GITHUB_REPO}")
                    )
                    context.startActivity(intent)
                }
            ) {
                Text(
                    text = "ÖFFNEN",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = Color(0xFF555555),
        letterSpacing = 3.sp,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF555555)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> String.format("%.1f MB", bytes / (1024.0 * 1024.0))
    }
}
