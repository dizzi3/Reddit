package company.unknown.redditapi.ActivitiesAndFragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import company.unknown.redditapi.DataClasses.*
import company.unknown.redditapi.OtherFiles.getFormattedDate
import company.unknown.redditapi.R
import kotlinx.android.synthetic.main.self_thread_fragment_layout.view.*
import android.text.style.URLSpan
import android.text.SpannableStringBuilder
import android.widget.TextView
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import company.unknown.redditapi.OtherFiles.HtmlToTextConverter
import company.unknown.redditapi.OtherFiles.OnLinkClickedListener
import kotlinx.android.synthetic.main.self_thread_fragment_layout.*


class SelfThreadFragment : Fragment(){

    private lateinit var redditThread : RedditThread

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.self_thread_fragment_layout, container, false)
        redditThread = arguments?.getSerializable("thread") as RedditThread

        initializeWidgets(view)

        return view
    }

    private fun initializeWidgets(view : View){
        view.subreddit_name.text = redditThread.Subreddit
        view.titleTV.text = redditThread.title
        view.timeTV.text = getFormattedDate(redditThread)
        view.scoreTV.text = "score: " + redditThread.score.toString()
        view.commentsTV.text = "comments: " + redditThread.numberOfComments.toString()

        setSelfText(view, redditThread)
        switchWidgetsBasedOnLength(view, redditThread)

        initializeCommentSection()
    }

    private fun initializeCommentSection(){
        val fragmentTransaction = childFragmentManager.beginTransaction()

        val fragment = CommentSectionFragment()
        val bundle = Bundle()
        bundle.putString("permalink", redditThread.permalink)
        fragment.arguments = bundle

        fragmentTransaction.add(R.id.commentSectionFrame, fragment)
        fragmentTransaction.commit()
    }

    private fun setSelfText(view : View, redditThread: RedditThread){
        HtmlToTextConverter.setTextView(view.self_text, redditThread.selfTextHtml, context!!)
    }

    private fun switchWidgetsBasedOnLength(view : View, redditThread : RedditThread){
        if(redditThread.author.length > 15){
            view.postedBy.visibility = View.GONE
            view.postedByExtended.text = "posted by u/" + redditThread.author
            view.postedByExtended.visibility = View.VISIBLE
        }else
            view.postedBy.text = "posted by u/" + redditThread.author
    }
}