package com.example.gioia.controllers;

import com.example.gioia.entity.Prodotto;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.gioia.service.ProdottiService;

import java.util.List;

@RestController
@RequestMapping("/prodotto")
public class ProdottiController {
    @Autowired
    private ProdottiService prodottiService;


    @PostMapping()
    //@PreAuthorize("hasRole=amm")
    public ResponseEntity creaProdotto(@RequestBody Prodotto prodotto) {
        try{
            Prodotto res= prodottiService.addProdotto(prodotto);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
   // @PreAuthorize("hasRole=amm")
    public ResponseEntity rimuoviProdotto(@RequestBody Prodotto prodotto) {
        try{
            prodottiService.removeProduct(prodotto);
            return new ResponseEntity(HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update")
    //@PreAuthorize("hasRole=amm")
    public ResponseEntity aggiornaProdotto(@RequestBody Prodotto prodotto) {
        try{
            Prodotto res= prodottiService.updateProduct(prodotto);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    public Prodotto getProductByID(@RequestParam int id) {
        return prodottiService.getProdotto(id);
    }

    @GetMapping("/prodotto/all")
    //@PreAuthorize("hasRole=all")
    public List<Prodotto> getProdotti(@RequestParam(required = false, defaultValue = "0")int numPag, @RequestParam(required = false, defaultValue = "25") int dimPag, @PathVariable String ordine) {
        return prodottiService.mostraProdotti(numPag,dimPag,ordine);
    }


    @GetMapping("/prodotto/nome")
    //@PreAuthorize("hasRole=all")

    public List<Prodotto> getProdottiNome(@PathVariable String nome,@PathVariable int numPag, @PathVariable int dimPag) {
        return prodottiService.mostraProdottiNome(nome,numPag,dimPag);
    }


    @GetMapping("/prodotto/categoria")
    //@PreAuthorize("hasRole=all")


    public List<Prodotto> getProdottiCategoria(@PathVariable String categoria,@PathVariable int numPag, @PathVariable int dimPag, @PathVariable String ordine) {
        return prodottiService.mostraProdottiCategoria(categoria,numPag,dimPag,ordine);
    }


    @GetMapping("/prodotto/prezzo")
    //@PreAuthorize("hasRole=all")

    public List<Prodotto> getProdottiPrezzo(@PathVariable int min, @PathVariable int max,@PathVariable int numPag, @PathVariable int dimPag, @PathVariable String ordine) {
        return prodottiService.mostraProdottiPrezzo(min, max,numPag,dimPag,ordine);
    }

}
