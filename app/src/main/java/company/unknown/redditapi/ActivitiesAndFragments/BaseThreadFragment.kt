package company.unknown.redditapi.ActivitiesAndFragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import company.unknown.redditapi.DataClasses.RedditThread
import company.unknown.redditapi.OtherFiles.getFormattedDate
import company.unknown.redditapi.R
import kotlinx.android.synthetic.main.self_thread_fragment_layout.view.*

open class BaseThreadFragment : Fragment(){

    protected fun initializeWidgets(view : View, thread : RedditThread){
        initializeTextViews(view, thread)
        switchWidgetsBasedOnLength(view, thread)
        initializeCommentSection(thread)
    }

    private fun initializeTextViews(view : View, redditThread: RedditThread){
        view.subreddit_name.text = redditThread.subreddit
        view.titleTV.text = redditThread.title
        view.timeTV.text = getFormattedDate(redditThread)
        view.scoreTV.text = redditThread.score.toString()
        view.commentsTV.text = redditThread.numberOfComments.toString()
    }

    private fun switchWidgetsBasedOnLength(view : View, redditThread : RedditThread){
        if(redditThread.author.length > 15){
            view.postedBy.visibility = View.GONE
            view.postedByExtended.text = "posted by u/" + redditThread.author
            view.postedByExtended.visibility = View.VISIBLE
        }else
            view.postedBy.text = "posted by u/" + redditThread.author
    }

    private fun initializeCommentSection(redditThread: RedditThread){
        val fragmentTransaction = childFragmentManager.beginTransaction()

        val fragment = CommentSectionFragment()
        val bundle = Bundle()
        bundle.putString("permalink", redditThread.permalink)
        fragment.arguments = bundle

        fragmentTransaction.replace(R.id.commentSectionFrame, fragment)
        fragmentTransaction.commit()
    }
}