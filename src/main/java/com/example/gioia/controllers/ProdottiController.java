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

    @GetMapping("/one")
    //@PreAuthorize("hasRole=all")
    public ResponseEntity getProductByID(@RequestParam int id) {
        try{
            Prodotto res= prodottiService.getProdotto(id);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    //@PreAuthorize("hasRole=all")
    public List<Prodotto> getProdotti(@RequestParam(required = false, defaultValue = "0")int numPag, @RequestParam(required = false, defaultValue = "25") int dimPag, @RequestParam String ordine) {
        return prodottiService.mostraProdotti(numPag,dimPag,ordine);
    }

    @GetMapping("/nome")
    //@PreAuthorize("hasRole=all")
    public ResponseEntity getProdottiNome(@RequestParam String nome,@RequestParam int numPag, @RequestParam int dimPag) {
        try{
            List<Prodotto> res= prodottiService.mostraProdottiNome(nome,numPag,dimPag);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/categoria")
    //@PreAuthorize("hasRole=all")
    public ResponseEntity getProdottiCategoria(@RequestParam String categoria,@RequestParam int numPag, @RequestParam int dimPag, @RequestParam String ordine) {
        try{
            List<Prodotto> res= prodottiService.mostraProdottiCategoria(categoria,numPag,dimPag,ordine);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/prezzo")
    //@PreAuthorize("hasRole=all")
    public ResponseEntity getProdottiPrezzo(@RequestParam int min, @RequestParam int max,@RequestParam int numPag, @RequestParam int dimPag, @RequestParam String ordine) {
        try{
            List<Prodotto> res= prodottiService.mostraProdottiPrezzo(min, max,numPag,dimPag,ordine);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/terminati")
    //PreAuthorize("hasRole=amm")
    public ResponseEntity getTerminati(){
        try{
            List<Prodotto> res= prodottiService.getProdottiTerminati();
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }
}
