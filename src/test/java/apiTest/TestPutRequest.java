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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

@Execution(ExecutionMode.CONCURRENT)
public class TestPutRequest {

	String requestbody = null;
	JsonCreator Json = new JsonCreator();
	
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = Resources.baseURI;
    }
    
    
    // Positive PUT Request Test with all valid date
    @Tag("Positive")
    @Test
    public void putRequestTest() {
    	requestbody = Json.GenerateJSON(1, "1", "Sumedh", "My Updated Body");
    	Response response = given()
    			.log()
				.all()
                .header("Content-type", "application/json")
                .pathParam("id", 1)
                .and()
                .body(requestbody)
                .when()
                .put(Resources.POSTS_ENDPOINT+"/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
        Assertions.assertAll("Group Assertions for Request", 
        		()-> assertEquals("Sumedh", response.jsonPath().getString("title")),
        		()-> assertEquals("My Updated Body", response.jsonPath().getString("body")),
        		()-> assertEquals("1", response.jsonPath().getString("userId")),
        		()-> assertEquals("1", response.jsonPath().getString("id")));
    }

    // Negative PUT Request Test with invalid query parameter
    @Tag("Negative")
    @Test
    public void putRequestTestWithInvalidQueryParamValue() {
    	requestbody = Json.GenerateJSON(1, "1", "Sumedh", "My Updated Body");
    	Response response = given()
    			.log()
				.all()
                .header("Content-type", "application/json")
                .pathParam("id", -1)
                .and()
                .body(requestbody)
                .when()
                .put(Resources.POSTS_ENDPOINT+"/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.statusCode());
        
    }
}
