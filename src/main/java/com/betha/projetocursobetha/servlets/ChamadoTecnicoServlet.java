package com.betha.projetocursobetha.servlets;

import com.betha.projetocursobetha.daos.ChamadoTecnicoDao;
import com.betha.projetocursobetha.models.ChamadoTecnico;
import com.betha.projetocursobetha.utils.Utils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lucionei.chequeto
 */
@WebServlet("/api/chamados")
public class ChamadoTecnicoServlet extends HttpServlet {

    private final ChamadoTecnicoDao dao = new ChamadoTecnicoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Utils.isNotEmpty(req.getParameter("id"))) { // get de um registro
            ChamadoTecnico chamado = null;
            try {
                chamado = dao.findById(Utils.parseLong(req.getParameter("id")));
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao consultar chamado " + ex.getMessage());
            }
            if (chamado == null) {
                resp.sendError(404);
            } else {
                resp.setContentType("application/json");
                resp.getWriter().append(chamado.toString());
            }
        } else if (Utils.isNotEmpty(req.getParameter("pagina"))) { // get dos registros com paginação
            resp.setContentType("application/json");
            if (Utils.isEmpty(req.getParameter("pesquisa"))) {
                try {
                    resp.getWriter().append(dao.findAll(Utils.parseLong(req.getParameter("pagina")), Utils.parseInt(req.getParameter("limitePagina"))).toString());
                } catch (Exception ex) {
                    resp.sendError(406, "Erro ao consultar chamados " + ex.getMessage());
                }
                System.out.println(resp.getWriter().toString());
            } else {
                try {
                    resp.getWriter().append(dao.findAll(Utils.parseLong(req.getParameter("pagina")), Utils.parseInt(req.getParameter("limitePagina")), req.getParameter("pesquisa")).toString());
                } catch (Exception ex) {
                    resp.sendError(406, "Erro ao consultar chamados " + ex.getMessage());
                }
            }
        } else { // get de todos
            resp.setContentType("application/json");
            try {
                resp.getWriter().append(dao.findAll().toString());
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao consultar chamados " + ex.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ChamadoTecnico chamado = new ChamadoTecnico();
        chamado.parse(Utils.parseMap(req));
        String mensagem = chamado.validaDados();
        if (mensagem.trim().length() > 0) {
            resp.sendError(406, mensagem);
        } else if (Utils.isNotEmpty(req.getParameter("id"))) {
            try {
                dao.update(chamado);
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao editar chamado " + ex.getMessage());
            }
        } else {
            try {
                dao.insert(chamado);
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao inserir chamado " + ex.getMessage());
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Utils.isNotEmpty(req.getParameter("id"))) { try {
            // realizar o delete
            dao.delete(Utils.parseLong(req.getParameter("id")));
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao excluir chamado " + ex.getMessage());
            }
        } else {
            resp.sendError(406, "Propriedade ID obrigatória");
        }
    }

}
