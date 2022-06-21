package com.civilcam.ui.terms.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.TermsType


@Composable
fun WebButton(
    buttonType: TermsType,
    onButtonClicked: (TermsType) -> Unit
) {

    OutlinedButton(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, CCTheme.colors.grayTwo),
        onClick = { onButtonClicked.invoke(buttonType) }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = buttonType.title),
                modifier = Modifier.padding(vertical = 8.dp),
                style = CCTheme.typography.common_text_medium,
                fontWeight = FontWeight.W500
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_web_redirect),
                contentDescription = null,
                tint = CCTheme.colors.black
            )
        }
    }

}
