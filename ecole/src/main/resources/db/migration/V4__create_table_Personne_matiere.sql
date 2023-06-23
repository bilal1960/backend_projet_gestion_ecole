
CREATE TABLE personne_matiere
(
    matiere_id  UUID NOT NULL,
    personne_id UUID NOT NULL,
    CONSTRAINT fk_permat_on_matiere FOREIGN KEY (matiere_id) REFERENCES matiere (id),
    CONSTRAINT fk_permat_on_personne FOREIGN KEY (personne_id) REFERENCES personne (id)
);




