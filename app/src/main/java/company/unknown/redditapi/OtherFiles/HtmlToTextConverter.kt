package company.unknown.redditapi.OtherFiles

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast

class HtmlToTextConverter{
    companion object {

        fun setTextView(textView: TextView, html: String, context : Context) {
            //Reason I'm using fromHtml(fromHtml().toString))
            //https://stackoverflow.com/questions/2918920/decode-html-entities-in-android
            val sequence = fromHtml(fromHtml(html).toString())
            val strBuilder = SpannableStringBuilder(sequence)
            val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
            for (span in urls)
                makeLinkClickable(strBuilder, span, context)

            textView.text = strBuilder
            textView.movementMethod = LinkMovementMethod.getInstance()
        }

        fun getSpannableStringBuilder(html : String, context : Context) : SpannableStringBuilder{
            //Reason I'm using fromHtml(fromHtml().toString))
            //https://stackoverflow.com/questions/2918920/decode-html-entities-in-android
            val sequence = fromHtml(fromHtml(html).toString())
            val strBuilder = SpannableStringBuilder(sequence)
            val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
            for (span in urls)
                makeLinkClickable(strBuilder, span, context)

            return strBuilder
        }

        private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan, context : Context) {
            val start = strBuilder.getSpanStart(span)
            val end = strBuilder.getSpanEnd(span)
            val flags = strBuilder.getSpanFlags(span)
            val clickable = object : ClickableSpan() {
                override fun onClick(view: View) {
                    val listener = context as OnLinkClickedListener
                    listener.performLinkClicked(span.url)
                }
            }

            strBuilder.setSpan(clickable, start, end, flags)
            strBuilder.removeSpan(span)
        }

        @SuppressWarnings("deprecation")
        private fun fromHtml(html : String) : Spanned {
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
            else
                Html.fromHtml(html)
        }

    }
}