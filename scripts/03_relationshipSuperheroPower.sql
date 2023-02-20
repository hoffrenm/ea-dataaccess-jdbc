CREATE TABLE superhero_power (
	superhero_id int REFERENCES superheroes(id),
	power_id int REFERENCES powers(id),
	CONSTRAINT superhero_power_pk PRIMARY KEY (superhero_id, power_id)
)
