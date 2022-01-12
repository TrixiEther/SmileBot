--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4
-- Dumped by pg_dump version 13.4

-- Started on 2022-01-12 17:50:25

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
-- TOC entry 204 (class 1259 OID 35559)
-- Name: server_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.server_users (
    server_sn bigint NOT NULL,
    user_sn bigint NOT NULL
);


ALTER TABLE public.server_users OWNER TO postgres;

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
-- TOC entry 203 (class 1259 OID 35551)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    u_snowflake bigint NOT NULL,
    name character varying NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 2872 (class 2606 OID 35545)
-- Name: channels channels_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.channels
    ADD CONSTRAINT channels_pkey PRIMARY KEY (c_snowflake);


--
-- TOC entry 2870 (class 2606 OID 35532)
-- Name: emoji emoji_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji
    ADD CONSTRAINT emoji_pkey PRIMARY KEY (e_snowflake);


--
-- TOC entry 2868 (class 2606 OID 35524)
-- Name: servers servers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.servers
    ADD CONSTRAINT servers_pkey PRIMARY KEY (s_snowflake);


--
-- TOC entry 2874 (class 2606 OID 35558)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (u_snowflake);


--
-- TOC entry 2876 (class 2606 OID 35546)
-- Name: channels fkey_channel_server; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.channels
    ADD CONSTRAINT fkey_channel_server FOREIGN KEY (server_sn) REFERENCES public.servers(s_snowflake);


--
-- TOC entry 2875 (class 2606 OID 35533)
-- Name: emoji fkey_emojis_servers; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji
    ADD CONSTRAINT fkey_emojis_servers FOREIGN KEY (server_sn) REFERENCES public.servers(s_snowflake);


--
-- TOC entry 2877 (class 2606 OID 35562)
-- Name: server_users fkey_servers_us; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.server_users
    ADD CONSTRAINT fkey_servers_us FOREIGN KEY (server_sn) REFERENCES public.servers(s_snowflake);


--
-- TOC entry 2878 (class 2606 OID 35567)
-- Name: server_users fkey_users_us; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.server_users
    ADD CONSTRAINT fkey_users_us FOREIGN KEY (user_sn) REFERENCES public.users(u_snowflake);


-- Completed on 2022-01-12 17:50:25

--
-- PostgreSQL database dump complete
--

