--
-- PostgreSQL database dump
--

-- Dumped from database version 16.8
-- Dumped by pg_dump version 16.8

-- Started on 2025-06-17 19:41:48 -05

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 847 (class 1247 OID 25111)
-- Name: applicability; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.applicability AS ENUM (
    'all',
    'selected'
);


ALTER TYPE public.applicability OWNER TO postgres;

--
-- TOC entry 850 (class 1247 OID 25116)
-- Name: promotion_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.promotion_type AS ENUM (
    'percentage',
    'fixed',
    'added_value'
);


ALTER TYPE public.promotion_type OWNER TO postgres;

--
-- TOC entry 4213 (class 2605 OID 25123)
-- Name: CAST (character varying AS public.applicability); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.applicability) WITH INOUT AS IMPLICIT;


--
-- TOC entry 4214 (class 2605 OID 25124)
-- Name: CAST (character varying AS public.promotion_type); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.promotion_type) WITH INOUT AS IMPLICIT;


--
-- TOC entry 220 (class 1255 OID 25125)
-- Name: delete_test_entries(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.delete_test_entries() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
	max_id BIGINT;
BEGIN
	IF OLD.promotion_name ILIKE 'test%' THEN
		DELETE FROM promotions WHERE promotion_name ILIKE 'test%';
		SELECT COALESCE(MAX(promotion_id), 0) into max_id from promotions;
		perform setval('promotions_promotion_id_seq', max_id, true);
	END IF;
	RETURN OLD;
END;
$$;


ALTER FUNCTION public.delete_test_entries() OWNER TO postgres;

--
-- TOC entry 222 (class 1255 OID 25167)
-- Name: insert_all_rooms_to_promotion_room(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.insert_all_rooms_to_promotion_room() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.room_applicability = 'all'::applicability THEN
        INSERT INTO promotion_room (fk_promotion, fk_room)
        SELECT NEW.promotion_id, r.room_id
        FROM rooms r;
    END IF;

    RETURN NEW;
END;
$$;


ALTER FUNCTION public.insert_all_rooms_to_promotion_room() OWNER TO postgres;

--
-- TOC entry 221 (class 1255 OID 25126)
-- Name: update_updated_at_column(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.update_updated_at_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
   NEW.updated_at = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$;


ALTER FUNCTION public.update_updated_at_column() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 25127)
-- Name: promotion_room; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.promotion_room (
    fk_promotion integer NOT NULL,
    fk_room integer NOT NULL
);


ALTER TABLE public.promotion_room OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 25130)
-- Name: promotions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.promotions (
    promotion_id bigint NOT NULL,
    promotion_name character varying(255),
    promotion_description character varying(255),
    discount_value double precision,
    start_date date,
    end_date date,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    type public.promotion_type DEFAULT 'percentage'::public.promotion_type NOT NULL,
    is_active boolean DEFAULT true NOT NULL,
    minimun_stay bigint DEFAULT 1,
    room_applicability public.applicability DEFAULT 'all'::public.applicability,
    CONSTRAINT check_discount_value CHECK ((((type = 'percentage'::public.promotion_type) AND (discount_value >= ((0)::numeric)::double precision) AND (discount_value <= ((100)::numeric)::double precision)) OR ((type = 'fixed'::public.promotion_type) AND (discount_value >= ((0)::numeric)::double precision)) OR ((type = 'added_value'::public.promotion_type) AND (discount_value IS NULL))))
);


ALTER TABLE public.promotions OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 25142)
-- Name: promotions_promotion_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.promotions_promotion_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.promotions_promotion_id_seq OWNER TO postgres;

--
-- TOC entry 4467 (class 0 OID 0)
-- Dependencies: 217
-- Name: promotions_promotion_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.promotions_promotion_id_seq OWNED BY public.promotions.promotion_id;


--
-- TOC entry 218 (class 1259 OID 25143)
-- Name: rooms; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rooms (
    room_id integer NOT NULL,
    name character varying(255)
);


ALTER TABLE public.rooms OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 25146)
-- Name: rooms_room_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.rooms_room_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rooms_room_id_seq OWNER TO postgres;

--
-- TOC entry 4468 (class 0 OID 0)
-- Dependencies: 219
-- Name: rooms_room_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rooms_room_id_seq OWNED BY public.rooms.room_id;


--
-- TOC entry 4294 (class 2604 OID 25147)
-- Name: promotions promotion_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promotions ALTER COLUMN promotion_id SET DEFAULT nextval('public.promotions_promotion_id_seq'::regclass);


