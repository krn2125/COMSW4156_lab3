package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.coms4156.project.individualproject.model.Book;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class contains the unit tests for the Book class.
 */
@SpringBootTest
final class BookTests {
  /**
   * Book object for global book tests.
   */
  /* package */ private static Book book;

  private BookTests() {
  // Private constructor to prevent PMD error.
  }

  /**
   * This function runs before each test, giving a workable object.
   */
  @BeforeAll
  public static void setUpBookForTesting() {
    book = new Book("When Breath Becomes Air", 0);
    book.addCopy();
  }

  @Test
  void equalsBothAreTheSameTest() {
    final Book cmpBook = book;
    assertEquals(cmpBook, book);
  }

  @Test
   void hasCopiesTest() {
    assertTrue(book.hasCopies(), "New book should have copies available");
  }

  @Test
  void deleteCopyTest() {
    book.addCopy();
    assertTrue(book.deleteCopy(), "Should be able to delete a copy");
  }

  @Test
  void noCopiesTest() {
    book.deleteCopy();
    assertFalse(book.hasCopies(), "Book shouldn't have copies after deletion");
  }

  @Test
  void secondAddCopyTest() {
    book.addCopy();
    assertTrue(book.hasCopies(), "Book should have copies after adding one");
  }

  @Test
  void defaultBookAuthorsTest() {
    assertFalse(book.hasMultipleAuthors());
  }

  @Test
  void hasMultipleAuthorsTest() {
    final List<String> authors = new ArrayList<>();
    authors.add("Harry Cohen");
    authors.add("J. K. Rowling");
    final List<String> subjects = new ArrayList<>();
    authors.add("Romance");
    final Book thirdBook = new Book("Gone With the Wind", authors, "English", "Your Mom's House",
          "9/10/2025", "Harper Collins", subjects, 1, 3, 4);
    assertTrue(thirdBook.hasMultipleAuthors(),
          "Book with two authors should "
                + "have multiple authors");
  }

  @Test
  void addCopyTest() {
    assertEquals(1, book.getCopiesAvailable(), "Should have 1 copy available");
  }

  @Test
  void checkoutCopyTest() {
    assertNotNull(book.checkoutCopy(), "Checkout should return due date.");
  }

  @Test
  void returnCopyTest() {
    assertTrue(book.returnCopy(book.checkoutCopy()), "Should"
          + " be able to return a book if given a valid due date");
  }
}
