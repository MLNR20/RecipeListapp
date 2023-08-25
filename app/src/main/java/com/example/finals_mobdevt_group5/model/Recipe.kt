package com.example.finals_mobdevt_group5.model

import androidx.activity.viewModels
import com.example.finals_mobdevt_group5.viewmodel.MainViewModel

data class Recipe(
    val recipeName: String? = null,
    val cookTime: String? = null,
    val email: String? = null,
    val ingredient: String? = null,
    val instructions: String?=null,
    val shortdescription: String?= null,
    val url: String?= null
)