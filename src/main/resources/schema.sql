CREATE TABLE medicine (
   item_id SERIAL PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   manf_name VARCHAR(100) NOT NULL,
   generic_name VARCHAR(100),
   sell_uom INT NOT NULL,
   uom_price DECIMAL(10, 2) NOT NULL,
   package_size INT,
   unique_identifier VARCHAR(50) UNIQUE NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE,  -- Soft delete column
   -- Add additional fields as needed
);

CREATE TABLE users (
   user_id SERIAL PRIMARY KEY,
   username VARCHAR(50) UNIQUE NOT NULL,
   password VARCHAR(255) NOT NULL,
   email VARCHAR(100) UNIQUE,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE,  -- Soft delete column
   -- Add additional fields as needed
);

CREATE TABLE customers (
   customer_id SERIAL PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   phone_number VARCHAR(15),
   email VARCHAR(100),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE,  -- Soft delete column
   -- Add additional fields as needed
);


CREATE TABLE stock (
   stock_id SERIAL PRIMARY KEY,
   item_id INT REFERENCES medicine(item_id),
   quantity INT NOT NULL,
   price DECIMAL(10, 2) NOT NULL,
   expiry_date DATE,
   manufacturing_date DATE,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE,  -- Soft delete column
   -- Add additional fields as needed
);

CREATE TABLE sales (
   sale_id SERIAL PRIMARY KEY,
   user_id INT REFERENCES users(user_id),
   customer_id INT REFERENCES customers(customer_id),
   total_amount DECIMAL(10, 2) NOT NULL,
   sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE,  -- Soft delete column
   -- Add additional fields as needed
);

CREATE TABLE sales_items (
   sales_item_id SERIAL PRIMARY KEY,
   sale_id INT REFERENCES sales(sale_id),
   item_id INT REFERENCES medicine(item_id),
   quantity INT NOT NULL,
   price_per_unit DECIMAL(10, 2) NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE,  -- Soft delete column
   -- Add additional fields as needed
);

CREATE TABLE stock_returns (
   return_id SERIAL PRIMARY KEY,
   stock_id INT REFERENCES stock(stock_id),
   return_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   soft_delete BOOLEAN DEFAULT FALSE,  -- Soft delete column
   -- Add additional fields as needed
);
