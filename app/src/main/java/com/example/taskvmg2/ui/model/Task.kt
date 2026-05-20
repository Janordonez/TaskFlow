package com.example.taskvmg2.ui.model

data class Task(
    val id : Int,
    val title : String,
    val completed : Boolean = false
)
