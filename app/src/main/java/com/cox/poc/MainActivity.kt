package com.cox.poc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cox.domain.model.ResultResponse
import com.cox.poc.ui.theme.CoxPocTheme
import com.cox.poc.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoxPocTheme {
                MovieScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(viewModel: MovieViewModel = hiltViewModel()) {
    val upcomingState by viewModel.upcomingState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Movies") })
        }
    ) { paddingValues ->
        when {
            upcomingState.isLoading ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
                }

            upcomingState.failed -> {
                val errorMessage = upcomingState.errorMessage ?: "An unknown error occurred."
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Error: $errorMessage")
                }
            }

            else ->
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        viewModel.getUpcoming()
                        isRefreshing = false
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(paddingValues),
                        state = rememberLazyListState()
                    ) {
                        itemsIndexed(upcomingState.data) { _, movie ->
                            ItemCardMovie(movie = movie)
                        }
                    }
                }
        }

        LaunchedEffect(Unit) {
            viewModel.getUpcoming()
        }
    }
}

@Composable
fun ItemCardMovie(movie: ResultResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = movie.original_title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold
            )
        )
        Text(
            text = movie.overview,
            maxLines = 6,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Thin
            )
        )
    }
}
