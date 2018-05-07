INSERT INTO language(language_id, name, last_update) VALUES (1,'English',CURRENT_TIMESTAMP());

INSERT INTO actor(actor_id, first_name, last_name, last_update) VALUES
(100,'SPENCER','DEPP',CURRENT_TIMESTAMP()),
(101,'SUSAN','DAVIS','2006-02-15 04:34:33');

INSERT INTO film (film_id,title, description, release_year, language_id, original_language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features, last_update) VALUES
(1,'ACADEMY DINOSAUR','A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies',CURRENT_DATE(),1,NULL,6,'0.99',86,'20.99','PG','Deleted Scenes,Behind the Scenes',CURRENT_TIMESTAMP()),
(2,'ACE GOLDFINGER','A Astounding Epistle of a Database Administrator And a Explorer who must Find a Car in Ancient China',CURRENT_DATE(),1,NULL,3,'4.99',48,'12.99','G','Trailers,Deleted Scenes',CURRENT_TIMESTAMP()),
(3,'ADAPTATION HOLES','A Astounding Reflection of a Lumberjack And a Car who must Sink a Lumberjack in A Baloon Factory',CURRENT_DATE(),1,NULL,7,'2.99',50,'18.99','NC-17','Trailers,Deleted Scenes',CURRENT_TIMESTAMP());

INSERT INTO film_actor(actor_id, film_id, last_update) VALUES (100, 1, CURRENT_TIMESTAMP()), (100, 2, CURRENT_TIMESTAMP());