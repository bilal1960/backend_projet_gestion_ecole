CREATE TABLE inscription
(
    id          UUID         NOT NULL,
    nom         VARCHAR(255) NOT NULL,
    prenom      VARCHAR(255) NOT NULL,
    naissance   date         NOT NULL,
    nationalite VARCHAR(255) NOT NULL,
    sexe        VARCHAR(255) NOT NULL,
    adresse     VARCHAR(255) NOT NULL,
    commune     VARCHAR(255) NOT NULL,
    minerval    FLOAT        NOT NULL,
    personne_id UUID,
    CONSTRAINT pk_inscription PRIMARY KEY (id),
    CONSTRAINT FK_INSCRIPTION_ON_PERSONNE FOREIGN KEY (personne_id) REFERENCES personne (id)
);

