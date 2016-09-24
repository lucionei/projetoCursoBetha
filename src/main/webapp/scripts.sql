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
   id integer not null, 
   nome character varying(200) not null, 
   telefone character varying(20) not null, 
   documento character varying(20) not null, 
   email character varying(120),
   CONSTRAINT pk_clientes PRIMARY KEY (id)
);

CREATE TABLE public.tecnicos
(
   id integer not null, 
   nome character varying(150) not null,
   tipo character varying(1) not null,
   CONSTRAINT pk_tecnicos PRIMARY KEY (id)
);

ALTER TABLE public.tecnicos
  OWNER TO postgres;
COMMENT ON COLUMN public.tecnicos.tipo IS 'T - T�cnico G-Gerente';

CREATE TABLE public.equipamentos
(
   id integer not null, 
   descricao character varying(150) not null,
   CONSTRAINT pk_equipamentos PRIMARY KEY (id)
);


CREATE TABLE public.chamados_tecnicos
(
   id integer not null, 
   emissao timestamp without time zone not null, 
   aprovacao timestamp without time zone, 
   id_cliente integer not null, 
   id_tecnico integer not null, 
   id_gerente integer not null,
   id_equipamento integer not null,
   descricao_problema character varying(1000) not null,
   status character varying(2) not null,
   tipo character varying(1) not null,
   valor NUMERIC(14,2) not null default 0,
   CONSTRAINT pk_chamados_tecnicos PRIMARY KEY (id), 
   CONSTRAINT fk_cliente FOREIGN KEY (id_cliente) REFERENCES public.clientes (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_tecnico FOREIGN KEY (id_tecnico) REFERENCES public.tecnicos (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_gerente FOREIGN KEY (id_gerente) REFERENCES public.tecnicos (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_equipamento FOREIGN KEY (id_equipamento) REFERENCES public.equipamentos (id) ON UPDATE NO ACTION ON DELETE NO ACTION

);

ALTER TABLE public.chamados_tecnicos
  OWNER TO postgres;
COMMENT ON COLUMN public.chamados_tecnicos.status IS '01-Aberto 02-Aprovado 03-Cancelado 04-Finalizado';
COMMENT ON COLUMN public.chamados_tecnicos.tipo IS 'I-Interno E-Externo';

CREATE SEQUENCE public.seq_clientes;

CREATE SEQUENCE public.seq_tecnicos;

CREATE SEQUENCE public.seq_equipamentos;

CREATE SEQUENCE public.seq_chamados_tecnicos;


/*
CREATE FUNCTION pedido_itens_total() RETURNS trigger AS $$
    BEGIN
        IF NEW.quantidade IS NULL THEN
            RAISE EXCEPTION 'quantidade n�o pode ser nulo';
        END IF;
        IF NEW.valor_unitario IS NULL THEN
            RAISE EXCEPTION 'valor unitario n�o pode ser nulo';
        END IF;

        -- calcular o valor total
        NEW.valor_total := NEW.quantidade * NEW.valor_unitario;
        RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER pedido_itens_total BEFORE INSERT OR UPDATE ON pedido_itens
    FOR EACH ROW EXECUTE PROCEDURE pedido_itens_total();

CREATE OR REPLACE FUNCTION update_total_pedido(id_pedido integer) RETURNS void AS $$
    UPDATE pedidos SET valor_total = (SELECT SUM(i.valor_total) - COALESCE(MAX(p.desconto), 0) FROM pedidos p LEFT JOIN pedido_itens i ON (p.id = i.id_pedido) WHERE p.id = $1)
        WHERE id = $1;
$$ LANGUAGE SQL;

CREATE OR REPLACE FUNCTION pedido_itens_total_pedido() RETURNS trigger AS $$
    BEGIN
        IF TG_OP = 'DELETE' THEN
            IF OLD.id_pedido IS NULL THEN
                RAISE EXCEPTION 'pedido n�o pode ser nulo';
            END IF;

            -- atualizar valor total
            PERFORM update_total_pedido(OLD.id_pedido);
            RETURN NEW;
        ELSE
            IF NEW.id_pedido IS NULL THEN
                RAISE EXCEPTION 'pedido n�o pode ser nulo';
            END IF;

            -- atualizar valor total
            PERFORM update_total_pedido(NEW.id_pedido);
            RETURN NEW;
        END IF;
    END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS pedido_itens_total_pedido ON pedido_itens;

CREATE TRIGGER pedido_itens_total_pedido AFTER INSERT OR UPDATE OR DELETE ON pedido_itens
    FOR EACH ROW EXECUTE PROCEDURE pedido_itens_total_pedido();

CREATE OR REPLACE FUNCTION pedidos_total() RETURNS trigger AS $$
    DECLARE vl_total NUMERIC(12,5);
    BEGIN
        -- calcular o valor total
        SELECT SUM(valor_total) INTO vl_total FROM pedido_itens WHERE id_pedido = NEW.id;
        NEW.valor_total := vl_total - COALESCE(NEW.desconto, 0);
        RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS pedidos_total ON pedidos;

CREATE TRIGGER pedidos_total BEFORE UPDATE ON pedidos
    FOR EACH ROW EXECUTE PROCEDURE pedidos_total();

CREATE OR REPLACE VIEW public.totais_por_clientes AS 
 SELECT c.id,
    c.codigo,
    c.nome,
    max(p.emissao) AS ultima_compra,
    sum(p.valor_total) AS valor_total
   FROM clientes c
     LEFT JOIN pedidos p ON p.id_cliente = c.id
  GROUP BY c.id, c.codigo, c.nome;
  */