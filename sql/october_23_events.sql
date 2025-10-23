-- October 23 Historical Events
USE history_db;

INSERT INTO events (year, month, day, title, description, category) VALUES
(1956, 10, 23, 'Hungarian Revolution of 1956', 'Hungarian uprising against Soviet control begins in Budapest', 'Politics'),
(1983, 10, 23, 'Beirut barracks bombings', 'Suicide truck bombings killed 241 American and 58 French peacekeepers in Lebanon', 'Military'),
(2001, 10, 23, 'Apple releases iPod', 'Apple Computer introduces the first iPod portable music player', 'Technology'),
(1942, 10, 23, 'Second Battle of El Alamein begins', 'Major World War II battle in North Africa begins', 'Military'),
(2011, 10, 23, 'Turkish earthquake', 'Magnitude 7.2 earthquake strikes eastern Turkey killing over 600 people', 'Disasters'),
(1915, 10, 23, 'Woman Suffrage Parade in New York', '25,000 women march in New York City for the right to vote', 'Politics'),
(1973, 10, 23, 'Nixon refuses to hand over tapes', 'Watergate scandal: President Nixon refuses to comply with subpoena', 'Politics'),
(1989, 10, 23, 'Hungary becomes a republic', 'Hungary declares itself an independent republic, ending communist rule', 'Politics'),
(1906, 10, 23, 'Alberto Santos-Dumont flies first airplane in Europe', 'Brazilian aviator flies 60 meters in Paris', 'Technology'),
(2002, 10, 23, 'Moscow theater hostage crisis begins', 'Chechen rebels take 850 hostages in Moscow theater', 'Military'),
(1992, 10, 23, 'Emperor Akihito becomes first Japanese Emperor to visit China', 'Historic diplomatic visit to China', 'Politics'),
(1954, 10, 23, 'Western European Union established', 'WEU defense alliance created', 'Politics'),
(1970, 10, 23, 'Gary Gabelich sets land speed record', 'Reaches 622 mph in Blue Flame rocket car', 'Sports'),
(1981, 10, 23, 'National debt of US reaches $1 trillion', 'First time US national debt exceeds one trillion dollars', 'Economics');

SELECT 'October 23 events added successfully!' AS Status;
SELECT COUNT(*) AS Total FROM events WHERE month=10 AND day=23;
