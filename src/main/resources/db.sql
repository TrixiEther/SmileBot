--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4
-- Dumped by pg_dump version 13.4

-- Started on 2022-08-18 15:58:52

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 202 (class 1259 OID 35538)
-- Name: channels; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.channels (
    c_snowflake bigint NOT NULL,
    server_sn bigint NOT NULL,
    name character varying NOT NULL
);


ALTER TABLE public.channels OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 35525)
-- Name: emoji; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.emoji (
    e_snowflake bigint NOT NULL,
    server_sn bigint NOT NULL,
    content character varying NOT NULL
);


ALTER TABLE public.emoji OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 36056)
-- Name: emoji_in_message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.emoji_in_message (
    id bigint NOT NULL,
    message_sn bigint NOT NULL,
    emoji_sn bigint NOT NULL,
    count integer NOT NULL
);


ALTER TABLE public.emoji_in_message OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 36054)
-- Name: emoji_in_message_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.emoji_in_message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.emoji_in_message_id_seq OWNER TO postgres;

--
-- TOC entry 3071 (class 0 OID 0)
-- Dependencies: 206
-- Name: emoji_in_message_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.emoji_in_message_id_seq OWNED BY public.emoji_in_message.id;


--
-- TOC entry 200 (class 1259 OID 35520)
-- Name: servers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.servers (
    s_snowflake bigint NOT NULL,
    name character varying(64) NOT NULL
);


ALTER TABLE public.servers OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 46883)
-- Name: in_message_summary; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.in_message_summary AS
 SELECT servers.s_snowflake,
    emoji.e_snowflake,
    sum(emoji_in_message.count) AS in_message
   FROM ((public.servers
     LEFT JOIN public.emoji ON ((emoji.server_sn = servers.s_snowflake)))
     LEFT JOIN public.emoji_in_message ON ((emoji.e_snowflake = emoji_in_message.emoji_sn)))
  GROUP BY servers.s_snowflake, emoji.e_snowflake;


ALTER TABLE public.in_message_summary OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 36383)
-- Name: reaction_to_messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reaction_to_messages (
    id bigint NOT NULL,
    message_sn bigint NOT NULL,
    user_sn bigint NOT NULL,
    emoji_sn bigint NOT NULL
);


ALTER TABLE public.reaction_to_messages OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 46852)
-- Name: in_reaction_summary; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.in_reaction_summary AS
 SELECT servers.s_snowflake,
    emoji.e_snowflake,
    count(*) AS in_reaction
   FROM ((public.servers
     JOIN public.emoji ON ((emoji.server_sn = servers.s_snowflake)))
     JOIN public.reaction_to_messages ON ((emoji.e_snowflake = reaction_to_messages.emoji_sn)))
  GROUP BY servers.s_snowflake, emoji.e_snowflake;


ALTER TABLE public.in_reaction_summary OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 46953)
-- Name: general_summary; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.general_summary AS
 SELECT in_message_summary.s_snowflake AS server_sn,
    in_message_summary.e_snowflake AS emoji_sn,
    COALESCE(in_message_summary.in_message, (0)::bigint) AS in_message_result,
    COALESCE(in_reaction_summary.in_reaction, (0)::bigint) AS in_reaction_result
   FROM (public.in_message_summary
     FULL JOIN public.in_reaction_summary ON ((in_message_summary.e_snowflake = in_reaction_summary.e_snowflake)));


ALTER TABLE public.general_summary OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 36025)
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
    m_snowflake bigint NOT NULL,
    channel_sn bigint,
    user_sn bigint NOT NULL,
    thread_sn bigint
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 36381)
-- Name: reaction_to_messages_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.reaction_to_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.reaction_to_messages_id_seq OWNER TO postgres;

--
-- TOC entry 3072 (class 0 OID 0)
-- Dependencies: 208
-- Name: reaction_to_messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.reaction_to_messages_id_seq OWNED BY public.reaction_to_messages.id;


--
-- TOC entry 204 (class 1259 OID 35559)
-- Name: server_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.server_users (
    server_sn bigint NOT NULL,
    user_sn bigint NOT NULL
);


ALTER TABLE public.server_users OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 45168)
-- Name: threads; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.threads (
    t_snowflake bigint NOT NULL,
    channel_sn bigint NOT NULL,
    name character varying NOT NULL,
    is_archived boolean NOT NULL
);


ALTER TABLE public.threads OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 35551)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    u_snowflake bigint NOT NULL,
    name character varying NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 2900 (class 2604 OID 36059)
-- Name: emoji_in_message id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji_in_message ALTER COLUMN id SET DEFAULT nextval('public.emoji_in_message_id_seq'::regclass);


--
-- TOC entry 2901 (class 2604 OID 36386)
-- Name: reaction_to_messages id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reaction_to_messages ALTER COLUMN id SET DEFAULT nextval('public.reaction_to_messages_id_seq'::regclass);


