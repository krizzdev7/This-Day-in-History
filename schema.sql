CREATE DATABASE IF NOT EXISTS thisdayhistory CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE thisdayhistory;

CREATE TABLE IF NOT EXISTS historical_facts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  month TINYINT NOT NULL,         -- 1-12
  day TINYINT NOT NULL,           -- 1-31 (validate by month on insert)
  year SMALLINT NULL,             -- optional year of event, allow NULL
  event TEXT NOT NULL,
  category VARCHAR(64),
  source VARCHAR(255),
  favorite BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (month, day, event(200))
);

-- Sample Data
INSERT INTO historical_facts (month, day, year, event, category, source) VALUES
(1, 1, 1801, 'The Act of Union united Great Britain and Ireland.', 'Politics', 'Wikipedia'),
(1, 15, 1929, 'Martin Luther King Jr. was born.', 'Births', 'History.com'),
(2, 14, 1929, 'The St. Valentine''s Day Massacre occurred in Chicago.', 'Crime', 'Wikipedia'),
(3, 8, 1917, 'The February Revolution begins in Russia.', 'Revolutions', 'Wikipedia'),
(3, 15, 44, 'Julius Caesar was assassinated.', 'Assassinations', 'History.com'),
(4, 15, 1912, 'The Titanic sank in the North Atlantic Ocean.', 'Disasters', 'Wikipedia'),
(4, 30, 1789, 'George Washington was inaugurated as the first President of the United States.', 'Politics', 'History.com'),
(5, 1, 1931, 'The Empire State Building opened in New York City.', 'Architecture', 'Wikipedia'),
(5, 25, 1977, 'Star Wars was released in theaters.', 'Culture', 'History.com'),
(6, 6, 1944, 'D-Day: The Allied invasion of Normandy began.', 'Military', 'Wikipedia'),
(6, 28, 1914, 'Archduke Franz Ferdinand of Austria was assassinated, leading to World War I.', 'Assassinations', 'History.com'),
(7, 4, 1776, 'The United States Declaration of Independence was adopted.', 'Politics', 'History.com'),
(7, 20, 1969, 'Neil Armstrong walked on the Moon.', 'Space', 'NASA'),
(8, 15, 1947, 'India gained independence from British rule.', 'Politics', 'Wikipedia'),
(8, 24, 79, 'Mount Vesuvius erupted, destroying the city of Pompeii.', 'Disasters', 'History.com'),
(9, 1, 1939, 'Germany invaded Poland, beginning World War II.', 'Military', 'Wikipedia'),
(9, 11, 2001, 'A series of terrorist attacks occurred in the United States.', 'Terrorism', 'History.com'),
(10, 12, 1492, 'Christopher Columbus arrived in the Americas.', 'Exploration', 'History.com'),
(10, 31, 1517, 'Martin Luther posted his Ninety-five Theses.', 'Religion', 'Wikipedia'),
(11, 9, 1989, 'The Berlin Wall fell.', 'Politics', 'History.com'),
(11, 11, 1918, 'World War I ended.', 'Military', 'Wikipedia'),
(12, 7, 1941, 'The attack on Pearl Harbor occurred.', 'Military', 'History.com'),
(12, 17, 1903, 'The Wright brothers made their first successful flight.', 'Inventions', 'Wikipedia'),
(12, 25, 800, 'Charlemagne was crowned Emperor of the Romans.', 'Royalty', 'History.com'),
(1, 20, 1961, 'John F. Kennedy was inaugurated as the 35th President of the United States.', 'Politics', 'History.com'),
(2, 2, 1848, 'The Treaty of Guadalupe Hidalgo was signed, ending the Mexican-American War.', 'Treaties', 'History.com'),
(3, 4, 1789, 'The first U.S. Congress met in New York City.', 'Politics', 'History.com'),
(4, 1, 1976, 'Apple Inc. was founded by Steve Jobs, Steve Wozniak, and Ronald Wayne.', 'Business', 'Wikipedia'),
(5, 5, 1821, 'Napoleon Bonaparte died in exile on the island of Saint Helena.', 'Deaths', 'History.com'),
(6, 18, 1815, 'The Battle of Waterloo was fought.', 'Military', 'Wikipedia');
