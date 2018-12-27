package company.unknown.redditapi.ActivitiesAndFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import company.unknown.redditapi.R
import kotlinx.android.synthetic.main.browse_web_fragment_layout.*

class BrowseWebFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.browse_web_fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true

        if(arguments?.getString("url") != null) {
            val url = arguments?.getString("url")

            if(!loadSubredditURL(url))
                webView.loadUrl(url)
        }
    }

    private fun loadSubredditURL(url : String?) : Boolean{
        val urlCheck = url?.substring(IntRange(0,1))?.toLowerCase()
        val urlCheck2 = url?.substring(IntRange(0,2))?.toLowerCase()

        //TODO: MAYBE IMPLEMENT OPENING /r LINKS IN THIS APP INSTEAD OF IN WEBVIEW

        if(urlCheck == "r/") {
            webView.loadUrl("https://www.reddit.com/$url")
            return true
        }

        if(urlCheck2 == "/r/"){
            webView.loadUrl("https://www.reddit.com$url")
            return true
        }

        return false
    }
}