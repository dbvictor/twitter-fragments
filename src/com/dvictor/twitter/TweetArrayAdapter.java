package com.dvictor.twitter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dvictor.twitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
	public TweetArrayAdapter(Context context, List<Tweet> objects){
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		View v;
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			v = inflator.inflate(R.layout.tweet_item, parent, false);
		} else {
			v = convertView;
		}
		// Find views within template
		ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
		TextView  tvRealName     = (TextView ) v.findViewById(R.id.tvRealName);
		TextView  tvUserName     = (TextView ) v.findViewById(R.id.tvUserName);
		TextView  tvTime         = (TextView ) v.findViewById(R.id.tvTime);
		TextView  tvBody         = (TextView ) v.findViewById(R.id.tvBody);
		// Clear existing image (needed if it was reused)
		ivProfileImage.setImageResource(android.R.color.transparent);
		// Populate views with tweet data.
		ImageLoader imageLoader = ImageLoader.getInstance();  // Universal loader we will use to get the image for us (asynchronously)
		imageLoader.displayImage(tweet.getUser().getImageUrl(), ivProfileImage); // Asynchronously load image using universal loader.
		tvRealName.setText(tweet.getUser().getRealName());
		tvUserName.setText("@"+tweet.getUser().getScreenName());
		tvTime.setText("("+getRelativeTimeAgo(tweet.getCreatedAt())+")");
		tvBody.setText(tweet.getBody());
		return v;
	}
	
	// getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
	public String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);

		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return relativeDate;
	}	
}
