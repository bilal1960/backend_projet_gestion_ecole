CREATE TABLE vacance
(
    id          UUID NOT NULL,
    datedebut   date NOT NULL,
    datefin     date NOT NULL,
    type        VARCHAR(255),
    commentaire VARCHAR(255),
    personne_vacance UUID,

    CONSTRAINT pk_vacance PRIMARY KEY (id),
    CONSTRAINT fk_personne_vacance FOREIGN KEY (personne_vacance) REFERENCES personne (id)
);


