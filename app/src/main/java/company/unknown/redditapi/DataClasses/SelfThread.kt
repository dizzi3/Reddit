package company.unknown.redditapi.DataClasses

import java.io.Serializable

class SelfThread(subreddit : String, title : String, URL : String, author : String,
                 score : Int, createdUTC : Int, numberOfComments : Int, permalink : String,
                 val selfTextHtml : String)
                : RedditThread(subreddit, title, URL, author, score, createdUTC,
                numberOfComments, permalink), Serializable