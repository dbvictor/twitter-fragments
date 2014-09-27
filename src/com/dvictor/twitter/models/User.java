package com.dvictor.twitter.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model implements Serializable{
	private static final long serialVersionUID = -7608183936172537118L;
	
	// TODO: The following comment-out causes duplicate users.  But with it, it causes only 1 tweet per user to appear.
	@Column(name = "uid") //, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long   uid;
	
	@Column(name = "realName")
	private String realName;
	
	@Column(name = "screenName")
	private String screenName;
	
	@Column(name = "imageUrl")
	private String imageUrl;

	public static User fromJSON(JSONObject json) {
		User user = new User();
		// Extract values from JSON to populate the member variables.
		try{
			user.uid        = json.getLong  ("id"  );
			user.realName   = json.getString("name");
			user.screenName = json.getString("screen_name");
			user.imageUrl   = json.getString("profile_image_url");
		}catch(JSONException e){
			e.printStackTrace();
			return null;
		}
		return user;
	}

	public long getUid() {
		return uid;
	}

	public String getRealName() {
		return realName;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

}
