package com.dvictor.twitter;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dvictor.twitter.models.User;
import com.dvictor.twitter.util.InternetStatus;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	private InternetStatus internetStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		internetStatus = new InternetStatus(this);
		loadProfile();
	}
	
	private void loadProfile(){
		if(!internetStatus.isAvailable()){ // If no network, don't allow create tweet.
			Toast.makeText(this, "Network Not Available!", Toast.LENGTH_SHORT).show();
			loadProfileOffline();
		}else{
			final ProfileActivity parentThis = this;
			TwitterApp.getRestClient().getMyProfile(new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONObject json) {
					Log.d("json", "MyInfo JSON: "+json.toString());
					User u = User.fromJSON(json);
					getActionBar().setTitle("@"+u.getScreenName());
					populateProfileHeader(u);
				}
				@Override
				public void onFailure(Throwable e, String s) {
					Log.d("debug", e.toString());
					Log.d("debug", s.toString());
					Toast.makeText(parentThis, "PROFILE FAILED!", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	private void loadProfileOffline(){
		//TODO
		Toast.makeText(this, "Offline Profile Not Implemented", Toast.LENGTH_SHORT).show();
		return;		
	}
	
	private void populateProfileHeader(User u){
		// Get access to our views.
		TextView  tvRealName     = (TextView)  findViewById(R.id.tvRealName    );
		TextView  tvTagline      = (TextView)  findViewById(R.id.tvTagline     );
		TextView  tvFollowers    = (TextView)  findViewById(R.id.tvFollowers   );
		TextView  tvFollowing    = (TextView)  findViewById(R.id.tvFollowing   );
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		// Set the user values to these views.
		tvRealName .setText(u.getRealName());
		tvTagline  .setText(u.getDescription());
		tvFollowers.setText(u.getFollowersCount() + " Followers");
		tvFollowing.setText(u.getFriendsCount() + " Following");
		ImageLoader.getInstance().displayImage(u.getImageUrl(), ivProfileImage);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu, this adds items to the action bar if present.
		//FUTURE: getMenuInflater().inflate(R.menu.menu_profile, menu);
		//FUTURE: return true;
		return false;
	}
}
