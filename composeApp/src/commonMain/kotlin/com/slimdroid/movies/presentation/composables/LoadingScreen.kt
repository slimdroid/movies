package com.slimdroid.movies.presentation.composables

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.slimdroid.movies.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ContainedLoadingIndicator(
            modifier = Modifier
                .size(128.dp)
                .align(Alignment.Center),
        )
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    AppTheme {
        Surface {
            LoadingScreen()
        }
    }
}

@Composable
fun FourBallsAnimation() {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
    val ballCount = colors.size
    val anims = remember { List(ballCount) { androidx.compose.animation.core.Animatable(1f) } }

    LaunchedEffect(Unit) {
        while (true) {
            anims.forEachIndexed { i, anim ->
                launch {
                    delay(i * 150L) // stagger start
                    anim.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 1200)
                    )
                    anim.snapTo(1f)
                }
            }
            delay(1200 + (ballCount - 1) * 150L)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = 12.dp.toPx()
            val spacing = size.width / (ballCount + 1)
            for (i in 0 until ballCount) {
                val x = spacing * (i + 1)
                val y = size.height * anims[i].value
                drawCircle(
                    color = colors[i],
                    radius = radius,
                    center = Offset(x = x, y = y - radius)
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomAnimationPreview() {
    AppTheme {
        Surface {
            FourBallsAnimation()
        }
    }
}