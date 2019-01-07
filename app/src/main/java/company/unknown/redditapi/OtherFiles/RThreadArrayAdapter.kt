package company.unknown.redditapi.OtherFiles

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import company.unknown.redditapi.DataClasses.ImageOrGifThread
import company.unknown.redditapi.DataClasses.RedditThread
import company.unknown.redditapi.DataClasses.ThreadType
import company.unknown.redditapi.R
import company.unknown.redditapi.Tasks.LoadImageTask
import kotlinx.android.synthetic.main.thread_list_item.view.*
import kotlin.collections.ArrayList


class RThreadArrayAdapter(private val context : AppCompatActivity, private val redditThreads : ArrayList<RedditThread>)
                    : ArrayAdapter<RedditThread>(context, -1){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.thread_list_item, parent, false)

        val result = redditThreads[position]

        setValuesForWidgets(view, result)

        if(result is ImageOrGifThread){
            if(result.type == ThreadType.IMAGE)
                LoadImageTask(view.imageView, context).execute(result.URL)
            else
                initializeVideoView(result, view)
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

        view.subreddit_name.text = redditThread.subreddit
        view.titleTV.text = redditThread.title
        view.timeTV.text = getFormattedDate(redditThread)
        view.scoreTV.text = redditThread.score.toString()
        view.commentsTV.text = redditThread.numberOfComments.toString()


        if(redditThread.author.length > 15){
            view.postedBy.visibility = View.GONE
            view.postedByExtended.text = "posted by u/" + redditThread.author
            view.postedByExtended.visibility = View.VISIBLE
        }else
            view.postedBy.text = "posted by u/" + redditThread.author
    }

    override fun getCount(): Int {
        return redditThreads.size
    }
}