package company.unknown.redditapi.DataClasses

import java.io.Serializable

class ImageOrGifThread(subreddit : String, title : String, URL : String, author : String,
                 score : Int, createdUTC : Int, numberOfComments : Int, permalink : String,
                 val type : ThreadType)
        : RedditThread(subreddit, title, URL, author, score, createdUTC,
        numberOfComments, permalink), Serializable

enum class ThreadType{
    GIF,
    IMAGE
}