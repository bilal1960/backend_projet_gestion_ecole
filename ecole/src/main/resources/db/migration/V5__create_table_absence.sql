CREATE TABLE absence
(
    id         UUID        NOT NULL,
    presence   VARCHAR(50) NOT NULL,
    date       date        NOT NULL,
    heuredebut time        Not null,
    heurefin   time        Not Null,
    certificat BOOLEAN     Not Null,
    personne_idpresence UUID,
    CONSTRAINT pk_absence PRIMARY KEY (id),
    CONSTRAINT fk_personne_presence FOREIGN KEY (personne_idpresence) REFERENCES personne (id)
);

