CREATE TABLE ecole
(
    id               UUID        DEFAULT RANDOM_UUID() NOT NULL,
    nom              VARCHAR(30) NOT NULL,
    adresse          VARCHAR(30) NOT NULL,
    mail             varchar(30) not null,
    number           varchar(30) not null,
    type             VARCHAR(30) NOT NULL,
    CONSTRAINT pk_ecole PRIMARY KEY (id)
);
INSERT INTO ecole ( id,nom, adresse, mail, number, type)
VALUES
    (RANDOM_UUID(), 'Saint peter', '123 Rue royale', 'ecolea@example.com', '04789652', 'Primaire');
