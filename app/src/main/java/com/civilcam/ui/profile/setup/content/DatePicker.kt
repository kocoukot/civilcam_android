package com.civilcam.ui.profile.setup.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.Constant.MINIMUM_AGE
import com.civilcam.ui.common.compose.ListItemPicker
import com.civilcam.ui.common.compose.TextActionButton
import timber.log.Timber
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.*

@Composable
fun DatePickerContent(
    onPickerAction: (Long?) -> Unit,
) {
    val calendar = LocalDate.now()

    val maxYear = calendar.year - 18
    var yearPickerValue by remember { mutableStateOf(maxYear) }
    var monthPickerValue by remember { mutableStateOf(calendar.month) }
    var daysPickerValue by remember { mutableStateOf(calendar.dayOfMonth) }

    val isAllowed = ChronoUnit.YEARS.between(
        LocalDate.of(
            yearPickerValue,
            monthPickerValue.value,
            daysPickerValue
        ), calendar
    ) >= MINIMUM_AGE

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.white, RoundedCornerShape(2.dp)),
        contentAlignment = Alignment.Center
    ) {

        Column {
            Text(
                text = stringResource(id = R.string.profile_setup_date_of_birth),
                modifier = Modifier.padding(start = 16.dp, top = 20.dp),
                style = CCTheme.typography.button_text,
                fontWeight = FontWeight.W700
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                ListItemPicker(
                    modifier = Modifier.weight(1f),
                    label = { it.getDisplayName(TextStyle.FULL, Locale.US) },
                    value = monthPickerValue,
                    onValueChange = { monthPickerValue = it },
                    list = Month.values().toList(),
                    dividersColor = CCTheme.colors.black,
                    textStyle = CCTheme.typography.common_text_small_regular_spacing
                )


                Spacer(modifier = Modifier.width(14.dp))

                NumberPicker(
                    modifier = Modifier.weight(1f),
                    dividersColor = CCTheme.colors.black,
                    value = daysPickerValue,
                    range = 1..YearMonth.of(yearPickerValue, monthPickerValue.value)
                        .lengthOfMonth(),
                    onValueChange = { daysPickerValue = it },
                    textStyle = CCTheme.typography.common_text_small_regular


                )
                Spacer(modifier = Modifier.width(14.dp))


                NumberPicker(
                    modifier = Modifier.weight(1f),
                    dividersColor = CCTheme.colors.black,
                    value = yearPickerValue,
                    range = 1900..maxYear,
                    onValueChange = { yearPickerValue = it },
                    textStyle = CCTheme.typography.common_text_small_regular

                )
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextActionButton(
                    actionTitle = stringResource(id = R.string.cancel_text_caps),
                    textColor = CCTheme.colors.cianColor,
                    actionAction = { onPickerAction.invoke(null) }
                )

                TextActionButton(
                    isEnabled = isAllowed,
                    actionTitle = stringResource(id = R.string.ok_text_caps),
                    textColor = CCTheme.colors.cianColor,
                    actionAction = {
                        val date = Calendar.getInstance().apply {
                            set(
                                yearPickerValue,
                                monthPickerValue.value - 1,
                                daysPickerValue
                            )
                        }
                        Timber.d("LocalDate ${date.timeInMillis}")
                        onPickerAction.invoke(date.timeInMillis)
                    }
                )
            }
        }
    }
}