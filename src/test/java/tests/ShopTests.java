package tests;


import com.codeborne.selenide.Configuration;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class ShopTests {
    private String cookie;
    private static final String MINIMAL_CONTENT_PATH = "/Themes/DefaultClean/Content/styles.css";

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
    }

    @BeforeEach
    void login() {
        cookie = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("Email", "besfian@mail.ru")
                .formParam("Password", "dpride0930237p")
                .when()
                .post("/login")
                .then()
                .extract().cookie("NOPCOMMERCE.AUTH");
    }

    @Test
    void shouldAddAddresses() {
        step("check code 302", () ->
                given().log().all()
                        .filter(new AllureRestAssured())
                        .cookie("NOPCOMMERCE.AUTH", cookie)
                        .contentType("application/x-www-form-urlencoded")
                        .body("Address.Id=0&Address.FirstName=test&Address.LastName=test&Address.Email=test%40maul.ru&Address.Company=&Address.CountryId=2&Address.StateProvinceId=63&Address.City=test&Address.Address1=test&Address.Address2=&Address.ZipPostalCode=123456&Address.PhoneNumber=284118&Address.FaxNumber=")
                        .when()
                        .post("/customer/addressadd")
                        .then().log().all()
                        .statusCode(302));


    }

    @Test
    void addToWishlistWithTemplate() {

        String body = "addtocart_51.EnteredQuantity=1";

        step("Add product to wishlist", () -> {
            given()
                    .filter(customLogFilter().withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .body(body)
                    .when()
                    .post("addproducttocart/details/51/2")
                    .then()
                    .statusCode(200)
                    .body("updatetopwishlistsectionhtml", is("(1)"))
                    .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"));
        });
    }
}