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

@Service
public class ClientiService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public Cliente registaCliente(Cliente cliente) {
        if(clienteRepository.existsByEmail(cliente.getEmail()) || clienteRepository.existsByUsernameIgnoreCase(cliente.getUsername())){
            throw new RuntimeException();//TODO
        }
        else if ((Pattern.matches("[a-zA-Z ]+", cliente.getNome()) && //controlla che il nome sia solo lettere
                Pattern.matches("[a-zA-Z0-9]{1,15}+", cliente.getUsername()) && //controlla che l'username sia lettere ed eventualmente numeri
                Pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*.-]).{8,}$", cliente.getPassword()) && //Contenga almeno una lettera maiuscola, una lettera minuscola, una cifra, un carattere speciale  sia lunga almeno 8 caratteri.
                Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", cliente.getEmail()))){ //controllo abbia la forma di un'email
        clienteRepository.save(cliente);}
        else
            throw new RuntimeException();//TODO
        return cliente;
    }
    @Transactional
    public List<Cliente> mostraClienti(){
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente mostraClientiEmail(String email){
        return clienteRepository.findByEmail(email);
    }

    public List<Cliente> mostraClientiNome(String nome){
        List<Cliente> res= new ArrayList<>();
         res.addAll(clienteRepository.findByNomeContainingIgnoreCase(nome));
         return res;
    }

    @Transactional(readOnly = false)
    public void removeCliente(Cliente cliente) {
        if (clienteRepository.existsById(cliente.getId_cliente())) {
            Optional<Cliente> cli = clienteRepository.findById(cliente.getId_cliente());
            if (cli.isPresent()) clienteRepository.delete(cli.get());
        }
    }
    @Transactional(readOnly = false)
    public Cliente updateCliente(Cliente utente) {//TODO throws Exception {
        for (Cliente u : mostraClienti()) {
            if (utente.getUsername().equals(u.getUsername()) || utente.getEmail().equals(u.getEmail())) {
                if ((Pattern.matches("[a-zA-Z ]+", utente.getNome()) &&
                        Pattern.matches("[a-zA-Z0-9]{1,15}+", utente.getUsername()) &&
                        Pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*.-]).{8,}$", utente.getPassword()) &&
                        Pattern.matches("[a-zA-Z0-9]+@[a-zA-Z]+.[a-zA-Z]{2,3}", utente.getEmail())
                )) {
                    clienteRepository.save(utente);
                } else {
                    //TODO throw new InvalidCredentials("Invalid credentials");
                }
                return utente;
            }
        }
        //TODO throw new UserNotFoundException();
        return null;
    }
    //TODO keycloack

}
