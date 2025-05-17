-- Create the rentcars user and grant privileges
CREATE USER rentcars IDENTIFIED BY rentcars123;
GRANT CONNECT, RESOURCE TO rentcars;
GRANT CREATE SESSION TO rentcars;
GRANT UNLIMITED TABLESPACE TO rentcars;

-- Connect as rentcars user
CONNECT rentcars/rentcars123

-- Drop existing tables if they exist
DROP TABLE Paiement PURGE;
DROP TABLE Penalite PURGE;
DROP TABLE Location PURGE;
DROP TABLE Assurance PURGE;
DROP TABLE Voiture PURGE;
DROP TABLE Personnel PURGE;
DROP TABLE Client PURGE;
DROP TABLE Utilisateur PURGE;

-- Create tables
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

CREATE TABLE Client (
    CIN NUMBER(8) PRIMARY KEY,
    adresse VARCHAR2(255),
    date_naissance DATE,
    permis_conduire NUMBER(6) UNIQUE,
    date_expiration_permis DATE,
    FOREIGN KEY (CIN) REFERENCES Utilisateur(CIN) ON DELETE CASCADE
);

CREATE TABLE Personnel (
    CIN NUMBER(8) PRIMARY KEY,
    role VARCHAR2(20) CHECK (role IN ('admin', 'employe')),
    FOREIGN KEY (CIN) REFERENCES Utilisateur(CIN) ON DELETE CASCADE
);

CREATE TABLE Voiture (
    immatriculation VARCHAR2(20) PRIMARY KEY,
    marque VARCHAR2(50),
    modele VARCHAR2(50),
    annee NUMBER(4),
    etat VARCHAR2(20) CHECK (etat IN ('en_marche', 'en_panne')),
    disponibilite VARCHAR2(20) CHECK (disponibilite IN ('disponible', 'louee', 'indisponible')),
    prix_jour DECIMAL(10,3),
    carburant VARCHAR2(20) CHECK (carburant IN ('essence', 'diesel', 'electrique')),
    transmission VARCHAR2(20) CHECK (transmission IN ('automatique', 'manuel'))
);

CREATE TABLE Location (
    id_location NUMBER(10) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    CIN_client NUMBER(8),
    immatriculation VARCHAR2(20),
    CIN_personnel NUMBER(8),
    date_debut DATE,
    date_fin DATE,
    prix_total DECIMAL(10,3),
    statut VARCHAR2(50) CHECK (statut IN ('en_cours', 'terminee', 'annulee')),
    km_depart DECIMAL(10,2),
    km_retour DECIMAL(10,2),
    FOREIGN KEY (CIN_client) REFERENCES Client(CIN) ON DELETE CASCADE,
    FOREIGN KEY (CIN_personnel) REFERENCES Personnel(CIN) ON DELETE CASCADE,
    FOREIGN KEY (immatriculation) REFERENCES Voiture(immatriculation) ON DELETE CASCADE
);

CREATE TABLE Paiement (
    id_paiement NUMBER(10) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_location NUMBER(10),
    montant DECIMAL(10,3),
    mode_paiement VARCHAR2(20) CHECK (mode_paiement IN ('carte bancaire', 'espece', 'application D17')),
    date_paiement DATE,
    statut_paiement VARCHAR2(20) CHECK (statut_paiement IN ('effectue', 'en_attente', 'annule')),
    FOREIGN KEY (id_location) REFERENCES Location(id_location) ON DELETE CASCADE
);

CREATE TABLE Penalite (
    id_penalite NUMBER(10) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_location NUMBER(10),
    montant DECIMAL(10,3),
    raison VARCHAR2(255),
    date_penalite DATE,
    FOREIGN KEY (id_location) REFERENCES Location(id_location) ON DELETE CASCADE
);

CREATE TABLE Assurance (
    id_assurance NUMBER(10) PRIMARY KEY,
    immatriculation VARCHAR2(20),
    compagnie VARCHAR2(100),
    date_debut DATE,
    date_expiration DATE,
    FOREIGN KEY (immatriculation) REFERENCES Voiture(immatriculation) ON DELETE CASCADE
);

-- Insert sample data
-- Utilisateurs
INSERT INTO Utilisateur (CIN, login, mot_de_passe, nom, prenom, tel, email, type_utilisateur)
VALUES (12345678, 'sahar.b', '1234', 'Bougares', 'Sahar', 48999562, 'sahar.bougares@email.com', 'client');

INSERT INTO Utilisateur (CIN, login, mot_de_passe, nom, prenom, tel, email, type_utilisateur)
VALUES (11223344, 'selim.ahmed', 'pass1234', 'Ahmed', 'Selim', 75123456, 'selim.ahmed@email.com', 'client');

