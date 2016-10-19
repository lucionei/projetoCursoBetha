package com.betha.projetocursobetha.servlets;

import com.betha.projetocursobetha.daos.GerenteDao;
import com.betha.projetocursobetha.models.Gerente;
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
@WebServlet("/api/gerentes")
public class GerenteServlet extends HttpServlet {

    private final GerenteDao dao = new GerenteDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gerente gerente = new Gerente();
        gerente.parse(Utils.parseMap(req));
        String mensagem = gerente.validaDados();        
        if (mensagem.trim().length() > 0) {
            resp.sendError(406, mensagem);
        } else if (Utils.isNotEmpty(req.getParameter("id"))) {
            try {
                dao.update(gerente);
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao editar gerente");
            }
        } else {
            try {
                dao.insert(gerente);
            } catch (Exception ex) {
                resp.sendError(406, "Erro ao inserir gerente");
            }
        }
    }

}
