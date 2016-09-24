package com.betha.projetocursobetha.servlets;



import com.betha.projetocursobetha.daos.ClienteDao;
import com.betha.projetocursobetha.models.Cliente;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
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
        if (req.getParameter("id") != null) { // get de um registro
            Cliente cliente = dao.get(Long.parseLong(req.getParameter("id")));
            if (cliente == null)
                resp.sendError(404, "Nada encontrado");
            else {
                resp.setContentType("application/json");
                resp.getWriter().append(cliente.toString());
            }
        } else { // get de todos
            resp.setContentType("application/json");
            resp.getWriter().append(dao.getAll().toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") != null && !req.getParameter("id").isEmpty()) { // update
            
        } else {  // insert
            Map<String, String> dados = new HashMap<>();
            Enumeration<String> parans = req.getParameterNames();
            while (parans.hasMoreElements()) {
                String key = parans.nextElement();
                dados.put(key, req.getParameter(key));
            }
            Cliente cliente = new Cliente();
            cliente.parse(dados);
            dao.insert(cliente);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") == null) {
            resp.sendError(406, "Propriedade ID obrigatória");
        } else { // realizar o delete
            dao.delete(Long.parseLong(req.getParameter("id")));
        }
    }
    
}
