package com.civilcam.domainLayer.model

import androidx.annotation.StringRes
import com.civilcam.domainLayer.R

enum class SubscriptionType(@StringRes val type: Int, @StringRes val plan: Int) {
	TRIAL(R.string.subscription_trial_title, R.string.subscription_trial_plan),
	MONTHLY(R.string.subscription_monthly_title, R.string.subscription_monthly_plan),
	ANNUAL(R.string.subscription_annual_title, R.string.subscription_annual_plan);
}