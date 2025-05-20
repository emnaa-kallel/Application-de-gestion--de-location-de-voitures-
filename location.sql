-- Suppression des tables définitivement si elles existent
DROP TABLE Penalite;
DROP TABLE Location;
DROP TABLE Voiture;
DROP TABLE Personnel;
DROP TABLE Client;
DROP TABLE Utilisateur;
DROP TABLE Assurance;

-- Création des tables
-- Table Utilisateur
CREATE TABLE Utilisateur (
    CIN NUMBER(8) PRIMARY KEY,
    login VARCHAR2(50) UNIQUE NOT NULL,
    mot_de_passe VARCHAR2(255) NOT NULL,
    nom VARCHAR2(50),
    prenom VARCHAR2(50),
    tel NUMBER(8),
    email VARCHAR2(100),
    type_utilisateur VARCHAR2(20) NOT NULL CHECK (type_utilisateur IN ('client', 'personnel')),
    CONSTRAINT email_format CHECK (email LIKE '%@%.%')
);

-- Table Client
CREATE TABLE Client (
    CIN NUMBER(8) PRIMARY KEY,
    adresse VARCHAR2(255),
    date_naissance DATE,
    permis_conduire NUMBER(6) UNIQUE,
    date_expiration_permis DATE,
    FOREIGN KEY (CIN) REFERENCES Utilisateur(CIN) ON DELETE CASCADE
);

-- Table Personnel
CREATE TABLE Personnel (
    CIN NUMBER(8) PRIMARY KEY,
    role VARCHAR2(20) CHECK (role IN ('admin', 'employe')),
    FOREIGN KEY (CIN) REFERENCES Utilisateur(CIN) ON DELETE CASCADE
);

-- Table Assurance
CREATE TABLE Assurance (
    id_assurance NUMBER(10) PRIMARY KEY,
    compagnie VARCHAR2(100)
);

-- Table Voiture
CREATE TABLE Voiture (
    immatriculation VARCHAR2(20) PRIMARY KEY,
    marque VARCHAR2(50),
    modele VARCHAR2(50),
    annee NUMBER(4),
    etat VARCHAR2(20) CHECK (etat IN ('en_marche', 'en_panne')),
    disponibilite VARCHAR2(20) CHECK (disponibilite IN ('disponible', 'louee', 'indisponible')),
    prix_jour DECIMAL(10,3),
    carburant VARCHAR2(20) CHECK (carburant IN ('essence', 'diesel', 'electrique')),  
    transmission VARCHAR2(20) CHECK (transmission IN ('automatique', 'manuel')),
    date_debut_assurance DATE,
    date_expiration_assurance DATE,
    id_assurance NUMBER(10),
    FOREIGN KEY (id_assurance) REFERENCES Assurance(id_assurance) ON DELETE CASCADE
);



-- Table Location
CREATE TABLE Location (
    id_location NUMBER(10)  PRIMARY KEY,
    CIN_client NUMBER(8),
    immatriculation VARCHAR2(20),
    date_debut DATE,
    date_fin DATE,
    date_retour DATE,
    mode_paiement VARCHAR2(20) CHECK (mode_paiement IN ('carte bancaire', 'application D17')),
    statut VARCHAR2(50) CHECK (statut IN ('en_cours', 'terminee')),
    FOREIGN KEY (CIN_client) REFERENCES Client(CIN) ON DELETE CASCADE,  
    FOREIGN KEY (immatriculation) REFERENCES Voiture(immatriculation) ON DELETE CASCADE
);

-- Table Penalite
CREATE TABLE Penalite (
    id_penalite NUMBER(10)  PRIMARY KEY,
    id_location NUMBER(10),
    mode_paiement VARCHAR2(20) CHECK (mode_paiement IN ('carte bancaire', 'application D17')),
    etat_paiement VARCHAR2(50) CHECK (etat_paiement IN ('en_attente', 'effectue')),
    FOREIGN KEY (id_location) REFERENCES Location(id_location) ON DELETE CASCADE
);

-- Séquence pour les locations
CREATE SEQUENCE location_seq START WITH 1 INCREMENT BY 1;
-- Séquence pour les pénalités
CREATE SEQUENCE penalite_seq START WITH 1 INCREMENT BY 1;

DROP SEQUENCE location_seq;
DROP SEQUENCE penalite_seq;


-- Insertion de données

