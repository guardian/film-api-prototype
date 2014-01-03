-- name: film-by-pk
-- Direct film lookup
SELECT *
FROM flm_film
WHERE seq_no = ?

-- name: player-by-pk
-- Direct film lookup
SELECT *
FROM flm_player
WHERE seq_no = ?

-- name: genres-by-film
-- Lookup the genres associated with a film
SELECT name
FROM flm_genre
INNER JOIN flm_genre_to_film ON (flm_genre.seq_no = flm_genre_to_film.genre_seq_no)
WHERE flm_genre_to_film.film_seq_no = ?

-- name: films-by-player
-- Lookup the films that feature the specified player
SELECT film.*, role_type.name as role_name
FROM flm_player plyr
INNER JOIN flm_role plyr_role ON (player_seq_no = plyr.seq_no)
INNER JOIN flm_role_type role_type ON (role_type.seq_no = plyr_role.role_type_seq_no)
INNER JOIN flm_film film ON (film.seq_no = plyr_role.film_seq_no)
WHERE plyr.seq_no = ?

-- name: players-by-film
-- Lookup who is associated with a particular film
SELECT plyr.*, role_type.name as role_name
FROM flm_player plyr
INNER JOIN flm_role plyr_role ON (player_seq_no = plyr.seq_no)
INNER JOIN flm_role_type role_type ON (role_type.seq_no = plyr_role.role_type_seq_no)
WHERE plyr_role.film_seq_no = ?