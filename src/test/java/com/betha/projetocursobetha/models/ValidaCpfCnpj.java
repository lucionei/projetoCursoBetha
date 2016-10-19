package com.betha.projetocursobetha.models;

import org.junit.Test;

/**
 *
 * @author lucionei.chequeto
 */
public class ValidaCpfCnpj {
    
    @Test
    public void validaDados() {
        Cliente cliente = new Cliente();
        
        cliente.setNome("Jose da Silva");
        cliente.setTelefone("(48) 3433-1234");
        cliente.setEmail("lucionei@gmail.com");
        cliente.setDocumento("12345678910123");
        System.out.println(cliente.validaDados());
        
        String ns = cliente.getTelefone().replaceAll("\\D*", "" );
        cliente.setTelefone(ns);
        System.out.println(cliente.getTelefone());

        
    }
    
}
