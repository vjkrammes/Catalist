package solutions.vjk.catalist.transitions

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally

val rootExitTransition = slideOutHorizontally(
    targetOffsetX = { -300 },
    animationSpec = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
)