-- Insertion dans la table Utilisateur
INSERT INTO Utilisateur VALUES (12345678, 'client1', 'password123', 'Doe', 'John', 98765432, 'john.doe@email.com', 'client');
INSERT INTO Utilisateur VALUES (87654321, 'admin1', 'adminpass', 'Smith', 'Jane', 12345678, 'jane.smith@email.com', 'personnel');
INSERT INTO Utilisateur VALUES ( 11223344, 'employe1','motdepasse123','Ali','Mejri',55667788, 'ali.mejri@email.com', 'personnel' );


-- Insertion dans la table Client
INSERT INTO Client VALUES (12345678, '12 rue de la Paix, Tunis', TO_DATE('1990-05-15', 'YYYY-MM-DD'), 123456, TO_DATE('2030-05-15', 'YYYY-MM-DD'));

-- Insertion dans la table Personnel
INSERT INTO Personnel VALUES (87654321, 'admin');

INSERT INTO Personnel VALUES (11223344, 'employe');    

-- Insertion dans la table Assurance
INSERT INTO Assurance VALUES (1, 'AXA');
INSERT INTO Assurance VALUES (2, 'Allianz');

-- Insertion dans la table Voiture
INSERT INTO Voiture VALUES ('123TUN4567', 'Toyota', 'Corolla', 2020, 'en_marche', 'disponible', 30.500, 'essence', 'automatique', TO_DATE('2021-01-01', 'YYYY-MM-DD'), TO_DATE('2022-01-01', 'YYYY-MM-DD'), 1);
INSERT INTO Voiture VALUES ('234TUN4568', 'Honda', 'Civic', 2021, 'en_panne', 'indisponible', 40.250, 'diesel', 'manuel', TO_DATE('2022-02-01', 'YYYY-MM-DD'), TO_DATE('2023-02-01', 'YYYY-MM-DD'), 2);
INSERT INTO Voiture VALUES ('123TUN4569', 'BMW', 'X3', 2020, 'en_marche', 'disponible', 30.500, 'essence', 'automatique', TO_DATE('2021-01-01', 'YYYY-MM-DD'), TO_DATE('2022-01-01', 'YYYY-MM-DD'), 1);

-- Insertion dans la table Location (avec séquence pour id_location)
INSERT INTO Location (id_location, CIN_client, immatriculation, date_debut, date_fin, date_retour, mode_paiement, statut)VALUES (location_seq.NEXTVAL, 12345678, '123TUN4567', TO_DATE('2023-04-01', 'YYYY-MM-DD'), TO_DATE('2023-04-05', 'YYYY-MM-DD'), TO_DATE('2023-04-06', 'YYYY-MM-DD'), 'carte bancaire', 'terminee');
INSERT INTO Location (id_location, CIN_client, immatriculation, date_debut, date_fin, date_retour, mode_paiement, statut)VALUES (location_seq.NEXTVAL, 12345678, '123TUN4569', TO_DATE('2023-05-01', 'YYYY-MM-DD'), TO_DATE('2023-05-04', 'YYYY-MM-DD'), TO_DATE('2023-05-05', 'YYYY-MM-DD'), 'application D17', 'terminee');

-- Insertion dans la table Penalite (id_location = 1 déjà connu)
INSERT INTO Penalite (id_penalite, id_location, mode_paiement, etat_paiement)VALUES (penalite_seq.NEXTVAL, 1, 'carte bancaire', 'effectue');
INSERT INTO Penalite (id_penalite, id_location, mode_paiement, etat_paiement)VALUES (penalite_seq.NEXTVAL, 1, 'application D17', 'en_attente');
INSERT INTO Penalite (id_penalite, id_location, mode_paiement, etat_paiement)VALUES (penalite_seq.NEXTVAL, 2, 'application D17', 'en_attente');






select * from assurance;
SELECT * FROM voiture;
select * from penalite;
select * from UTILISATEUR;
select * from location;
select * from client;
select * from personnel;


-- Requêtes demandées

-- Liste des voitures existantes et leurs états
SELECT immatriculation, marque, modele, etat
FROM Voiture;



-- Liste des voitures disponibles et louées
SELECT immatriculation, marque, modele, disponibilite
FROM Voiture
WHERE disponibilite IN ('disponible', 'louee');



SELECT *
FROM (
    SELECT immatriculation, COUNT(*) AS nb_locations
    FROM Location
    GROUP BY immatriculation
    ORDER BY nb_locations DESC
)
WHERE ROWNUM <= 3;


-- Le client régulier
SELECT *
FROM (
SELECT u.CIN, u.nom, u.prenom, COUNT(*) AS total_locations
FROM Client c
JOIN Utilisateur u ON u.CIN = c.CIN
JOIN Location l ON l.CIN_client = c.CIN
GROUP BY u.CIN, u.nom, u.prenom
ORDER BY total_locations DESC
)
WHERE ROWNUM <= 3;


