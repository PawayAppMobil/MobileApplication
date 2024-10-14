package com.paway.mobileapplication

import InvoiceDashboardScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paway.mobileapplication.invoces.data.repository.WebServiceRepository
import com.paway.mobileapplication.common.RetrofitClient
import com.paway.mobileapplication.invoces.presentation.facturas.InvoiceDashboardViewModel
import com.paway.mobileapplication.ui.theme.MobileApplicationTheme

class MainActivitySelector : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = intent.getStringExtra("USER_ID")

        val viewModel = InvoiceDashboardViewModel(WebServiceRepository(RetrofitClient.webService))

        setContent {
            MobileApplicationTheme {
                MainScreen(userId, viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(userId: String?, viewModel: InvoiceDashboardViewModel) {
    var selectedScreen by remember { mutableStateOf("home") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1f)
        ) {
            when (selectedScreen) {
                "home" -> HomeScreen(userId)
                "inventory" -> InventoryScreen()
                "invoice" -> InvoiceDashboardScreen(viewModel, userId)
                "balance" -> BalanceScreen()
            }
        }

        BottomNavigationBar(selectedScreen) { screen ->
            selectedScreen = screen
        }
    }
}

@Composable
fun HomeScreen(userId: String?) {
    Text(text = userId ?: "Pantalla de Inicio")
}

@Composable
fun InventoryScreen() {
    Text(text = "Pantalla de Inventario")
}

@Composable
fun BalanceScreen() {
    Text(text = "Pantalla de Balance General")
}

@Composable
fun BottomNavigationBar(selectedScreen: String, onItemSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0E0E0))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomNavItem(
            icon = Icons.Default.Home,
            label = "Inicio",
            isSelected = selectedScreen == "home",
            onClick = { onItemSelected("home") }
        )
        BottomNavItem(
            icon = Icons.Default.List,
            label = "Inventario",
            isSelected = selectedScreen == "inventory",
            onClick = { onItemSelected("inventory") }
        )
        BottomNavItem(
            icon = Icons.Default.Add,
            label = "Generar Factura",
            isSelected = selectedScreen == "invoice",
            onClick = { onItemSelected("invoice") }
        )
        BottomNavItem(
            icon = Icons.Default.DateRange,
            label = "Balance",
            isSelected = selectedScreen == "balance",
            onClick = { onItemSelected("balance") }
        )
    }
}

@Composable
fun BottomNavItem(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.Yellow else Color.Gray
        )
        Text(
            text = label,
            color = if (isSelected) Color.Yellow else Color.Gray
        )
    }
}

