CREATE TABLE note
(
    id           UUID    NOT NULL,
    nom          VARCHAR(255) not null ,
    deliberation date not null ,
    session      VARCHAR(255),
    resultat     FLOAT    NOT NULL,

    reussi       BOOLEAN NOT NULL,
    personne_note UUID,

    CONSTRAINT pk_note PRIMARY KEY (id),
    CONSTRAINT fk_personne_note FOREIGN KEY (personne_note) REFERENCES personne (id)
);
