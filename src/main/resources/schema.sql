--create datatables and the schema for the database

--CREATE DATABASE pharmacy;
--CREATE SCHEMA store_one;

CREATE TABLE items (
   id SERIAL PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   manf_name VARCHAR(100) NOT NULL,
   generic_name VARCHAR(100),
   sell_uom INT NOT NULL,
   uom_price DECIMAL(10, 2) NOT NULL,
   package_size INT NOT NULL,
   unique_identifier VARCHAR(50),
   general_text VARCHAR(255),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   deleted_at TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE
);

CREATE TABLE suppliers (
   supplier_id SERIAL PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   phone_number VARCHAR(15),
   email VARCHAR(100),
   address VARCHAR(255),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   deleted_at TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE  -- Soft delete column
);

CREATE TABLE users (
   user_id SERIAL PRIMARY KEY,
   username VARCHAR(50) UNIQUE NOT NULL,
   password VARCHAR(255) NOT NULL,
   email VARCHAR(100) UNIQUE,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   deleted_at TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE  -- Soft delete column
   -- Add additional fields as needed
);

CREATE TABLE customers (
   customer_id SERIAL PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   phone_number VARCHAR(15),
   email VARCHAR(100),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   deleted_at TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE  -- Soft delete column
   -- Add additional fields as needed
);

--create a table for storing stock entry details like supplier, date , phone no, address etc which communicates with stock items for storing items that are entered this should also support the stock return and stock partial return
CREATE TABLE stock_entries (
   id SERIAL PRIMARY KEY,
   supplier_id INT REFERENCES suppliers(supplier_id),
   entry_date DATE,
   grn VARCHAR(50) UNIQUE NOT NULL,
   phone_number VARCHAR(15),
   address VARCHAR(255),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   deleted_at TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE  -- Soft delete column
   -- Add additional fields as needed
   );

CREATE TABLE stock_items (
   id SERIAL PRIMARY KEY,
   stock_entry_id INT REFERENCES stock_entries(id),
   item_id INT REFERENCES items(id),
   quantity INT NOT NULL,
   batch_no VARCHAR(50),
   price DECIMAL(10, 2) NOT NULL,
   expiry_date DATE,
   manufacturing_date DATE,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   deleted_at TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE  -- Soft delete column
   -- Add additional fields as needed
);

CREATE TABLE sales (
   id SERIAL PRIMARY KEY,
   user_id INT REFERENCES users(user_id),
   customer_id INT REFERENCES customers(customer_id),
   total_amount DECIMAL(10, 2) NOT NULL,
   sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   deleted_at TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE  -- Soft delete column
   -- Add additional fields as needed
);

CREATE TABLE sales_items (
   sales_item_id SERIAL PRIMARY KEY,
   sale_id INT REFERENCES sales(id),
   item_id INT REFERENCES items(id),
   batch_no VARCHAR(50),
   quantity INT NOT NULL,
   price_per_unit DECIMAL(10, 2) NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   deleted_at TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE  -- Soft delete column
   -- Add additional fields as needed
);

--taxes table structure which can hold types of taxes and their rates and detasils of taxes
CREATE TABLE taxes (
   id SERIAL PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   rate DECIMAL(10, 2) NOT NULL,
   description VARCHAR(255),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   deleted_at TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE  -- Soft delete column
   -- Add additional fields as needed
);

CREATE TABLE item_taxes (
   id SERIAL PRIMARY KEY,
   item_id INT REFERENCES items(id),
   tax_id INT REFERENCES taxes(id),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE,
   deleted_at TIMESTAMP
);

CREATE INDEX idx_items_name ON items (name);
CREATE INDEX idx_items_manf_name ON items (manf_name);
CREATE INDEX idx_items_generic_name ON items (generic_name);
