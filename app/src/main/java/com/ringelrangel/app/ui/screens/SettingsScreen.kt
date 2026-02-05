@file:OptIn(ExperimentalMaterial3Api::class)

package com.ringelrangel.app.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ringelrangel.app.BuildConfig
import com.ringelrangel.app.update.UpdateChecker
import com.ringelrangel.app.update.UpdateInfo
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var updateInfo by remember { mutableStateOf<UpdateInfo?>(null) }
    var isChecking by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableFloatStateOf(0f) }

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

        // Check button
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

            if (isChecking) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 1.5.dp,
                    color = Color(0xFF666666)
                )
            } else {
                TextButton(
                    onClick = {
                        scope.launch {
                            isChecking = true
                            updateInfo = null
                            val result = UpdateChecker.checkForUpdate(context)
                            updateInfo = result
                            isChecking = false
                            if (result == null) {
                                Toast.makeText(
                                    context,
                                    "Verbindung fehlgeschlagen",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    enabled = !isDownloading
                ) {
                    Text(
                        text = "PRÜFEN",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isDownloading) Color(0xFF333333) else Color.White,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Update result
        updateInfo?.let { info ->
            Spacer(modifier = Modifier.height(16.dp))

            if (info.hasUpdate) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "Version ${info.latestVersion} verfügbar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    if (info.releaseNotes.isNotBlank()) {
                        Text(
                            text = info.releaseNotes,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF666666),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isDownloading) {
                        LinearProgressIndicator(
                            progress = downloadProgress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp),
                            color = Color.White,
                            trackColor = Color(0xFF1A1A1A)
                        )
                        Text(
                            text = "${(downloadProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF555555),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    isDownloading = true
                                    downloadProgress = 0f
                                    val success = UpdateChecker.downloadAndInstall(
                                        context = context,
                                        downloadUrl = info.downloadUrl,
                                        onProgress = { downloadProgress = it }
                                    )
                                    isDownloading = false
                                    if (!success) {
                                        Toast.makeText(
                                            context,
                                            "Download fehlgeschlagen",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "INSTALLIEREN",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "Aktuelle Version",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF555555),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
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
