package com.kounalem.moviedatanase.core.ui.model

data class ListItemModel<ID, IMAGE_PATH>(
    val id: ID,
    val imagePath: IMAGE_PATH,
    val title: String,
    val description: String,
)