package com.ringelrangel.app.update

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import com.ringelrangel.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.URL

data class UpdateInfo(
    val versionName: String,
    val releaseNotes: String,
    val downloadUrl: String,
    val fileSize: Long
)

sealed class UpdateStatus {
    object Idle : UpdateStatus()
    object Checking : UpdateStatus()
    data class UpdateAvailable(val info: UpdateInfo) : UpdateStatus()
    object UpToDate : UpdateStatus()
    data class Downloading(val progress: Int) : UpdateStatus()
    data class ReadyToInstall(val apkFile: File) : UpdateStatus()
    data class Error(val message: String) : UpdateStatus()
}

class UpdateRepository(private val context: Context) {

    companion object {
        private const val TAG = "UpdateRepository"
    }

    private val _status = MutableStateFlow<UpdateStatus>(UpdateStatus.Idle)
    val status: StateFlow<UpdateStatus> = _status

    fun getCurrentVersion(): String {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    suspend fun checkForUpdates() = withContext(Dispatchers.IO) {
        _status.value = UpdateStatus.Checking
        Log.d(TAG, "Checking for updates...")

        try {
            val repo = BuildConfig.GITHUB_REPO
            val url = "https://api.github.com/repos/$repo/releases/latest"
            Log.d(TAG, "Fetching $url")

            val connection = URL(url).openConnection()
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json")
            connection.setRequestProperty("User-Agent", "Ringelrangel App")
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            val response = connection.getInputStream().bufferedReader().readText()
            val json = JSONObject(response)

            val tagName = json.optString("tag_name", "").removePrefix("v")
            val releaseNotes = json.optString("body", "")
            val remoteVersion = tagName.ifEmpty { "0.0.0" }

            val remoteVersionCode = parseVersionCode(remoteVersion)
            val currentVersionCode = parseVersionCode(getCurrentVersion())

            Log.d(TAG, "Remote: $remoteVersion ($remoteVersionCode), Current: ${getCurrentVersion()} ($currentVersionCode)")

            // Find APK asset
            val assets = json.optJSONArray("assets")
            var apkUrl: String? = null
            var apkSize: Long = 0

            if (assets != null) {
                for (i in 0 until assets.length()) {
                    val asset = assets.getJSONObject(i)
                    val name = asset.optString("name", "")
                    if (name.endsWith(".apk")) {
                        apkUrl = asset.optString("browser_download_url")
                        apkSize = asset.optLong("size", 0)
                        break
                    }
                }
            }

            if (remoteVersionCode > currentVersionCode && apkUrl != null) {
                _status.value = UpdateStatus.UpdateAvailable(
                    UpdateInfo(
                        versionName = remoteVersion,
                        releaseNotes = releaseNotes,
                        downloadUrl = apkUrl,
                        fileSize = apkSize
                    )
                )
                Log.d(TAG, "Update available: $remoteVersion")
            } else {
                _status.value = UpdateStatus.UpToDate
                Log.d(TAG, "App is up to date")
            }

        } catch (e: Exception) {
            Log.e(TAG, "Check failed", e)
            _status.value = UpdateStatus.Error("Update-Prüfung fehlgeschlagen: ${e.message}")
        }
    }

    suspend fun downloadUpdate(info: UpdateInfo) = withContext(Dispatchers.IO) {
        _status.value = UpdateStatus.Downloading(0)
        Log.d(TAG, "Downloading from ${info.downloadUrl}")

        try {
            val connection = URL(info.downloadUrl).openConnection()
            connection.setRequestProperty("User-Agent", "Ringelrangel App")
            connection.connectTimeout = 30000
            connection.readTimeout = 60000

            val totalSize = connection.contentLengthLong
            val inputStream = connection.getInputStream()

            val apkFile = File(context.cacheDir, "ringelrangel-update.apk")
            apkFile.outputStream().use { output ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                var totalBytesRead: Long = 0

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                    totalBytesRead += bytesRead

                    if (totalSize > 0) {
                        val progress = ((totalBytesRead * 100) / totalSize).toInt()
                        _status.value = UpdateStatus.Downloading(progress)
                    }
                }
            }

            inputStream.close()
            _status.value = UpdateStatus.ReadyToInstall(apkFile)
            Log.d(TAG, "Download complete: ${apkFile.absolutePath}")

        } catch (e: Exception) {
            Log.e(TAG, "Download failed", e)
            _status.value = UpdateStatus.Error("Download fehlgeschlagen: ${e.message}")
        }
    }

    fun installUpdate(apkFile: File) {
        try {
            Log.d(TAG, "Installing from ${apkFile.absolutePath}")

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Install failed", e)
            _status.value = UpdateStatus.Error("Installation fehlgeschlagen: ${e.message}")
        }
    }

    fun resetStatus() {
        _status.value = UpdateStatus.Idle
    }

    private fun parseVersionCode(version: String): Int {
        return try {
            val cleanVersion = version.split("-").first()
            val parts = cleanVersion.split(".")
            val major = parts.getOrNull(0)?.toIntOrNull() ?: 0
            val minor = parts.getOrNull(1)?.toIntOrNull() ?: 0
            val patch = parts.getOrNull(2)?.toIntOrNull() ?: 0
            major * 10000 + minor * 100 + patch
        } catch (e: Exception) {
            0
        }
    }
}
