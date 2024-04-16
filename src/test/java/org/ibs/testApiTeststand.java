package org.ibs;

import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class testApiTeststand {

    @Test
    void addExoticFruit() {
       Response response = given()
                .baseUri("http://localhost:8080/")
                .when()
               .get("food");

        Cookies cookies = response.getDetailedCookies();


        String exoticFruit = "{\n" +
                "  \"name\": \"Манго\",\n" +
                "  \"type\": \"FRUIT\",\n" +
                "  \"exotic\": true\n" +
                "}";
        given()
                .baseUri("http://localhost:8080/")
                .when()
                .get("api/food")
                .then()
                .assertThat()
                .statusCode(200)
                .log().all();

        given()
                .baseUri("http://localhost:8080/")
                .cookies(cookies)
                .header("Content-type", "application/json")
                .body(exoticFruit)
                .when()
                .post("api/food")
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .baseUri("http://localhost:8080/")
                .cookies(cookies)
                .when()
                .get("api/food")
                .then()
                .assertThat()
                .statusCode(200)
                .assertThat()
                .body("name[4]", equalTo("Манго"),
                        "type[4]", equalTo("FRUIT"),
                        "exotic[4]", equalTo(true))
                .log().all();
        List<FruitList> fruitLists = given()
                .cookies(cookies)
                .when()
                .basePath("api/food")
                .log().all()
                .get()
                .then()
                .log().all()
                .extract()
                .jsonPath().getList(basePath, FruitList.class);

        String[] strings = {"Манго", "FRUIT", "true"};
        Assertions.assertEquals(strings[0], fruitLists.getLast().getName()
        );
        Assertions.assertEquals(strings[1], fruitLists.getLast().getType()
        );
        Assertions.assertTrue(fruitLists.getLast().isExotic()
        );
    }

    @Test
    void addNotExoticVegetable() {
        Response response = given()
                .baseUri("http://localhost:8080/")
                .when()
                .get("api/food");

        Cookies cookies = response.getDetailedCookies();

        String exoticFruit = "{\n" +
                "  \"name\": \"Огурец\",\n" +
                "  \"type\": \"VEGETABLE\",\n" +
                "  \"exotic\": false\n" +
                "}";


        given()
                .baseUri("http://localhost:8080/")
                .cookies(cookies)
                .header("Content-type", "application/json")
                .body(exoticFruit)
                .when()
                .post("api/food")
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .baseUri("http://localhost:8080/")
                .cookies(cookies)
                .when()
                .get("api/food")
                .then()
                .assertThat()
                .statusCode(200)
                .assertThat()
                .body("name[4]", equalTo("Огурец"),
                        "type[4]", equalTo("VEGETABLE"),
                        "exotic[4]", equalTo(false))
                .log().all();
        List<FruitList> fruitLists = given()
                .cookies(cookies)
                .when()
                .basePath("api/food")
                .log().all()
                .get()
                .then()
                .log().all()
                .extract()
                .jsonPath().getList(basePath, FruitList.class);

        String[] strings = {"Огурец", "VEGETABLE", "false"};
        Assertions.assertEquals(strings[0], fruitLists.getLast().getName()
        );
        Assertions.assertEquals(strings[1], fruitLists.getLast().getType()
        );
        Assertions.assertFalse(fruitLists.getLast().isExotic()
        );
    }

}
