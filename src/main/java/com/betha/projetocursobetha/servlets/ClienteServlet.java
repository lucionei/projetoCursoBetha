package com.betha.projetocursobetha.servlets;

import com.betha.projetocursobetha.daos.ClienteDao;
import com.betha.projetocursobetha.models.Cliente;
import com.betha.projetocursobetha.utils.Utils;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/clientes")
public class ClienteServlet extends HttpServlet {

    private final ClienteDao dao = new ClienteDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Utils.isNotEmpty(req.getParameter("id"))) { // get de um registro
            Cliente cliente = null;
            try {
                cliente = dao.findById(Utils.parseLong(req.getParameter("id")));
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao consultar cliente");
            }
            if (cliente == null) {
                resp.sendError(404);
            } else {
                resp.setContentType("application/json");
                resp.getWriter().append(cliente.toString());
            }
        } else if (Utils.isNotEmpty(req.getParameter("pagina"))) { // get dos registros com paginação
            resp.setContentType("application/json");
            if (Utils.isEmpty(req.getParameter("pesquisa"))) {
                try {
                    resp.getWriter().append(dao.findAll(Utils.parseLong(req.getParameter("pagina")),Utils.parseInt(req.getParameter("limitePagina"))).toString());
                } catch (Exception ex) {
                    resp.sendError(406, "Erro ao consultar clientes");
                }
            } else {
                try {
                    resp.getWriter().append(dao.findAll(Utils.parseLong(req.getParameter("pagina")),Utils.parseInt(req.getParameter("limitePagina")),req.getParameter("pesquisa")).toString());
                } catch (Exception ex) {
                    resp.sendError(406, "Erro ao consultar clientes");
                }
            }
        } else { // get de todos
            resp.setContentType("application/json");
            try {
                resp.getWriter().append(dao.findAll().toString());
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao consultar clientes");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cliente cliente = new Cliente();
        cliente.parse(Utils.parseMap(req));
        String mensagem = cliente.validaDados();
        if (mensagem.trim().length() > 0) {
            resp.sendError(406, mensagem);
        } else if (Utils.isNotEmpty(req.getParameter("id"))) {
            try {
                dao.update(cliente);
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao editar cliente");
            }
        } else {
            try {
                dao.insert(cliente);
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao inserir cliente");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Utils.isNotEmpty(req.getParameter("id"))) { try {
            // realizar o delete
            dao.delete(Utils.parseLong(req.getParameter("id")));
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao excluir cliente");
            }
        } else { 
            resp.sendError(406, "Propriedade ID obrigatória");
        }
    }

}
