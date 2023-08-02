CREATE TABLE ecole
(
    id               UUID         NOT NULL,
    nom              VARCHAR(30) NOT NULL,
    adresse          VARCHAR(30) NOT NULL,
    mail             varchar(30) not null,
    number           varchar(30) not null,
    type             VARCHAR(30) NOT NULL,
    CONSTRAINT pk_ecole PRIMARY KEY (id)
);
INSERT INTO ecole ( id,nom, adresse, mail, number, type)
VALUES('04ece6ce-2690-4152-a5c9-09d40d5891b7', 'Saint peter', ' Rue royale 123', 'ecolea@example.com', '04789652', 'secondaire');
