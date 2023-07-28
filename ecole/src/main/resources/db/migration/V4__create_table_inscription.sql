CREATE TABLE inscription
(
    id          UUID         NOT NULL,
    commune     VARCHAR(30) NOT NULL,
    minerval    FLOAT        NOT NULL,
    date_inscrit date not null,
    rembourser FLOAT NOT NULL,
    section VARCHAR(20),
    secondaire_anne VARCHAR(30) not null ,
    personne_id UUID not null,
    CONSTRAINT pk_inscription PRIMARY KEY (id),
    CONSTRAINT FK_INSCRIPTION_ON_PERSONNE FOREIGN KEY (personne_id) REFERENCES personne (id)
);




