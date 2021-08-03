package com.maikkkko1.android_base_arq.arq.common

import kotlin.reflect.KClass


interface UserInteraction

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OnInteraction(val target: KClass<out UserInteraction>)