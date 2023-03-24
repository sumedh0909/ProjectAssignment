package apiTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Resources;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

import java.io.ObjectInputFilter.Config;

@Execution(ExecutionMode.CONCURRENT)
public class TestGetRequest {

	
	// Before Class to initiate base URL
	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = Resources.baseURI;// "https://jsonplaceholder.typicode.com";
		
	}

	// GetRequest Test --> GET -- /posts
	@Tag("Positive")
	@Test
	public void getRequestWithoutParams() {
		Response response = given()
				.log()
				.all()
				.contentType(ContentType.JSON)
				.when().get(Resources.POSTS_ENDPOINT)
				.then()
				.extract()
				.response();

		Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
		Assertions.assertEquals("qui est esse", response.jsonPath().getString("title[1]"));
	}
	
	@Tag("Positive")
	@Test
	public void getRequestWithPathParam() {
		Response response = given()
				.log()
				.all()
				.contentType(ContentType.JSON)
				.pathParam("postid", 2)
				// .param("postId", "2")
				.when()
				.get(Resources.POSTS_ENDPOINT + "/{postid}")
				.then()
				.extract()
				.response();

		Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
		Assertions.assertEquals("qui est esse", response.jsonPath().getString("title"));
	}
	
	@Tag("Positive")
	@Test
	public void getRequestWithQueryParam() {
		Response response = given()
				.log()
				.all()
				.contentType(ContentType.JSON)
				.param("postId", "2").when()
				.get(Resources.COMMENTS_ENDPOINT)
				.then()
				.extract()
				.response();

		Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
		Assertions.assertEquals("Meghan_Littel@rene.us", response.jsonPath().getString("email[3]"));
	}
	
	@Tag("Positive")
	@Test
	public void getRequestWithPathParamInURI() {
		Response response = given()
				.log()
				.all()
				.contentType(ContentType.JSON)
				.pathParam("postid", 2)
				// .param("postId", "2")
				.when()
				.get(Resources.POSTS_ENDPOINT + "/{postid}/"
						+ Resources.COMMENTS_ENDPOINT)
				.then()
				.extract()
				.response();

		Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
		Assertions.assertNotEquals("Meghan_Littel@rene.us", response.jsonPath().getString("email[2]"));
	}
	
	@Tag("Negative")
	@Test
	public void getRequestWithIncorrectEndPoint() {
		Response response = given()
				.log()
				.all()
				.contentType(ContentType.JSON)
				.when().get(Resources.POSTS_ENDPOINT.replace("t", ""))
				.then()
				.extract()
				.response();

		Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.statusCode());

	}
}