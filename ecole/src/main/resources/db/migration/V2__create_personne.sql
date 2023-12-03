CREATE TABLE personne
(
    id          UUID        NOT NULL,
    prenom      VARCHAR(30) NOT NULL,
    nom         VARCHAR(30) NOT NULL,
    naissance   date        NOT NULL,
    nationalite VARCHAR(30) NOT NULL,
    adresse     VARCHAR(50) NOT NULL,
    sexe        VARCHAR(10) NOT NULL,
    moyenne FLOAT,
    mail VARCHAR(255) NOT NULL,
    auth0_id VARCHAR(255),

    CONSTRAINT pk_personne PRIMARY KEY (id)
);
ALTER TABLE personne
    ADD CONSTRAINT uc_fe31b440568efb37437d6af59 UNIQUE (prenom, nom);
ALTER TABLE personne
    ADD statut VARCHAR(30);

INSERT INTO personne (id, prenom, nom, naissance, nationalite, adresse, sexe, moyenne, mail, auth0_id, statut)
VALUES ('a7555be7-6af7-4154-850d-ff71990dc924', 'bilal', 'barbe', '1996-08-09', 'belge', 'rue de la pacification 44', 'homme', 0.0, 'bilal45001@hotmail.com', 'auth0|6491063c4a5f17311d5691fb', 'professeur');


