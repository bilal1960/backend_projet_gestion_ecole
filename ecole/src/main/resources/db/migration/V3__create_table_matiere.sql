CREATE TABLE matiere
(
    id          UUID         NOT NULL,
    nom         VARCHAR(255) NOT NULL,
    debut       DATE         NOT NULL,
    fin         DATE         NOT NULL,
    personne_id UUID,
    CONSTRAINT pk_matiere PRIMARY KEY (id),
    CONSTRAINT uc_matiere_nom UNIQUE (nom),
    CONSTRAINT fk_matiere_personne FOREIGN KEY (personne_id) REFERENCES personne (id)
);


