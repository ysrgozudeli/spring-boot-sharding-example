

-- CONDITIONAL FLYWAY
DO $$
BEGIN
    IF (SELECT name FROM sharding LIMIT 1) = 'webshop_shard3' THEN
       DELETE
       FROM PRODUCT
       WHERE SHOP_ID NOT IN(37);
    END IF;
END $$;
