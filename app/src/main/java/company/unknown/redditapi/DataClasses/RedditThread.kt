package company.unknown.redditapi.DataClasses

import java.io.Serializable

open class RedditThread(val subreddit : String, val title : String, val URL : String, val author : String,
                        val score : Int, val createdUTC : Int, val numberOfComments : Int, val permalink : String)
                        : Serializable