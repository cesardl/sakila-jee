select actor0_.actor_id as actor_id1_0_0_, actor0_.first_name as first_na2_0_0_, actor0_.last_name as last_nam3_0_0_, actor0_.last_update as last_upd4_0_0_
from sakila.actor actor0_ where actor0_.actor_id=122;

select filmactors0_.actor_id as actor_id1_9_0_, filmactors0_.film_id as film_id2_9_0_, filmactors0_.actor_id as actor_id1_9_1_,
  filmactors0_.film_id as film_id2_9_1_, filmactors0_.last_update as last_upd3_9_1_
from sakila.film_actor filmactors0_ where filmactors0_.actor_id=122;

select * from film f
inner join film_actor fa on f.film_id = fa.film_id
where fa.actor_id = 1;

select * from actor where actor_id = 122;

select * from film where film_id = 100;

select * from film_actor where actor_id = 122;

select f.*, l.name from film f inner join film_actor fa on f.film_id = fa.film_id inner join language l on f.language_id = l.language_id where fa.actor_id = 100;
