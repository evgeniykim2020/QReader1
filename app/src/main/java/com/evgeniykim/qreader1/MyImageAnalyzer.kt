package com.evgeniykim.qreader1

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.FragmentManager
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class MyImageAnalyzer(
    private val fragmentManager: FragmentManager
): ImageAnalysis.Analyzer {

    private val bottomSheet = BarcodeResultBottomSheet()

    override fun analyze(imageProxy: ImageProxy) {
        scanBarCode(imageProxy)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun scanBarCode(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
        val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()
        scanner.process(inputImage)
            .addOnCompleteListener {
                imageProxy.close()
                if (it.isSuccessful) {
                    readBarCodeData(it.result as List<Barcode>)
                } else {
                    it.exception?.printStackTrace()
                }
            }

        }
    }

    private fun readBarCodeData(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            when (barcode.valueType) {
                Barcode.TYPE_URL -> {
                    if (!bottomSheet.isAdded) {
                        bottomSheet.show(fragmentManager, "")
                        bottomSheet.updateURL(barcode.url?.url.toString())
                    }


                }
            }
        }
    }

}