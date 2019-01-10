package company.unknown.redditapi.ActivitiesAndFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import company.unknown.redditapi.DataClasses.LinkThread
import company.unknown.redditapi.OtherFiles.OnLinkClickedListener
import company.unknown.redditapi.R
import company.unknown.redditapi.Tasks.LoadImageTask
import kotlinx.android.synthetic.main.link_thread_fragment_layout.view.*

class LinkThreadFragment : BaseThreadFragment(){

    lateinit var linkThread : LinkThread

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.link_thread_fragment_layout, container, false)

        linkThread = arguments?.getSerializable("thread") as LinkThread
        initializeWidgetsWithoutSwitchingPostedBy(view, linkThread)
        initializeURLAndThumbnail(view)
        setOnClickListenerForLayout(view)

        return view
    }

    private fun initializeURLAndThumbnail(view : View){
        view.urlTV.text = linkThread.URL
        LoadImageTask(view.thumbnail, context as AppCompatActivity).execute(linkThread.thumbnailURL)
    }

    private fun setOnClickListenerForLayout(view : View){
        view.subLayout.setOnClickListener {
            (context as OnLinkClickedListener).performLinkClicked(linkThread.URL)
        }
    }
}