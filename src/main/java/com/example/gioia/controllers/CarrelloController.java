package com.example.gioia.controllers;

import com.example.gioia.entity.Carrello;
import com.example.gioia.entity.Cliente;
import com.example.gioia.entity.Prodotto;
import com.example.gioia.service.CarrelloService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/carrello")
public class CarrelloController {
    @Autowired
    private CarrelloService carrelloService;


   // @PostMapping()
    //@RolesAllowed({"amm","cliente"})
 /*   TODO public ResponseEntity creaOrdine(@RequestBody @Valid Carrello ordine){
        try{
            Carrello res= carrelloService.(ordine);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }
*/
    @PutMapping("/addp")
    //@PreAuthorize("hasRole=cliente")

    public Carrello addp(@PathVariable int carrelloId, @PathVariable List<Prodotto> prodotti){
        return carrelloService.setProdottiCarrello(carrelloId, prodotti);
    }

    @PutMapping("/removep")
    //@PreAuthorize("hasRole=cliente")

    public Carrello removep(@PathVariable int carrelloId, @PathVariable int prodottoId){
        return carrelloService.removeProdottiCarrello(carrelloId, prodottoId);
    }


    @GetMapping("/all")
    //@PreAuthorize("hasRole=amm")
    public List<Carrello> getOrdini(){
        return carrelloService.getOrdini();
    }


    @GetMapping("/cliente")
    //@PreAuthorize("hasRole=all")
    public List<Carrello> getOrdiniCliente(@PathVariable Cliente cliente){
        return carrelloService.mostraOrdiniCliente(cliente);
    }


    @GetMapping("/periodo")
    //@PreAuthorize("hasRole=amm")
    public List<Carrello> getOrdiniPeriodo(@PathVariable Date start, @PathVariable Date end){
        return carrelloService.mostraOrdiniPeriodo(start,end);
    }


    @GetMapping("/prodotto")
    //@PreAuthorize("hasRole=amm")

    public List<Carrello> getOrdiniProdotto(@PathVariable Prodotto prodotto){
        return carrelloService.mostraOrdiniProdotto(prodotto);
    }

    @GetMapping("/prodotti")
    //@PreAuthorize("hasRole=amm")

    public List<Prodotto> getProdottiOrdine(@PathVariable Carrello carrello){
        return carrelloService.mostraProdottiOrdine(carrello);
    }
}
