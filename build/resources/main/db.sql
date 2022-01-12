--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4
-- Dumped by pg_dump version 13.4

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
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: channels; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.channels (
    id integer NOT NULL,
    server_id integer NOT NULL,
    c_snowflake bigint NOT NULL,
    name character varying(64) NOT NULL
);


ALTER TABLE public.channels OWNER TO postgres;

--
-- Name: channels_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.channels_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.channels_id_seq OWNER TO postgres;

--
-- Name: channels_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.channels_id_seq OWNED BY public.channels.id;


--
-- Name: emoji_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.emoji_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.emoji_id_seq OWNER TO postgres;

--
-- Name: emoji; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.emoji (
    id integer DEFAULT nextval('public.emoji_id_seq'::regclass) NOT NULL,
    server_id integer NOT NULL,
    content character varying(64) NOT NULL,
    e_snowflake bigint NOT NULL
);


ALTER TABLE public.emoji OWNER TO postgres;

--
-- Name: emoji_in_message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.emoji_in_message (
    id integer NOT NULL,
    message_id integer NOT NULL,
    emoji_id integer NOT NULL,
    count smallint NOT NULL
);


ALTER TABLE public.emoji_in_message OWNER TO postgres;

--
-- Name: emoji_in_message_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.emoji_in_message_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.emoji_in_message_id_seq OWNER TO postgres;

--
-- Name: emoji_in_message_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.emoji_in_message_id_seq OWNED BY public.emoji_in_message.id;


--
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
    id integer NOT NULL,
    channel_id integer NOT NULL,
    user_id integer NOT NULL,
    m_snowflake bigint NOT NULL
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- Name: messages_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.messages_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.messages_id_seq OWNER TO postgres;

--
-- Name: messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.messages_id_seq OWNED BY public.messages.id;


--
-- Name: reactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reactions (
    id integer NOT NULL,
    message_id integer NOT NULL,
    user_id integer NOT NULL,
    emoji_id integer NOT NULL
);


ALTER TABLE public.reactions OWNER TO postgres;

--
-- Name: reactions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.reactions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.reactions_id_seq OWNER TO postgres;

--
-- Name: reactions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.reactions_id_seq OWNED BY public.reactions.id;


--
-- Name: servers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.servers (
    id integer NOT NULL,
    s_snowflake bigint NOT NULL,
    name character varying(64) NOT NULL
);


ALTER TABLE public.servers OWNER TO postgres;

--
-- Name: servers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.servers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.servers_id_seq OWNER TO postgres;

--
-- Name: servers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.servers_id_seq OWNED BY public.servers.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    u_snowflake bigint NOT NULL,
    name character varying(64) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: channels id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.channels ALTER COLUMN id SET DEFAULT nextval('public.channels_id_seq'::regclass);


--
-- Name: emoji_in_message id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji_in_message ALTER COLUMN id SET DEFAULT nextval('public.emoji_in_message_id_seq'::regclass);


--
-- Name: messages id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages ALTER COLUMN id SET DEFAULT nextval('public.messages_id_seq'::regclass);


--
-- Name: reactions id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reactions ALTER COLUMN id SET DEFAULT nextval('public.reactions_id_seq'::regclass);


--
-- Name: servers id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.servers ALTER COLUMN id SET DEFAULT nextval('public.servers_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Name: channels c_snowflake_uc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.channels
    ADD CONSTRAINT c_snowflake_uc UNIQUE (c_snowflake);


--
-- Name: channels channel_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.channels
    ADD CONSTRAINT channel_id_pkey PRIMARY KEY (id);


--
-- Name: emoji e_snowflake_uc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji
    ADD CONSTRAINT e_snowflake_uc UNIQUE (e_snowflake);


--
-- Name: emoji emoji_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji
    ADD CONSTRAINT emoji_id_pkey PRIMARY KEY (id);


--
-- Name: messages m_snowflake_uc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT m_snowflake_uc UNIQUE (m_snowflake);


--
-- Name: emoji_in_message message_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji_in_message
    ADD CONSTRAINT message_id_pkey PRIMARY KEY (id);


--
-- Name: messages messages_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_id_pkey PRIMARY KEY (id);


--
-- Name: reactions reaction_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reactions
    ADD CONSTRAINT reaction_id_pkey PRIMARY KEY (id);


--
-- Name: servers s_snowflake_uc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.servers
    ADD CONSTRAINT s_snowflake_uc UNIQUE (s_snowflake);


--
-- Name: servers server_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.servers
    ADD CONSTRAINT server_id_pkey PRIMARY KEY (id);


--
-- Name: users u_snowflake_uc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT u_snowflake_uc UNIQUE (u_snowflake);


--
-- Name: users user_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_id_pkey PRIMARY KEY (id);


--
-- Name: fki_channel_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_channel_id_fkey ON public.messages USING btree (channel_id);


--
-- Name: fki_emoji_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_emoji_id_fkey ON public.emoji_in_message USING btree (emoji_id);


--
-- Name: fki_fkey_servers; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_fkey_servers ON public.emoji USING btree (server_id);


--
-- Name: fki_message_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_message_id_fkey ON public.emoji_in_message USING btree (message_id);


--
-- Name: fki_servers_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_servers_id_fkey ON public.channels USING btree (server_id);


--
-- Name: fki_user_id_fkey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_user_id_fkey ON public.messages USING btree (user_id);


--
-- Name: messages channel_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT channel_id_fkey FOREIGN KEY (channel_id) REFERENCES public.channels(id);


--
-- Name: emoji_in_message emoji_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji_in_message
    ADD CONSTRAINT emoji_id_fkey FOREIGN KEY (emoji_id) REFERENCES public.emoji(id);


--
-- Name: reactions emoji_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reactions
    ADD CONSTRAINT emoji_id_fkey FOREIGN KEY (emoji_id) REFERENCES public.emoji(id);


--
-- Name: emoji_in_message message_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji_in_message
    ADD CONSTRAINT message_id_fkey FOREIGN KEY (message_id) REFERENCES public.messages(id);


--
-- Name: reactions message_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reactions
    ADD CONSTRAINT message_id_fkey FOREIGN KEY (message_id) REFERENCES public.messages(id);


--
-- Name: channels servers_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.channels
    ADD CONSTRAINT servers_id_fkey FOREIGN KEY (server_id) REFERENCES public.servers(id);


--
-- Name: emoji servers_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emoji
    ADD CONSTRAINT servers_id_fkey FOREIGN KEY (server_id) REFERENCES public.servers(id);


--
-- Name: messages user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: reactions user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reactions
    ADD CONSTRAINT user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- PostgreSQL database dump complete
--

