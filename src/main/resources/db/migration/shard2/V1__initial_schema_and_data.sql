-- Create ORGANIZATION table
CREATE TABLE ORGANIZATION (
    ID BIGSERIAL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT NOW() NOT NULL
);

-- Create SHOP table
CREATE TABLE SHOP (
    ID BIGSERIAL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    ORGANIZATION_ID BIGINT REFERENCES ORGANIZATION(ID),
    CREATED_AT TIMESTAMP DEFAULT NOW() NOT NULL
);

-- Create PRODUCT table
CREATE TABLE PRODUCT (
    ID BIGSERIAL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION TEXT,
    PRICE NUMERIC(10, 2) NOT NULL,
    SHOP_ID BIGINT REFERENCES SHOP(ID) ON DELETE CASCADE,
    CREATED_AT TIMESTAMP DEFAULT NOW() NOT NULL
);

-- Insert initial data into ORGANIZATION table
INSERT INTO ORGANIZATION (NAME) VALUES
('Organization Alpha'),
('Organization Beta');

-- Insert initial data into SHOP table
INSERT INTO SHOP (NAME, ORGANIZATION_ID) VALUES
('Shop 1', 1), -- Belongs to Organization Alpha
('Shop 2', 1), -- Belongs to Organization Alpha
('Shop 3', 2); -- Belongs to Organization Beta

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
