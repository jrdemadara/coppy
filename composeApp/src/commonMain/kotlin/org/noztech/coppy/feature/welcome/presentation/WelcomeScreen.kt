package org.noztech.coppy.feature.welcome.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.composables.icons.lucide.ExternalLink
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ServerOff
import coppy.composeapp.generated.resources.Res
import coppy.composeapp.generated.resources.logo
import coppy.composeapp.generated.resources.welcome
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.noztech.coppy.core.ui.components.TermsAndPrivacy
import org.noztech.coppy.navigation.AuthRoutes

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun WelcomeScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel = koinViewModel<WelcomeViewModel>()
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) { paddingValues ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(38.dp)
                )
            }

            OrbitingDotsIllustration(
                illustration = painterResource(Res.drawable.welcome),
                illustrationSize = 350.dp,
                dotColors = listOf(
                    Color(0xFFFFEB3B),
                    Color(0xFF6D49FF),
                    Color(0xFF7B1FA2)
                ),
                dotRadius = 16f,
                orbitOffset = 45f,
                modifier = Modifier.size(400.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()

                    .padding(horizontal = 24.dp, vertical = 24.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Lucide.ServerOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(12.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Trusted Local Storage",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            imageVector = Lucide.ExternalLink,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Coppy",
                            fontSize = 52.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.Cursive
                        )

                        Text(
                            text = "Numbers slip. Valuables shouldn’t. Coppy keeps everything safe, simple, and always within reach.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Center
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }

                    // Buttons
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                viewModel.firstLaunch()
                                navController.navigate(AuthRoutes.Home)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .height(48.dp)
                                .fillMaxWidth()
                        ) {
                            Text("Get Started", color = Color.White, fontSize = 16.sp)
                        }
                    }

                    TermsAndPrivacy(
                        onClick = {
                            println("Terms and Condition & Privacy Policy")
                        }
                    )
                }
            }

        }

    }
}

@Composable
fun OrbitingDotsIllustration(
    illustration: Painter,
    modifier: Modifier = Modifier,
    illustrationSize: Dp = 350.dp,
    dotColors: List<Color> = listOf(
        Color(0xFF9C27B0),
        Color(0xFFBA68C8),
        Color(0xFF7B1FA2)
    ),
    dotRadius: Float = 12f,
    orbitOffset: Float = 60f
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing)
        )
    )

    val orbitRadiusPx by remember {
        derivedStateOf { illustrationSize.value / 2 + 80f }
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val angles = listOf(0f, 120f, 240f)

            // Dots behind
            angles.forEachIndexed { index, angle ->
                val rad = (angle + rotation) * PI.toFloat() / 180f
                val zScale = 0.5f + 0.5f * (sin(rad) + 1) / 2f
                if (zScale < 0.75f) {
                    val x = center.x + orbitRadiusPx * cos(rad)
                    val y = center.y + (orbitRadiusPx / 2) * sin(rad)
                    val alpha = 0.4f + 0.6f * zScale
                    drawCircle(
                        color = dotColors[index % dotColors.size].copy(alpha = alpha),
                        radius = dotRadius * zScale,
                        center = Offset(x, y)
                    )
                }
            }
        }

        Image(
            painter = illustration,
            contentDescription = null,
            modifier = Modifier.size(illustrationSize)
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val angles = listOf(0f, 120f, 240f)

            // Dots in front
            angles.forEachIndexed { index, angle ->
                val rad = (angle + rotation) * PI.toFloat() / 180f
                val zScale = 0.5f + 0.5f * (sin(rad) + 1) / 2f
                if (zScale >= 0.75f) {
                    val x = center.x + orbitRadiusPx * cos(rad)
                    val y = center.y + (orbitRadiusPx / 2) * sin(rad)
                    val alpha = 0.4f + 0.6f * zScale
                    drawCircle(
                        color = dotColors[index % dotColors.size].copy(alpha = alpha),
                        radius = dotRadius * zScale,
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}