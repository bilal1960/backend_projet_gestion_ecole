CREATE TABLE matiere
(
    id          UUID         NOT NULL,
    nom         VARCHAR(255) NOT NULL,
    debut       date         NOT NULL,
    fin         date         NOT NULL,
    CONSTRAINT pk_matiere PRIMARY KEY (id)
);



