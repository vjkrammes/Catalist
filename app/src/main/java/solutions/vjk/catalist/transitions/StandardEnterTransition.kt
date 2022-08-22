package solutions.vjk.catalist.transitions

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally

val standardEnterTransition: EnterTransition =
    slideInHorizontally(
        initialOffsetX = { 300 },
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )
