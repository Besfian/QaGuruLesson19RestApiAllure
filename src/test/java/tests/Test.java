package tests;


import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static filters.CustomLogFilter.customLogFilter;

public class Test {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://demoqa.com";
    }



    @org.junit.jupiter.api.Test
    void authorizeWithTemplateTest() {
        String data = "{\"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\"}";

        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType("application/json")
                .body(data)
                .when()
                .log().uri()
                .log().body()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }
    @org.junit.jupiter.api.Test
    void authorizeWithAllureTest() {
        String data = "{\"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\"}";

//        RestAssured.filters(new AllureRestAssured());
        given()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .body(data)
                .when()
                .log().uri()
                .log().body()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }
}
