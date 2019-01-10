package company.unknown.redditapi.ActivitiesAndFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import company.unknown.redditapi.DataClasses.RedditThread
import company.unknown.redditapi.OtherFiles.OnThreadSelectedListener
import company.unknown.redditapi.OtherFiles.RThreadArrayAdapter
import company.unknown.redditapi.R
import kotlinx.android.synthetic.main.search_result_fragment_layout.*

class SearchResultFragment : Fragment(){

    private lateinit var  redditThreads : ArrayList<RedditThread>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_result_fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initialize()
    }

    private fun initialize(){
        initializeThreads()
        initializeListView()

        subredditTV.text = redditThreads[0].subreddit
    }

    private fun initializeThreads(){
        val threads = arguments?.get("threads")
        if(threads != null) {
            @Suppress("UNCHECKED_CAST")
            redditThreads = threads as ArrayList<RedditThread>
        }
    }

    private fun initializeListView(){
        val adapter = RThreadArrayAdapter(activity as AppCompatActivity, redditThreads)
        threadsListView.adapter = adapter

        threadsListView.setOnItemClickListener { _, _, position, _ ->
            val listener = context as OnThreadSelectedListener
            listener.onThreadSelected(position)
        }
    }
}