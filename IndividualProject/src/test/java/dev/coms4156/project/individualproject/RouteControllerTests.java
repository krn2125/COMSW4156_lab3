package dev.coms4156.project.individualproject;

import dev.coms4156.project.individualproject.controller.RouteController;
import dev.coms4156.project.individualproject.model.Book;
import dev.coms4156.project.individualproject.service.MockApiService;
//import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

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
  }

  @Test
   void welcomeTest() {
    assertTrue(route.index().contains("Welcome to the home page"),
            "Home page should include message");
  }

  @Test
   void bookTest() {
    final ResponseEntity<?> response = route.getBook(1);
    assertEquals(404, response.getStatusCode().value(), "Should receive an error code");
    assertFalse(response.getBody() instanceof Book);
  }

  @Test
    void bookDoesntExistTest() {
    final ResponseEntity<?> response = route.addCopy(73);
    assertEquals(404, response.getStatusCode().value(), "Should receive an error code");
  }

  @Test
    void availableBooksTest() {
    final ResponseEntity<?> response = route.getAvailableBooks();
    assertEquals(200, response.getStatusCode().value(), "Should get a success code");
    Object body = response.getBody();
    assertFalse(body instanceof String, "Response body should be a string");
  }

  @Test
   void addBookTest() {
    final ResponseEntity<?> response = route.addCopy(0);
    assertEquals(200, response.getStatusCode().value(), "Should get a success code");
  }

  @Test
  void getBookBooksNullTests() {
    MockApiService mock = new MockApiService();
    if (mock.getBooks() != null) {
      mock.getBooks().clear();
    }
    RouteController controller = new RouteController(mock);

    ResponseEntity<?> response = controller.getBook(99);
    assertEquals(404, response.getStatusCode().value(), "Should receive an error code");
    assertInstanceOf(String.class, response.getBody());
  }

  @Test
  void getRecommendedBooksTest(){
    MockApiService mock = new MockApiService();
    if (mock.getBooks() != null) {
      mock.getBooks().clear();
    }

    for (int i = 0; i < 15; i++){
      Book book = new Book();
      for(int j = 0; j < i; j++){
        book.checkoutCopy();
      }
      mock.getBooks().add(book);
    }

    RouteController controller = new RouteController(mock);
    ResponseEntity<?> response = controller.getRecommendedBooks();
    assertEquals(200, response.getStatusCode().value(), "Should get a success code");
  }

  @Test
  void failToGetRecommendedBooksTest(){
    MockApiService mock = new MockApiService();
    if (mock.getBooks() != null) {
      mock.getBooks().clear();
    }

    for (int i = 0; i < 5; i++){
      Book book = new Book();
      for(int j = 0; j < i; j++){
        book.checkoutCopy();
      }
      mock.getBooks().add(book);
    }

    RouteController controller = new RouteController(mock);
    ResponseEntity<?> response = controller.getRecommendedBooks();
    assertEquals(500, response.getStatusCode().value(), "Should get a failure code, not enough books.");
  }

  @Test
  void checkoutBookSuccessTest() {
    MockApiService mock = new MockApiService();

    if(mock.getBooks() != null) {
      mock.getBooks().clear();
    }

    Book book = new Book();
    book.setId(2);
    book.setTotalCopies(4);
    mock.getBooks().add(book);

    RouteController controller = new RouteController(mock);

    ResponseEntity<?> response = controller.checkout(2);

    assertEquals(200, response.getStatusCode().value(), "Should get a success code");
  }

  @Test
  void checkoutBookFailTest() {
    MockApiService mock = new MockApiService();
    if(mock.getBooks() != null) {
      mock.getBooks().clear();
    }

    Book book = new Book();
    book.setId(2);
    mock.getBooks().add(book);
    RouteController controller = new RouteController(mock);
    ResponseEntity<?> response = controller.checkout(99);

    assertEquals(404, response.getStatusCode().value(), "Should get a not-found code");
  }

}
