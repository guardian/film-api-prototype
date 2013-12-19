(ns film-api.queries)

(def queries {:players-by-film (str "SELECT plyr.*, role_type.name as role_name"
	" FROM flm_player plyr"
	" INNER JOIN flm_role plyr_role ON (player_seq_no = plyr.seq_no)"
	" INNER JOIN flm_role_type role_type ON (role_type.seq_no = plyr_role.role_type_seq_no)"
	" WHERE plyr_role.film_seq_no = ?")
	:genres-by-film (str "SELECT name"
		" FROM flm_genre"
		" INNER JOIN flm_genre_to_film ON (flm_genre.seq_no = flm_genre_to_film.genre_seq_no)"
		" WHERE flm_genre_to_film.film_seq_no = ?")
	:films-by-player (str "SELECT film.*, role_type.name as role_name"
		" FROM flm_player plyr"
		" INNER JOIN flm_role plyr_role ON (player_seq_no = plyr.seq_no)"
		" INNER JOIN flm_role_type role_type ON (role_type.seq_no = plyr_role.role_type_seq_no)"
		" INNER JOIN flm_film film ON (film.seq_no = plyr_role.film_seq_no)"
		" WHERE plyr.seq_no = ?")})