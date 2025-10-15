package com.library.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Library Management API application.
 *
 * This application provides RESTful endpoints for managing a book library,
 * including operations to create, read, update, and delete books.
 */
@SpringBootApplication
public class LibraryApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApiApplication.class, args);
    }
}
