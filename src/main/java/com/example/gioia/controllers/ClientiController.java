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
    public ResponseEntity update(@RequestParam Cliente cliente){
        try{
            Cliente res = clientiService.updateCliente(cliente);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/update")
   // @PreAuthorize("hasRole=amm")
    public ResponseEntity delete(@RequestParam Cliente cliente){
        try{
            clientiService.removeCliente(cliente);
            return new ResponseEntity(cliente, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
   // @PreAuthorize("hasRole=amm")
    public ResponseEntity getClienti(){
        try{
            List<Cliente> res = clientiService.mostraClienti();
            return new ResponseEntity(res, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/nome")
    //@PreAuthorize("hasRole=amm")
    public ResponseEntity getClientiNome(@RequestParam String nome){
        try{
            List<Cliente> res = clientiService.mostraClientiNome(nome);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

}
