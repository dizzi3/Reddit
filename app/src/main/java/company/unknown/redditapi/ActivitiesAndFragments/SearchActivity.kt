package company.unknown.redditapi.ActivitiesAndFragments

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import company.unknown.redditapi.DataClasses.RedditThread
import company.unknown.redditapi.R
import company.unknown.redditapi.Tasks.SearchRedditTask
import kotlinx.android.synthetic.main.search_layout.*

class SearchActivity : AppCompatActivity() {

    private val RESULTS_LIMIT = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_layout)

        setupViews()
    }

    private fun setupViews(){
        setupSpinner()
        setupCheckBox()
        setupSearchButton()
    }

    private fun setupSpinner(){
        ArrayAdapter.createFromResource(this, R.array.viewing_options,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            optionsSpinner.adapter = adapter
        }
    }

    private fun setupCheckBox(){
        queryCheckBox.setOnClickListener {
            queryET.isEnabled = queryCheckBox.isChecked
        }
    }

    private fun setupSearchButton(){
        searchButton.setOnClickListener {
            //val subreddit = subredditET.text
            //TODO: CHEANGE IT BACK
            val subreddit = "globaloffensive"

            val sortBy = optionsSpinner.selectedItem.toString().toLowerCase()

            val query : String
            if(queryCheckBox.isChecked) {
                query = queryET.text.toString()
                SearchRedditTask(this).execute("https://www.reddit.com/r/" +
                        subreddit + "/search.json?q=" + query + "&sort=" + sortBy + "&restrict_sr=1&limit="
                        + RESULTS_LIMIT.toString())
            }
            else
                SearchRedditTask(this).execute("https://www.reddit.com/r/" + subreddit +
                        "/" + sortBy + ".json?restrict_sr=1&limit=" + RESULTS_LIMIT.toString())
        }
    }

    fun displayResulsActivity(redditThreads : ArrayList<RedditThread>){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("redditThreads", redditThreads)
        startActivity(intent)
    }
}
