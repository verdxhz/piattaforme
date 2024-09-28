package com.example.gioia.controllers;

import com.example.gioia.eccezioni.UtenteEsistente;
import com.example.gioia.eccezioni.UtenteNonTrovato;
import com.example.gioia.entity.Cliente;
import com.example.gioia.security.authentication.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.gioia.service.*;//ClientiService;
import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClientiController {

    @Autowired
    private ClientiService clientiService;


    @PostMapping("/ad")
    public ResponseEntity creaCliente(@RequestBody Cliente cliente) {
        try{
            Cliente res = clientiService.registaCliente(cliente);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch(UtenteEsistente e ){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Cliente cliente) {
        try{
            Cliente res = clientiService.updateCliente(cliente);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch(UtenteNonTrovato e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestBody Cliente cliente){
        try{
            clientiService.removeCliente(cliente);
            return new ResponseEntity(cliente, HttpStatus.OK);
        }catch(UtenteNonTrovato  e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity getClienti(){
        try{
            List<Cliente> res = clientiService.mostraClienti();
            return new ResponseEntity(res, HttpStatus.OK);
        }catch(UtenteNonTrovato e){ e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity getCliente(){
        try{
            Cliente res = clientiService.mostraCliente(Utils.getId());
            return new ResponseEntity(res, HttpStatus.OK);
        }catch(UtenteNonTrovato e){
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/nome")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity getClientiNome(@RequestParam String nome){
        try{
            List<Cliente> res = clientiService.mostraClientiNome(nome);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch( UtenteNonTrovato e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

}
