package company.unknown.redditapi.OtherFiles

interface OnLinkClickedListener{
    fun performLinkClicked(url : String)
}

interface OnThreadSelectedListener{
    fun onThreadSelected(index : Int)
}