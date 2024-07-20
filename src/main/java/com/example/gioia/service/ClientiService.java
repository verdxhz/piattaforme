package com.example.gioia.service;

import com.example.gioia.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.gioia.repositories.ClienteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
//TODO CHIEDERE AD ANDREA COSA SI DEVE GESTIRE DA KEYCLOAK E COSA DA QUI
@Service
public class ClientiService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public Cliente registaCliente(Cliente cliente) {
        if(clienteRepository.existsById(cliente.getId_cliente())){
            throw new RuntimeException();//TODO
        }
        else
        clienteRepository.save(cliente);
        return cliente;
    }

    @Transactional(readOnly = false)
    public Cliente updateCliente(Cliente cliente) {//TODO throws Exception {
        if(clienteRepository.existsById(cliente.getId_cliente())){
            Optional<Cliente> c= clienteRepository.findById(cliente.getId_cliente());
            Cliente cc=c.get();
            cc.setNome(cliente.getNome());
            cc.setCarrello(cliente.getCarrello());
            cc.setOrdini(cliente.getOrdini());
            clienteRepository.save(cliente);
                }
        //TODO throw new UserNotFoundException();
        return null;
    }

    @Transactional(readOnly = false)
    public void removeCliente(Cliente cliente) {
        if (clienteRepository.existsById(cliente.getId_cliente())) {
            Optional<Cliente> cli = clienteRepository.findById(cliente.getId_cliente());
            if (cli.isPresent()) clienteRepository.delete(cli.get());
        }
    }

    @Transactional(readOnly = true)
    public List<Cliente> mostraClienti(){
        List<Cliente> res= new ArrayList<>();
        res.addAll(clienteRepository.findAll());
        return res;
    }

    @Transactional(readOnly = true)
    public List<Cliente> mostraClientiNome(String nome){
        List<Cliente> res= new ArrayList<>();
        res.addAll(clienteRepository.findByNomeContainingIgnoreCase(nome));
        return res;
    }

    //TODO keycloack

}
