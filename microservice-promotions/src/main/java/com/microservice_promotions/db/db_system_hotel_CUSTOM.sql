PGDMP  8                    }            db_system_hotel    17.4    17.4 B               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false                       1262    25297    db_system_hotel    DATABASE     �   CREATE DATABASE db_system_hotel WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Spanish_Mexico.1252';
    DROP DATABASE db_system_hotel;
                     postgres    false            \           1247    25379    applicability    TYPE     H   CREATE TYPE public.applicability AS ENUM (
    'all',
    'selected'
);
     DROP TYPE public.applicability;
       public               postgres    false            e           1247    25455    availability_status_enum    TYPE     j   CREATE TYPE public.availability_status_enum AS ENUM (
    'available',
    'booked',
    'maintenance'
);
 +   DROP TYPE public.availability_status_enum;
       public               postgres    false            _           1247    25384    promotion_type    TYPE     `   CREATE TYPE public.promotion_type AS ENUM (
    'percentage',
    'fixed',
    'added_value'
);
 !   DROP TYPE public.promotion_type;
       public               postgres    false            �           2605    25391 0   CAST (character varying AS public.applicability)    CAST     P   CREATE CAST (character varying AS public.applicability) WITH INOUT AS IMPLICIT;
 7   DROP CAST (character varying AS public.applicability);
                        false    860            �           2605    25392 1   CAST (character varying AS public.promotion_type)    CAST     Q   CREATE CAST (character varying AS public.promotion_type) WITH INOUT AS IMPLICIT;
 8   DROP CAST (character varying AS public.promotion_type);
                        false    863            �            1255    25393    delete_test_entries()    FUNCTION     �  CREATE FUNCTION public.delete_test_entries() RETURNS trigger
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
 ,   DROP FUNCTION public.delete_test_entries();
       public               postgres    false            �            1255    25394 $   insert_all_rooms_to_promotion_room()    FUNCTION     Y  CREATE FUNCTION public.insert_all_rooms_to_promotion_room() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.room_applicability = 'all'::applicability THEN
        INSERT INTO promotion_room_type (fk_promotion, fk_room_type)
        SELECT NEW.promotion_id, rt.id
        FROM room_types rt;
    END IF;

    RETURN NEW;
END;
$$;
 ;   DROP FUNCTION public.insert_all_rooms_to_promotion_room();
       public               postgres    false            �            1255    25395    update_updated_at_column()    FUNCTION     �   CREATE FUNCTION public.update_updated_at_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
   NEW.updated_at = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$;
 1   DROP FUNCTION public.update_updated_at_column();
       public               postgres    false            �            1259    25759    promotion_room_type    TABLE     r   CREATE TABLE public.promotion_room_type (
    fk_promotion integer NOT NULL,
    fk_room_type integer NOT NULL
);
 '   DROP TABLE public.promotion_room_type;
       public         heap r       postgres    false            �            1259    25399 
   promotions    TABLE     �  CREATE TABLE public.promotions (
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
    DROP TABLE public.promotions;
       public         heap r       postgres    false    863    860    863    863    860            �            1259    25411    promotions_promotion_id_seq    SEQUENCE     �   CREATE SEQUENCE public.promotions_promotion_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.promotions_promotion_id_seq;
       public               postgres    false    217                       0    0    promotions_promotion_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.promotions_promotion_id_seq OWNED BY public.promotions.promotion_id;
          public               postgres    false    218            �            1259    25461    room_images    TABLE     �   CREATE TABLE public.room_images (
    id bigint NOT NULL,
    image_url character varying(1000) NOT NULL,
    room_id bigint NOT NULL
);
    DROP TABLE public.room_images;
       public         heap r       postgres    false            �            1259    25466    room_images_id_seq    SEQUENCE     �   CREATE SEQUENCE public.room_images_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.room_images_id_seq;
       public               postgres    false    219                       0    0    room_images_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.room_images_id_seq OWNED BY public.room_images.id;
          public               postgres    false    220            �            1259    25467 	   room_tags    TABLE     [   CREATE TABLE public.room_tags (
    room_id bigint NOT NULL,
    tag_id bigint NOT NULL
);
    DROP TABLE public.room_tags;
       public         heap r       postgres    false            �            1259    25470 
   room_types    TABLE     "  CREATE TABLE public.room_types (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    max_occupancy integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.room_types;
       public         heap r       postgres    false            �            1259    25477    room_types_id_seq    SEQUENCE     �   CREATE SEQUENCE public.room_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.room_types_id_seq;
       public               postgres    false    222                       0    0    room_types_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.room_types_id_seq OWNED BY public.room_types.id;
          public               postgres    false    223            �            1259    25478    rooms    TABLE     �  CREATE TABLE public.rooms (
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
    DROP TABLE public.rooms;
       public         heap r       postgres    false            �            1259    25486    rooms_room_id_seq    SEQUENCE     �   CREATE SEQUENCE public.rooms_room_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.rooms_room_id_seq;
       public               postgres    false    224                        0    0    rooms_room_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.rooms_room_id_seq OWNED BY public.rooms.room_id;
          public               postgres    false    225            �            1259    25487    tags    TABLE     _   CREATE TABLE public.tags (
    id bigint NOT NULL,
    name character varying(100) NOT NULL
);
    DROP TABLE public.tags;
       public         heap r       postgres    false            �            1259    25490    tags_id_seq    SEQUENCE     �   CREATE SEQUENCE public.tags_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.tags_id_seq;
       public               postgres    false    226            !           0    0    tags_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.tags_id_seq OWNED BY public.tags.id;
          public               postgres    false    227            K           2604    25416    promotions promotion_id    DEFAULT     �   ALTER TABLE ONLY public.promotions ALTER COLUMN promotion_id SET DEFAULT nextval('public.promotions_promotion_id_seq'::regclass);
 F   ALTER TABLE public.promotions ALTER COLUMN promotion_id DROP DEFAULT;
       public               postgres    false    218    217            R           2604    25491    room_images id    DEFAULT     p   ALTER TABLE ONLY public.room_images ALTER COLUMN id SET DEFAULT nextval('public.room_images_id_seq'::regclass);
 =   ALTER TABLE public.room_images ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    220    219            S           2604    25492    room_types id    DEFAULT     n   ALTER TABLE ONLY public.room_types ALTER COLUMN id SET DEFAULT nextval('public.room_types_id_seq'::regclass);
 <   ALTER TABLE public.room_types ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    223    222            V           2604    25493    rooms room_id    DEFAULT     n   ALTER TABLE ONLY public.rooms ALTER COLUMN room_id SET DEFAULT nextval('public.rooms_room_id_seq'::regclass);
 <   ALTER TABLE public.rooms ALTER COLUMN room_id DROP DEFAULT;
       public               postgres    false    225    224            Z           2604    25494    tags id    DEFAULT     b   ALTER TABLE ONLY public.tags ALTER COLUMN id SET DEFAULT nextval('public.tags_id_seq'::regclass);
 6   ALTER TABLE public.tags ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    227    226                      0    25759    promotion_room_type 
   TABLE DATA           I   COPY public.promotion_room_type (fk_promotion, fk_room_type) FROM stdin;
    public               postgres    false    228   �V                 0    25399 
   promotions 
   TABLE DATA           �   COPY public.promotions (promotion_id, promotion_name, promotion_description, discount_value, start_date, end_date, created_at, updated_at, type, is_active, minimun_stay, room_applicability) FROM stdin;
    public               postgres    false    217   ,W                 0    25461    room_images 
   TABLE DATA           =   COPY public.room_images (id, image_url, room_id) FROM stdin;
    public               postgres    false    219   X                 0    25467 	   room_tags 
   TABLE DATA           4   COPY public.room_tags (room_id, tag_id) FROM stdin;
    public               postgres    false    221   mX                 0    25470 
   room_types 
   TABLE DATA           b   COPY public.room_types (id, name, description, max_occupancy, created_at, updated_at) FROM stdin;
    public               postgres    false    222   �X                 0    25478    rooms 
   TABLE DATA           �   COPY public.rooms (room_id, room_number, room_type_id, price_per_night, capacity, room_size, description, availability_status, created_at, updated_at) FROM stdin;
    public               postgres    false    224   EY                 0    25487    tags 
   TABLE DATA           (   COPY public.tags (id, name) FROM stdin;
    public               postgres    false    226   Z       "           0    0    promotions_promotion_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.promotions_promotion_id_seq', 25, true);
          public               postgres    false    218            #           0    0    room_images_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.room_images_id_seq', 12, true);
          public               postgres    false    220            $           0    0    room_types_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.room_types_id_seq', 9, true);
          public               postgres    false    223            %           0    0    rooms_room_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.rooms_room_id_seq', 9, true);
          public               postgres    false    225            &           0    0    tags_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.tags_id_seq', 8, true);
          public               postgres    false    227            o           2606    25763 ,   promotion_room_type promotion_room_type_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.promotion_room_type
    ADD CONSTRAINT promotion_room_type_pkey PRIMARY KEY (fk_promotion, fk_room_type);
 V   ALTER TABLE ONLY public.promotion_room_type DROP CONSTRAINT promotion_room_type_pkey;
       public                 postgres    false    228    228            ]           2606    25421    promotions promotions_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.promotions
    ADD CONSTRAINT promotions_pkey PRIMARY KEY (promotion_id);
 D   ALTER TABLE ONLY public.promotions DROP CONSTRAINT promotions_pkey;
       public                 postgres    false    217            _           2606    25496    room_images room_images_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.room_images
    ADD CONSTRAINT room_images_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.room_images DROP CONSTRAINT room_images_pkey;
       public                 postgres    false    219            a           2606    25498    room_tags room_tags_pkey 
   CONSTRAINT     c   ALTER TABLE ONLY public.room_tags
    ADD CONSTRAINT room_tags_pkey PRIMARY KEY (room_id, tag_id);
 B   ALTER TABLE ONLY public.room_tags DROP CONSTRAINT room_tags_pkey;
       public                 postgres    false    221    221            c           2606    25500    room_types room_types_name_key 
   CONSTRAINT     Y   ALTER TABLE ONLY public.room_types
    ADD CONSTRAINT room_types_name_key UNIQUE (name);
 H   ALTER TABLE ONLY public.room_types DROP CONSTRAINT room_types_name_key;
       public                 postgres    false    222            e           2606    25502    room_types room_types_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.room_types
    ADD CONSTRAINT room_types_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.room_types DROP CONSTRAINT room_types_pkey;
       public                 postgres    false    222            g           2606    25504    rooms rooms_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_pkey PRIMARY KEY (room_id);
 :   ALTER TABLE ONLY public.rooms DROP CONSTRAINT rooms_pkey;
       public                 postgres    false    224            i           2606    25506    rooms rooms_room_number_key 
   CONSTRAINT     ]   ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_room_number_key UNIQUE (room_number);
 E   ALTER TABLE ONLY public.rooms DROP CONSTRAINT rooms_room_number_key;
       public                 postgres    false    224            k           2606    25508    tags tags_name_key 
   CONSTRAINT     M   ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_name_key UNIQUE (name);
 <   ALTER TABLE ONLY public.tags DROP CONSTRAINT tags_name_key;
       public                 postgres    false    226            m           2606    25510    tags tags_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.tags DROP CONSTRAINT tags_pkey;
       public                 postgres    false    226            v           2620    25424 +   promotions insert_rooms_on_promotion_update    TRIGGER     "  CREATE TRIGGER insert_rooms_on_promotion_update AFTER UPDATE ON public.promotions FOR EACH ROW WHEN (((new.room_applicability = 'all'::public.applicability) AND (old.room_applicability IS DISTINCT FROM new.room_applicability))) EXECUTE FUNCTION public.insert_all_rooms_to_promotion_room();
 D   DROP TRIGGER insert_rooms_on_promotion_update ON public.promotions;
       public               postgres    false    231    860    217    217            w           2620    25700    promotions trg_insert_all_rooms    TRIGGER     �   CREATE TRIGGER trg_insert_all_rooms AFTER INSERT ON public.promotions FOR EACH ROW EXECUTE FUNCTION public.insert_all_rooms_to_promotion_room();
 8   DROP TRIGGER trg_insert_all_rooms ON public.promotions;
       public               postgres    false    217    231            x           2620    25425 )   promotions trigger_delete_test_promotions    TRIGGER     �   CREATE TRIGGER trigger_delete_test_promotions AFTER DELETE ON public.promotions FOR EACH ROW EXECUTE FUNCTION public.delete_test_entries();
 B   DROP TRIGGER trigger_delete_test_promotions ON public.promotions;
       public               postgres    false    217    229            y           2620    25426 (   promotions trigger_promotions_updated_at    TRIGGER     �   CREATE TRIGGER trigger_promotions_updated_at BEFORE UPDATE ON public.promotions FOR EACH ROW EXECUTE FUNCTION public.update_updated_at_column();
 A   DROP TRIGGER trigger_promotions_updated_at ON public.promotions;
       public               postgres    false    230    217            t           2606    25764 9   promotion_room_type promotion_room_type_fk_promotion_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.promotion_room_type
    ADD CONSTRAINT promotion_room_type_fk_promotion_fkey FOREIGN KEY (fk_promotion) REFERENCES public.promotions(promotion_id) ON DELETE CASCADE;
 c   ALTER TABLE ONLY public.promotion_room_type DROP CONSTRAINT promotion_room_type_fk_promotion_fkey;
       public               postgres    false    4701    217    228            u           2606    25769 9   promotion_room_type promotion_room_type_fk_room_type_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.promotion_room_type
    ADD CONSTRAINT promotion_room_type_fk_room_type_fkey FOREIGN KEY (fk_room_type) REFERENCES public.room_types(id) ON DELETE CASCADE;
 c   ALTER TABLE ONLY public.promotion_room_type DROP CONSTRAINT promotion_room_type_fk_room_type_fkey;
       public               postgres    false    228    4709    222            p           2606    25511 $   room_images room_images_room_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.room_images
    ADD CONSTRAINT room_images_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.rooms(room_id) ON DELETE CASCADE;
 N   ALTER TABLE ONLY public.room_images DROP CONSTRAINT room_images_room_id_fkey;
       public               postgres    false    219    224    4711            q           2606    25516     room_tags room_tags_room_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.room_tags
    ADD CONSTRAINT room_tags_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.rooms(room_id) ON DELETE CASCADE;
 J   ALTER TABLE ONLY public.room_tags DROP CONSTRAINT room_tags_room_id_fkey;
       public               postgres    false    4711    221    224            r           2606    25521    room_tags room_tags_tag_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.room_tags
    ADD CONSTRAINT room_tags_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES public.tags(id) ON DELETE CASCADE;
 I   ALTER TABLE ONLY public.room_tags DROP CONSTRAINT room_tags_tag_id_fkey;
       public               postgres    false    4717    221    226            s           2606    25526    rooms rooms_room_type_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_room_type_id_fkey FOREIGN KEY (room_type_id) REFERENCES public.room_types(id) ON DELETE RESTRICT;
 G   ALTER TABLE ONLY public.rooms DROP CONSTRAINT rooms_room_type_id_fkey;
       public               postgres    false    224    222    4709               @   x�%��� C�3��d�������OV��qHG��s�)((�0a�	'\��/�y��� -n         �   x���Mk�0������I��r]vܥ�����.���l0��`d�#x�@�s.�����u��)-�`{d;p�g�[�)t��=x��H-t�u�\>Y�2ϒ�a�]�Cj�Ts���QG2�hZ��6/Q[�]=��Qg�y(|�t|��c�>6Q-�ݝA!ah���8o�b^��8�*NSҽ�4*��J��T�O��?6E���S��g���ч�R~Qy��         A   x������MLO-�/���-�740�7��*H�4�24�"i�4Đ4���244�JZr��qqq l�"k            x�3�4�2�4��4bS.K ����� *�+         �   x��ͱ�0���<�- 2b��HO��lS�d�������bQJ���ާa�x��N�W�||��"�ߌ6��	�h�xGL��"4��jy+d�U;(=4uYk-��O�����h!t1c��eP�{��`�{:��6�h�WfKP]��,�S�         �   x��ϱ
�0���� Z��1m�
�����rM3\���*�k����N..�~���H!���"r�u��p�C:_<��cG��|2>�!o߯�c������Pz)VKQ�ԕ2U���1���������U�4l��+Y���wM<�*�j�r���2����,��{t�e�i��:�x����Թ!�8��Ni�$�g_�         =   x�3��t��2��JL.����2�t�,JUHL��K�L���KL����,N��K����� ���     