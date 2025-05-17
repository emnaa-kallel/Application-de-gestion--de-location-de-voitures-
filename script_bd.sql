-- Suppression des tables définitivement si elles existent
DROP TABLE Penalite PURGE;
DROP TABLE Location PURGE;
DROP TABLE Voiture PURGE;
DROP TABLE Personnel PURGE;
DROP TABLE Client PURGE;
DROP TABLE Utilisateur PURGE;
DROP TABLE Assurance PURGE;



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
CREATE TABLE Personnel(
    CIN NUMBER(8) PRIMARY KEY, 
    role VARCHAR2(20) CHECK (role IN ('admin', 'employe')), 
    FOREIGN KEY (CIN) REFERENCES Utilisateur(CIN) ON DELETE CASCADE
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

-- Table des paiements
CREATE TABLE paiements (
    id NUMBER PRIMARY KEY,
    location_id NUMBER,
    penalite_id NUMBER,
    date_paiement DATE DEFAULT SYSDATE,
    montant NUMBER(10,2) NOT NULL,
    methode VARCHAR2(50) NOT NULL,
    reference VARCHAR2(100),
    CONSTRAINT fk_paiement_location FOREIGN KEY (location_id) REFERENCES locations(id),
    CONSTRAINT fk_paiement_penalite FOREIGN KEY (penalite_id) REFERENCES penalites(id),
    -- Une contrainte pour s'assurer qu'au moins un des deux IDs est non null
    CONSTRAINT check_paiement_ids CHECK (
        (location_id IS NOT NULL AND penalite_id IS NULL) OR
        (location_id IS NULL AND penalite_id IS NOT NULL) OR
        (location_id IS NOT NULL AND penalite_id IS NOT NULL)
    )
);

-- Séquences pour les IDs auto-incrémentés
CREATE SEQUENCE seq_voiture_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_location_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_penalite_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_paiement_id START WITH 1 INCREMENT BY 1;

-- Insertion de données

-- Utilisateurs
INSERT INTO Utilisateur VALUES (12345678, 'client1', 'password123', 'Doe', 'John', 98765432, 'john.doe@email.com', 'client');
INSERT INTO Utilisateur VALUES (87654321, 'admin1', 'adminpass', 'Smith', 'Jane', 12345678, 'jane.smith@email.com', 'personnel');

-- Clients
INSERT INTO Client VALUES (12345678, '12 rue de la Paix, Tunis', TO_DATE('1990-05-15', 'YYYY-MM-DD'), 123456, TO_DATE('2030-05-15', 'YYYY-MM-DD'));

-- Personnel
INSERT INTO Personnel VALUES (87654321, 'admin');

-- Assurances
INSERT INTO Assurance VALUES (1, 'AXA');
INSERT INTO Assurance VALUES (2, 'Allianz');

-- Voitures (avec id_assurance)
INSERT INTO Voiture VALUES ('123TUN4567', 'Toyota', 'Corolla', 2020, 'en_marche', 'disponible', 30.500, 'essence', 'automatique', TO_DATE('2021-01-01', 'YYYY-MM-DD'), TO_DATE('2022-01-01', 'YYYY-MM-DD'), 1);
INSERT INTO Voiture VALUES ('234TUN4568', 'Honda', 'Civic', 2021, 'en_panne', 'indisponible', 40.250, 'diesel', 'manuel', TO_DATE('2022-02-01', 'YYYY-MM-DD'), TO_DATE('2023-02-01', 'YYYY-MM-DD'), 2);

COMMIT;
