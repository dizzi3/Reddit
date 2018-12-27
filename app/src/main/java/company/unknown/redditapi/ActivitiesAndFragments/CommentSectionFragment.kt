package company.unknown.redditapi.ActivitiesAndFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import company.unknown.redditapi.CommentSectionFiles.Comment
import company.unknown.redditapi.CommentSectionFiles.ExpandableCommentGroup
import company.unknown.redditapi.R
import company.unknown.redditapi.Tasks.LoadCommentsTask
import kotlinx.android.synthetic.main.comment_section_fragment_layout.view.*

class CommentSectionFragment : Fragment(){

    private lateinit var threadPermalink : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutView = inflater.inflate(R.layout.comment_section_fragment_layout, container, false)

        threadPermalink = arguments?.getString("permalink") ?: ""

        return layoutView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LoadCommentsTask(this, context!!).execute(threadPermalink)
    }

    fun setComments(comments : ArrayList<Comment>){

        val groupAdapter = GroupAdapter<ViewHolder>()
        if(view != null) {
            view!!.recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = groupAdapter
            }

            for (comment in comments) {
                ExpandableCommentGroup(comment).apply {
                    groupAdapter.add(this)
                }
            }
        }
    }
}