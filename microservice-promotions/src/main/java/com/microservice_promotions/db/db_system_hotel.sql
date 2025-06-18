--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-06-18 18:51:17

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 860 (class 1247 OID 25379)
-- Name: applicability; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.applicability AS ENUM (
    'all',
    'selected'
);


ALTER TYPE public.applicability OWNER TO postgres;

--
-- TOC entry 869 (class 1247 OID 25455)
-- Name: availability_status_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.availability_status_enum AS ENUM (
    'available',
    'booked',
    'maintenance'
);


ALTER TYPE public.availability_status_enum OWNER TO postgres;

--
-- TOC entry 863 (class 1247 OID 25384)
-- Name: promotion_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.promotion_type AS ENUM (
    'percentage',
    'fixed',
    'added_value'
);


ALTER TYPE public.promotion_type OWNER TO postgres;

--
-- TOC entry 4602 (class 2605 OID 25391)
-- Name: CAST (character varying AS public.applicability); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.applicability) WITH INOUT AS IMPLICIT;


--
-- TOC entry 4603 (class 2605 OID 25392)
-- Name: CAST (character varying AS public.promotion_type); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.promotion_type) WITH INOUT AS IMPLICIT;


--
-- TOC entry 229 (class 1255 OID 25393)
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
-- TOC entry 230 (class 1255 OID 25394)
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
-- TOC entry 231 (class 1255 OID 25395)
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
-- TOC entry 228 (class 1259 OID 25532)
-- Name: promotion_room; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.promotion_room (
    fk_promotion integer NOT NULL,
    fk_room integer NOT NULL
);


ALTER TABLE public.promotion_room OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 25399)
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
-- TOC entry 218 (class 1259 OID 25411)
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
-- TOC entry 4891 (class 0 OID 0)
-- Dependencies: 218
-- Name: promotions_promotion_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.promotions_promotion_id_seq OWNED BY public.promotions.promotion_id;


--
-- TOC entry 219 (class 1259 OID 25461)
-- Name: room_images; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.room_images (
    id bigint NOT NULL,
    image_url character varying(1000) NOT NULL,
    room_id bigint NOT NULL
);


ALTER TABLE public.room_images OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 25466)
-- Name: room_images_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.room_images_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.room_images_id_seq OWNER TO postgres;

--
-- TOC entry 4892 (class 0 OID 0)
-- Dependencies: 220
-- Name: room_images_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.room_images_id_seq OWNED BY public.room_images.id;


--
-- TOC entry 221 (class 1259 OID 25467)
-- Name: room_tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.room_tags (
    room_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE public.room_tags OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 25470)
-- Name: room_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.room_types (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    max_occupancy integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.room_types OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 25477)
-- Name: room_types_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.room_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.room_types_id_seq OWNER TO postgres;

--
-- TOC entry 4893 (class 0 OID 0)
-- Dependencies: 223
-- Name: room_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.room_types_id_seq OWNED BY public.room_types.id;


--
-- TOC entry 224 (class 1259 OID 25478)
-- Name: rooms; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rooms (
    room_id bigint NOT NULL,
    room_number integer NOT NULL,
    room_type_id bigint NOT NULL,
    price_per_night numeric(10,2) NOT NULL,
    capacity integer,
    room_size double precision,
    description text,
    availability_status character varying(255) DEFAULT 'AVAILABLE'::character varying,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.rooms OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 25486)
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
-- TOC entry 4894 (class 0 OID 0)
-- Dependencies: 225
-- Name: rooms_room_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rooms_room_id_seq OWNED BY public.rooms.room_id;


--
-- TOC entry 226 (class 1259 OID 25487)
-- Name: tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tags (
    id bigint NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.tags OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 25490)
-- Name: tags_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tags_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tags_id_seq OWNER TO postgres;

--
-- TOC entry 4895 (class 0 OID 0)
-- Dependencies: 227
-- Name: tags_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tags_id_seq OWNED BY public.tags.id;


--
-- TOC entry 4683 (class 2604 OID 25416)
-- Name: promotions promotion_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promotions ALTER COLUMN promotion_id SET DEFAULT nextval('public.promotions_promotion_id_seq'::regclass);


