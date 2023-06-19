CREATE TABLE personne
(
    id          UUID         NOT NULL,
    prenom      VARCHAR(255) NOT NULL,
    nom         VARCHAR(255) NOT NULL,
    naissance   date         NOT NULL,
    nationalite VARCHAR(255) NOT NULL,
    adresse     VARCHAR(255) NOT NULL,
    sexe        VARCHAR(255) NOT NULL,
    CONSTRAINT pk_personne PRIMARY KEY (id)
);

ALTER TABLE personne
    ADD CONSTRAINT uc_fe31b440568efb37437d6af59 UNIQUE (prenom, nom);