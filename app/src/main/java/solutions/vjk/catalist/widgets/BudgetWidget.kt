package solutions.vjk.catalist.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import solutions.vjk.catalist.infrastructure.CurrencyAmountInputVisualTransformation
import solutions.vjk.catalist.infrastructure.convertMoney
import solutions.vjk.catalist.ui.theme.gray3
import solutions.vjk.catalist.ui.theme.gray4
import solutions.vjk.catalist.ui.theme.green2
import solutions.vjk.catalist.ui.theme.green3

@Composable
fun BudgetWidget(
    hasBudget: Boolean,
    budget: Int,
    spent: Int,
    doSetBudget: (Boolean, Int, Int) -> Unit
) {
    val budgetChanged: (value: String) -> Unit = out@{ value ->
        if (value.isEmpty()) {
            return@out
        }
        if (value.startsWith("0")) {
            doSetBudget(false, 0, 0)
            return@out
        }
        val b = convertMoney(value)
        val s = if (b == 0) 0 else spent
        doSetBudget(b != 0, b, s)
    }
    val spentChanged: (value: String) -> Unit = out@{ value ->
        if (value.isEmpty()) {
            return@out
        }
        if (value.startsWith("0")) {
            val s = convertMoney(value)
            doSetBudget(budget != 0, budget, s)
            return@out
        }
        if (budget != 0) {
            val s = convertMoney(value)
            doSetBudget(true, budget, s)
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            FormLabel(text = "Budget")
            Switch(
                colors = SwitchDefaults.colors(
                    checkedThumbColor = green3,
                    uncheckedThumbColor = gray4,
                    checkedTrackColor = green2,
                    uncheckedTrackColor = gray3
                ),
                checked = hasBudget,
                onCheckedChange = {
                    val hb = !hasBudget
                    doSetBudget(hb, 0, 0)
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = if (budget != 0) budget.toString() else "",
                onValueChange = budgetChanged,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                label = { Text("Budget") },
                singleLine = true,
                maxLines = 1,
                enabled = hasBudget,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = CurrencyAmountInputVisualTransformation()
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = if (spent != 0) spent.toString() else "",
                onValueChange = spentChanged,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                label = { Text("Spent") },
                singleLine = true,
                maxLines = 1,
                enabled = hasBudget && budget != 0,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = CurrencyAmountInputVisualTransformation()
            )
        }
    }
}
