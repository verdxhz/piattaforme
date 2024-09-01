package com.example.gioia.service;

import com.example.gioia.eccezioni.UtenteEsistente;
import com.example.gioia.eccezioni.UtenteNonTrovato;
import com.example.gioia.entity.Carrello;
import com.example.gioia.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.gioia.repositories.ClienteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ClientiService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public Cliente registaCliente(Cliente cliente) throws UtenteEsistente {
        if(clienteRepository.existsById(cliente.getId_cliente())){
            throw new UtenteEsistente("l'utente esiste già");
        }
        else {
            //cliente.setCarrello(new Carrello());
            clienteRepository.save(cliente);
            return cliente;
        }
    }

    @Transactional(readOnly = false)
    public Cliente updateCliente(Cliente cliente) throws UtenteNonTrovato {
        if(clienteRepository.existsById(cliente.getId_cliente())){
            Optional<Cliente> c= clienteRepository.findById(cliente.getId_cliente());
            Cliente cc=c.get();
            cc.setNome(cliente.getNome());
            cc.setCarrello(cliente.getCarrello());
            cc.setOrdini(cliente.getOrdini());
            clienteRepository.save(cliente);
            return cliente;
                }
        else
            throw new UtenteNonTrovato("l'utente che vuoi aggiornare non esiste");
    }

    @Transactional(readOnly = false)
    public void removeCliente(Cliente cliente) throws UtenteNonTrovato {
        if (clienteRepository.existsById(cliente.getId_cliente())) {
            Optional<Cliente> cli = clienteRepository.findById(cliente.getId_cliente());
            clienteRepository.delete(cli.get());
        }
        else
            throw new UtenteNonTrovato("l'utente che vuoi eliminare non esiste");

    }

    @Transactional(readOnly = true)
    public List<Cliente> mostraClienti() throws UtenteNonTrovato {
        List<Cliente> res= new ArrayList<>();
        res.addAll(clienteRepository.findAll());
        if (!res.isEmpty())
            return res;
        else
            throw new UtenteNonTrovato("non ci sono utenti registrati");
    }

    @Transactional(readOnly = true)
    public List<Cliente> mostraClientiNome(String nome) throws UtenteNonTrovato {
        List<Cliente> res= new ArrayList<>();
        res.addAll(clienteRepository.findByNomeContainingIgnoreCase(nome));
        if (!res.isEmpty())
            return res;
        else
            throw new UtenteNonTrovato("non ci sono utenti registrati a questo nome");
    }

    public Cliente mostraCliente(int id) throws UtenteNonTrovato {
        Optional<Cliente> res=clienteRepository.findById(id);
        if (!res.isEmpty())
            return res.get();
        else
            throw new UtenteNonTrovato("non ci sono utenti registrati a questo nome");
    }
    }

