package com.example.zero_fa.modules

import com.example.zero_fa.RvAdapter
import com.example.no_fa.dao.PasswordDao
import kotlinx.coroutines.delay
import java.util.*

class PasswordRender(private val rvAdapter: RvAdapter, private val dao: PasswordDao) {

    suspend fun passwordRender() {
        val records = dao.getAll()
        var totps = getTotps(records)
        val network = Network(dao)
        while (true) {
            val timeLeft = countTimeLeft()

            if (timeLeft == 30) {
                totps = getTotps(records)
                network.uploadPasswords(totps)
            }

            totps.forEach { password -> password.timeLeft = timeLeft }
            rvAdapter.updateAllPasswords(totps)
            delay(1000)

        }
    }

    fun countTimeLeft(): Int {
        val currentSeconds = Calendar.getInstance().get(Calendar.SECOND)
        return if (currentSeconds >= 30) (60 - currentSeconds) else (30 - currentSeconds)
    }

    fun getTotps(accounts: List<Account>): ArrayList<Totp> {
        val totps = ArrayList<Totp>()
        for (record in accounts) {
            val totp = record.convertToTotp()
            totps.add(totp)
        }
        return totps
    }
}