--
-- TOC entry 4301 (class 2604 OID 25148)
-- Name: rooms room_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms ALTER COLUMN room_id SET DEFAULT nextval('public.rooms_room_id_seq'::regclass);


--
-- TOC entry 4457 (class 0 OID 25127)
-- Dependencies: 215
-- Data for Name: promotion_room; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.promotion_room (fk_promotion, fk_room) FROM stdin;
\.


--
-- TOC entry 4458 (class 0 OID 25130)
-- Dependencies: 216
-- Data for Name: promotions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.promotions (promotion_id, promotion_name, promotion_description, discount_value, start_date, end_date, created_at, updated_at, type, is_active, minimun_stay, room_applicability) FROM stdin;
2	Summer Sale	20% off all summer items	30	2025-06-14	2025-07-15	\N	\N	percentage	f	1	all
3	Summer Sale	20% off all summer items	30	2025-06-14	2025-07-15	\N	\N	percentage	f	1	all
4	Summer Sale	20% off all summer items	40	2025-06-14	2025-07-15	\N	\N	percentage	f	1	all
1	Summer Sale	20% off all summer items	20	2025-06-14	2025-07-15	\N	2025-06-11 18:30:16.060139	percentage	f	1	all
5	Spooky Sale	20% off all hallowing items	20	2025-06-14	2025-07-15	2025-06-11 18:34:47.58427	2025-06-11 18:34:47.58427	percentage	f	1	all
7	Spooky Sale Added Value	20% off all hallowing items + Free Wine	\N	2025-06-14	2025-07-15	2025-06-11 18:42:04.111194	2025-06-11 18:42:04.111194	added_value	f	1	all
14	Regular	Test description	20	2025-06-15	2025-07-15	2025-06-13 19:16:41.736387	2025-06-13 19:16:41.736387	percentage	t	1	all
\.


--
-- TOC entry 4460 (class 0 OID 25143)
-- Dependencies: 218
-- Data for Name: rooms; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rooms (room_id, name) FROM stdin;
\.


--
-- TOC entry 4469 (class 0 OID 0)
-- Dependencies: 217
-- Name: promotions_promotion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.promotions_promotion_id_seq', 14, true);


--
-- TOC entry 4470 (class 0 OID 0)
-- Dependencies: 219
-- Name: rooms_room_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.rooms_room_id_seq', 1, false);


--
-- TOC entry 4304 (class 2606 OID 25150)
-- Name: promotion_room promotion_room_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promotion_room
    ADD CONSTRAINT promotion_room_pkey PRIMARY KEY (fk_promotion, fk_room);


--
-- TOC entry 4306 (class 2606 OID 25152)
-- Name: promotions promotions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promotions
    ADD CONSTRAINT promotions_pkey PRIMARY KEY (promotion_id);


--
-- TOC entry 4308 (class 2606 OID 25154)
-- Name: rooms rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_pkey PRIMARY KEY (room_id);


--
-- TOC entry 4311 (class 2620 OID 25168)
-- Name: promotions insert_rooms_on_promotion_update; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER insert_rooms_on_promotion_update AFTER UPDATE ON public.promotions FOR EACH ROW WHEN (((new.room_applicability = 'all'::public.applicability) AND (old.room_applicability IS DISTINCT FROM new.room_applicability))) EXECUTE FUNCTION public.insert_all_rooms_to_promotion_room();


--
-- TOC entry 4312 (class 2620 OID 25155)
-- Name: promotions trigger_delete_test_promotions; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_delete_test_promotions AFTER DELETE ON public.promotions FOR EACH ROW EXECUTE FUNCTION public.delete_test_entries();


--
-- TOC entry 4313 (class 2620 OID 25156)
-- Name: promotions trigger_promotions_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_promotions_updated_at BEFORE UPDATE ON public.promotions FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- TOC entry 4309 (class 2606 OID 25157)
-- Name: promotion_room promotion_room_fk_promotion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promotion_room
    ADD CONSTRAINT promotion_room_fk_promotion_fkey FOREIGN KEY (fk_promotion) REFERENCES public.promotions(promotion_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4310 (class 2606 OID 25162)
-- Name: promotion_room promotion_room_fk_room_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promotion_room
    ADD CONSTRAINT promotion_room_fk_room_fkey FOREIGN KEY (fk_room) REFERENCES public.rooms(room_id) ON UPDATE CASCADE ON DELETE CASCADE;


-- Completed on 2025-06-17 19:41:49 -05

--
-- PostgreSQL database dump complete
--

