package company.unknown.redditapi.ActivitiesAndFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import company.unknown.redditapi.DataClasses.*
import company.unknown.redditapi.OtherFiles.getFormattedDate
import company.unknown.redditapi.R
import kotlinx.android.synthetic.main.self_thread_fragment_layout.view.*
import androidx.fragment.app.Fragment
import company.unknown.redditapi.OtherFiles.HtmlToTextConverter


class SelfThreadFragment : BaseThreadFragment(){

    lateinit var selfThread : SelfThread

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.self_thread_fragment_layout, container, false)

        selfThread = arguments?.getSerializable("thread") as SelfThread
        initializeWidgets(view, selfThread)
        initializeSelfText(view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addPaddingIfSelfTextIsEmpty()
    }

    private fun addPaddingIfSelfTextIsEmpty(){
        if(view?.self_text?.visibility == View.GONE)
            view?.commentsTV?.setPadding(0, 20, 0, 0)
    }

    private fun initializeSelfText(view : View){

        if(selfThread.selfTextHtml.isEmpty() || selfThread.selfTextHtml == "null"){
            view.self_text.visibility = View.GONE
            return
        }

        HtmlToTextConverter.setTextView(view.self_text!!, selfThread.selfTextHtml, context!!)
    }
}