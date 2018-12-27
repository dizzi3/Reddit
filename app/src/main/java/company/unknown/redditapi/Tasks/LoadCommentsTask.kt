package company.unknown.redditapi.Tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import company.unknown.redditapi.ActivitiesAndFragments.CommentSectionFragment
import company.unknown.redditapi.CommentSectionFiles.Comment
import company.unknown.redditapi.OtherFiles.HtmlToTextConverter
import company.unknown.redditapi.OtherFiles.getFormattedDate
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoadCommentsTask(private val fragment : CommentSectionFragment, private val context : Context)
    : AsyncTask<String, String, ArrayList<Comment>?>(){

    override fun doInBackground(vararg p0: String?): ArrayList<Comment>? {
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

            return getCommentsArrayFromJSON(buffer.toString())

        }catch(exception : Exception){}

        return null
    }

    private fun getCommentsArrayFromJSON(json : String) : ArrayList<Comment>?{

        if(json.isNotEmpty()){
            val commentsArrayJSON = JSONArray(json).getJSONObject(1).getJSONObject("data")
                    .getJSONArray("children")

            val comments = ArrayList<Comment>()

            for(index in 0 until commentsArrayJSON.length()){
                val commentJSON = commentsArrayJSON.getJSONObject(index).getJSONObject("data")

                val author = commentJSON.getString("author")
                val body = HtmlToTextConverter.getSpannableStringBuilder(
                        commentJSON.getString("body_html"), context)
                val score = commentJSON.getString("score")
                val date = getFormattedDate(commentJSON.getLong("created_utc"))

                val currentComment = Comment(author, score, date, body, ArrayList())

                comments.add(getSingleCommentFromJSONObject(commentJSON, currentComment))
            }

            return comments
        }

        return null
    }

    private fun getSingleCommentFromJSONObject(jsonObj : JSONObject, comment : Comment) : Comment{

        try{
            if(jsonObj.getJSONObject("replies") != null) {
                val childrenArray = jsonObj.getJSONObject("replies").getJSONObject("data")
                        .getJSONArray("children")
                for (index in 0 until childrenArray.length()) {
                    val commentJSON = childrenArray.getJSONObject(index).getJSONObject("data")

                    val author = commentJSON.getString("author")
                    Log.i("INFO", author)
                    //TODO: MAKE IT BODY HTML AND CONVERT
                    val body = HtmlToTextConverter.getSpannableStringBuilder(
                            commentJSON.getString("body_html"), context)
                    val score = commentJSON.getString("score")
                    val date = getFormattedDate(commentJSON.getLong("created_utc"))

                    val currentComment = Comment(author, score, date, body, ArrayList())

                    /*
                    comment.childs.add(currentComment)
                    getSingleCommentFromJSONObject(commentJSON, currentComment)
                    */
                    comment.childs.add(getSingleCommentFromJSONObject(commentJSON, currentComment))
                }
                return comment
            }
            return comment
        }catch (e : Exception){return comment}

        /*
        if(jsonObj.getJSONObject("replies").getJSONObject("data") != null){
            val childrenArray = jsonObj.getJSONObject("replies").getJSONObject("data")
                    .getJSONArray("children")
            for(index in 0 until childrenArray.length()){
                val commentJSON = childrenArray.getJSONObject(index).getJSONObject("data")

                val author = commentJSON.getString("author")
                Log.i("INFO", author)
                //TODO: MAKE IT BODY HTML AND CONVERT
                val body = commentJSON.getString("body")
                val score = commentJSON.getString("score")
                val date = getFormattedDate(commentJSON.getLong("created_utc"))

                val currentComment = Comment(author, score, date, body, ArrayList())

                /*
                comment.childs.add(currentComment)
                getSingleCommentFromJSONObject(commentJSON, currentComment)
                */
                comment.childs.add(getSingleCommentFromJSONObject(commentJSON, currentComment))
            }
            return comment
        }else {
            Log.i("INFO", "OUT OF")
            return comment
        }
        */
    }

    override fun onPostExecute(result: ArrayList<Comment>?) {
        super.onPostExecute(result)

        if(result != null)
            fragment.setComments(result)
        else
            Log.i("INFO", "NO COMMENTS")
    }
}