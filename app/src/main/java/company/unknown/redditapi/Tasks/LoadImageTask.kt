package company.unknown.redditapi.Tasks

import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

class LoadImageTask(private val imageView: ImageView, private val context : AppCompatActivity)
    : AsyncTask<String, Void, RequestBuilder<Drawable>>(){

    override fun doInBackground(vararg p0: String?): RequestBuilder<Drawable>? {
        return Glide.with(context).load(p0[0])
    }

    override fun onPostExecute(result: RequestBuilder<Drawable>?) {
        super.onPostExecute(result)

        if(result != null) {
            result.into(imageView)
            imageView.visibility = View.VISIBLE
        }
    }
}