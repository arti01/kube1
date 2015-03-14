CREATE TABLE extpass
(
  pass character varying(255),
  login character varying(255) NOT NULL,
  rola character varying(255),
  id bigint NOT NULL,
  ulimit character varying(50),
  CONSTRAINT extpass_pkey PRIMARY KEY (id )
)

CREATE VIEW userpass AS 
         SELECT u.adr_email AS username, p.pass AS password
           FROM uzytkownik u
      JOIN passwords p ON p.id = u.haslo_id
UNION 
         SELECT extpass.login AS username, extpass.pass AS password
           FROM extpass;


CREATE VIEW user_roles_view AS 
         SELECT u.adr_email AS username, ur.role_name
           FROM user_roles ur
      JOIN uzytkownik_user_roles uur ON ur.id = uur.role_id
   JOIN uzytkownik u ON u.id = uur.uzytkownik_id
UNION 
         SELECT extpass.login AS username, extpass.rola AS role_name
           FROM extpass;

ALTER TABLE user_roles_view
  OWNER TO eod;


