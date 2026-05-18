package com.example.nammakathey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.nammakathey.services.AIService
import com.example.nammakathey.services.StoryResult
import com.example.nammakathey.ui.theme.NammaKatheyTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NammaKatheyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { 
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AccountCircle, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Namma Kathey", fontWeight = FontWeight.Bold) 
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    StoryScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun StoryScreen(modifier: Modifier = Modifier) {
    var theme by remember { mutableStateOf("") }
    var storyResult by remember { mutableStateOf<StoryResult?>(null) }
    
    val suggestions = listOf("Gandhi", "Subhash Bose", "Bhagat Singh", "Ambedkar", "Jhansi Rani")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Explore Our History",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = theme,
            onValueChange = { theme = it },
            label = { Text("Search National Hero") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(suggestions) { suggestion ->
                SuggestionChip(
                    onClick = { 
                        theme = suggestion
                        storyResult = AIService.generateStory(suggestion)
                    },
                    label = { Text(suggestion) },
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (theme.isNotBlank()) {
                    storyResult = AIService.generateStory(theme)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(14.dp)
        ) {
            Text("Show Picture & Information", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (storyResult != null) {
            HeroDetailsCard(storyResult!!)
        }
    }
}

@Composable
fun HeroDetailsCard(result: StoryResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            // Simplified Image Loading for better compatibility
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(result.imageUrl)
                    .setHeader("User-Agent", "Mozilla/5.0") // Identify as a browser to avoid blocks
                    .crossfade(true)
                    .build(),
                contentDescription = result.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(40.dp))
                    }
                },
                error = {
                    Box(Modifier.fillMaxSize().background(Color.LightGray), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("Image load failed.", fontWeight = FontWeight.Bold)
                            Text("Please check internet.", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = result.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = result.subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = result.story,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)

                Text(
                    text = "Key Contributions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                result.keyFacts.forEach { fact ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Default.Star, 
                            contentDescription = null, 
                            modifier = Modifier.size(20.dp).padding(top = 2.dp), 
                            tint = Color(0xFFFF9800)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = fact, 
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoryScreenPreview() {
    NammaKatheyTheme {
        StoryScreen()
    }
}
