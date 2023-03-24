package apiTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.Resources;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TestDeleteRequest {

	
	Resources restconfig = new Resources();

	
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = Resources.baseURI;
    }
    @Tag("Positive")
    @Test
    public void deleteRequest() {
        Response response = given()
        		.log()
        		.all()
                .header("Content-type", "application/json")
                .pathParam("id", "1")
                .when()
                .delete(Resources.POSTS_ENDPOINT+"/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
    }
    @Tag("Negative")
    @Test
    public void deleteRequestWithoutPathParam() {
        Response response = given()
        		.log()
        		.all()
        		.header("Content-type", "application/json")
                .pathParam("id", "")
                .when()
                .delete(Resources.POSTS_ENDPOINT+"/{id}")
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.statusCode());
    }
}
