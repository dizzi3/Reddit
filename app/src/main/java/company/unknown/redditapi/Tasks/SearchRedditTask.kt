package company.unknown.redditapi.Tasks

import android.os.AsyncTask
import android.util.Log
import company.unknown.redditapi.ActivitiesAndFragments.SearchActivity
import company.unknown.redditapi.CommentSectionFiles.Comment
import company.unknown.redditapi.DataClasses.ImageOrGifThread
import company.unknown.redditapi.DataClasses.RedditThread
import company.unknown.redditapi.DataClasses.SelfThread
import company.unknown.redditapi.DataClasses.ThreadType
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchRedditTask(private val context : SearchActivity)
    : AsyncTask<String, String, ArrayList<RedditThread>?>(){

    override fun doInBackground(vararg p0: String?): ArrayList<RedditThread>? {
        var connection : HttpURLConnection
        var reader : BufferedReader

        try{
            var url = URL(p0[0])
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            reader = BufferedReader(InputStreamReader(connection.inputStream))
            var buffer = StringBuffer()

            try {
                for (line in reader.readLine())
                    buffer.append(line)
            }catch(exception : Exception){}

            connection.disconnect()

           return decodeJSON(buffer.toString())

        }catch(exception : Exception){}

        return null
    }

    fun decodeJSON(json : String) : ArrayList<RedditThread>?{

        if(json.isNotEmpty()){

            val jsonObj = JSONObject(json).getJSONObject("data")
            val jsonArray = jsonObj.getJSONArray("children")

            var results = ArrayList<RedditThread>()

            for(i in 0 until jsonArray.length()){

                val obj = jsonArray.getJSONObject(i)
                val data = obj.getJSONObject("data")

                results.add(getResultFromJSONObject(data))
            }

            return results
        }

        return null
    }

    fun getResultFromJSONObject(data : JSONObject) : RedditThread {
        val title = data.getString("title")
        val subreddit = data.getString("subreddit_name_prefixed")
        val score = data.getInt("score")
        val createdUtc = data.getInt("created_utc")
        val author = data.getString("author")
        val numberOfComments = data.getInt("num_comments")
        val isSelf = data.getBoolean("is_self")
        var permalink = "https://www.reddit.com" + data.getString("permalink")
        //Removing the '/' at last position from permalink
        permalink = permalink.substring(0, permalink.length-1)
        permalink += ".json"


        try{
            val hint = data.getString("post_hint")

            if(hint == "hosted:video") {
                val url = data.getJSONObject("secure_media").getJSONObject("reddit_video")
                        .getString("fallback_url")
                return ImageOrGifThread(subreddit, title, url, author, score, createdUtc,
                        numberOfComments, permalink, ThreadType.GIF)
            }else if(hint == "image"){
                val url = data.getString("url")
                return ImageOrGifThread(subreddit, title, url, author, score, createdUtc,
                        numberOfComments, permalink, ThreadType.IMAGE)
            }
        }catch (e : Exception){}

        val url = data.getString("url")

        if(isSelf){
            val selfTextHtml = data.getString("selftext_html")
            return SelfThread(subreddit, title, url, author, score, createdUtc,
                    numberOfComments, permalink, selfTextHtml)
        }

        return RedditThread(subreddit, title, url, author, score, createdUtc,
                numberOfComments, permalink)
    }

    override fun onPostExecute(redditThreads: ArrayList<RedditThread>?) {
        super.onPostExecute(redditThreads)

        if(redditThreads != null)
            context.displayResulsActivity(redditThreads)
    }
}