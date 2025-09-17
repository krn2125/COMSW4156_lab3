package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.coms4156.project.individualproject.model.Book;
//import java.lang.reflect.Array;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
//import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class contains the unit tests for the Book class.
 */
@SpringBootTest
 class BookTests {
  /* package */ private static Book book;

  @BeforeAll
  public static void setUpBookForTesting() {
    book = new Book("When Breath Becomes Air", 0);
  }

  @Test
  public void equalsBothAreTheSameTest() {
    Book cmpBook = book;
    assertEquals(cmpBook, book);
  }

  @Test
  public void dumbTest() {
    System.out.println("Hello World");
    assertTrue(true);
  }

  @Test
   void hasCopiesTest() {
    assertTrue(book.hasCopies(), "New book should have copies available");
    assertTrue(book.deleteCopy(), "Should be able to delete a copy");
    assertFalse(book.hasCopies(), "Book shouldn't have copies after deletion");
    book.addCopy();
    assertTrue(book.hasCopies(), "Book should have copies after adding one");
  }

  @Test
   void hasMultipleAuthorsTest() {
    assertFalse(book.hasMultipleAuthors());
    final ArrayList<String> authors = new ArrayList<>();
    authors.add("Harry Cohen");
    authors.add("J. K. Rowling");
    final ArrayList<String> subjects = new ArrayList<>();
    authors.add("Romance");
    authors.add("Drama");
    final Book thirdBook = new Book("Gone With the Wind", authors, "English", "Your Mom's House",
            "9/10/2025", "Harper Collins", subjects, 1, 3, 4);
    Book secondBook = new Book("Cracking the Coding Interview", 1);
    assertFalse(secondBook.hasMultipleAuthors());
    assertTrue(thirdBook.hasMultipleAuthors(),
            "Book with two authors should "
                    + "have multiple authors");
  }

  @Test
   void checkoutAndReturnCopyTest() {
    book.addCopy();
    String due = book.checkoutCopy();
    assertTrue(book.returnCopy(due), "Should be able to return a book if given a valid due date");
  }
}
