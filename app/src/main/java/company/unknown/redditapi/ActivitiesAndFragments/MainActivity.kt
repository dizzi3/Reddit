package company.unknown.redditapi.ActivitiesAndFragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import company.unknown.redditapi.DataClasses.RedditThread
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

        if(selectedThread.is_self)
            replaceWithSelfFragment(selectedThread)
    }

    override fun performLinkClicked(url : String) {
        val fragment = BrowseWebFragment()

        val bundle = Bundle()
        bundle.putString("url", url)
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

    private fun replaceContainerWithFragment(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun addFragmentToAContainer(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    /*
                when thread is a link
                post_hint -> link
                url -> "www.xd.com/XD"
                thumbnail -> miniaturka
                title ->
                domain -> twitter.com, idk, might be useful



                is_self -> text-only post

                https://www.youtube.com/watch?v=TUXui5ItBkM -> when user click on the link
                to make it open in our app, not in an external browser like chrome


                USE FRAGMENTS INSTEAD OF ACTIVITY, MAYBE HAVE A METHOD IN ACTIVITY TO DETERMINE
                WHEATHER IT SHOULD USE ONLY-TEXT FRAGMENT, IMAGE FRAGMENT OR JUST LINK FRAGMENT
                FOR LINK-ONLY THREADS. EACH OF THESE FRAGMENTS SHOULD HAVE THEIR OWN LAYOUT I GUESS


                CREATING A SEPARATE FRAGMENT FOR THE COMMENTS SECTION MIGHT BE A GOOD IDEA

                SAME FOR THE WEB VIEW WHEN THE USER CLICKS ON THE LINK EITHER IN THE SELF-TEXT
                SECTION OR IN THE COMMENTS IT SHOULD PERFORM A FRAGMENT TRANSACTION AND ALSO
                ADD THE PREVIOUS FRAGMENTS LAYOUT INTO BACK STACK SO THE USER CAN EASILY NAVIGATE
                BACKWARDS

             */
}