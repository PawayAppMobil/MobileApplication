package com.paway.mobileapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.paway.mobileapplication.inventory.presentation.home.HomeScreen
import com.paway.mobileapplication.ui.theme.MobileApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileApplicationTheme {
                BackgroundColor {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun BackgroundColor(content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFD9D9D9))
    ) {
        content()
    }
}

@Composable
fun MainScreen() {
    Scaffold(
        bottomBar = { BottomButtonBar() }
    ) { paddingValues ->

        HomeScreen(modifier = Modifier.padding(paddingValues))
    }
}


@Composable
fun BottomButtonBar(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF7FAAAA))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val circleSize = 72.dp


            Box(
                modifier = Modifier
                    .size(circleSize)
                    .background(Color(0xFF4C8888), shape = CircleShape)
                    .clickable(onClick = { /* Acción para Home */ }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = Color.Black,
                    modifier = Modifier.size(36.dp) // Tamaño del ícono
                )
            }


            Box(
                modifier = Modifier
                    .size(circleSize)
                    .background(Color(0xFF4C8888), shape = CircleShape)
                    .clickable(onClick = { /* Acción para notificaciones */ }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.Black,
                    modifier = Modifier.size(36.dp)
                )
            }


            Box(
                modifier = Modifier
                    .size(circleSize)
                    .background(Color(0xFF4C8888), shape = CircleShape)
                    .clickable(onClick = { /* Acción para ajustes */ }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Ajustes",
                    tint = Color.Black,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Botón para Perfil
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .background(Color(0xFF4C8888), shape = CircleShape)
                    .clickable(onClick = { /* Acción para Perfil */ }), // Hacer que todo el círculo sea clickeable
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Perfil",
                    tint = Color.Black,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

