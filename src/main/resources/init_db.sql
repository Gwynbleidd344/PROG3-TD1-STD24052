-- Creation de base de données
CREATE DATABASE product_management_db;

-- Creation de l'utilisatuer
CREATE USER product_manager_user WITH PASSWORD '123456';

--  Accorder le droit de se connecter à la DB
GRANT CONNECT ON DATABASE product_management_db TO product_manager_user;
\c product_management_db;

-- Accorder le droit de creer des tables dans la DB
GRANT CREATE ON DATABASE product_management_db TO product_manager_user;

-- Accorder le droit de faire le CRUD
GRANT SELECT, INSERT, UPDATE, DELETE ON Product TO product_manager_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON Product_Category TO product_manager_user;