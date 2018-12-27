package company.unknown.redditapi.Tasks

import android.os.AsyncTask
import company.unknown.redditapi.ActivitiesAndFragments.SearchActivity
import company.unknown.redditapi.CommentSectionFiles.Comment
import company.unknown.redditapi.DataClasses.RedditThread
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
        val url = data.getString("url")
        val score = data.getInt("score")
        val createdUtc = data.getInt("created_utc")
        val author = data.getString("author")
        val numberOfComments = data.getInt("num_comments")
        val isSelf = data.getBoolean("is_self")
        val selfTextHtml = data.getString("selftext_html")
        var permalink = "https://www.reddit.com" + data.getString("permalink")
        //Removing the '/' at last position from permalink
        permalink = permalink.substring(0, permalink.length-1)
        permalink += ".json"

        return RedditThread(subreddit, title, url, author, score, createdUtc,
                numberOfComments, isSelf, selfTextHtml, permalink)
    }

    override fun onPostExecute(redditThreads: ArrayList<RedditThread>?) {
        super.onPostExecute(redditThreads)

        if(redditThreads != null)
            context.displayResulsActivity(redditThreads)
    }
}