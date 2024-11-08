package com.paway.mobileapplication.user.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paway.mobileapplication.user.data.remote.dto.HomeResponse

@Composable
fun CircularChart(data: List<HomeResponse>) {
    val totalStock = data.sumOf { it.totalStock.toInt() }
    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow) // Colores para cada proveedor

    val paint = Paint().asFrameworkPaint().apply {
        color = android.graphics.Color.WHITE
        textSize = 30f
        typeface = android.graphics.Typeface.create("baskerville", android.graphics.Typeface.NORMAL)
    }

    Box(
        modifier = Modifier
            .size(200.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            var startAngle = 0f
            data.forEachIndexed { index, provider ->
                val sweepAngle = (provider.totalStock.toFloat() / totalStock) * 360f
                drawIntoCanvas {
                    rotate(startAngle) {
                        drawArc(
                            color = colors[index % colors.size],
                            startAngle = 0f,
                            sweepAngle = sweepAngle,
                            useCenter = true
                        )
                    }
                }

                // Calcular el ángulo medio para posicionar el texto
                val midAngle = startAngle + sweepAngle / 2
                val textX = (size.width / 2 * 0.5f * kotlin.math.cos(Math.toRadians(midAngle.toDouble()))).toFloat()
                val textY = (size.height / 2 * 0.5f * kotlin.math.sin(Math.toRadians(midAngle.toDouble()))).toFloat()

                // Dibuja las etiquetas de nombre y totalStock
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.apply {
                        drawText(
                            "${provider.name}: ${provider.totalStock}",
                            size.width / 2 + textX,
                            size.height / 2 + textY,
                            paint
                        )
                    }
                }

                startAngle += sweepAngle
            }
        }
    }
}