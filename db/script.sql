SHOW DATABASES;

CREATE DATABASE isinexus_db;

CREATE USER 'nexus'@'localhost' IDENTIFIED BY 'ThisIsTheMySQLserverpasswordforthejavaproject-1710';

#setting permissions
GRANT SELECT, INSERT, UPDATE, DELETE
ON isinexus_db.*
TO 'nexus'@'localhost';

FLUSH PRIVILEGES;

USE isinexus_db;

#Creating the admin tabl
CREATE TABLE Admins(
    admin_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY ,
    username VARCHAR(50) NOT NULL UNIQUE ,
    password VARCHAR(255) NOT NULL
);


# Creating "Etudiants" table
CREATE TABLE etudiant(
    etu_id INT PRIMARY KEY AUTO_INCREMENT,
    mat VARCHAR(30) NOT NULL,
    etu_nom VARCHAR(50) NOT NULL,
    etu_pr VARCHAR(50) NOT NULL,
    theme TEXT NOT NULL
) AUTO_INCREMENT = 4000;

# Creating table "Enseignants"
CREATE TABLE enseignant(
    en_id INT PRIMARY KEY AUTO_INCREMENT,
    en_nom VARCHAR(50) NOT NULL,
    en_pr VARCHAR(50) NOT NULL,
    grade VARCHAR(30) NOT NULL,
    speciality VARCHAR(30) NOT NULL
)auto_increment = 10000;

#Creating table "Classe"
CREATE TABLE classe(
    classe_id INT PRIMARY KEY AUTO_INCREMENT,
    classe_name VARCHAR(30) NOT NULL,
    capacity INT NOT NULL,
    hasVideoProjector BOOLEAN NOT NULL
);

ALTER TABLE classe MODIFY COLUMN classe_id INT AUTO_INCREMENT;
ALTER TABLE classe MODIFY COLUMN classe_name VARCHAR(30) UNIQUE NOT NULL;
ALTER TABLE classe
  ADD CONSTRAINT chk_hasVideoProjector
  CHECK (hasVideoProjector IN (0,1));


# Creating table "soutenance"
CREATE TABLE soutenance(
    soutenance_id INT PRIMARY KEY AUTO_INCREMENT,

    soutenance_date DATE NOT NULL,

    soutenance_salle INT NOT NULL,
    soutenance_etudiant INT NOT NULL,

    soutenance_president INT NOT NULL,
    soutenance_examinateur INT NOT NULL,
    soutenance_encadreur INT NOT NULL,

    heure_start TIME NOT NULL,
    heure_end TIME NOT NULL,

    CONSTRAINT fk_soutenance_salle
        FOREIGN KEY (soutenance_salle)
        REFERENCES classe(classe_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_soutenance_etudiant
        FOREIGN KEY (soutenance_etudiant)
        REFERENCES etudiant(etu_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_soutenance_president
        FOREIGN KEY (soutenance_president)
        REFERENCES enseignant(en_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_soutenance_examinateur
        FOREIGN KEY (soutenance_examinateur)
        REFERENCES enseignant(en_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_soutenance_encadreur
        FOREIGN KEY (soutenance_encadreur)
        REFERENCES enseignant(en_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_diff_heures
        CHECK (heure_end > heure_start),

    CONSTRAINT uq_etudiant UNIQUE (soutenance_etudiant)#1 "Etu" = 1 "soutenance"
);

DELIMITER $$

CREATE TRIGGER trg_soutenance_no_overlap_insert
BEFORE INSERT ON soutenance
FOR EACH ROW
BEGIN

# looking for multiple "Enseignant"
IF NEW.soutenance_president = NEW.soutenance_examinateur
   OR NEW.soutenance_president = NEW.soutenance_encadreur
   OR NEW.soutenance_examinateur = NEW.soutenance_encadreur
THEN
   SIGNAL SQLSTATE '45000'
   SET MESSAGE_TEXT = 'Les enseignants doivent etre differents';
END IF;

  IF EXISTS (
    SELECT 1
    FROM soutenance s
    WHERE s.soutenance_date = NEW.soutenance_date
      AND s.soutenance_salle = NEW.soutenance_salle
      AND NEW.heure_start < s.heure_end
      AND s.heure_start < NEW.heure_end
  ) THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Conflit: salle deja occupee a ce creneau';
  END IF;

  #checking for 2+ presence of an "Enseignant" in a single "Soutenance"
  IF EXISTS (
    SELECT 1
    FROM soutenance s
    WHERE s.soutenance_date = NEW.soutenance_date
      AND (
           s.soutenance_president = NEW.soutenance_president OR
           s.soutenance_examinateur = NEW.soutenance_examinateur OR
           s.soutenance_encadreur = NEW.soutenance_encadreur
          )
      AND NEW.heure_start < s.heure_end
      AND s.heure_start < NEW.heure_end
  ) THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Conflit: un enseignant est deja occupe a ce creneau';
  END IF;

END$$

CREATE TRIGGER trg_soutenance_no_overlap_update
BEFORE UPDATE ON soutenance
FOR EACH ROW
BEGIN

  IF EXISTS (
    SELECT 1
    FROM soutenance s
    WHERE s.soutenance_id <> NEW.soutenance_id
      AND s.soutenance_date = NEW.soutenance_date
      AND s.soutenance_salle = NEW.soutenance_salle
      AND NEW.heure_start < s.heure_end
      AND s.heure_start < NEW.heure_end
  ) THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Conflit: salle deja occupee a ce creneau';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM soutenance s
    WHERE s.soutenance_id <> NEW.soutenance_id
      AND s.soutenance_date = NEW.soutenance_date
      AND (
           s.soutenance_president = NEW.soutenance_president OR
           s.soutenance_examinateur = NEW.soutenance_examinateur OR
           s.soutenance_encadreur = NEW.soutenance_encadreur
          )
      AND NEW.heure_start < s.heure_end
      AND s.heure_start < NEW.heure_end
  ) THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Conflit: un enseignant est deja occupe a ce creneau';
  END IF;

END$$

DELIMITER ;

SELECT * FROM soutenance;
SELECT * FROM etudiant;
SELECT * FROM enseignant;
SELECT * FROM classe;
