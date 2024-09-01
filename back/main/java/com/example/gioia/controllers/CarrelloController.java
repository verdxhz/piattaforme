package com.example.gioia.controllers;

import com.example.gioia.eccezioni.*;
import com.example.gioia.entity.Carrello;
import com.example.gioia.entity.Cliente;
import com.example.gioia.entity.Ordine;
import com.example.gioia.entity.Prodotto;
import com.example.gioia.service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/carrello")
public class CarrelloController {
    @Autowired
    private CarrelloService carrelloService;

    @PostMapping()
    @PreAuthorize("hasRole('utente')")
    public ResponseEntity creaOrdine(@RequestBody Carrello carrello,@RequestParam String indirizzo){
        try{
            Ordine res= carrelloService.addOrdine(carrello, indirizzo);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (ProdottoInesistente | ProdottoInsufficiente | CarrelloErrato e){
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/addp")
    @PreAuthorize("hasRole('utente')")
    public ResponseEntity addp(@RequestParam int clienteId, @RequestBody Prodotto prodotto){
        try{
            Carrello res= carrelloService.addProdottiCarrello(clienteId, prodotto);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (UtenteNonTrovato | ProdottoInesistente | ProdottoErrato e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/removep")
    @PreAuthorize("hasRole('utente')")
    public ResponseEntity removep(@RequestParam int clienteId, @RequestParam int prodottoId){
        try{
            Carrello res= carrelloService.removeProdottiCarrello(clienteId, prodottoId);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (UtenteNonTrovato | ProdottoInesistente e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity getOrdini(){
        try{
            List<Ordine> res= carrelloService.getOrdini();
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (NessunOrdine e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cliente")
    public ResponseEntity getOrdiniCliente(@RequestParam int cliente){
        try{
            List<Ordine> res= carrelloService.mostraOrdiniCliente(cliente);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (UtenteNonTrovato | NessunOrdine e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/periodo")
    public ResponseEntity getOrdiniPeriodo(@RequestBody Cliente cliente, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end){
        try{
            List<Ordine> res= carrelloService.mostraOrdiniPeriodo(cliente,start,end);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (IntervalloErrato | NessunOrdine | UtenteNonTrovato e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/prodotto")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity getOrdiniProdotto(@RequestBody Prodotto prodotto){
        try{
            List<Ordine> res= carrelloService.mostraOrdiniProdotto(prodotto);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (ProdottoInesistente | NessunOrdine e) {
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/prodotti")
    public ResponseEntity getProdottiOrdine(@RequestParam int carrello){
        try{
            List<Prodotto> res= carrelloService.mostraProdottiOrdine(carrello);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (NessunOrdine e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("/carrello")
    @PreAuthorize("hasRole('utente')")
    public ResponseEntity getCarrello(@RequestParam int cliente){
        try{
            Carrello res= carrelloService.mostraCarrelloCliente(cliente);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch ( UtenteNonTrovato | ProdottoInesistente e){
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/zona")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity getZona(@RequestParam String città){
        try{
            List<Ordine> res= carrelloService.mostraPerZona(città);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (NessunOrdine e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ordine")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity getOrdine(@RequestParam int id){
        try{
            Ordine res= carrelloService.getOrdine(id);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (NessunOrdine e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }
}
