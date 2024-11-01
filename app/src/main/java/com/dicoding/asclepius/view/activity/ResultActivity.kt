package com.dicoding.asclepius.view.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.database.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.viewmodel.HistoryViewModel
import com.dicoding.asclepius.view.viewmodel.ViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }

        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@ResultActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                println(it)
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }
                                val isCancer = sortedCategories[0].label
                                val confidenceScore = NumberFormat.getPercentInstance()
                                    .format(sortedCategories[0].score)
                                    .trim()
                                binding.resultText.text =
                                    getString(R.string.result_text, isCancer, confidenceScore)
                                binding.saveResultBtn.setOnClickListener {
                                    saveResult(imageUri, isCancer, confidenceScore)
                                    Toast.makeText(this@ResultActivity, "Result is saved", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            } else {
                                Toast.makeText(this@ResultActivity, "No results", Toast.LENGTH_SHORT).show()
                                binding.resultText.text = ""
                            }
                        }
                    }
                }
            }
        )

        imageClassifierHelper.classifyStaticImage(imageUri)
    }

    private fun saveResult(imageUri: Uri?, isCancer: String, confidenceScore: String) {
        imageUri?.let {
            val pathFile = saveUriAsFile(it)
            val history = HistoryEntity(
                result = getString(R.string.item_result_history, isCancer),
                score = getString(R.string.item_score_history, confidenceScore),
                imagePath = pathFile.toString())
            viewModel.addHistory(history)
        }
    }

    private fun saveUriAsFile(imageUri: Uri): String? {
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val destinationFile = File(this.filesDir, fileName)
        return try {
            val inputStream: InputStream? = this.contentResolver.openInputStream(imageUri)
            val outputStream: OutputStream = FileOutputStream(destinationFile)

            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream?.read(buffer).also { length = it ?: -1 } != -1) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            outputStream.close()
            inputStream?.close()

            destinationFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}