--
-- TOC entry 4690 (class 2604 OID 25491)
-- Name: room_images id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_images ALTER COLUMN id SET DEFAULT nextval('public.room_images_id_seq'::regclass);


--
-- TOC entry 4691 (class 2604 OID 25492)
-- Name: room_types id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_types ALTER COLUMN id SET DEFAULT nextval('public.room_types_id_seq'::regclass);


--
-- TOC entry 4694 (class 2604 OID 25493)
-- Name: rooms room_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms ALTER COLUMN room_id SET DEFAULT nextval('public.rooms_room_id_seq'::regclass);


--
-- TOC entry 4698 (class 2604 OID 25494)
-- Name: tags id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tags ALTER COLUMN id SET DEFAULT nextval('public.tags_id_seq'::regclass);


--
-- TOC entry 4885 (class 0 OID 25532)
-- Dependencies: 228
-- Data for Name: promotion_room; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.promotion_room (fk_promotion, fk_room) FROM stdin;
\.


--
-- TOC entry 4874 (class 0 OID 25399)
-- Dependencies: 217
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
-- TOC entry 4876 (class 0 OID 25461)
-- Dependencies: 219
-- Data for Name: room_images; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.room_images (id, image_url, room_id) FROM stdin;
9	/images/rooms/101_1.jpg	7
10	/images/rooms/101_2.jpg	7
11	/images/rooms/102_1.jpg	8
12	/images/rooms/103_1.jpg	9
\.


--
-- TOC entry 4878 (class 0 OID 25467)
-- Dependencies: 221
-- Data for Name: room_tags; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.room_tags (room_id, tag_id) FROM stdin;
7	6
7	5
8	7
8	5
9	5
\.


--
-- TOC entry 4879 (class 0 OID 25470)
-- Dependencies: 222
-- Data for Name: room_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.room_types (id, name, description, max_occupancy, created_at, updated_at) FROM stdin;
7	Suite	Habitación tipo suite con sala y baño privado	4	2025-06-09 15:27:43.377085	2025-06-09 15:27:43.377085
8	Doble	Habitación para dos personas	2	2025-06-09 15:27:43.377085	2025-06-09 15:27:43.377085
9	Sencilla	Habitación individual básica	1	2025-06-09 15:27:43.377085	2025-06-09 15:27:43.377085
\.


--
-- TOC entry 4881 (class 0 OID 25478)
-- Dependencies: 224
-- Data for Name: rooms; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rooms (room_id, room_number, room_type_id, price_per_night, capacity, room_size, description, availability_status, created_at, updated_at) FROM stdin;
7	101	7	150.00	4	45.5	Suite amplia con jacuzzi y balcón	AVAILABLE	2025-06-09 15:27:43.377085	2025-06-09 15:27:43.377085
8	102	8	80.00	2	25	Habitación doble con aire acondicionado	AVAILABLE	2025-06-09 15:27:43.377085	2025-06-09 15:27:43.377085
9	103	9	50.00	1	15	Habitación sencilla, ideal para viajeros solos	AVAILABLE	2025-06-09 15:27:43.377085	2025-06-09 15:27:43.377085
\.


--
-- TOC entry 4883 (class 0 OID 25487)
-- Dependencies: 226
-- Data for Name: tags; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tags (id, name) FROM stdin;
5	WiFi
6	Jacuzzi
7	Aire acondicionado
8	Piscina
\.


--
-- TOC entry 4896 (class 0 OID 0)
-- Dependencies: 218
-- Name: promotions_promotion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.promotions_promotion_id_seq', 14, true);


--
-- TOC entry 4897 (class 0 OID 0)
-- Dependencies: 220
-- Name: room_images_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.room_images_id_seq', 12, true);


--
-- TOC entry 4898 (class 0 OID 0)
-- Dependencies: 223
-- Name: room_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.room_types_id_seq', 9, true);


--
-- TOC entry 4899 (class 0 OID 0)
-- Dependencies: 225
-- Name: rooms_room_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.rooms_room_id_seq', 9, true);


--
-- TOC entry 4900 (class 0 OID 0)
-- Dependencies: 227
-- Name: tags_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tags_id_seq', 8, true);


