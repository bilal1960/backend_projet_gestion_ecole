CREATE TABLE note
(
    id           UUID    NOT NULL,
    nom          VARCHAR(255) not null ,
    deliberation date not null ,
    session      VARCHAR(255),
    resultat     INT     NOT NULL,
    moyenne      FLOAT   NOT NULL,
    reussi       BOOLEAN NOT NULL,
    CONSTRAINT pk_note PRIMARY KEY (id)
);