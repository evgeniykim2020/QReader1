package com.evgeniykim.qreader1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_barcode_data.view.*
import org.jsoup.Jsoup
import java.util.concurrent.Executors

class BarcodeResultBottomSheet: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_barcode_data, container, false)


    fun updateURL(url: String) {
        fetchUrlMetaData(url) { title, desc ->
            view?.apply {
                text_view_title?.text = title
                text_view_desc?.text = desc
                text_view_link?.text = url
                text_view_visit_link.setOnClickListener { _ ->
                    Intent(Intent.ACTION_VIEW).also {
                        it.data = Uri.parse(url)
                        startActivity(it)
                    }
                }
            }
        }
    }


    fun fetchUrlMetaData(url: String, callback: (title: String, desc: String) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            val doc = Jsoup.connect(url).get()
            val desc = doc.select("meta[name=description]")[0].attr("connect")
            handler.post {
                callback(doc.title(), desc)
            }
        }
    }


}