package company.unknown.redditapi.OtherFiles

import company.unknown.redditapi.DataClasses.RedditThread
import java.text.SimpleDateFormat
import java.util.*

fun getFormattedDate(redditThread : RedditThread) : String{
    return getFormattedDate(redditThread.createdUTC.toLong())
}

fun getFormattedDate(createdUTC : Long) : String{
    val date = Date(createdUTC * 1000L)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    format.timeZone = TimeZone.getDefault()
    return format.format(date)
}