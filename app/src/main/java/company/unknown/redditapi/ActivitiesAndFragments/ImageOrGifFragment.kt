package company.unknown.redditapi.ActivitiesAndFragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import company.unknown.redditapi.DataClasses.ImageOrGifThread
import company.unknown.redditapi.DataClasses.RedditThread
import company.unknown.redditapi.DataClasses.ThreadType
import company.unknown.redditapi.R
import company.unknown.redditapi.Tasks.LoadImageTask
import kotlinx.android.synthetic.main.image_gif_fragment_layout.*
import kotlinx.android.synthetic.main.image_gif_fragment_layout.view.*

class ImageOrGifFragment : BaseThreadFragment(){

    lateinit var thread : ImageOrGifThread

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.image_gif_fragment_layout, container, false)

        thread = arguments?.getSerializable("thread") as ImageOrGifThread
        initializeWidgets(view, thread)

        if(thread.type == ThreadType.IMAGE)
            initializeImageView(view)
        else
            initializeVideoView(view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adjustVisibility()
    }

    private fun adjustVisibility(){
        if(thread.type == ThreadType.IMAGE){
            imageView.visibility = View.VISIBLE
            videoView.visibility = View.GONE
        }else{
            videoView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
        }
    }

    private fun initializeImageView(view : View){
        val imageView = view.imageView
        val context = context as AppCompatActivity

        LoadImageTask(imageView, context).execute(thread.URL)
    }

    private fun initializeVideoView(view : View){
        val uri = Uri.parse(thread.URL)
        val videoView = view.videoView

        videoView.setVideoURI(uri)
        videoView.start()

        setVideoViewToLoop(videoView)
    }

    private fun setVideoViewToLoop(videoView : VideoView){
        videoView.setOnCompletionListener {
            videoView.start()
        }
    }
}