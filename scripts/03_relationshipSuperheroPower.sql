CREATE TABLE superhero_power (
	superhero_id int REFERENCES superhero(id),
	power_id int REFERENCES power(id),
	CONSTRAINT superhero_power_pk PRIMARY KEY (superhero_id, power_id)
);
