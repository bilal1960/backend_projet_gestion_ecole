CREATE TABLE ecole
(
    id               UUID        NOT NULL,
    nom              VARCHAR(30) NOT NULL,
    adresse          VARCHAR(30) NOT NULL,
    nombre_etudiants INT         NOT NULL,
    type             VARCHAR(30) NOT NULL,
    CONSTRAINT pk_ecole PRIMARY KEY (id)
);