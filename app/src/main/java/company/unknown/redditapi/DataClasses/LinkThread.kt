package company.unknown.redditapi.DataClasses

import java.io.Serializable

class LinkThread(subreddit : String, title : String, URL : String, author : String,
                       score : Int, createdUTC : Int, numberOfComments : Int, permalink : String,
                       val thumbnailURL : String)
    : RedditThread(subreddit, title, URL, author, score, createdUTC,
        numberOfComments, permalink), Serializable