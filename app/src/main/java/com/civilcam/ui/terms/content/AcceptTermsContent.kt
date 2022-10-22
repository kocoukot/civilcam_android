package com.civilcam.ui.terms.content

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun AcceptTermsContent(isAccepted: Boolean, acceptTerms: () -> Unit) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .border(
                    2.dp,
                    if (isAccepted) CCTheme.colors.primaryRed else CCTheme.colors.grayOne,
                    shape = RoundedCornerShape(2.dp)
                ),
            contentAlignment = Alignment.Center,

            ) {
            Checkbox(
                modifier = Modifier.padding(5.dp),
                colors = CheckboxDefaults.colors(
                    uncheckedColor = CCTheme.colors.lightGray,
                    checkedColor = CCTheme.colors.lightGray,
                    checkmarkColor = CCTheme.colors.primaryRed,
                ),
                checked = isAccepted,
                onCheckedChange = { acceptTerms.invoke() }
            )
        }


        Text(modifier = Modifier
            .padding(end = 12.dp, top = 6.dp),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = CCTheme.colors.black,
                        fontWeight = FontWeight.W500
                    ),
                ) {
                    append("${context.resources.getString(R.string.terms_conditions_i_agree)} ")
                }

                withStyle(
                    style = SpanStyle(
                        color = CCTheme.colors.primaryRed,
                        fontWeight = FontWeight.W500
                    ),
                ) {
                    append(context.resources.getString(R.string.terms_button))
                }

                withStyle(
                    style = SpanStyle(
                        color = CCTheme.colors.black,
                        fontWeight = FontWeight.W500
                    ),
                ) {
                    append(" ${context.resources.getString(R.string.and_text)}\n")
                }
                withStyle(
                    style = SpanStyle(
                        color = CCTheme.colors.primaryRed,
                        fontWeight = FontWeight.W500
                    ),
                ) {
                    append(context.resources.getString(R.string.privacy_button))
                }
            })
    }
}
