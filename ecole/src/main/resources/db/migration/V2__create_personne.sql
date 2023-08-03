CREATE TABLE personne
(
    id          UUID        NOT NULL,
    prenom      VARCHAR(30) NOT NULL,
    nom         VARCHAR(30) NOT NULL,
    naissance   date        NOT NULL,
    nationalite VARCHAR(30) NOT NULL,
    adresse     VARCHAR(50) NOT NULL,
    sexe        VARCHAR(10) NOT NULL,
    CONSTRAINT pk_personne PRIMARY KEY (id)
);
ALTER TABLE personne
    ADD CONSTRAINT uc_fe31b440568efb37437d6af59 UNIQUE (prenom, nom);
ALTER TABLE personne
    ADD statut VARCHAR(30);
