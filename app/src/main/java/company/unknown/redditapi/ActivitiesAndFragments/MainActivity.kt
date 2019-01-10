package company.unknown.redditapi.ActivitiesAndFragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import company.unknown.redditapi.DataClasses.ImageOrGifThread
import company.unknown.redditapi.DataClasses.LinkThread
import company.unknown.redditapi.DataClasses.RedditThread
import company.unknown.redditapi.DataClasses.SelfThread
import company.unknown.redditapi.OtherFiles.OnLinkClickedListener
import company.unknown.redditapi.OtherFiles.OnThreadSelectedListener
import company.unknown.redditapi.R

class MainActivity : AppCompatActivity(), OnThreadSelectedListener, OnLinkClickedListener {

    private lateinit var redditThreads : ArrayList<RedditThread>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        initialize()
    }

    private fun initialize(){
        initializeThreads()
        startSearchResultsFragment()
    }

    private fun initializeThreads(){
        @Suppress("UNCHECKED_CAST")
        redditThreads = intent.extras.get("redditThreads") as ArrayList<RedditThread>
    }

    private fun startSearchResultsFragment(){
        val fragment = SearchResultFragment()

        val bundle = Bundle()
        bundle.putSerializable("threads", redditThreads)
        fragment.arguments = bundle

        addFragmentToAContainer(fragment)
    }

    override fun onThreadSelected(index: Int) {
        val selectedThread = redditThreads[index]

        //TODO: ADD MORE IFS

        if(selectedThread is SelfThread)
            replaceWithSelfFragment(selectedThread)
        else if(selectedThread is ImageOrGifThread)
            replaceWithImageOrGifFragment(selectedThread)
        else if(selectedThread is LinkThread)
            replaceWithLinkFragment(selectedThread)
    }

    private fun replaceWithLinkFragment(thread : RedditThread){
        val fragment = LinkThreadFragment()

        val bundle = Bundle()
        bundle.putSerializable("thread", thread)
        fragment.arguments = bundle

        replaceContainerWithFragment(fragment)
    }

    private fun replaceWithSelfFragment(thread : RedditThread){
        val fragment = SelfThreadFragment()

        val bundle = Bundle()
        bundle.putSerializable("thread", thread)
        fragment.arguments = bundle

        replaceContainerWithFragment(fragment)
    }

    private fun replaceWithImageOrGifFragment(thread : RedditThread){
        val fragment = ImageOrGifFragment()

        val bundle = Bundle()
        bundle.putSerializable("thread", thread)
        fragment.arguments = bundle

        replaceContainerWithFragment(fragment)
    }

    private fun replaceContainerWithFragment(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.commentSectionFrame, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun addFragmentToAContainer(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.commentSectionFrame, fragment)
        fragmentTransaction.commit()
    }

    override fun performLinkClicked(url : String) {
        val fragment = BrowseWebFragment()

        val bundle = Bundle()
        bundle.putString("url", url)
        fragment.arguments = bundle

        replaceContainerWithFragment(fragment)
    }

    /*
       TODO:
       1) WHEN TEXT IS A LINK CHECK FOR /u/example BECAUSE ONLY /r/subreddit IS IMPLEMENTED ATM
    */
}