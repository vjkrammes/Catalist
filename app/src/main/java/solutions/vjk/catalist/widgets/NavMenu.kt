package solutions.vjk.catalist.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import solutions.vjk.catalist.R
import solutions.vjk.catalist.models.NavigationMenuItem

@Composable
fun NavMenu(
    navController: NavController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope
) {
    val lora = FontFamily(Font(R.font.lora, FontWeight.Normal))

    NavigationDrawer(
        items = NavigationMenuItem.getItems(),
        navController = navController,
        scaffoldState = scaffoldState,
        scope = scope,
        title = {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.tertiary)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painterResource(id = R.drawable.logo64),
                    contentDescription = "VJL Solutions Logo",
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 24.sp,
                    fontFamily = lora,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    )
}