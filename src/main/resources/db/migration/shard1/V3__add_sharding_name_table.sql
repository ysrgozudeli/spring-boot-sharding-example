CREATE TABLE sharding(
name varchar(255)
);

INSERT INTO sharding(name)
SELECT current_database();

-- CONDITIONAL FLYWAY
DO $$
BEGIN
    IF (SELECT name FROM sharding LIMIT 1) = 'webshop_shard1' THEN
       DELETE
       FROM PRODUCT
       WHERE SHOP_ID=3;
    END IF;
END $$;

DO $$
BEGIN
    IF (SELECT name FROM sharding LIMIT 1) = 'webshop_shard2' THEN
       DELETE
       FROM PRODUCT
       WHERE SHOP_ID IN(1,2);
    END IF;
END $$;