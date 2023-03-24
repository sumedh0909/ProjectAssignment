package apiTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.JsonCreator;
import utils.Resources;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.http.HttpStatus;
import static io.restassured.RestAssured.given;

@Execution(ExecutionMode.CONCURRENT)
public class TestPostRequest {

	String requestbody = null;
	//Resources restconfig = new Resources();

	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = Resources.baseURI;
	}
	@Tag("Positive")
	@Test
    public void postRequestTest() {
    	JsonCreator Json = new JsonCreator();
    	requestbody = Json.GenerateJSON(0, "1", "Sumedh", "My Body");
    	
        Response response = given()
        		.log()
				.all()
                .header("Content-type", "application/json")
                .and()
                .body(requestbody)
                .when()
                .post(Resources.POSTS_ENDPOINT)
                .then()
                .extract().response();
        Assertions.assertEquals(HttpStatus.SC_CREATED, response.statusCode());
        
        Assertions.assertAll("Group Assertions for Request", 
        		()-> assertEquals("Sumedh", response.jsonPath().getString("title")),
        		()-> assertEquals("My Body", response.jsonPath().getString("body")),
        		()-> assertEquals("1", response.jsonPath().getString("userId")),
        		()-> assertEquals("101", response.jsonPath().getString("id")));
    }
	
	// Status Code - 500 Internal Server Error
	@Tag("Negative")
	@Test
    public void postRequestTestWithIncorrectJSONStructure() {
    	JsonCreator Json = new JsonCreator();
    	requestbody = Json.GenerateJSON(0, "1", null, "My Body");
    	//System.out.println(requestbody.replace(",", ""));
        Response response = given()
        		.log()
				.all()
                .header("Content-type", "application/json")
                .and()
                .body(requestbody.replace(",", ""))
                .when()
                .post(Resources.POSTS_ENDPOINT)
                .then()
                .extract().response();
        Assertions.assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.statusCode());
        
        
    }
	
	
}