INSERT INTO Utilisateur (CIN, login, mot_de_passe, nom, prenom, tel, email, type_utilisateur)
VALUES (22334455, 'nabila.mohamed', 'secure567', 'Mohamed', 'Nabila', 74123456, 'nabila.mohamed@email.com', 'personnel');

INSERT INTO Utilisateur (CIN, login, mot_de_passe, nom, prenom, tel, email, type_utilisateur)
VALUES (87654321, 'amine.ali', 'secure123', 'Ali', 'Amine', 7012345, 'amine.ali@email.com', 'personnel');

-- Clients
INSERT INTO Client (CIN, adresse, date_naissance, permis_conduire, date_expiration_permis)
VALUES (12345678, 'Medenine', TO_DATE('2004-09-06', 'YYYY-MM-DD'), 512436, TO_DATE('2026-01-15', 'YYYY-MM-DD'));

INSERT INTO Client (CIN, adresse, date_naissance, permis_conduire, date_expiration_permis)
VALUES (11223344, 'Tunis', TO_DATE('1990-05-20', 'YYYY-MM-DD'), 753159, TO_DATE('2025-06-25', 'YYYY-MM-DD'));

-- Personnel
INSERT INTO Personnel (CIN, role)
VALUES (87654321, 'admin');

INSERT INTO Personnel (CIN, role)
VALUES (22334455, 'employe');

-- Voitures
INSERT INTO Voiture (immatriculation, marque, modele, annee, etat, disponibilite, prix_jour, carburant, transmission)
VALUES ('123TUN2022', 'Toyota', 'Yaris', 2022, 'en_marche', 'disponible', 120.000, 'essence', 'automatique');

INSERT INTO Voiture (immatriculation, marque, modele, annee, etat, disponibilite, prix_jour, carburant, transmission)
VALUES ('456TUN2021', 'Renault', 'Clio', 2021, 'en_panne', 'indisponible', 100.000, 'diesel', 'manuel');

INSERT INTO Voiture (immatriculation, marque, modele, annee, etat, disponibilite, prix_jour, carburant, transmission)
VALUES ('789TUN2023', 'Ford', 'Focus', 2023, 'en_marche', 'disponible', 110.000, 'essence', 'manuel');

INSERT INTO Voiture (immatriculation, marque, modele, annee, etat, disponibilite, prix_jour, carburant, transmission)
VALUES ('101TUN2024', 'Volkswagen', 'Golf', 2024, 'en_marche', 'disponible', 130.000, 'electrique', 'automatique');

-- Locations
INSERT INTO Location (CIN_client, immatriculation, CIN_personnel, date_debut, date_fin, prix_total, statut, km_depart, km_retour)
VALUES (12345678, '123TUN2022', 87654321, TO_DATE('2025-04-10', 'YYYY-MM-DD'), TO_DATE('2025-04-15', 'YYYY-MM-DD'), 600.000, 'en_cours', 1000, 1050);

INSERT INTO Location (CIN_client, immatriculation, CIN_personnel, date_debut, date_fin, prix_total, statut, km_depart, km_retour)
VALUES (11223344, '789TUN2023', 22334455, TO_DATE('2025-04-12', 'YYYY-MM-DD'), TO_DATE('2025-04-18', 'YYYY-MM-DD'), 660.000, 'en_cours', 2000, 2050);

-- Paiements
INSERT INTO Paiement (id_location, montant, mode_paiement, date_paiement, statut_paiement)
VALUES (1, 600.000, 'carte bancaire', TO_DATE('2025-04-10', 'YYYY-MM-DD'), 'effectue');

INSERT INTO Paiement (id_location, montant, mode_paiement, date_paiement, statut_paiement)
VALUES (2, 660.000, 'espece', TO_DATE('2025-04-12', 'YYYY-MM-DD'), 'effectue');

-- Pénalités
INSERT INTO Penalite (id_location, montant, raison, date_penalite)
VALUES (1, 50.000, 'Retard de retour', TO_DATE('2025-04-16', 'YYYY-MM-DD'));

INSERT INTO Penalite (id_location, montant, raison, date_penalite)
VALUES (2, 30.000, 'Retard de retour', TO_DATE('2025-04-19', 'YYYY-MM-DD'));

-- Assurances
INSERT INTO Assurance (id_assurance, immatriculation, compagnie, date_debut, date_expiration)
VALUES (1, '123TUN2022', 'Assurances ABC', TO_DATE('2025-01-01', 'YYYY-MM-DD'), TO_DATE('2026-01-01', 'YYYY-MM-DD'));

INSERT INTO Assurance (id_assurance, immatriculation, compagnie, date_debut, date_expiration)
VALUES (2, '456TUN2021', 'Assurances XYZ', TO_DATE('2025-02-01', 'YYYY-MM-DD'), TO_DATE('2026-02-01', 'YYYY-MM-DD'));

COMMIT;
