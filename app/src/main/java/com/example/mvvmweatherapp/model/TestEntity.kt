package com.example.mvvmweatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// todo its fake model and should be deleted

@Entity
data class TestEntity(
    @PrimaryKey val id : String
)
