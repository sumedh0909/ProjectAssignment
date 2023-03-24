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
@Execution(ExecutionMode.CONCURRENT)
public class TestPatchRequest {

	String requestbody = null;
	JsonCreator Json = new JsonCreator();
	
    
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = Resources.baseURI;
    }
    
    
    @Tag("Positive")
    @Test
    public void patchRequest() {
    	requestbody = Json.GenerateJSON(0, null, "My Updated Title", null);
    	Response response = given()
    			.log()
				.all()
                .header("Content-type", "application/json")
                .pathParam("id", 1)
                .and()
                .body(requestbody)
                .when()
                .patch(Resources.POSTS_ENDPOINT+"/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
        Assertions.assertAll("Group Assertions for Request", 
        		()-> assertEquals("My Updated Title", response.jsonPath().getString("title")),
        		()-> assertEquals("1", response.jsonPath().getString("userId")),
        		()-> assertEquals("1", response.jsonPath().getString("id")));
        
    }
    @Tag("Negative")
    @Test
    public void patchRequestWithIncorrectQueryParamValue() {
    	requestbody = Json.GenerateJSON(0, null, "My Updated Title", null);
    	Response response = given()
    			.log()
				.all()
                .header("Content-type", "application/json")
                .pathParam("id","")
                .and()
                .body(requestbody)
                .when()
                .patch(Resources.POSTS_ENDPOINT+"/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.statusCode());
    }   
}
