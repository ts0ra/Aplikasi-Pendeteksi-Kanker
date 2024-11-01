package com.dicoding.asclepius.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.viewModels
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.view.activity.ResultActivity
import com.dicoding.asclepius.view.viewmodel.HomeViewModel
import com.yalantis.ucrop.UCrop
import java.io.File

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startUCrop(uri)
        } else {
            logAndToast("Gallery: No media selected")
        }
    }

    private val launcherUCrop = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                viewModel.setCurrentImageUri(result.data?.let { UCrop.getOutput(it) })
            }
            UCrop.RESULT_ERROR -> {
                val cropError = UCrop.getError(result.data!!)
                cropError?.let { logAndToast("UCrop: $${it.message.toString()}") }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentImageUri.observe(viewLifecycleOwner) {
            showImage(it)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            viewModel.currentImageUri.value?.let {
                analyzeImage(it)
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startUCrop(uri: Uri) {
        val tempFile = File.createTempFile("cropped_", ".jpg", requireActivity().cacheDir)
        val destinationUri = Uri.fromFile(tempFile)
        val uCropIntent = UCrop.of(uri, destinationUri)
            .getIntent(requireActivity())

        launcherUCrop.launch(uCropIntent)
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage(uri: Uri?) {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        uri?.let {
            binding.previewImageView.setImageURI(it)
        } ?: run {
            logAndToast("Image URI (Show Image): Empty")
        }
    }

    private fun analyzeImage(uri: Uri) {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        val intent = Intent(requireActivity(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
        startActivity(intent)
    }

    private fun logAndToast(message: String) {
        Log.d("MainActivity", message)
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}