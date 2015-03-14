CREATE TABLE config (
    id bigint NOT NULL,
    nazwa character varying(255),
    opis character varying(255),
    wartosc character varying(255)
);


--
-- TOC entry 161 (class 1259 OID 46630)
-- Dependencies: 6
-- Name: dzial; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE dzial (
    id bigint NOT NULL,
    nazwa character varying(255) NOT NULL
);


--
-- TOC entry 171 (class 1259 OID 46690)
-- Dependencies: 6
-- Name: kom_kolejka; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE kom_kolejka (
    id bigint NOT NULL,
    adres_list character varying(255),
    status integer,
    temat character varying(255),
    tresc character varying(255)
);


--
-- TOC entry 162 (class 1259 OID 46637)
-- Dependencies: 6
-- Name: passwords; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE passwords (
    id bigint NOT NULL,
    pass character varying(255)
);


CREATE TABLE struktura (
    id bigint NOT NULL,
    przyjmowanie_wnioskow integer NOT NULL,
    st_kier integer NOT NULL,
    usuniety integer,
    dzial_id bigint,
    szef_id bigint,
    sec_user_id bigint,
    user_id bigint
);


--
-- TOC entry 164 (class 1259 OID 46647)
-- Dependencies: 6
-- Name: user_roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE user_roles (
    id integer NOT NULL,
    role_name character varying(255)
);


--
-- TOC entry 165 (class 1259 OID 46654)
-- Dependencies: 6
-- Name: uzytkownik; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE uzytkownik (
    id bigint NOT NULL,
    adr_email character varying(255),
    ext_id integer,
    fullname character varying(255) NOT NULL,
    haslo_id bigint
);


--
-- TOC entry 172 (class 1259 OID 46695)
-- Dependencies: 6
-- Name: uzytkownik_user_roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE uzytkownik_user_roles (
    role_id integer NOT NULL,
    uzytkownik_id bigint NOT NULL
);


--
-- TOC entry 181 (class 1259 OID 46797)
-- Dependencies: 1934 6
-- Name: user_roles_view; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW user_roles_view AS
    SELECT u.adr_email AS username, ur.role_name FROM ((user_roles ur JOIN uzytkownik_user_roles uur ON ((ur.id = uur.role_id))) JOIN uzytkownik u ON ((u.id = uur.uzytkownik_id)));


--
-- TOC entry 182 (class 1259 OID 46801)
-- Dependencies: 1935 6
-- Name: userpass; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW userpass AS
    SELECT u.adr_email AS username, p.pass AS password FROM (uzytkownik u JOIN passwords p ON ((p.id = u.haslo_id)));


--
-- TOC entry 166 (class 1259 OID 46661)
-- Dependencies: 6
-- Name: wn_historia; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE wn_historia (
    id bigint NOT NULL,
    data_zmiany timestamp without time zone,
    opis_zmiany character varying(255),
    akceptant bigint,
    status_id bigint,
    urlop_id bigint,
    zmieniajacy bigint
);


--
-- TOC entry 167 (class 1259 OID 46666)
-- Dependencies: 6
-- Name: wn_rodzaje; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE wn_rodzaje (
    id bigint NOT NULL,
    opis character varying(255)
);


--
-- TOC entry 168 (class 1259 OID 46673)
-- Dependencies: 6
-- Name: wn_statusy; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE wn_statusy (
    id bigint NOT NULL,
    kolor character varying(255),
    opis character varying(255),
    skrot character varying(255)
);


--
-- TOC entry 169 (class 1259 OID 46680)
-- Dependencies: 6
-- Name: wn_urlop; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE wn_urlop (
    id bigint NOT NULL,
    data_do date,
    data_od date,
    data_wprowadzenia timestamp without time zone,
    nr_wniosku character varying(255),
    akceptant_id bigint,
    przyjmujacy bigint,
    rodzaj_id bigint,
    status_id bigint,
    uid bigint
);


--
-- TOC entry 1965 (class 2606 OID 46689)
-- Dependencies: 170 170 1987
-- Name: config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY config
    ADD CONSTRAINT config_pkey PRIMARY KEY (id);


--
-- TOC entry 1937 (class 2606 OID 46636)
-- Dependencies: 161 161 1987
-- Name: dzial_nazwa_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dzial
    ADD CONSTRAINT dzial_nazwa_key UNIQUE (nazwa);


