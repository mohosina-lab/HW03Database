package com.example.hw03database

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.example.hw03database.ui.theme.HW03DatabaseTheme
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // UI state
            var username by remember { mutableStateOf("") }
            val prefs = getSharedPreferences("hw03_prefs", MODE_PRIVATE)
            var showOtherScreen by remember { mutableStateOf(false) }
            var imageUri by remember { mutableStateOf<Uri?>(null) }

            // Photo picker
            val pickImageLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia()
                ) { uri ->
                    if (uri != null) {
                        imageUri = copyImageToInternalStorage(uri)
                    }
                }

            // Load saved image and texts on app start
            LaunchedEffect(Unit) {
                val file = File(filesDir, "profile_image.png")
                if (file.exists()) {
                    imageUri = file.toUri()
                }
            }
            LaunchedEffect(Unit) {
                username = prefs.getString("username", "") ?: ""
            }

            HW03DatabaseTheme {
                if (!showOtherScreen) {
                    // Screen 1: Input screen
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(16.dp)
                    ) {
                        Text(text = "Username")

                        OutlinedTextField(
                            value = username,
                            onValueChange = {
                                username = it
                                prefs.edit().putString("username", it).apply()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            pickImageLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }) {
                            Text("Pick Image")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (imageUri == null) {
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Image Preview")
                            }
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "Picked image",
                                modifier = Modifier.size(150.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = { showOtherScreen = true }) {
                            Text("Go to Other Screen")
                        }
                    }
                } else {
                    // Screen 2: Display screen
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(16.dp)
                    ) {
                        Text(text = "Other screen")

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Username: $username")

                        Spacer(modifier = Modifier.height(16.dp))

                        if (imageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "Saved image",
                                modifier = Modifier.size(150.dp)
                            )
                        } else {
                            Text("No image selected yet.")
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(onClick = { showOtherScreen = false }) {
                            Text("Back")
                        }
                    }
                }
            }
        }
    }

    private fun copyImageToInternalStorage(uri: Uri): Uri {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(filesDir, "profile_image.png")
        val outputStream = FileOutputStream(file)

        inputStream?.copyTo(outputStream)

        inputStream?.close()
        outputStream.close()

        return file.toUri()
    }
}
