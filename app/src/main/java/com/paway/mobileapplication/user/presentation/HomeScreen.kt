// In your HomeScreenContent composable
package com.paway.mobileapplication.user.presentation

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import com.paway.mobileapplication.user.data.remote.dto.HomeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.URL

@Composable
fun HomeScreenContent(homeViewModel: HomeViewModel) {
    val providersState = remember { mutableStateOf<Response<List<HomeResponse>>?>(null) }
    val context = LocalContext.current

    DisposableEffect(homeViewModel) {
        val observer = Observer<Response<List<HomeResponse>>?> { response ->
            providersState.value = response
        }
        homeViewModel.providers.observeForever(observer)
        onDispose {
            homeViewModel.providers.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.fetchProviders()
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvendio", style = CustomTypography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        providersState.value?.let { response ->
            if (response.isSuccessful) {
                response.body()?.let { providers ->
                    // Load and display the image
                    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
                    LaunchedEffect(Unit) {
                        withContext(Dispatchers.IO) {
                            val url = URL("https://static.vecteezy.com/system/resources/previews/018/921/646/non_2x/finance-and-investment-flat-icon-element-blue-and-black-color-free-png.png")
                            bitmap = BitmapFactory.decodeStream(url.openStream())
                        }
                    }
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Display the circular chart inside a card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Mis Proveedores", style = CustomTypography.headlineSmall)
                            Spacer(modifier = Modifier.height(16.dp))
                            CircularChart(data = providers)
                        }
                    }
                }
            } else {
                Text(text = "Error: ${response.message()}", color = MaterialTheme.colorScheme.error)
            }
        } ?: run {
            CircularProgressIndicator()
        }
    }
}