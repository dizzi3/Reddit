package company.unknown.redditapi.CommentSectionFiles

import android.text.SpannableStringBuilder
import java.io.Serializable

class Comment(val author : String, val upvotes : String, val date : String,
              val body : SpannableStringBuilder, val childs : ArrayList<Comment>) : Serializable