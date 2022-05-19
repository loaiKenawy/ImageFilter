package com.internshala.filter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import coil.clear
import com.internshala.filter.model.ImageEntity
import com.internshala.filter.remote.Service
import com.internshala.filter.repo.RemoteRepo
import com.internshala.filter.utility.ImageConverter
import com.internshala.filter.utility.ImageFilters
import com.internshala.filter.viewmodel.MainViewModel
import com.internshala.filter.viewmodel.MainViewModelFactory
import kotlinx.coroutines.*


private const val pickImage = 100

private lateinit var viewModel: MainViewModel

private var imageUri: Uri? = null

private lateinit var filteredImage: Bitmap

private val filter: ImageFilters = ImageFilters()


@SuppressLint("StaticFieldLeak")
private lateinit var post: Button

@SuppressLint("StaticFieldLeak")
private lateinit var btnOpenGallery: Button

@SuppressLint("StaticFieldLeak")
private lateinit var btnBlackWhite: Button

@SuppressLint("StaticFieldLeak")
private lateinit var btnBlur: Button

@SuppressLint("StaticFieldLeak")
private lateinit var btnThreshHold: Button

@SuppressLint("StaticFieldLeak")
private lateinit var image: ImageView

@SuppressLint("StaticFieldLeak")
private lateinit var gallery: ImageButton

@SuppressLint("StaticFieldLeak")
private lateinit var progressBar: ProgressBar

@SuppressLint("StaticFieldLeak")
private lateinit var threshHoldBar: SeekBar


@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

        gallery.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
            image.setImageBitmap(null)
            btnOpenGallery.visibility = View.VISIBLE
        }

        btnOpenGallery.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)

        }

        post.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                postImage()
            }

        }
        btnBlackWhite.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                image.setImageBitmap(setBlackWhiteFilter())

            }
        }
        btnBlur.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                image.setImageBitmap(setBlurFilter())
            }
        }
        btnThreshHold.setOnClickListener {
            var threshHold = 0
            threshHoldBar.visibility = View.VISIBLE
            threshHoldBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    threshHold = p1
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                    GlobalScope.launch(Dispatchers.Main) {
                        image.setImageBitmap(setThreshHoldFilter(threshHold))
                    }
                }

            })
        }

    }

    private fun initViews() {
        post = findViewById(R.id.btn_post)
        btnOpenGallery = findViewById(R.id.btn_open_gallery)
        image = findViewById(R.id.image)
        gallery = findViewById(R.id.btn_gallery)
        btnBlackWhite = findViewById(R.id.black_and_white)
        progressBar = findViewById(R.id.progress_bar)
        btnBlur = findViewById(R.id.bluer)
        btnThreshHold = findViewById(R.id.btn_th)
        threshHoldBar = findViewById(R.id.thresh_hold_bar)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            image.setImageURI(imageUri)
            btnOpenGallery.visibility = View.INVISIBLE
        }
    }

    private suspend fun setBlackWhiteFilter(): Bitmap? {
        progressBar.visibility = View.VISIBLE
        if (imageUri != null) {
            val converter = ImageConverter(applicationContext)
            filteredImage = converter.getBitmap(imageUri)
            val copy = filteredImage.copy(Bitmap.Config.ARGB_8888, true)
            filteredImage = filter.filterBlackAndWhite(copy)
            progressBar.visibility = View.GONE
            return filteredImage
        }
        progressBar.visibility = View.GONE
        return null
    }

    private suspend fun setBlurFilter(): Bitmap? {
        progressBar.visibility = View.VISIBLE
        if (imageUri != null) {
            val converter = ImageConverter(applicationContext)
            filteredImage = converter.getBitmap(imageUri)
            val copy = filteredImage.copy(Bitmap.Config.ARGB_8888, true)
            filteredImage = filter.filterBlur(copy, 5)
            progressBar.visibility = View.GONE
            return filteredImage
        }
        progressBar.visibility = View.GONE
        return null
    }

    private suspend fun setThreshHoldFilter(threshHold: Int): Bitmap? {
        progressBar.visibility = View.VISIBLE
        if (imageUri != null) {
            val converter = ImageConverter(applicationContext)
            filteredImage = converter.getBitmap(imageUri)
            val copy = filteredImage.copy(Bitmap.Config.ARGB_8888, true)
            filteredImage = filter.filterBlur(copy, threshHold)
            progressBar.visibility = View.GONE
            return filteredImage
        }
        progressBar.visibility = View.GONE
        return null
    }

    private fun postImage() {
        progressBar.visibility = View.VISIBLE
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(RemoteRepo(Service.getInstance()!!))).get(
                MainViewModel::class.java
            )
        val converter = ImageConverter(applicationContext)
        if (imageUri != null) {
            Toast.makeText(applicationContext, "Saving...", Toast.LENGTH_LONG).show()
            viewModel.postImage(
                ImageEntity(
                    "ImageName",
                    converter.toByteArray(filteredImage)
                ), this
            )
        }
        image.setImageBitmap(null)
        progressBar.visibility = View.GONE
        btnOpenGallery.visibility = View.VISIBLE
    }
}
