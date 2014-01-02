SELECT plyr.*, role_type.name as role_name
FROM flm_player plyr
INNER JOIN flm_role plyr_role ON (player_seq_no = plyr.seq_no)
INNER JOIN flm_role_type role_type ON (role_type.seq_no = plyr_role.role_type_seq_no)
WHERE plyr_role.film_seq_no = ?