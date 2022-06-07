package com.example.no_fa.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.zero_fa.modules.Account

@Dao
interface PasswordDao {
    @Query("SELECT * FROM accounts")
    fun getAll(): List<Account>

    @Query("SELECT * FROM accounts WHERE domain LIKE :domain LIMIT 1")
    fun findByName(domain: String): Account

    @Insert
    fun insert(account: Account)

    @Delete
    fun delete(account: Account)

    @Query("DELETE from accounts")
    fun deleteAll()
}