--
-- TOC entry 1939 (class 2606 OID 46634)
-- Dependencies: 161 161 1987
-- Name: dzial_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY dzial
    ADD CONSTRAINT dzial_pkey PRIMARY KEY (id);


--
-- TOC entry 1967 (class 2606 OID 46694)
-- Dependencies: 171 171 1987
-- Name: kom_kolejka_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY kom_kolejka
    ADD CONSTRAINT kom_kolejka_pkey PRIMARY KEY (id);


--
-- TOC entry 1941 (class 2606 OID 46641)
-- Dependencies: 162 162 1987
-- Name: passwords_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY passwords
    ADD CONSTRAINT passwords_pkey PRIMARY KEY (id);


--
-- TOC entry 1943 (class 2606 OID 46646)
-- Dependencies: 163 163 1987
-- Name: struktura_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY struktura
    ADD CONSTRAINT struktura_pkey PRIMARY KEY (id);


--
-- TOC entry 1945 (class 2606 OID 46651)
-- Dependencies: 164 164 1987
-- Name: user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (id);


--
-- TOC entry 1947 (class 2606 OID 46653)
-- Dependencies: 164 164 1987
-- Name: user_roles_role_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_roles
    ADD CONSTRAINT user_roles_role_name_key UNIQUE (role_name);


--
-- TOC entry 1949 (class 2606 OID 46660)
-- Dependencies: 165 165 1987
-- Name: uzytkownik_adr_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY uzytkownik
    ADD CONSTRAINT uzytkownik_adr_email_key UNIQUE (adr_email);


--
-- TOC entry 1951 (class 2606 OID 46658)
-- Dependencies: 165 165 1987
-- Name: uzytkownik_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY uzytkownik
    ADD CONSTRAINT uzytkownik_pkey PRIMARY KEY (id);


--
-- TOC entry 1969 (class 2606 OID 46699)
-- Dependencies: 172 172 172 1987
-- Name: uzytkownik_user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY uzytkownik_user_roles
    ADD CONSTRAINT uzytkownik_user_roles_pkey PRIMARY KEY (role_id, uzytkownik_id);


--
-- TOC entry 1953 (class 2606 OID 46665)
-- Dependencies: 166 166 1987
-- Name: wn_historia_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_historia
    ADD CONSTRAINT wn_historia_pkey PRIMARY KEY (id);


--
-- TOC entry 1955 (class 2606 OID 46672)
-- Dependencies: 167 167 1987
-- Name: wn_rodzaje_opis_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_rodzaje
    ADD CONSTRAINT wn_rodzaje_opis_key UNIQUE (opis);


--
-- TOC entry 1957 (class 2606 OID 46670)
-- Dependencies: 167 167 1987
-- Name: wn_rodzaje_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_rodzaje
    ADD CONSTRAINT wn_rodzaje_pkey PRIMARY KEY (id);


--
-- TOC entry 1959 (class 2606 OID 46679)
-- Dependencies: 168 168 1987
-- Name: wn_statusy_opis_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_statusy
    ADD CONSTRAINT wn_statusy_opis_key UNIQUE (opis);


--
-- TOC entry 1961 (class 2606 OID 46677)
-- Dependencies: 168 168 1987
-- Name: wn_statusy_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_statusy
    ADD CONSTRAINT wn_statusy_pkey PRIMARY KEY (id);


--
-- TOC entry 1963 (class 2606 OID 46684)
-- Dependencies: 169 169 1987
-- Name: wn_urlop_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_urlop
    ADD CONSTRAINT wn_urlop_pkey PRIMARY KEY (id);


--
-- TOC entry 1972 (class 2606 OID 46710)
-- Dependencies: 1938 163 161 1987
-- Name: fk_struktura_dzial_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY struktura
    ADD CONSTRAINT fk_struktura_dzial_id FOREIGN KEY (dzial_id) REFERENCES dzial(id);


--
-- TOC entry 1973 (class 2606 OID 46715)
-- Dependencies: 1950 163 165 1987
-- Name: fk_struktura_sec_user_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY struktura
    ADD CONSTRAINT fk_struktura_sec_user_id FOREIGN KEY (sec_user_id) REFERENCES uzytkownik(id);


--
-- TOC entry 1971 (class 2606 OID 46705)
-- Dependencies: 1942 163 163 1987
-- Name: fk_struktura_szef_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY struktura
    ADD CONSTRAINT fk_struktura_szef_id FOREIGN KEY (szef_id) REFERENCES struktura(id);


