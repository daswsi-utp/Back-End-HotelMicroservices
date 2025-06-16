--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-06-15 22:29:10

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
-- TOC entry 849 (class 1247 OID 25051)
-- Name: promotion_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.promotion_type AS ENUM (
    'percentage',
    'fixed',
    'added_value'
);


ALTER TYPE public.promotion_type OWNER TO postgres;

--
-- TOC entry 4567 (class 2605 OID 25057)
-- Name: CAST (character varying AS public.promotion_type); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.promotion_type) WITH INOUT AS IMPLICIT;


--
-- TOC entry 219 (class 1255 OID 25058)
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
-- TOC entry 220 (class 1255 OID 25059)
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
-- TOC entry 217 (class 1259 OID 25060)
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
    CONSTRAINT check_discount_value CHECK ((((type = 'percentage'::public.promotion_type) AND (discount_value >= ((0)::numeric)::double precision) AND (discount_value <= ((100)::numeric)::double precision)) OR ((type = 'fixed'::public.promotion_type) AND (discount_value >= ((0)::numeric)::double precision)) OR ((type = 'added_value'::public.promotion_type) AND (discount_value IS NULL))))
);


ALTER TABLE public.promotions OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 25070)
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
-- TOC entry 4810 (class 0 OID 0)
-- Dependencies: 218
-- Name: promotions_promotion_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.promotions_promotion_id_seq OWNED BY public.promotions.promotion_id;


--
-- TOC entry 4647 (class 2604 OID 25071)
-- Name: promotions promotion_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promotions ALTER COLUMN promotion_id SET DEFAULT nextval('public.promotions_promotion_id_seq'::regclass);


--
-- TOC entry 4803 (class 0 OID 25060)
-- Dependencies: 217
-- Data for Name: promotions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.promotions (promotion_id, promotion_name, promotion_description, discount_value, start_date, end_date, created_at, updated_at, type, is_active, minimun_stay) FROM stdin;
2	Summer Sale	20% off all summer items	30	2025-06-14	2025-07-15	\N	\N	percentage	f	1
3	Summer Sale	20% off all summer items	30	2025-06-14	2025-07-15	\N	\N	percentage	f	1
4	Summer Sale	20% off all summer items	40	2025-06-14	2025-07-15	\N	\N	percentage	f	1
1	Summer Sale	20% off all summer items	20	2025-06-14	2025-07-15	\N	2025-06-11 18:30:16.060139	percentage	f	1
5	Spooky Sale	20% off all hallowing items	20	2025-06-14	2025-07-15	2025-06-11 18:34:47.58427	2025-06-11 18:34:47.58427	percentage	f	1
7	Spooky Sale Added Value	20% off all hallowing items + Free Wine	\N	2025-06-14	2025-07-15	2025-06-11 18:42:04.111194	2025-06-11 18:42:04.111194	added_value	f	1
14	Regular	Test description	20	2025-06-15	2025-07-15	2025-06-13 19:16:41.736387	2025-06-13 19:16:41.736387	percentage	t	1
\.


--
-- TOC entry 4811 (class 0 OID 0)
-- Dependencies: 218
-- Name: promotions_promotion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.promotions_promotion_id_seq', 14, true);


--
-- TOC entry 4655 (class 2606 OID 25073)
-- Name: promotions promotions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promotions
    ADD CONSTRAINT promotions_pkey PRIMARY KEY (promotion_id);


--
-- TOC entry 4656 (class 2620 OID 25074)
-- Name: promotions trigger_delete_test_promotions; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_delete_test_promotions AFTER DELETE ON public.promotions FOR EACH ROW EXECUTE FUNCTION public.delete_test_entries();


--
-- TOC entry 4657 (class 2620 OID 25075)
-- Name: promotions trigger_promotions_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_promotions_updated_at BEFORE UPDATE ON public.promotions FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();


-- Completed on 2025-06-15 22:29:11

--
-- PostgreSQL database dump complete
--