CREATE OR REPLACE FUNCTION get_location_summary (
    p_CIN_client IN NUMBER
)
RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    v_total_voitures NUMBER := 0;
BEGIN
    -- Afficher le nombre total de voitures différentes louées par le client
    SELECT COUNT(DISTINCT immatriculation)
    INTO v_total_voitures
    FROM Location
    WHERE CIN_client = p_CIN_client;

    DBMS_OUTPUT.PUT_LINE('Nombre total de voitures louées par le client ' || p_CIN_client || ' : ' || v_total_voitures);

    -- Ouvrir le curseur avec les détails des durées de location
    OPEN v_cursor FOR
    SELECT
        v.immatriculation,
        v.marque,
        v.modele,
        SUM(l.date_fin - l.date_debut) AS duree_totale_jours
    FROM Location l
    JOIN Voiture v ON v.immatriculation = l.immatriculation
    WHERE l.CIN_client = p_CIN_client
    GROUP BY v.immatriculation, v.marque, v.modele;

    RETURN v_cursor;
END;
/

SET SERVEROUTPUT ON;
DECLARE
    v_cursor SYS_REFCURSOR;
    v_immat VARCHAR2(20);
    v_marque VARCHAR2(50);
    v_modele VARCHAR2(50);
    v_duree NUMBER;
BEGIN
    -- Appel de la fonction
    v_cursor := get_location_summary(12345678);

    -- Affichage des résultats
    DBMS_OUTPUT.PUT_LINE('Résumé des locations pour le client 12345678 :');

    LOOP
        FETCH v_cursor INTO v_immat, v_marque, v_modele, v_duree;
        EXIT WHEN v_cursor%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('Voiture : ' || v_immat || ', ' || v_marque || ' ' || v_modele || ', Durée totale : ' || v_duree || ' jours');
    END LOOP;

    CLOSE v_cursor;
END;
/



-- Requetes demandées

-- Liste des voitures existantes et leurs états
SELECT immatriculation, marque, modele, etat
FROM Voiture;

-- Liste des voitures disponibles et louées
SELECT immatriculation, marque, modele, disponibilite
FROM Voiture
WHERE disponibilite IN ('disponible', 'louee');

-- Liste des 3 voitures les plus demandées
SELECT *
FROM (
    SELECT immatriculation, COUNT(*) AS nb_locations
    FROM Location
    GROUP BY immatriculation
    ORDER BY nb_locations DESC
)
WHERE ROWNUM <= 3;



-- Le client régulier
SELECT *
FROM (
    SELECT
        u.CIN,
        u.nom,
        u.prenom,
        COUNT(*) AS total_locations
    FROM
        Client c
        JOIN Utilisateur u ON u.CIN = c.CIN
        JOIN Location l ON l.CIN_client = c.CIN
    GROUP BY
        u.CIN, u.nom, u.prenom
    ORDER BY
        total_locations DESC
)
WHERE ROWNUM = 1;




select * from Voiture ORDER BY immatriculation;










CREATE OR REPLACE TRIGGER trg_check_etat_disponibilite
BEFORE INSERT OR UPDATE ON Voiture
FOR EACH ROW
BEGIN
    IF :NEW.etat = 'en_panne' AND :NEW.disponibilite != 'indisponible' THEN
        RAISE_APPLICATION_ERROR(-20001, 'Une voiture en panne doit avoir une disponibilité "indisponible".');
    END IF;
END;
/


SET SERVEROUTPUT ON
BEGIN
    -- Ce bloc devrait provoquer une erreur
    INSERT INTO Voiture (
        immatriculation, marque, modele, annee, etat, disponibilite,
        prix_jour, carburant, transmission,
        date_debut_assurance, date_expiration_assurance, id_assurance
    )
    VALUES (
        'TEST1234', 'Renault', 'Clio', 2019, 'en_panne', 'disponible',
        35.000, 'essence', 'manuel',
        TO_DATE('2023-01-01', 'YYYY-MM-DD'), TO_DATE('2024-01-01', 'YYYY-MM-DD'), 1
    );
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Erreur attendue : ' || SQLERRM);
END;
/




-- Supprimer les enregistrements dépendants d'abord
DELETE FROM Penalite;
DELETE FROM Location;
DELETE FROM Voiture;
DELETE FROM Personnel;
DELETE FROM Client;
DELETE FROM Utilisateur;
DELETE FROM Assurance;