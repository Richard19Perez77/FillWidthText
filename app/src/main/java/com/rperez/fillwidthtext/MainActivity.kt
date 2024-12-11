package com.rperez.fillwidthtext

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentDateTime = remember { mutableStateOf(LocalDateTime.now()) }
            val pattern = "MM-dd-yyyy HH:mm:ss.SSS"
            val formatter = DateTimeFormatter.ofPattern(pattern)

            LaunchedEffect(Unit) {
                while (true) {
                    currentDateTime.value = LocalDateTime.now()
                    delay(16)
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                FitText(modifier = Modifier, currentDateTime.value.format(formatter))
            }
        }
    }
}

@Composable
fun FitText(modifier: Modifier = Modifier, input: String) {
    var textSizeState = remember { mutableFloatStateOf(1.0f) }
    var fontScaleSet = remember { mutableStateOf(false) }
    val localDensity = LocalDensity.current

    val font = FontFamily(
        Font(R.font.robotomonoregular)
    )

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .onSizeChanged { it ->
                if (!fontScaleSet.value) {
                    var columnWidthDp = with(localDensity) { it.width.toDp() }
                    var result: Dp = Paint()
                        .apply {
                            textSize = textSizeState.floatValue
                        }
                        .measureText(input).dp

                    while (result < columnWidthDp) {
                        textSizeState.floatValue = textSizeState.floatValue.inc()
                        result = Paint()
                            .apply {
                                textSize = textSizeState.floatValue
                            }
                            .measureText(input).dp
                    }

                    textSizeState.floatValue = textSizeState.floatValue.dec()
                    fontScaleSet.value = true
                }
            },
        style = TextStyle(
            fontFamily = font,
            textAlign = TextAlign.Center,
        ),
        fontSize = textSizeState.floatValue.sp,
        text = input,
    )
}