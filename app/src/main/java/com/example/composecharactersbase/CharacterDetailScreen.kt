package com.example.composecharactersbase

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    viewModel: CharacterViewModel,
    characterId: String,
    onBackClick: () -> Unit
) {
    val selectedCharacter by viewModel.selectedCharacter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(characterId) {
        viewModel.getCharacterById(characterId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = selectedCharacter?.name ?: "Character Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    selectedCharacter?.let { character ->
                        val isFavorite = viewModel.isFavorite(character.id)
                        IconButton(onClick = { viewModel.toggleFavorite(character) }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color.Yellow else Color.Gray
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null) {
                Text(
                    text = error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else {
                selectedCharacter?.let { character ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = character.imageUrl),
                            contentDescription = "Character image",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DetailRow(label = "Name", value = character.name)
                                if (character.gender.isNotEmpty()) {
                                    DetailRow(label = "Gender", value = character.gender)
                                }
                                if (character.age.isNotEmpty()) {
                                    DetailRow(label = "Age", value = character.age)
                                }
                                if (character.eye_color.isNotEmpty()) {
                                    DetailRow(label = "Eye Color", value = character.eye_color)
                                }
                                if (character.hair_color.isNotEmpty()) {
                                    DetailRow(label = "Hair Color", value = character.hair_color)
                                }
                                Text(
                                    text = "Appears in:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                character.films.forEach { filmUrl ->
                                    val filmName = filmUrl.substringAfterLast("/")
                                    Text(text = "• Film ID: $filmName")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}