package com.example.zero_fa.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.zero_fa.AppDatabase
import com.example.zero_fa.modules.Account
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class ScannerActivity : AppCompatActivity(), ZBarScannerView.ResultHandler {
    private lateinit var zBarScannerView: ZBarScannerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        zBarScannerView = ZBarScannerView(this)
        setContentView(zBarScannerView)
    }


    override fun onResume() {
        super.onResume()
        zBarScannerView.setResultHandler(this)
        zBarScannerView.startCamera()
    }


    override fun onPause() {
        super.onPause()
        zBarScannerView.stopCamera()
    }

    override fun handleResult(result: Result?) {
        parseAndSave(result?.contents.toString())
        Log.d("my_logs", "finishing")
        finish()
    }

    private fun parseAndSave(url: String): Boolean {
        val dao = AppDatabase.getDatabase(application).totpDao()
        Log.d("my_logs", url)
        try {
            val uri = Uri.parse(url)
            val secret = uri.getQueryParameter("secret").toString()
            val issuer = uri.getQueryParameter("issuer").toString()
            val totp = Account(domain = issuer, salt = secret)
            Log.d("my_logs", "saved")
            dao.insert(totp)
            return true

        } catch (e: Exception) {
            Log.d("my_logs", "failed to parse")
            return false
        }

    }


}