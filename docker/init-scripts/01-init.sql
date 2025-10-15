-- init-scripts/01-init.sql

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create schema
CREATE SCHEMA IF NOT EXISTS library;

-- Grant privileges
GRANT ALL PRIVILEGES ON SCHEMA library TO library_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA library TO library_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA library TO library_user;

-- Create indexes for better performance (will be created by Hibernate, but you can add custom ones here)
-- Example: CREATE INDEX IF NOT EXISTS idx_books_isbn ON library.books(isbn);

-- Add any custom database initialization here
