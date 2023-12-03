CREATE TABLE matiere
(
    id            UUID        NOT NULL,
    nom           VARCHAR(30) NOT NULL,
    debut         date        NOT NULL,
    fin           date        NOT NULL,
    debutime      Time        not null,
    fintime       Time        not null,
    jour          VARCHAR(30),
    local         VARCHAR(50),
    secondaire    VARCHAR(20),
    professeur_id UUID,
    professeurauth0 VARCHAR(255),
    CONSTRAINT pk_matiere PRIMARY KEY (id),
    CONSTRAINT fk_matiere_professeur FOREIGN KEY (professeur_id) REFERENCES personne (id)
);


ALTER TABLE matiere
    ADD revision VARCHAR(255);
