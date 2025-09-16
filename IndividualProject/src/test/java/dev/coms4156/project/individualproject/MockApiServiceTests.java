package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.coms4156.project.individualproject.model.Book;
import dev.coms4156.project.individualproject.service.MockApiService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class contains the unit tests for the MockApiService class.
 */
@SpringBootTest
 class MockApiServiceTests {
    /**
     * MockAPIService.
     */
    /* package */ private static MockApiService mock;

  /**
   * Javadoc comment, before all functionality.
   */
  @BeforeAll
   static void beforeAll() {
    mock = new MockApiService();
    if (mock.getBooks() != null) {
      mock.getBooks().clear();
      mock.getBooks().add(new Book());
    }
  }

  @Test
   void fetchBooksTest() {
    if (mock.getBooks() == null) {
      return;
    }
    assertEquals(mock.getBooks().size(), 1, "Book should be fetchable");
  }

  @Test
   void updateBooksTest() {
    if (mock.getBooks() == null) {
      return;
    }
    mock.updateBook(new Book());
    assertEquals(mock.getBooks().get(0).getTotalCopies(), 1, "There should be one copy available");
  }

}

