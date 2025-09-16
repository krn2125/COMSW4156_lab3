package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.coms4156.project.individualproject.controller.RouteController;
import dev.coms4156.project.individualproject.model.Book;
import dev.coms4156.project.individualproject.service.MockApiService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

/**
 * This class contains the unit tests for the RouteController class.
 */
@SpringBootTest
 class RouteControllerTests {

    /**
     * RouteController.
     */
    /* package */ private static RouteController route;
    /**
     * Book.
     */
    /* package */ private static Book book;

  /**
  * Javadoc for BeforeAll functionality.
  */
  @BeforeAll
   static void setup() {
    final MockApiService mock = new MockApiService();
    if (mock.getBooks() != null) {
      mock.getBooks().clear();
      mock.getBooks().add(new Book());
      mock.getBooks().add(new Book());
    }
    route = new RouteController(mock);
    book = new Book();
  }

  @Test
   void welcomeTest() {
    assertTrue(route.index().contains("Welcome to the home page"),
            "Home page should include message");
  }

  @Test
   void bookTest() {
    final ResponseEntity<?> response = route.getBook(1);
    assertEquals(404, response.getStatusCodeValue(), "Should receive an error code");
    assertFalse(response.getBody() instanceof Book);
  }

  @Test
    void bookDoesntExistTest() {
    final ResponseEntity<?> response = route.addCopy(73);
    assertEquals(404, response.getStatusCodeValue(), "Should receive an error code");
  }

  @Test
    void availableBooksTest() {
    final ResponseEntity<?> response = route.getAvailableBooks();
    assertEquals(200, response.getStatusCodeValue(), "Should get a success code");
    Object body = response.getBody();
    assertFalse(body instanceof String, "Response body should be a string");
  }

  @Test
   void addBookTest() {
    final ResponseEntity<?> response = route.addCopy(0);
    assertEquals(200, response.getStatusCodeValue(), "Should get a success code");
  }

  @Test
  void getBookBooksNullTests() {
    MockApiService mock = new MockApiService();
    if (mock.getBooks() != null) {
      mock.getBooks().clear();
    }
    RouteController controller = new RouteController(mock);

    ResponseEntity<?> response = controller.getBook(99);
    assertEquals(404, response.getStatusCodeValue(), "Should receive an error code");
    assertTrue(response.getBody() instanceof String);
  }
}
