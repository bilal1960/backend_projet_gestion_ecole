CREATE TABLE matiere
(
    id          UUID         NOT NULL,
    nom         VARCHAR(255) NOT NULL,
    debut       date         NOT NULL,
    fin         date         NOT NULL,
    debutime time not null,
    fintime time not null,
    inscriptions_id UUID,
    professeur_id UUID,
    CONSTRAINT pk_matiere PRIMARY KEY (id),
    CONSTRAINT fk_matiere_inscription FOREIGN KEY (inscriptions_id) REFERENCES inscription (id),
    CONSTRAINT fk_matiere_professeur FOREIGN KEY (professeur_id) REFERENCES personne (id)
);