--
-- TOC entry 1970 (class 2606 OID 46700)
-- Dependencies: 163 165 1950 1987
-- Name: fk_struktura_user_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY struktura
    ADD CONSTRAINT fk_struktura_user_id FOREIGN KEY (user_id) REFERENCES uzytkownik(id);


--
-- TOC entry 1974 (class 2606 OID 46720)
-- Dependencies: 162 1940 165 1987
-- Name: fk_uzytkownik_haslo_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY uzytkownik
    ADD CONSTRAINT fk_uzytkownik_haslo_id FOREIGN KEY (haslo_id) REFERENCES passwords(id);


--
-- TOC entry 1985 (class 2606 OID 46775)
-- Dependencies: 1944 164 172 1987
-- Name: fk_uzytkownik_user_roles_role_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY uzytkownik_user_roles
    ADD CONSTRAINT fk_uzytkownik_user_roles_role_id FOREIGN KEY (role_id) REFERENCES user_roles(id);


--
-- TOC entry 1984 (class 2606 OID 46770)
-- Dependencies: 172 1950 165 1987
-- Name: fk_uzytkownik_user_roles_uzytkownik_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY uzytkownik_user_roles
    ADD CONSTRAINT fk_uzytkownik_user_roles_uzytkownik_id FOREIGN KEY (uzytkownik_id) REFERENCES uzytkownik(id);


--
-- TOC entry 1976 (class 2606 OID 46730)
-- Dependencies: 165 166 1950 1987
-- Name: fk_wn_historia_akceptant; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_historia
    ADD CONSTRAINT fk_wn_historia_akceptant FOREIGN KEY (akceptant) REFERENCES uzytkownik(id);


--
-- TOC entry 1975 (class 2606 OID 46725)
-- Dependencies: 166 168 1960 1987
-- Name: fk_wn_historia_status_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_historia
    ADD CONSTRAINT fk_wn_historia_status_id FOREIGN KEY (status_id) REFERENCES wn_statusy(id);


--
-- TOC entry 1977 (class 2606 OID 46735)
-- Dependencies: 169 1962 166 1987
-- Name: fk_wn_historia_urlop_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_historia
    ADD CONSTRAINT fk_wn_historia_urlop_id FOREIGN KEY (urlop_id) REFERENCES wn_urlop(id);


--
-- TOC entry 1978 (class 2606 OID 46740)
-- Dependencies: 1950 165 166 1987
-- Name: fk_wn_historia_zmieniajacy; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_historia
    ADD CONSTRAINT fk_wn_historia_zmieniajacy FOREIGN KEY (zmieniajacy) REFERENCES uzytkownik(id);


--
-- TOC entry 1981 (class 2606 OID 46755)
-- Dependencies: 169 165 1950 1987
-- Name: fk_wn_urlop_akceptant_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_urlop
    ADD CONSTRAINT fk_wn_urlop_akceptant_id FOREIGN KEY (akceptant_id) REFERENCES uzytkownik(id);


--
-- TOC entry 1980 (class 2606 OID 46750)
-- Dependencies: 165 169 1950 1987
-- Name: fk_wn_urlop_przyjmujacy; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_urlop
    ADD CONSTRAINT fk_wn_urlop_przyjmujacy FOREIGN KEY (przyjmujacy) REFERENCES uzytkownik(id);


--
-- TOC entry 1982 (class 2606 OID 46760)
-- Dependencies: 169 167 1956 1987
-- Name: fk_wn_urlop_rodzaj_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_urlop
    ADD CONSTRAINT fk_wn_urlop_rodzaj_id FOREIGN KEY (rodzaj_id) REFERENCES wn_rodzaje(id);


--
-- TOC entry 1979 (class 2606 OID 46745)
-- Dependencies: 169 168 1960 1987
-- Name: fk_wn_urlop_status_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_urlop
    ADD CONSTRAINT fk_wn_urlop_status_id FOREIGN KEY (status_id) REFERENCES wn_statusy(id);


--
-- TOC entry 1983 (class 2606 OID 46765)
-- Dependencies: 1950 169 165 1987
-- Name: fk_wn_urlop_uid; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY wn_urlop
    ADD CONSTRAINT fk_wn_urlop_uid FOREIGN KEY (uid) REFERENCES uzytkownik(id);