--
-- TOC entry 4719 (class 2606 OID 25536)
-- Name: promotion_room promotion_room_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promotion_room
    ADD CONSTRAINT promotion_room_pkey PRIMARY KEY (fk_promotion, fk_room);


--
-- TOC entry 4701 (class 2606 OID 25421)
-- Name: promotions promotions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promotions
    ADD CONSTRAINT promotions_pkey PRIMARY KEY (promotion_id);


--
-- TOC entry 4703 (class 2606 OID 25496)
-- Name: room_images room_images_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_images
    ADD CONSTRAINT room_images_pkey PRIMARY KEY (id);


--
-- TOC entry 4705 (class 2606 OID 25498)
-- Name: room_tags room_tags_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_tags
    ADD CONSTRAINT room_tags_pkey PRIMARY KEY (room_id, tag_id);


--
-- TOC entry 4707 (class 2606 OID 25500)
-- Name: room_types room_types_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_types
    ADD CONSTRAINT room_types_name_key UNIQUE (name);


--
-- TOC entry 4709 (class 2606 OID 25502)
-- Name: room_types room_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_types
    ADD CONSTRAINT room_types_pkey PRIMARY KEY (id);


--
-- TOC entry 4711 (class 2606 OID 25504)
-- Name: rooms rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_pkey PRIMARY KEY (room_id);


--
-- TOC entry 4713 (class 2606 OID 25506)
-- Name: rooms rooms_room_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_room_number_key UNIQUE (room_number);


--
-- TOC entry 4715 (class 2606 OID 25508)
-- Name: tags tags_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_name_key UNIQUE (name);


--
-- TOC entry 4717 (class 2606 OID 25510)
-- Name: tags tags_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);


--
-- TOC entry 4726 (class 2620 OID 25424)
-- Name: promotions insert_rooms_on_promotion_update; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER insert_rooms_on_promotion_update AFTER UPDATE ON public.promotions FOR EACH ROW WHEN (((new.room_applicability = 'all'::public.applicability) AND (old.room_applicability IS DISTINCT FROM new.room_applicability))) EXECUTE FUNCTION public.insert_all_rooms_to_promotion_room();


--
-- TOC entry 4727 (class 2620 OID 25425)
-- Name: promotions trigger_delete_test_promotions; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_delete_test_promotions AFTER DELETE ON public.promotions FOR EACH ROW EXECUTE FUNCTION public.delete_test_entries();


--
-- TOC entry 4728 (class 2620 OID 25426)
-- Name: promotions trigger_promotions_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_promotions_updated_at BEFORE UPDATE ON public.promotions FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


--
-- TOC entry 4724 (class 2606 OID 25537)
-- Name: promotion_room promotion_room_fk_promotion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promotion_room
    ADD CONSTRAINT promotion_room_fk_promotion_fkey FOREIGN KEY (fk_promotion) REFERENCES public.promotions(promotion_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4725 (class 2606 OID 25542)
-- Name: promotion_room promotion_room_fk_room_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promotion_room
    ADD CONSTRAINT promotion_room_fk_room_fkey FOREIGN KEY (fk_room) REFERENCES public.rooms(room_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4720 (class 2606 OID 25511)
-- Name: room_images room_images_room_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_images
    ADD CONSTRAINT room_images_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.rooms(room_id) ON DELETE CASCADE;


--
-- TOC entry 4721 (class 2606 OID 25516)
-- Name: room_tags room_tags_room_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_tags
    ADD CONSTRAINT room_tags_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.rooms(room_id) ON DELETE CASCADE;


--
-- TOC entry 4722 (class 2606 OID 25521)
-- Name: room_tags room_tags_tag_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_tags
    ADD CONSTRAINT room_tags_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES public.tags(id) ON DELETE CASCADE;


--
-- TOC entry 4723 (class 2606 OID 25526)
-- Name: rooms rooms_room_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_room_type_id_fkey FOREIGN KEY (room_type_id) REFERENCES public.room_types(id) ON DELETE RESTRICT;


-- Completed on 2025-06-18 18:51:17

--
-- PostgreSQL database dump complete
--

