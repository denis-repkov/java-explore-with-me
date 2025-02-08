DROP TABLE IF EXISTS endpoint_hits CASCADE;

CREATE TABLE endpoint_hits (
                               id SERIAL PRIMARY KEY,
                               app VARCHAR(255) NOT NULL,
                               uri VARCHAR(2000) NOT NULL,
                               ip VARCHAR(45) NOT NULL,
                               timestamp TIMESTAMP NOT NULL
);