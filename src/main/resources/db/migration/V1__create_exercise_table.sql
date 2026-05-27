CREATE TABLE exercise (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          category VARCHAR(255) NOT NULL,
                          weight INT NOT NULL,
                          reps INT NOT NULL
);