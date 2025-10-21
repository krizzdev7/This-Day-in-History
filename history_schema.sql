-- ============================================================================
-- This Day in History - Database Schema
-- MySQL 8.x
-- ============================================================================

DROP DATABASE IF EXISTS history_db;
CREATE DATABASE history_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE history_db;

-- ============================================================================
-- Table: events
-- Stores historical events with date, category, and description
-- ============================================================================
CREATE TABLE events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    year INT NOT NULL,
    month INT NOT NULL,
    day INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_date (month, day),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- Table: people
-- Stores famous people with birth/death dates
-- ============================================================================
CREATE TABLE people (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_year INT,
    death_year INT,
    birth_month INT,
    birth_day INT,
    death_month INT,
    death_day INT,
    category VARCHAR(100),
    description TEXT,
    image_url VARCHAR(255),
    INDEX idx_birth_date (birth_month, birth_day),
    INDEX idx_death_date (death_month, death_day)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- Table: quotes
-- Stores inspirational and historical quotes
-- ============================================================================
CREATE TABLE quotes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    author VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- SEED DATA - Historical Events (40+ events across different dates)
-- ============================================================================

INSERT INTO events (year, month, day, title, description, category, image_url) VALUES
-- January Events
(1793, 1, 21, 'King Louis XVI Executed', 'King Louis XVI of France was executed by guillotine in Paris during the French Revolution, marking a pivotal moment in European history.', 'Politics', 'https://example.com/louis_xvi.jpg'),
(1924, 1, 21, 'Lenin Dies', 'Vladimir Lenin, leader of the Russian Revolution and founder of the Soviet Union, died in Gorki, Russia.', 'Politics', 'https://example.com/lenin.jpg'),
(1976, 1, 21, 'First Commercial Concorde Flight', 'The supersonic Concorde began commercial passenger service, revolutionizing air travel with speeds over twice the speed of sound.', 'Technology', 'https://example.com/concorde.jpg'),

-- April Events
(1865, 4, 14, 'Abraham Lincoln Assassinated', 'President Abraham Lincoln was shot by John Wilkes Booth at Ford\'s Theatre in Washington, D.C. He died the following morning.', 'Politics', 'https://example.com/lincoln.jpg'),
(1912, 4, 15, 'Titanic Sinks', 'The RMS Titanic sank in the North Atlantic Ocean after hitting an iceberg, resulting in the deaths of over 1,500 passengers and crew.', 'Disaster', 'https://example.com/titanic.jpg'),
(1955, 4, 18, 'Einstein Dies', 'Albert Einstein, the renowned physicist who developed the theory of relativity, died in Princeton, New Jersey.', 'Science', 'https://example.com/einstein.jpg'),

-- July Events
(1789, 7, 14, 'Storming of the Bastille', 'The Bastille prison in Paris was stormed by revolutionaries, marking the beginning of the French Revolution. This day is now celebrated as Bastille Day.', 'Politics', 'https://example.com/bastille.jpg'),
(1969, 7, 20, 'Apollo 11 Moon Landing', 'Neil Armstrong and Buzz Aldrin became the first humans to walk on the moon during the Apollo 11 mission.', 'Space', 'https://example.com/moon_landing.jpg'),
(1969, 7, 21, 'Armstrong Walks on Moon', 'Neil Armstrong took his historic first steps on the lunar surface, uttering the famous words: "That\'s one small step for man, one giant leap for mankind."', 'Space', 'https://example.com/armstrong.jpg'),

-- October Events
(1492, 10, 12, 'Columbus Reaches the Americas', 'Christopher Columbus and his crew made landfall in the Bahamas, marking the beginning of European exploration of the Americas.', 'Exploration', 'https://example.com/columbus.jpg'),
(1968, 10, 16, 'Black Power Salute at Olympics', 'American athletes Tommie Smith and John Carlos raised their fists in a Black Power salute during the medal ceremony at the Mexico City Olympics.', 'Sports', 'https://example.com/black_power.jpg'),
(1973, 10, 17, 'OPEC Oil Embargo', 'OPEC proclaimed an oil embargo against nations supporting Israel in the Yom Kippur War, triggering the 1973 oil crisis.', 'Economics', 'https://example.com/oil_crisis.jpg'),
(1931, 10, 18, 'Thomas Edison Dies', 'Thomas Edison, the prolific American inventor who created the light bulb and phonograph, died in West Orange, New Jersey.', 'Science', 'https://example.com/edison.jpg'),
(1987, 10, 19, 'Black Monday Stock Market Crash', 'Stock markets around the world crashed, with the Dow Jones Industrial Average falling 22.6% in a single day.', 'Economics', 'https://example.com/black_monday.jpg'),
(1944, 10, 20, 'MacArthur Returns to Philippines', 'General Douglas MacArthur fulfilled his famous promise "I shall return" by landing on Leyte Island in the Philippines during World War II.', 'Military', 'https://example.com/macarthur.jpg'),
(1805, 10, 21, 'Battle of Trafalgar', 'British Admiral Horatio Nelson defeated the combined French and Spanish fleets off the coast of Spain, establishing British naval supremacy.', 'Military', 'https://example.com/trafalgar.jpg'),
(1879, 10, 21, 'Edison Tests First Light Bulb', 'Thomas Edison successfully tested the first practical incandescent light bulb, which burned for 13.5 hours.', 'Science', 'https://example.com/light_bulb.jpg'),
(1959, 10, 21, 'Guggenheim Museum Opens', 'The Solomon R. Guggenheim Museum, designed by Frank Lloyd Wright, opened in New York City.', 'Arts', 'https://example.com/guggenheim.jpg'),

-- November Events
(1989, 11, 9, 'Fall of the Berlin Wall', 'The Berlin Wall, which had divided East and West Berlin for 28 years, was opened, leading to German reunification.', 'Politics', 'https://example.com/berlin_wall.jpg'),
(1963, 11, 22, 'JFK Assassination', 'President John F. Kennedy was assassinated in Dallas, Texas, while riding in a motorcade through Dealey Plaza.', 'Politics', 'https://example.com/jfk.jpg'),
(1620, 11, 21, 'Mayflower Compact Signed', 'The Mayflower Compact was signed by the Pilgrims aboard the Mayflower, establishing a form of self-government.', 'Politics', 'https://example.com/mayflower.jpg'),

-- December Events
(1903, 12, 17, 'First Powered Flight', 'The Wright Brothers achieved the first sustained powered flight in Kitty Hawk, North Carolina, lasting 12 seconds.', 'Technology', 'https://example.com/wright_brothers.jpg'),
(1941, 12, 7, 'Pearl Harbor Attack', 'The Imperial Japanese Navy launched a surprise attack on Pearl Harbor, bringing the United States into World War II.', 'Military', 'https://example.com/pearl_harbor.jpg'),
(1991, 12, 26, 'Soviet Union Dissolves', 'The Soviet Union officially dissolved, ending the Cold War and marking the end of communist rule in Russia.', 'Politics', 'https://example.com/soviet_flag.jpg'),

-- February Events
(1945, 2, 19, 'Battle of Iwo Jima Begins', 'U.S. Marines landed on Iwo Jima, beginning one of the bloodiest battles of World War II in the Pacific.', 'Military', 'https://example.com/iwo_jima.jpg'),
(1962, 2, 20, 'John Glenn Orbits Earth', 'Astronaut John Glenn became the first American to orbit Earth aboard Friendship 7.', 'Space', 'https://example.com/john_glenn.jpg'),
(1986, 2, 28, 'Olof Palme Assassinated', 'Swedish Prime Minister Olof Palme was assassinated in Stockholm, a crime that remains unsolved.', 'Politics', 'https://example.com/palme.jpg'),

-- March Events
(1876, 3, 10, 'First Telephone Call', 'Alexander Graham Bell made the first successful telephone call to his assistant, saying "Mr. Watson, come here, I want to see you."', 'Technology', 'https://example.com/bell.jpg'),
(1965, 3, 21, 'Selma to Montgomery March', 'Martin Luther King Jr. led thousands of civil rights supporters on a march from Selma to Montgomery, Alabama.', 'Civil Rights', 'https://example.com/selma_march.jpg'),
(1918, 3, 31, 'Daylight Saving Time Begins', 'The United States implemented daylight saving time for the first time as a wartime measure.', 'Society', 'https://example.com/dst.jpg'),

-- May Events
(1937, 5, 6, 'Hindenburg Disaster', 'The German airship Hindenburg caught fire and was destroyed while attempting to dock in New Jersey, killing 36 people.', 'Disaster', 'https://example.com/hindenburg.jpg'),
(1945, 5, 8, 'V-E Day', 'Victory in Europe Day marked the formal acceptance of Nazi Germany\'s unconditional surrender, ending World War II in Europe.', 'Military', 'https://example.com/ve_day.jpg'),
(1954, 5, 17, 'Brown v. Board of Education', 'The U.S. Supreme Court ruled that racial segregation in public schools was unconstitutional.', 'Civil Rights', 'https://example.com/brown_v_board.jpg'),

-- June Events
(1944, 6, 6, 'D-Day Invasion', 'Allied forces launched the largest amphibious invasion in history on the beaches of Normandy, France, beginning the liberation of Western Europe.', 'Military', 'https://example.com/dday.jpg'),
(1989, 6, 4, 'Tiananmen Square Massacre', 'Chinese troops violently suppressed pro-democracy demonstrations in Beijing\'s Tiananmen Square.', 'Politics', 'https://example.com/tiananmen.jpg'),
(1967, 6, 12, 'Loving v. Virginia', 'The U.S. Supreme Court struck down laws banning interracial marriage in the landmark Loving v. Virginia decision.', 'Civil Rights', 'https://example.com/loving.jpg'),

-- August Events
(1945, 8, 6, 'Hiroshima Atomic Bombing', 'The United States dropped the first atomic bomb on Hiroshima, Japan, killing an estimated 140,000 people.', 'Military', 'https://example.com/hiroshima.jpg'),
(1969, 8, 15, 'Woodstock Festival Begins', 'The Woodstock Music and Art Fair began in upstate New York, becoming an iconic symbol of the 1960s counterculture.', 'Music', 'https://example.com/woodstock.jpg'),
(1991, 8, 24, 'Ukraine Declares Independence', 'Ukraine declared independence from the Soviet Union, beginning its path as a sovereign nation.', 'Politics', 'https://example.com/ukraine.jpg'),

-- September Events
(2001, 9, 11, 'September 11 Attacks', 'Terrorist attacks on the World Trade Center and Pentagon killed nearly 3,000 people, changing the course of modern history.', 'Terrorism', 'https://example.com/9_11.jpg'),
(1957, 9, 25, 'Little Rock Nine', 'Nine African American students enrolled at Central High School in Little Rock, Arkansas, under federal protection.', 'Civil Rights', 'https://example.com/little_rock.jpg'),
(1991, 9, 30, 'Haiti Coup d\'État', 'President Jean-Bertrand Aristide was overthrown in a military coup in Haiti.', 'Politics', 'https://example.com/haiti.jpg');

-- ============================================================================
-- SEED DATA - Famous People (20+ individuals)
-- ============================================================================

INSERT INTO people (name, birth_year, death_year, birth_month, birth_day, death_month, death_day, category, description, image_url) VALUES
-- Scientists
('Albert Einstein', 1879, 1955, 3, 14, 4, 18, 'Science', 'Theoretical physicist who developed the theory of relativity, one of the two pillars of modern physics.', 'https://example.com/einstein_portrait.jpg'),
('Marie Curie', 1867, 1934, 11, 7, 7, 4, 'Science', 'Pioneer in radioactivity research, first woman to win a Nobel Prize, and only person to win Nobel Prizes in two scientific fields.', 'https://example.com/curie.jpg'),
('Isaac Newton', 1643, 1727, 1, 4, 3, 31, 'Science', 'Mathematician and physicist who formulated the laws of motion and universal gravitation.', 'https://example.com/newton.jpg'),
('Charles Darwin', 1809, 1882, 2, 12, 4, 19, 'Science', 'Naturalist who established that all species descended from common ancestors through natural selection.', 'https://example.com/darwin.jpg'),
('Nikola Tesla', 1856, 1943, 7, 10, 1, 7, 'Science', 'Inventor and engineer known for contributions to the design of the modern alternating current electricity supply system.', 'https://example.com/tesla.jpg'),

-- Political Leaders
('Mahatma Gandhi', 1869, 1948, 10, 2, 1, 30, 'Politics', 'Leader of Indian independence movement through nonviolent civil disobedience.', 'https://example.com/gandhi.jpg'),
('Winston Churchill', 1874, 1965, 11, 30, 1, 24, 'Politics', 'British Prime Minister during World War II, known for his inspirational speeches and leadership.', 'https://example.com/churchill.jpg'),
('Nelson Mandela', 1918, 2013, 7, 18, 12, 5, 'Politics', 'Anti-apartheid revolutionary and first black president of South Africa.', 'https://example.com/mandela.jpg'),
('Abraham Lincoln', 1809, 1865, 2, 12, 4, 15, 'Politics', '16th President of the United States who preserved the Union during the Civil War and abolished slavery.', 'https://example.com/lincoln_portrait.jpg'),

-- Artists and Musicians
('Pablo Picasso', 1881, 1973, 10, 25, 4, 8, 'Arts', 'Spanish painter and sculptor, co-founder of Cubism and one of the most influential artists of the 20th century.', 'https://example.com/picasso.jpg'),
('Wolfgang Amadeus Mozart', 1756, 1791, 1, 27, 12, 5, 'Music', 'Prolific and influential composer of the Classical era, composed over 600 works.', 'https://example.com/mozart.jpg'),
('Leonardo da Vinci', 1452, 1519, 4, 15, 5, 2, 'Arts', 'Italian polymath: painter, inventor, scientist, and architect of the Renaissance.', 'https://example.com/davinci.jpg'),
('Vincent van Gogh', 1853, 1890, 3, 30, 7, 29, 'Arts', 'Post-Impressionist painter known for bold colors and emotional honesty in his works.', 'https://example.com/vangogh.jpg'),

-- Writers
('William Shakespeare', 1564, 1616, 4, 23, 4, 23, 'Literature', 'English playwright and poet, widely regarded as the greatest writer in the English language.', 'https://example.com/shakespeare.jpg'),
('Mark Twain', 1835, 1910, 11, 30, 4, 21, 'Literature', 'American author and humorist, wrote The Adventures of Tom Sawyer and Adventures of Huckleberry Finn.', 'https://example.com/twain.jpg'),
('Jane Austen', 1775, 1817, 12, 16, 7, 18, 'Literature', 'English novelist known for her wit and social commentary in works like Pride and Prejudice.', 'https://example.com/austen.jpg'),

-- Athletes
('Muhammad Ali', 1942, 2016, 1, 17, 6, 3, 'Sports', 'Professional boxer and activist, widely regarded as one of the greatest athletes of all time.', 'https://example.com/ali.jpg'),
('Babe Ruth', 1895, 1948, 2, 6, 8, 16, 'Sports', 'Baseball legend who set numerous records and became an American cultural icon.', 'https://example.com/ruth.jpg'),

-- Activists
('Martin Luther King Jr.', 1929, 1968, 1, 15, 4, 4, 'Civil Rights', 'Baptist minister and activist who became the most visible spokesperson of the civil rights movement.', 'https://example.com/mlk.jpg'),
('Rosa Parks', 1913, 2005, 2, 4, 10, 24, 'Civil Rights', 'Civil rights activist whose refusal to give up her bus seat sparked the Montgomery Bus Boycott.', 'https://example.com/parks.jpg'),

-- Entrepreneurs
('Steve Jobs', 1955, 2011, 2, 24, 10, 5, 'Technology', 'Co-founder of Apple Inc., pioneer of personal computing and modern technology.', 'https://example.com/jobs.jpg');

-- ============================================================================
-- SEED DATA - Inspirational Quotes (15+ quotes)
-- ============================================================================

INSERT INTO quotes (author, text) VALUES
('Albert Einstein', 'Life is like riding a bicycle. To keep your balance, you must keep moving.'),
('Mahatma Gandhi', 'Be the change that you wish to see in the world.'),
('Nelson Mandela', 'Education is the most powerful weapon which you can use to change the world.'),
('Martin Luther King Jr.', 'Darkness cannot drive out darkness; only light can do that. Hate cannot drive out hate; only love can do that.'),
('Winston Churchill', 'Success is not final, failure is not fatal: it is the courage to continue that counts.'),
('Marie Curie', 'Nothing in life is to be feared, it is only to be understood. Now is the time to understand more, so that we may fear less.'),
('Leonardo da Vinci', 'Learning never exhausts the mind.'),
('Pablo Picasso', 'Action is the foundational key to all success.'),
('William Shakespeare', 'We know what we are, but know not what we may be.'),
('Mark Twain', 'The secret of getting ahead is getting started.'),
('Steve Jobs', 'Innovation distinguishes between a leader and a follower.'),
('Rosa Parks', 'I would like to be known as a person who is concerned about freedom and equality and justice and prosperity for all people.'),
('Abraham Lincoln', 'Whatever you are, be a good one.'),
('Jane Austen', 'There is no charm equal to tenderness of heart.'),
('Nikola Tesla', 'The present is theirs; the future, for which I really worked, is mine.'),
('Muhammad Ali', 'Don\'t count the days, make the days count.'),
('Vincent van Gogh', 'Great things are done by a series of small things brought together.'),
('Charles Darwin', 'It is not the strongest of the species that survives, nor the most intelligent, but the one most responsive to change.');

-- ============================================================================
-- Create Views for Easy Data Access
-- ============================================================================

CREATE VIEW today_events AS
SELECT e.*, DATE_FORMAT(CONCAT(e.year, '-', e.month, '-', e.day), '%Y-%m-%d') as event_date
FROM events e
ORDER BY e.year DESC;

CREATE VIEW people_birthdays AS
SELECT p.*, CONCAT(p.name, ' born on ', DATE_FORMAT(CONCAT('2000-', p.birth_month, '-', p.birth_day), '%M %d')) as birth_info
FROM people p
WHERE p.birth_month IS NOT NULL AND p.birth_day IS NOT NULL
ORDER BY p.birth_month, p.birth_day;

-- ============================================================================
-- End of Schema
-- ============================================================================

SELECT 'Database setup completed successfully!' as Status;
SELECT COUNT(*) as TotalEvents FROM events;
SELECT COUNT(*) as TotalPeople FROM people;
SELECT COUNT(*) as TotalQuotes FROM quotes;
