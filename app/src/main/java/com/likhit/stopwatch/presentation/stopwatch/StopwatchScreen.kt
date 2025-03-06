@file:OptIn(ExperimentalAnimationApi::class)

package com.likhit.stopwatch.presentation.stopwatch

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.likhit.stopwatch.ui.theme.NumbersColor
import com.likhit.stopwatch.ui.theme.PlayColor
import com.likhit.stopwatch.ui.theme.ResetColor
import com.likhit.stopwatch.ui.theme.StopColor

@Composable
fun StopwatchScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: StopwatchViewModel = hiltViewModel()
) {
    StopwatchScreen(
        modifier = modifier,
        seconds = viewModel.seconds,
        minutes = viewModel.minutes,
        hours = viewModel.hours,
        isPlaying = viewModel.isPlaying,
        onStart = viewModel::start,
        onPause = viewModel::pause,
        onReset = viewModel::reset
    )
}

@Composable
fun StopwatchScreen(
    modifier: Modifier = Modifier,
    seconds: String,
    minutes: String,
    hours: String,
    isPlaying: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit
) {
    val numberTransitionSpec: AnimatedContentTransitionScope<String>.() -> ContentTransform = {
        slideInHorizontally(
            initialOffsetX = { it }
        ) + fadeIn() with slideOutHorizontally(
            targetOffsetX = { -it }
        ) + fadeOut() using SizeTransform(
            false
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = hours,
                transitionSpec = numberTransitionSpec,

                ) {
                Text(
                    text = it,
                    fontSize = 130.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NumbersColor
                )
            }
            AnimatedContent(
                targetState = minutes,
                transitionSpec = numberTransitionSpec
            ) {
                Text(
                    text = it,
                    fontSize = 130.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NumbersColor
                )
            }
            AnimatedContent(
                targetState = seconds,
                transitionSpec = numberTransitionSpec
            ) {
                Text(
                    text = it,
                    fontSize = 130.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NumbersColor
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = isPlaying
            ) {
                if(it){
                    IconButton(
                        onClick = onPause,
                        modifier = Modifier.size(50.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = StopColor
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pause,
                            contentDescription = "Pause",
                            Modifier.size(50.dp)
                        )
                    }
                }else{
                    IconButton(
                        onClick = onStart,
                        modifier = Modifier.size(50.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = PlayColor
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            Modifier.size(50.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            if(!isPlaying){
                IconButton(
                    onClick = onReset,
                    modifier = Modifier.size(50.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = ResetColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Restore,
                        contentDescription = "Reset",
                        Modifier.size(50.dp)
                    )
                }
            }
        }
    }
}