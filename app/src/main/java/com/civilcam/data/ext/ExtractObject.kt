package com.civilcam.data.ext

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExtractObject(val fieldName: String)