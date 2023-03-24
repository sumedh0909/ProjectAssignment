package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import dao.DaoPost;

public class JsonCreator {
	// Function Creates new JSON String used for creating a new Record
	public String GenerateJSON(int id, String userid, String title, String body) {

		String jsonString = null;
		DaoPost posts = new DaoPost();
		posts.setId(id);
		posts.setUserId(userid);
		posts.setTitle(title);
		posts.setBody(body);

		Gson gson = null;
		if (posts.getId() == 0 ) {
			gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			
		} else {
			gson = new Gson();
		}
		System.out.println(gson.toJson(posts));
		return gson.toJson(posts);
	}
	
	

	
}
