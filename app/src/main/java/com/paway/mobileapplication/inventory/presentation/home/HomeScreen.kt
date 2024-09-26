package com.paway.mobileapplication.inventory.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Contenido de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "RETAIL INNOVATION",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF005555)
                )
                Row {
                    IconButton(onClick = { /* Acción de notificaciones */ }) {
                        Icon(imageVector = Icons.Filled.Notifications, contentDescription = "Notificaciones")
                    }
                    IconButton(onClick = { /* Acción de ajustes */ }) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = "Ajustes")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E88E5)), // Azul
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Saldo Total", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Text(text = "S/ 10,000.00", style = MaterialTheme.typography.displayLarge, color = Color.White)
                }
            }


            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F)), // Rojo
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Artículos Críticos", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Text(text = "3", style = MaterialTheme.typography.displayLarge, color = Color.White)
                }
            }


            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E88E5)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Facturas por Cobrar", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Text(text = "S/ 5,000.00", style = MaterialTheme.typography.displayLarge, color = Color.White)
                }
            }
            // Detalles de facturas próximas a cobrar
            Column {
                Text(text = "Factura 1: S/ 1,000.00 - Fecha: 01/10/2024", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Factura 2: S/ 500.00 - Fecha: 05/10/2024", style = MaterialTheme.typography.bodyMedium)
            }


            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F)), // Rojo
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Facturas por Pagar", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Text(text = "S/ 3,000.00", style = MaterialTheme.typography.displayLarge, color = Color.White)
                }
            }

            Column {
                Text(text = "Factura A: S/ 1,500.00 - Fecha: 02/10/2024", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Factura B: S/ 1,000.00 - Fecha: 06/10/2024", style = MaterialTheme.typography.bodyMedium)
            }

        }


    }
}


