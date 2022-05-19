package com.internshala.filter

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.internshala.filter.adapter.GalleryAdapter
import com.internshala.filter.remote.Service
import com.internshala.filter.repo.RemoteRepo
import com.internshala.filter.viewmodel.GalleryViewModel
import com.internshala.filter.viewmodel.GalleryViewModelFactory
import kotlinx.coroutines.*

@SuppressLint("StaticFieldLeak")
private lateinit var progressBar: ProgressBar

@DelicateCoroutinesApi
class GalleryActivity : AppCompatActivity() {
    private lateinit var viewModel: GalleryViewModel
    private lateinit var rvGallery: RecyclerView
    private lateinit var adapter: GalleryAdapter
    private lateinit var tvNoGallery: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        backToHome()
        GlobalScope.launch(Dispatchers.Main) {

            getPhotos()
        }
    }

    private fun backToHome() {
        val btnHome: ImageButton = findViewById(R.id.btn_home)
        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initGalleryRV() {
        rvGallery = findViewById(R.id.rv_image)
        rvGallery.layoutManager = LinearLayoutManager(applicationContext)
        rvGallery.adapter = adapter
    }

    private suspend fun getPhotos() {

        tvNoGallery = findViewById(R.id.no_gallery)
        progressBar = findViewById(R.id.progress_bar)
        progressBar.visibility = View.VISIBLE
        tvNoGallery.visibility = View.INVISIBLE

        viewModel =
            ViewModelProvider(
                this,
                GalleryViewModelFactory(RemoteRepo(Service.getInstance()!!))
            ).get(
                GalleryViewModel::class.java
            )

        viewModel.getAllImages(this)
        viewModel.imageList.observe(this, Observer {
            if (viewModel.failure) {
                tvNoGallery.visibility = View.VISIBLE
                progressBar.visibility = View.GONE

            } else {
                tvNoGallery.visibility = View.INVISIBLE
                adapter = GalleryAdapter(it, applicationContext)
                initGalleryRV()
                rvGallery.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        })


    }

}