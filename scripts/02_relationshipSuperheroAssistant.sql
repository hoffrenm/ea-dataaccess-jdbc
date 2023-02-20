ALTER TABLE assistants ADD COLUMN superhero_id_fk int;
ALTER TABLE assistants ADD CONSTRAINT superhero_id_fk FOREIGN KEY(id) REFERENCES superheroes(id);
