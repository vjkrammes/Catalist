package solutions.vjk.catalist.models

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import solutions.vjk.catalist.R

sealed class NavigationMenuItem(
    val route: String,
    val iconId: Int,
    val text: String,
    val iconSize: Dp = 32.dp,
    val itemStyle: TextStyle = TextStyle.Default,
    val fontWeight: FontWeight = FontWeight.Normal,
    val fontStyle: FontStyle = FontStyle.Normal
) {
    override fun toString() = text

    object All : NavigationMenuItem("allitems", R.drawable.ic_baseline_checklist_rtl_24, "All Items")
    object Home : NavigationMenuItem("root", R.drawable.ic_baseline_home_24, "Home")
    object OpenList :
        NavigationMenuItem("openlist", R.drawable.ic_baseline_description_24, "Open List")

    object NewList : NavigationMenuItem("newlist", R.drawable.ic_baseline_add_24, "New List")
    object ManageCategories :
        NavigationMenuItem("categories", R.drawable.ic_baseline_list_24, "Manage Categories")

    object ManageAssignees :
        NavigationMenuItem("assignees", R.drawable.ic_baseline_person_24, "Manage Assignees")

    object SearchItems :
        NavigationMenuItem("search", R.drawable.ic_baseline_search_24, "Search Items")

    object ListInfo :
        NavigationMenuItem("listinfo", R.drawable.ic_baseline_info_24, "List Information")

    object Settings : NavigationMenuItem("settings", R.drawable.ic_baseline_settings_24, "Settings")
    object AboutCatalist :
        NavigationMenuItem("about", R.drawable.ic_outline_info_24, "About Catalist")

    object Divider : NavigationMenuItem("", 0, "divider")

    companion object {
        fun getItems(): List<NavigationMenuItem> {
            return listOf(
                Home,
                OpenList,
                NewList,
                Divider,
                ManageAssignees,
                ManageCategories,
                Divider,
                All,
                SearchItems,
                Divider,
                ListInfo,
                Settings,
                AboutCatalist
            )
        }
    }
}