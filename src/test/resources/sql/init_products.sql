-- TRUNACTE TABLES SECTION
TRUNCATE TABLE PRODUCT RESTART IDENTITY CASCADE;

-- Insert initial data into PRODUCT table
INSERT INTO PRODUCT (NAME, DESCRIPTION, PRICE, SHOP_ID) VALUES
('Product A1', 'Description for Product A1', 10.00, 1),
('Product A2', 'Description for Product A2', 20.00, 1),
('Product B1', 'Description for Product B1', 15.00, 2),
('Product B2', 'Description for Product B2', 25.00, 2),
('Product C1', 'Description for Product C1', 12.00, 3),
('Product C2', 'Description for Product C2', 22.00, 3),
('Product A3', 'Description for Product A3', 18.00, 1),
('Product B3', 'Description for Product B3', 28.00, 2),
('Product C3', 'Description for Product C3', 19.00, 3),
('Product A4', 'Description for Product A4', 30.00, 1);

