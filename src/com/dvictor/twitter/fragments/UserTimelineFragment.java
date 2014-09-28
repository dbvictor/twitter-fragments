package com.dvictor.twitter.fragments;

import java.util.List;

import android.os.Bundle;

import com.dvictor.twitter.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/** Call the correct client API method for this fragment's tweets. */
	@Override
	protected void getTweets(long lastItemId, AsyncHttpResponseHandler handler){
		getClient().getUserTimeline(lastItemId, handler);		
	}
	
	/** Query for the correct offline tweets for the particular fragment type. */
	@Override
	protected List<Tweet> getOfflineTweets(){
		return Tweet.retrieveAll();
	}

}
