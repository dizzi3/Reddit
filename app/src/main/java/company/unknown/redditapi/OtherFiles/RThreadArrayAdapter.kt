package company.unknown.redditapi.OtherFiles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import company.unknown.redditapi.DataClasses.RedditThread
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

        if(result.URL.isNotEmpty() && isURLImageOrGif(result.URL))
            LoadImageTask(view.imageView, context).execute(result.URL)

        return view
    }

    private fun setValuesForWidgets(view : View, redditThread : RedditThread){

        view.subreddit_name.text = redditThread.Subreddit
        view.titleTV.text = redditThread.title
        view.timeTV.text = getFormattedDate(redditThread)
        view.scoreTV.text = "score: " + redditThread.score.toString()
        view.commentsTV.text = "comments: " + redditThread.numberOfComments.toString()


        if(redditThread.author.length > 15){
            view.postedBy.visibility = View.GONE
            view.postedByExtended.text = "posted by u/" + redditThread.author
            view.postedByExtended.visibility = View.VISIBLE
        }else
            view.postedBy.text = "posted by u/" + redditThread.author
    }

    private fun isURLImageOrGif(url : String) : Boolean{
        val URLExtension = url.substring(url.length-4)

        val imgExtensions = ArrayList<String>()
        imgExtensions.add(".jpg")
        imgExtensions.add(".png")
        imgExtensions.add(".gif")
        return imgExtensions.contains(URLExtension)
    }

    override fun getCount(): Int {
        return redditThreads.size
    }
}