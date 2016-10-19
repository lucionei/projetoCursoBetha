package com.betha.projetocursobetha.servlets;

import com.betha.projetocursobetha.daos.EquipamentoDao;
import com.betha.projetocursobetha.models.Equipamento;
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
@WebServlet("/api/equipamentos")
public class EquipamentoServlet extends HttpServlet {

    private final EquipamentoDao dao = new EquipamentoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Utils.isNotEmpty(req.getParameter("id"))) { // get de um registro
            Equipamento equipamento = null;
            try {
                equipamento = dao.findById(Utils.parseLong(req.getParameter("id")));
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao consultar equipamento");
            }
            if (equipamento == null) {
                resp.sendError(404);
            } else {
                resp.setContentType("application/json");
                resp.getWriter().append(equipamento.toString());
            }
        } else if (Utils.isNotEmpty(req.getParameter("pagina"))) { // get dos registros com paginação
            resp.setContentType("application/json");
            if (Utils.isEmpty(req.getParameter("pesquisa"))) {
                try {
                    resp.getWriter().append(dao.findAll(Utils.parseLong(req.getParameter("pagina")), Utils.parseInt(req.getParameter("limitePagina"))).toString());
                } catch (Exception ex) {
                    resp.sendError(406, "Erro ao consultar equipamentos");
                }
            } else {
                try {
                    resp.getWriter().append(dao.findAll(Utils.parseLong(req.getParameter("pagina")), Utils.parseInt(req.getParameter("limitePagina")), req.getParameter("pesquisa")).toString());
                } catch (Exception ex) {
                    resp.sendError(406, "Erro ao consultar equipamentos");
                }
            }
        } else { // get de todos
            resp.setContentType("application/json");
            try {
                resp.getWriter().append(dao.findAll().toString());
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao consultar equipamentos");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Equipamento equipamento = new Equipamento();
        equipamento.parse(Utils.parseMap(req));
        String mensagem = equipamento.validaDados();
        if (mensagem.trim().length() > 0) {
            resp.sendError(406, mensagem);
        } else if (Utils.isNotEmpty(req.getParameter("id"))) {
            try {
                dao.update(equipamento);
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao editar equipamento");
            }
        } else {
            try {
                dao.insert(equipamento);
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao inserir equipamento");
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
                resp.sendError(406, "Erro ao excluir equipamento");
            }
        } else {
            resp.sendError(406, "Propriedade ID obrigatória");
        }
    }

}
