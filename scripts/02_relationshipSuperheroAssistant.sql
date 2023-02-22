ALTER TABLE assistant ADD COLUMN superhero_id_fk int;
ALTER TABLE assistant ADD CONSTRAINT superhero_id_fk FOREIGN KEY(id) REFERENCES superhero(id);
