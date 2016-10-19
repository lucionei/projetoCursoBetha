package com.betha.projetocursobetha.servlets;

import com.betha.projetocursobetha.daos.TecnicoDao;
import com.betha.projetocursobetha.models.Tecnico;
import com.betha.projetocursobetha.utils.Utils;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lucionei.chequeto
 */
@WebServlet("/api/tecnicos")
public class TecnicoServlet extends HttpServlet {

    private final TecnicoDao dao = new TecnicoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Utils.isNotEmpty(req.getParameter("id"))) { // get de um registro
            Tecnico tecnico = null;
            try {
                tecnico = dao.findById(Utils.parseLong(req.getParameter("id")));
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao consultar técnico");
            }
            if (tecnico == null) {
                resp.sendError(404);
            } else {
                resp.setContentType("application/json");
                resp.getWriter().append(tecnico.toString());
            }
        } else if (Utils.isNotEmpty(req.getParameter("pagina"))) { // get dos registros com paginação
            resp.setContentType("application/json");
            if (Utils.isEmpty(req.getParameter("pesquisa"))) {
                try {
                    resp.getWriter().append(dao.findAll(Utils.parseLong(req.getParameter("pagina")), Utils.parseInt(req.getParameter("limitePagina"))).toString());
                } catch (Exception ex) {
                    resp.sendError(406, "Erro ao consultar técnicos");
                }
            } else {
                try {
                    resp.getWriter().append(dao.findAll(Utils.parseLong(req.getParameter("pagina")), Utils.parseInt(req.getParameter("limitePagina")), req.getParameter("pesquisa")).toString());
                } catch (Exception ex) {
                    resp.sendError(406, "Erro ao consultar técnicos");
                }
            }
        } else if (Utils.isNotEmpty(req.getParameter("tipo"))) { // get de todos os reggistros de um determinado tipo
            resp.setContentType("application/json");
            try {
                resp.getWriter().append(dao.findAll(req.getParameter("tipo")).toString());
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao consultar técnicos");
            }
        } else { // get de todos
            resp.setContentType("application/json");
            try {
                resp.getWriter().append(dao.findAll().toString());
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao consultar técnicos");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Tecnico tecnico = new Tecnico();
        tecnico.parse(Utils.parseMap(req));
        String mensagem = tecnico.validaDados();
        if (mensagem.trim().length() > 0) {
            resp.sendError(406, mensagem);
        } else if (Utils.isNotEmpty(req.getParameter("id"))) {
            try {
                dao.update(tecnico);
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao editar técnico");
            }
        } else {
            try {
                dao.insert(tecnico);
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao inserir técnico");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Utils.isNotEmpty(req.getParameter("id"))) {
            try {
                // realizar o delete
                dao.delete(Utils.parseLong(req.getParameter("id")));
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao excluir técnico");
            }
        } else {
            resp.sendError(406, "Propriedade ID obrigatória");
        }
    }

}
