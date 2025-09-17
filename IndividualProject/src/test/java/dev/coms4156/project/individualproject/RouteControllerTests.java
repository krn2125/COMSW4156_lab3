package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.coms4156.project.individualproject.controller.RouteController;
import dev.coms4156.project.individualproject.model.Book;
import dev.coms4156.project.individualproject.service.MockApiService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/**
 * This class contains the unit tests for the RouteController class.
 */
@SpringBootTest
final class RouteControllerTests {
  /**
   * RouteController.
   */
  /* package */ private static RouteController route;

  private RouteControllerTests() {
    // Private constructor to prevent PMD error.
  }

  /**
  * Javadoc for BeforeAll functionality.
  */
  @BeforeAll
   static void setup() {
    final MockApiService mock = new MockApiService();
    if (mock.getBooks() != null) {
      mock.getBooks().clear();
      Book book = new Book();
      book.setId(1);
      book.setTitle("Book 1");
      mock.getBooks().add(new Book());
      mock.getBooks().add(new Book());
      mock.getBooks().add(book);
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
    final ResponseEntity<?> response = route.getBook(99);
    HttpStatusCode status = response.getStatusCode();
    assertEquals(HttpStatus.NOT_FOUND, status, "Should receive an error code");
  }

  @Test
  void secondBookTest() {
    final ResponseEntity<?> response = route.getBook(99);
    assertFalse(response.getBody() instanceof Book);
  }

  @Test
    void bookDoesntExistTest() {
    assertEquals(HttpStatus.NOT_FOUND, route.addCopy(73).getStatusCode(),
          "Should receive an error code");
  }

  @Test
    void availableBooksTest() {
    final ResponseEntity<?> response = route.getAvailableBooks();
    HttpStatusCode status = response.getStatusCode();
    assertEquals(HttpStatus.OK, status, "There should be available books.");
    final Object body = response.getBody();
    assertFalse(body instanceof String, "Response body should be a string");
  }

  @Test
   void addBookTest() {
    final ResponseEntity<?> response = route.addCopy(1);
    HttpStatusCode status = response.getStatusCode();
    assertEquals(HttpStatus.OK, status, "Should get a success code");
  }

  @Test
  void booksNullTests() {
    final MockApiService mock = new MockApiService();
    if (mock.getBooks() != null) {
      mock.getBooks().clear();
    }
    final RouteController controller = new RouteController(mock);

    final ResponseEntity<?> response = controller.getBook(99);
    HttpStatusCode status = response.getStatusCode();
    assertEquals(HttpStatus.NOT_FOUND, status, "Should receive an error code");
    assertInstanceOf(String.class, response.getBody());
  }

  @Test
  void recBooksTest() {
    final MockApiService mock = new MockApiService();
    if (mock.getBooks() != null) {
      mock.getBooks().clear();

      for (int i = 0; i < 15; i++) {
        mock.getBooks().add(createBook(i));
      }

      final RouteController controller = new RouteController(mock);
      final ResponseEntity<?> response = controller.getRecommendedBooks();
      HttpStatusCode status = response.getStatusCode();
      assertEquals(HttpStatus.OK, status, "Should get a success code");
    }
  }

  @Test
  void failRecBooksTest() {
    final MockApiService mock = new MockApiService();
    if (mock.getBooks() != null) {
      mock.getBooks().clear();


      for (int i = 0; i < 5; i++) {
        mock.getBooks().add(createBook(i));
      }

      final RouteController controller = new RouteController(mock);
      final ResponseEntity<?> response = controller.getRecommendedBooks();
      HttpStatusCode status = response.getStatusCode();
      assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status,
            "Should get a failure code, not enough books.");
    }
  }

  @Test
  void checkoutBookSuccessTest() {
    final MockApiService mock = new MockApiService();

    if (mock.getBooks() != null) {
      mock.getBooks().clear();
    }

    final Book book = new Book();
    book.setId(2);
    book.setTotalCopies(4);
    mock.getBooks().add(book);

    final RouteController controller = new RouteController(mock);

    final ResponseEntity<?> response = controller.checkout(2);
    HttpStatusCode status = response.getStatusCode();
    assertEquals(HttpStatus.OK, status, "Should get a success code");
  }

  @Test
  void checkoutBookFailTest() {
    final MockApiService mock = new MockApiService();
    if (mock.getBooks() != null) {
      mock.getBooks().clear();
    }

    final Book book = new Book();
    book.setId(2);
    mock.getBooks().add(book);
    final RouteController controller = new RouteController(mock);
    final ResponseEntity<?> response = controller.checkout(99);
    HttpStatusCode status = response.getStatusCode();
    assertEquals(HttpStatus.NOT_FOUND, status, "Should get a not-found code");
  }

  private Book createBook(final int checkoutCount) {
    final Book book = new Book();
    for (int i = 0; i < checkoutCount; i++) {
      book.checkoutCopy();
    }
    return book;
  }

}
