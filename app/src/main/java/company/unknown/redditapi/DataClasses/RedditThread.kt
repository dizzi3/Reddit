package company.unknown.redditapi.DataClasses

import java.io.Serializable

class RedditThread(val Subreddit : String, val title : String, val URL : String, val author : String,
                   val score : Int, val createdUTC : Int, val numberOfComments : Int,
                   val is_self : Boolean, val selfTextHtml : String, val permalink : String) : Serializable