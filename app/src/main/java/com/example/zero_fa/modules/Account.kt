package com.example.zero_fa.modules

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import java.sql.Date

@Entity(tableName = "accounts")
data class Account(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val domain: String,
    val salt: String


) {
    fun convertToTotp(): Totp {
        val baseSalt = salt.toByteArray()
        val code = GoogleAuthenticator(baseSalt).generate(Date(System.currentTimeMillis()))
        Log.d("code generator", code)
        return Totp(domain, code)
    }

}

