package dev.coms4156.project.individualproject.controller;

import dev.coms4156.project.individualproject.model.Book;
import dev.coms4156.project.individualproject.service.MockApiService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class contains all the API routes for the application.
 */
@RestController
public class RouteController {

  private final MockApiService mockApiService;

  public RouteController(final MockApiService mockApiService) {
    this.mockApiService = mockApiService;
  }

  @GetMapping({"/", "/index"})
  public String index() {
    return "Welcome to the home page! In order to make an API call direct your browser"
        + "or Postman to an endpoint.";
  }

  /**
   * Returns the details of the specified book.
   *
   * @param id An {@code int} representing the unique identifier of the book to retrieve.
   *
   * @return A {@code ResponseEntity} containing either the matching {@code Book} object with an
   *         HTTP 200 response, or a message indicating that the book was not
   *         found with an HTTP 404 response.
   */
  @GetMapping({"/book/{id}"})
  public ResponseEntity<?> getBook(@PathVariable int id) {
    for (Book book : mockApiService.getBooks()) {
      if (book.getId() == id) {
        return new ResponseEntity<>(book, HttpStatus.OK);
      }
    }

    return new ResponseEntity<>("Book not found.", HttpStatus.NOT_FOUND);
  }

  /**
   * Returns a list of all the books with available copies.
   *
   * @return A {@code ResponseEntity} containing a list of available {@code Book} objects with an
   *         HTTP 200 response if successful, or a message indicating an error occurred with an
   *         HTTP 500 response.
   */
  @GetMapping({"/books/available"})
  public ResponseEntity<?> getAvailableBooks() {
    try {
      final List<Book> availableBooks = new ArrayList<>();

      for (final Book book : mockApiService.getBooks()) {
        if (book.hasCopies()) {
          availableBooks.add(book);
        }
      }

      return new ResponseEntity<>(availableBooks, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error occurred when getting all available books",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }



  /**
   * Adds a copy to the {@code} Book object if it exists.
   *
   * @param bookId An {@code Integer} representing the unique id of the book.
   * @return A {@code ResponseEntity} containing the updated {@code Book} object with an
   *         HTTP 200 response if successful or HTTP 404 if the book is not found,
   *         or a message indicating an error occurred with an HTTP 500 code.
   */
  @PatchMapping({"/book/{bookId}/add"})
  public ResponseEntity<?> addCopy(final @PathVariable Integer bookId) {
    try {
      for (final Book book : mockApiService.getBooks()) {
        if (bookId.equals(book.getId())) {
          book.addCopy();
          return new ResponseEntity<>(book, HttpStatus.OK);
        }
      }

      return new ResponseEntity<>("Book not found.", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      System.err.println(e);
      return new ResponseEntity<>("Error occurred when adding a copy.",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Returns a list of 10 recommended books.
   *
   * @return A {@code ResponseEntity} containing a list of recommended books.
   */
  @GetMapping({"/books/recommendation"})
  public ResponseEntity<?> getRecommendedBooks() {
    try {
      final List<Book> recommendedBooks = new ArrayList<>();
      final List<Book> allBooks = mockApiService.getBooks();

      final int neededNum = 10;
      if (allBooks.size() < neededNum) {
        return new ResponseEntity<>("Error occurred, not enough books",
              HttpStatus.INTERNAL_SERVER_ERROR);
      }

      final List<Book> popularBooks = new ArrayList<>(allBooks);
      popularBooks.sort(Comparator.comparingInt(Book::getAmountOfTimesCheckedOut).reversed());

      for (int i = 0; i < 5; i++) {
        recommendedBooks.add(popularBooks.get(i));
        allBooks.remove(popularBooks.get(i));
      }

      Collections.shuffle(allBooks);
      for (int i = 0; i < 5; i++) {
        recommendedBooks.add(allBooks.get(i));
      }
      return new ResponseEntity<>(recommendedBooks, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error occurred when getting recommended books",
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Action of checking out a book.
   *
   * @param bookId ID of book to-be-checked-out.
   * @return A {@code ResponseEntity} containing the checked-out {@code Book} object.
   */

  @GetMapping("/checkout")
  public ResponseEntity<?> checkout(final @RequestParam int bookId) {
    try {
      final List<Book> allBooks = mockApiService.getBooks();

      if (allBooks == null) {
        return new ResponseEntity<>("No books.", HttpStatus.INTERNAL_SERVER_ERROR);
      }

      Book checkedOut = null;
      for (final Book book : allBooks) {
        if (book.getId() == bookId) {
          checkedOut = book;
          break;
        }
      }

      if (checkedOut == null) {
        return new ResponseEntity<>("Book not found.", HttpStatus.NOT_FOUND);
      }

      if (checkedOut.getCopiesAvailable() < 0) {
        return new ResponseEntity<>("No copies available", HttpStatus.NOT_FOUND);
      }

      checkedOut.checkoutCopy();

      return new ResponseEntity<>(checkedOut, HttpStatus.OK);

    } catch (Exception e) {
      return new ResponseEntity<>("Error occurred when getting recommended books",
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