--
-- TOC entry 2907 (class 2606 OID 35545)
-- Name: channels channels_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.channels
    ADD CONSTRAINT channels_pkey PRIMARY KEY (c_snowflake);


--
-- TOC entry 2914 (class 2606 OID 36061)
-- Name: emoji_in_message emoji_in_message_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji_in_message
    ADD CONSTRAINT emoji_in_message_pkey PRIMARY KEY (id);


--
-- TOC entry 2905 (class 2606 OID 35532)
-- Name: emoji emoji_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji
    ADD CONSTRAINT emoji_pkey PRIMARY KEY (e_snowflake);


--
-- TOC entry 2912 (class 2606 OID 36029)
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (m_snowflake);


--
-- TOC entry 2916 (class 2606 OID 36388)
-- Name: reaction_to_messages reaction_to_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reaction_to_messages
    ADD CONSTRAINT reaction_to_messages_pkey PRIMARY KEY (id);


--
-- TOC entry 2903 (class 2606 OID 35524)
-- Name: servers servers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.servers
    ADD CONSTRAINT servers_pkey PRIMARY KEY (s_snowflake);


--
-- TOC entry 2919 (class 2606 OID 45175)
-- Name: threads threads_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.threads
    ADD CONSTRAINT threads_pkey PRIMARY KEY (t_snowflake);


--
-- TOC entry 2909 (class 2606 OID 35558)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (u_snowflake);


--
-- TOC entry 2917 (class 1259 OID 45328)
-- Name: fki_fkey_thread_channel; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_fkey_thread_channel ON public.threads USING btree (channel_sn);


--
-- TOC entry 2910 (class 1259 OID 36053)
-- Name: fki_fkey_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_fkey_user ON public.messages USING btree (user_sn);


--
-- TOC entry 2924 (class 2606 OID 36030)
-- Name: messages fkey_channel; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fkey_channel FOREIGN KEY (channel_sn) REFERENCES public.channels(c_snowflake);


--
-- TOC entry 2921 (class 2606 OID 35546)
-- Name: channels fkey_channel_server; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.channels
    ADD CONSTRAINT fkey_channel_server FOREIGN KEY (server_sn) REFERENCES public.servers(s_snowflake);


--
-- TOC entry 2928 (class 2606 OID 36067)
-- Name: emoji_in_message fkey_emoji; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji_in_message
    ADD CONSTRAINT fkey_emoji FOREIGN KEY (emoji_sn) REFERENCES public.emoji(e_snowflake);


--
-- TOC entry 2931 (class 2606 OID 36399)
-- Name: reaction_to_messages fkey_emoji; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reaction_to_messages
    ADD CONSTRAINT fkey_emoji FOREIGN KEY (emoji_sn) REFERENCES public.emoji(e_snowflake);


--
-- TOC entry 2920 (class 2606 OID 35533)
-- Name: emoji fkey_emojis_servers; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji
    ADD CONSTRAINT fkey_emojis_servers FOREIGN KEY (server_sn) REFERENCES public.servers(s_snowflake);


--
-- TOC entry 2927 (class 2606 OID 36062)
-- Name: emoji_in_message fkey_message; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji_in_message
    ADD CONSTRAINT fkey_message FOREIGN KEY (message_sn) REFERENCES public.messages(m_snowflake);


--
-- TOC entry 2929 (class 2606 OID 36389)
-- Name: reaction_to_messages fkey_message; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reaction_to_messages
    ADD CONSTRAINT fkey_message FOREIGN KEY (message_sn) REFERENCES public.messages(m_snowflake);


--
-- TOC entry 2922 (class 2606 OID 35562)
-- Name: server_users fkey_servers_us; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.server_users
    ADD CONSTRAINT fkey_servers_us FOREIGN KEY (server_sn) REFERENCES public.servers(s_snowflake);


--
-- TOC entry 2926 (class 2606 OID 45181)
-- Name: messages fkey_thread; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fkey_thread FOREIGN KEY (thread_sn) REFERENCES public.threads(t_snowflake);


--
-- TOC entry 2932 (class 2606 OID 45323)
-- Name: threads fkey_thread_channel; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.threads
    ADD CONSTRAINT fkey_thread_channel FOREIGN KEY (channel_sn) REFERENCES public.channels(c_snowflake);


--
-- TOC entry 2925 (class 2606 OID 36048)
-- Name: messages fkey_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fkey_user FOREIGN KEY (user_sn) REFERENCES public.users(u_snowflake);


--
-- TOC entry 2930 (class 2606 OID 36394)
-- Name: reaction_to_messages fkey_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reaction_to_messages
    ADD CONSTRAINT fkey_user FOREIGN KEY (user_sn) REFERENCES public.users(u_snowflake);


--
-- TOC entry 2923 (class 2606 OID 35567)
-- Name: server_users fkey_users_us; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.server_users
    ADD CONSTRAINT fkey_users_us FOREIGN KEY (user_sn) REFERENCES public.users(u_snowflake);


-- Completed on 2022-08-18 15:58:52

--
-- PostgreSQL database dump complete
--

