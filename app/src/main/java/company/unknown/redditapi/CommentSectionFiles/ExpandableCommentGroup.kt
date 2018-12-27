package company.unknown.redditapi.CommentSectionFiles

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import company.unknown.redditapi.OtherFiles.HtmlToTextConverter
import company.unknown.redditapi.R
import kotlinx.android.synthetic.main.single_comment_layout.view.*

class ExpandableCommentGroup constructor(comment : Comment, depth: Int = 0)
    : ExpandableGroup(ExpandableCommentItem(comment, depth)){

    init{
        for(comm in comment.childs){
            add(ExpandableCommentGroup(comm, depth.plus(1)))
        }
    }

    class ExpandableCommentItem constructor(private val comment : Comment,
            private val depth : Int) : Item<ViewHolder>(), ExpandableItem{

        private lateinit var expandableGroup : ExpandableGroup

        override fun bind(viewHolder: ViewHolder, position: Int) {
            addingDepthViews(viewHolder)

            viewHolder.itemView.authorTV.text = comment.author
            viewHolder.itemView.dateTV.text = comment.date
            viewHolder.itemView.commentBodyTV.text = comment.body
            viewHolder.itemView.commentBodyTV.movementMethod = LinkMovementMethod.getInstance()
            viewHolder.itemView.upvotesTV.text = comment.upvotes

            /*
            viewHolder.itemView.apply{
                setOnClickListener {
                    expandableGroup.onToggleExpanded()
                }
            }*/
            viewHolder.itemView.authorTV.apply {
                setOnClickListener {
                    expandableGroup.onToggleExpanded()
                }
            }
            viewHolder.itemView.dateTV.apply {
                setOnClickListener {
                    expandableGroup.onToggleExpanded()
                }
            }
            viewHolder.itemView.upvotesTV.apply {
                setOnClickListener {
                    expandableGroup.onToggleExpanded()
                }
            }
        }

        private fun addingDepthViews(viewHolder : ViewHolder){
            viewHolder.itemView.separatorContainer.removeAllViews()
            viewHolder.itemView.separatorContainer.visibility =
                    if(depth > 0)
                        View.VISIBLE
                    else
                        View.GONE

            for(i in 1..depth){
                val v : View = LayoutInflater.from(viewHolder.itemView.context)
                        .inflate(R.layout.separator_view, viewHolder.itemView.separatorContainer, false)
                viewHolder.itemView.separatorContainer.addView(v)
            }
        }

        override fun getLayout(): Int = R.layout.single_comment_layout

        override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
            this.expandableGroup = onToggleListener
        }
    }
}