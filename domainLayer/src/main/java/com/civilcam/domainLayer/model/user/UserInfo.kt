package com.civilcam.domainLayer.model.user

data class UserInfo(
	val userId: Int = 0,
	val userName: String = "",
	var dateOfBirth: Long = 0,
	val address: String = "",
	val phoneNumber: String = "",
	val avatar: Int = 0,
	val email: String = "",
	val pinCode: String = "",
	var firstName: String = "",
	var lastName: String = "",
)