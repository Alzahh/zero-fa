package com.example.zero_fa.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.no_fa.dao.PasswordDao
import com.example.zero_fa.AppDatabase
import com.example.zero_fa.R
import com.example.zero_fa.RvAdapter
import com.example.zero_fa.databinding.ActivityMainBinding
import com.example.zero_fa.modules.Network
import com.example.zero_fa.modules.PasswordRender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: PasswordDao
    private lateinit var pr: PasswordRender
    private lateinit var password_render_job: Job

    //    private lateinit var network_job: Job
    private lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    private val rvAdapter = RvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = AppDatabase.getDatabase(application).totpDao()

        pr = PasswordRender(rvAdapter, dao)
//        val network = Network(dao)


        password_render_job = CoroutineScope(Dispatchers.Main).launch { pr.passwordRender() }
//        network_job = CoroutineScope(Dispatchers.Main).launch { network.uploadPasswords() }


        cameraResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            password_render_job = CoroutineScope(Dispatchers.Main).launch { pr.passwordRender() }
            Log.d("my_logs", "restarted")
            update_passwords()

        }


        binding.apply {
            rvContent.layoutManager = LinearLayoutManager(this@MainActivity)
            rvContent.adapter = rvAdapter
            btAdd.setOnClickListener {
                launchCameraActionIfPermitted()
            }

            include.btNetwork.setOnClickListener {
                Log.d("my_logs", "clicked")
//                CoroutineScope(Dispatchers.Main).launch {
//                  Network().doGet()
//                }

            }
        }
    }

    private fun launchCameraActionIfPermitted() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            password_render_job.cancel()
            Log.d("my_logs", "job canceled")
            val intent = Intent(this, ScannerActivity::class.java)
            cameraResultLauncher.launch(intent)

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        }
    }
    private fun update_passwords(){
        val records = dao.getAll()
        var totps = pr.getTotps(records)
        val network = Network(dao)
        network.uploadPasswords(totps)
    }
}