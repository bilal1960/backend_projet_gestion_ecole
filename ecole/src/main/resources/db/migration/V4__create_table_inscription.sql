CREATE TABLE inscription
(
    id          UUID         NOT NULL,
    commune     VARCHAR(50) NOT NULL,
    minerval    FLOAT        NOT NULL,
    personne_id UUID not null,
    CONSTRAINT pk_inscription PRIMARY KEY (id),
    CONSTRAINT FK_INSCRIPTION_ON_PERSONNE FOREIGN KEY (personne_id) REFERENCES personne (id)

);

