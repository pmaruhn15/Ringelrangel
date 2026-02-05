package com.ringelrangel.app.update

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import com.ringelrangel.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

data class UpdateInfo(
    val hasUpdate: Boolean,
    val latestVersion: String,
    val downloadUrl: String,
    val releaseNotes: String
)

object UpdateChecker {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun checkForUpdate(context: Context): UpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val repo = BuildConfig.GITHUB_REPO
            val url = "https://api.github.com/repos/$repo/releases/latest"

            val request = Request.Builder()
                .url(url)
                .header("Accept", "application/vnd.github.v3+json")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                // No releases yet, try checking actions artifacts
                return@withContext checkActionsArtifact(context)
            }

            val body = response.body?.string() ?: return@withContext null
            val json = JSONObject(body)

            val tagName = json.getString("tag_name").removePrefix("v")
            val releaseNotes = json.optString("body", "")
            val assets = json.getJSONArray("assets")

            var apkUrl = ""
            for (i in 0 until assets.length()) {
                val asset = assets.getJSONObject(i)
                if (asset.getString("name").endsWith(".apk")) {
                    apkUrl = asset.getString("browser_download_url")
                    break
                }
            }

            val currentVersion = BuildConfig.VERSION_NAME
            val hasUpdate = isNewerVersion(tagName, currentVersion)

            UpdateInfo(
                hasUpdate = hasUpdate,
                latestVersion = tagName,
                downloadUrl = apkUrl,
                releaseNotes = releaseNotes
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun checkActionsArtifact(context: Context): UpdateInfo? {
        try {
            val repo = BuildConfig.GITHUB_REPO
            val url = "https://api.github.com/repos/$repo/actions/artifacts?per_page=1"

            val request = Request.Builder()
                .url(url)
                .header("Accept", "application/vnd.github.v3+json")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return null

            val body = response.body?.string() ?: return null
            val json = JSONObject(body)
            val artifacts = json.getJSONArray("artifacts")

            if (artifacts.length() == 0) {
                return UpdateInfo(
                    hasUpdate = false,
                    latestVersion = BuildConfig.VERSION_NAME,
                    downloadUrl = "",
                    releaseNotes = ""
                )
            }

            // Artifacts from actions are available but need auth to download
            // Redirect to releases page
            return UpdateInfo(
                hasUpdate = false,
                latestVersion = BuildConfig.VERSION_NAME,
                downloadUrl = "",
                releaseNotes = "Besuche die GitHub Releases Seite für neue Builds."
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun isNewerVersion(remote: String, current: String): Boolean {
        try {
            val remoteParts = remote.split(".").map { it.toIntOrNull() ?: 0 }
            val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }

            for (i in 0 until maxOf(remoteParts.size, currentParts.size)) {
                val r = remoteParts.getOrElse(i) { 0 }
                val c = currentParts.getOrElse(i) { 0 }
                if (r > c) return true
                if (r < c) return false
            }
        } catch (_: Exception) {}
        return false
    }

    suspend fun downloadAndInstall(
        context: Context,
        downloadUrl: String,
        onProgress: (Float) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            if (downloadUrl.isBlank()) return@withContext false

            val request = Request.Builder()
                .url(downloadUrl)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext false

            val body = response.body ?: return@withContext false
            val contentLength = body.contentLength()

            val updatesDir = File(
                context.getExternalFilesDir(null),
                "updates"
            )
            updatesDir.mkdirs()

            val apkFile = File(updatesDir, "ringelrangel-update.apk")

            FileOutputStream(apkFile).use { output ->
                body.byteStream().use { input ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Long = 0

                    while (true) {
                        val read = input.read(buffer)
                        if (read == -1) break
                        output.write(buffer, 0, read)
                        bytesRead += read
                        if (contentLength > 0) {
                            withContext(Dispatchers.Main) {
                                onProgress(bytesRead.toFloat() / contentLength.toFloat())
                            }
                        }
                    }
                }
            }

            withContext(Dispatchers.Main) {
                installApk(context, apkFile)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun installApk(context: Context, apkFile: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            apkFile
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        context.startActivity(intent)
    }
}
