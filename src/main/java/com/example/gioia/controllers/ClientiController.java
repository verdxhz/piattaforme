package com.example.gioia.controllers;

import com.example.gioia.entity.Cliente;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;
import com.example.gioia.service.ClientiService;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClientiController {
    @Autowired
    private ClientiService clientiService;

    //@PreAuthorize("hasRole=all")
    @PostMapping("/ad")
    public ResponseEntity creaCliente(@RequestBody Cliente cliente) {
        try{
            Cliente res = clientiService.registaCliente(cliente);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update")
    //@PreAuthorize("hasRole=amm")

    public Cliente update(@RequestParam Cliente cliente){
        return clientiService.updateCliente(cliente);
    }
    @DeleteMapping("/update")
   // @PreAuthorize("hasRole=amm")

    public void delete(@RequestParam Cliente cliente){
        clientiService.removeCliente(cliente);
    }

    @GetMapping("/all")
   // @PreAuthorize("hasRole=amm")

    public List<Cliente> getClienti(){
        return clientiService.mostraClienti();
    }

    @GetMapping("/email")
   // @PreAuthorize("hasRole=amm")

    public Cliente getClienteEmail(@RequestParam String email){
        return clientiService.mostraClientiEmail(email);
    }

    @GetMapping("/nome")
    //@PreAuthorize("hasRole=amm")

    public List<Cliente> getClientiNome(@RequestParam String nome){
        return clientiService.mostraClientiNome(nome);
    }

}
