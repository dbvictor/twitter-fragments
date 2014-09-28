package com.dvictor.twitter;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dvictor.twitter.fragments.HomeTimelineFragment;
import com.dvictor.twitter.fragments.MentionsTimelineFragment;
import com.dvictor.twitter.fragments.TweetsListFragment;
import com.dvictor.twitter.listeners.FragmentTabListener;
import com.dvictor.twitter.models.Tweet;
import com.dvictor.twitter.util.InternetStatus;

public class TimelineActivity extends FragmentActivity {
	// Constants
	private static final String FRAGMENTTAG_HOME     = "home";
	private static final String FRAGMENTTAG_MENTIONS = "mentions";
	// Member Variables
	private InternetStatus     internetStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		internetStatus = new InternetStatus(this);
		setupTabs();
	}
	
	private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        Tab tab1 = actionBar
            .newTab()
            .setText("Home")
            .setIcon(R.drawable.ic_home)
            .setTag("HomeTimelineFragment")
            .setTabListener(new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, FRAGMENTTAG_HOME, HomeTimelineFragment.class));

        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        Tab tab2 = actionBar
            .newTab()
            .setText("Mentions")
            .setIcon(R.drawable.ic_mentions)
            .setTag("MentionsTimelineFragment")
            .setTabListener(new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, FRAGMENTTAG_MENTIONS, MentionsTimelineFragment.class));

        actionBar.addTab(tab2);
    }
	
	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_timeline, menu);
		MenuItem internetToggle = menu.findItem(R.id.actionInternetToggle);
		setupInternetToggle(internetToggle);
		return true;
	}

	/** Menu selection to turn on/off Internet to simulate offline. */
	public void internetToggle(MenuItem menuItem){
		internetStatus.setAppToggleEnabled(!internetStatus.isAppToggleEnabled());
		setupInternetToggle(menuItem);
		// If re-enabled, re-setup endless scroll & load data if if none loaded yet.
		if(internetStatus.isAppToggleEnabled()){
			TweetsListFragment fHome     = (TweetsListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENTTAG_HOME    );
			TweetsListFragment fMentions = (TweetsListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENTTAG_MENTIONS);
			if(fHome    !=null) fHome    .onInternetResume();
			if(fMentions!=null) fMentions.onInternetResume();
		}
	}

	/** Update the Internet status visual indicators. */
	public void setupInternetToggle(MenuItem menuItem){
		// Update the menu item to show the correct toggle state.
		// + toast just to make it clear what the current state is.
		if(internetStatus.isAppToggleEnabled()){
			Toast.makeText(this, "Internet ON", Toast.LENGTH_SHORT).show();
			menuItem.setIcon(R.drawable.ic_action_internet_off);
		}else{
			Toast.makeText(this, "Internet OFF", Toast.LENGTH_SHORT).show();
			menuItem.setIcon(R.drawable.ic_action_internet_on);
		}
	}
	
	/** Menu selection to create a new tweet. */
	public void create(MenuItem menuItem){
		if(!internetStatus.isAvailable()){ // If no network, don't allow create tweet.
			Toast.makeText(this, "Network Not Available!", Toast.LENGTH_SHORT).show();
		}else{
			//Toast.makeText(this, "Settings!", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this,CreateActivity.class);
			//no args: i.putExtra("settings", searchFilters);
			startActivityForResult(i, 1);
		}
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	if(requestCode==1){ // CreateActivity Result
    		if(resultCode == RESULT_OK){
    			Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
    			if(tweet!=null){
    				TweetsListFragment fragmentHome = (TweetsListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENTTAG_HOME);
    				if(fragmentHome!=null){
    					fragmentHome.insert(tweet, 0);
    					Toast.makeText(this, "Timeline Updated", Toast.LENGTH_SHORT).show();
    				}
    			}else Toast.makeText(this, "MISSING RESULT", Toast.LENGTH_SHORT).show();    				
    		}
    	}
    }
	
}
