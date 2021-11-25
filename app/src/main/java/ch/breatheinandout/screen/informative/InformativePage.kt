package ch.breatheinandout.screen.informative

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.breatheinandout.R
import com.orhanobut.logger.Logger

private const val ARG_URL = "arg_url_informative"
private const val ARG_FROM = "arg_from_text"

class InformativePage : Fragment() {
    private var targetUrl: String = ""
    private var textFrom: String = ""

    private lateinit var webView: WebView
    private lateinit var textViewFrom: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            targetUrl = it.getString(ARG_URL) ?: ""
            textFrom = it.getString(ARG_FROM) ?: ""
        }
        Logger.v("[ARG] URL: $targetUrl , FROM: $textFrom")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_informative, container, false)
        webView = view.findViewById(R.id.webView)
        textViewFrom = view.findViewById(R.id.text_from)
        progressBar = view.findViewById(R.id.progressBar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewFrom.text = textFrom
        loadWebPage(targetUrl)
    }

    private fun loadWebPage(url: String) {
        showProgressBar(visible = true)
        try {
            if (url.isEmpty()) {
                throw IllegalArgumentException("url is not correct.")
            }

            webView.apply {
                settings.apply {
                    javaScriptEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                }
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        } catch (e: Exception) {
            Logger.e("url is empty.")
        } finally {
            showProgressBar(visible = false)
        }
    }

    private fun showProgressBar(visible: Boolean) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
            webView.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            webView.visibility = View.VISIBLE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String, from: String) =
            InformativePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_URL, url)
                    putString(ARG_FROM, from)
                }
            }
    }
}