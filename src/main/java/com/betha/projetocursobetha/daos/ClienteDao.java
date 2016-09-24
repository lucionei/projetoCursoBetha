package com.betha.projetocursobetha.daos;

import com.betha.projetocursobetha.models.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {

    private static final List<Cliente> RECORDS = new ArrayList<>();

    public Cliente insert(Cliente cliente) {
        RECORDS.add(cliente);
        cliente.setId(Long.valueOf(RECORDS.size()));
        return cliente;
    }

    public Cliente update(Cliente cliente) {
        return cliente;
    }

    public void delete(Long id) {
        Cliente cliente = get(id);
        if (cliente != null) {
            RECORDS.remove(cliente);
        }
    }

    public Cliente get(Long id) {
        for (Cliente cliente : RECORDS) {
            if (cliente.getId().equals(id)) {
                return cliente;
            }
        }
        return null;
    }

    public List<Cliente> getAll() {
        return RECORDS;
    }

}
