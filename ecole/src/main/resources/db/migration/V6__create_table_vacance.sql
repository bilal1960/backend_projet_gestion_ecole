CREATE TABLE vacance
(
    id          UUID NOT NULL,
    datedebut   date NOT NULL,
    datefin     date NOT NULL,
    conge       INT NOT NULL DEFAULT 10,
    statut      VARCHAR(255),
    type        VARCHAR(255),
    commentaire VARCHAR(255),
    CONSTRAINT pk_vacance PRIMARY KEY (id)
);