CREATE DATABASE cursobetha
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Portuguese_Brazil.1252'
       LC_CTYPE = 'Portuguese_Brazil.1252'
       CONNECTION LIMIT = -1;

COMMENT ON DATABASE cursobetha
  IS 'Database curso Betha';

CREATE TABLE public.clientes
(
   id_cliente integer not null, 
   nome character varying(200) not null, 
   telefone character varying(20) not null, 
   documento character varying(20) not null, 
   email character varying(120),
   CONSTRAINT pk_clientes PRIMARY KEY (id_cliente)
);

CREATE TABLE public.tecnicos
(
   id_tecnico integer not null, 
   nome character varying(150) not null,
   tipo character varying(1) not null,
   CONSTRAINT pk_tecnicos PRIMARY KEY (id_tecnico)
);

ALTER TABLE public.tecnicos
  OWNER TO postgres;
COMMENT ON COLUMN public.tecnicos.tipo IS 'T - T�cnico G-Gerente';

CREATE TABLE public.equipamentos
(
   id_equipamento integer not null, 
   descricao character varying(150) not null,
   CONSTRAINT pk_equipamentos PRIMARY KEY (id_equipamento)
);


CREATE TABLE public.chamados_tecnicos
(
   id_chamado_tecnico integer not null, 
   emissao timestamp without time zone not null, 
   aprovacao timestamp without time zone, 
   id_cliente integer not null, 
   id_tecnico integer not null, 
   id_gerente integer not null,
   id_equipamento integer not null,
   descricao_problema character varying(1000) not null,
   status character varying(2) not null,
   tipo character varying(1) not null,
   valor_total NUMERIC(14,5) not null default 0,
   CONSTRAINT pk_chamados_tecnicos PRIMARY KEY (id_chamados_tecnicos), 
   CONSTRAINT fk_cliente FOREIGN KEY (id_cliente) REFERENCES public.clientes (id_cliente) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_tecnico FOREIGN KEY (id_tecnico) REFERENCES public.tecnicos (id_tecnico) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_gerente FOREIGN KEY (id_gerente) REFERENCES public.tecnicos (id_tecnico) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_equipamento FOREIGN KEY (id_equipamento) REFERENCES public.equipamentos (id_equipamento) ON UPDATE NO ACTION ON DELETE NO ACTION

);

ALTER TABLE public.chamados_tecnicos
  OWNER TO postgres;
COMMENT ON COLUMN public.chamados_tecnicos.status IS '01-Aberto 02-Aprovado 03-Cancelado 04-Finalizado';
COMMENT ON COLUMN public.chamados_tecnicos.tipo IS 'I-Interno E-Externo';

CREATE SEQUENCE public.seq_clientes;

CREATE SEQUENCE public.seq_tecnicos;

CREATE SEQUENCE public.seq_equipamentos;

CREATE SEQUENCE public.seq_chamados_tecnicos;

CREATE OR REPLACE FUNCTION trata_erros(codigo_erro integer) RETURNS void AS $$
    BEGIN
        IF codigo_erro = 1 THEN
            RAISE EXCEPTION 'Chamado já finalizado';
        END IF;
        IF codigo_erro = 2 THEN
            RAISE EXCEPTION 'Chamado já cancelado'; 
        END IF;
        IF codigo_erro = 3 THEN
            RAISE EXCEPTION 'Chamado já aprovado'; 
        END IF;
    END;
$$ LANGUAGE SQL;

CREATE OR REPLACE FUNCTION aprova_chamado() RETURNS trigger AS $$
    BEGIN
        IF TG_OP = 'UPDATE' THEN
	   IF OLD.status = '04' THEN
              PERFORM trata_erros(1);
           END IF;
           IF OLD.aprovacao IS NOT NULL AND NEW.status <> '04' THEN
              PERFORM trata_erros(3);
           END IF;
        END IF;
        IF NEW.aprovacao IS NOT NULL AND NEW.status <> '04' THEN
           NEW.status := '02';
        END IF;
        RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS aprova_chamado ON chamados_tecnicos;

CREATE TRIGGER aprova_chamado BEFORE INSERT OR UPDATE ON chamados_tecnicos
    FOR EACH ROW EXECUTE PROCEDURE aprova_chamado();

CREATE OR REPLACE FUNCTION deleta_chamado() RETURNS trigger AS $$
    BEGIN
        IF OLD.status = '03' THEN
            PERFORM trata_erros(2);
        END IF;
        IF OLD.status = '04' THEN
            PERFORM trata_erros(1);
        END IF;
        IF OLD.aprovacao IS NOT NULL THEN
            PERFORM trata_erros(3);
        END IF;
        RETURN OLD;
    END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS deleta_chamado ON chamados_tecnicos;

CREATE TRIGGER deleta_chamado BEFORE DELETE ON chamados_tecnicos
    FOR EACH ROW EXECUTE PROCEDURE deleta_chamado();