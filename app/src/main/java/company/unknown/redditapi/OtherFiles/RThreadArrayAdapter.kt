package company.unknown.redditapi.OtherFiles

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import company.unknown.redditapi.DataClasses.ImageOrGifThread
import company.unknown.redditapi.DataClasses.LinkThread
import company.unknown.redditapi.DataClasses.RedditThread
import company.unknown.redditapi.DataClasses.ThreadType
import company.unknown.redditapi.R
import company.unknown.redditapi.Tasks.LoadImageTask
import kotlinx.android.synthetic.main.link_thread_list_item.view.urlTV as urlTV
import kotlinx.android.synthetic.main.link_thread_list_item.view.thumbnail as thumbnail
import kotlinx.android.synthetic.main.thread_list_item.view.*
import kotlin.collections.ArrayList


class RThreadArrayAdapter(private val context : AppCompatActivity, private val redditThreads : ArrayList<RedditThread>)
                    : ArrayAdapter<RedditThread>(context, -1){

    private val NORMAL_THREAD_TYPE = 0
    private val LINK_THREAD_TYPE = 1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = if(getItemViewType(position) == NORMAL_THREAD_TYPE)
            inflater.inflate(R.layout.thread_list_item, parent, false)
        else
            inflater.inflate(R.layout.link_thread_list_item, parent, false)


        val currentThread = redditThreads[position]
        setValuesForWidgets(view, currentThread)

        if(currentThread is ImageOrGifThread){
            if(currentThread.type == ThreadType.IMAGE)
                LoadImageTask(view.imageView, context).execute(currentThread.URL)
            else
                initializeVideoView(currentThread, view)
        }

        return view
    }

    private fun initializeVideoView(thread : ImageOrGifThread, view : View){
        val uri = Uri.parse(thread.URL)
        val videoView = view.videoView

        videoView.setVideoURI(uri)
        videoView.start()

        setVideoViewToLoop(videoView)

        view.imageView.visibility = View.GONE
        view.videoView.visibility = View.VISIBLE
    }

    private fun setVideoViewToLoop(videoView : VideoView){
        videoView.setOnCompletionListener {
            videoView.start()
        }
    }

    private fun setValuesForWidgets(view : View, redditThread : RedditThread){

        initializeCommonWidgets(view, redditThread)

        if(redditThread is LinkThread)
            initializeLinkThreadWidgets(view, redditThread)
        else
            switchPostedByWidgetBasedOnAuthorLength(view, redditThread)
    }

    private fun switchPostedByWidgetBasedOnAuthorLength(view : View, redditThread: RedditThread){
        if (redditThread.author.length > 15) {
            view.postedBy.visibility = View.GONE
            view.postedByExtended.text = "posted by u/" + redditThread.author
            view.postedByExtended.visibility = View.VISIBLE
        } else
            view.postedBy.text = "posted by u/" + redditThread.author
    }

    private fun initializeCommonWidgets(view : View, redditThread : RedditThread){
        view.titleTV.text = redditThread.title
        view.timeTV.text = getFormattedDate(redditThread)
        view.scoreTV.text = redditThread.score.toString()
        view.commentsTV.text = redditThread.numberOfComments.toString()
    }

    private fun initializeLinkThreadWidgets(view : View, linkThread : LinkThread){
        view.postedBy.text = "posted by u/" + linkThread.author

        if(linkThread.URL.length > 47)
            view.urlTV.text = linkThread.URL.substring(0, 47) + "..."
        else
            view.urlTV.text = linkThread.URL

        LoadImageTask(view.thumbnail, context).execute(linkThread.thumbnailURL)
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        return if(redditThreads[position] is LinkThread)
            LINK_THREAD_TYPE
        else
            NORMAL_THREAD_TYPE
    }

    override fun getCount(): Int {
        return redditThreads.size
    }
}