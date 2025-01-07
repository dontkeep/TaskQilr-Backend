package com.al.model

import kotlinx.serialization.Serializable

@Serializable
data class InputInstance(
    val keras_tensor: List<Int>
)

@Serializable
data class PredictionResponse(val predictions: List<List<Float>>)

