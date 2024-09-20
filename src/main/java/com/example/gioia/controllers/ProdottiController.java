package com.example.gioia.controllers;

import com.example.gioia.eccezioni.IntervalloErrato;
import com.example.gioia.eccezioni.ProdottoInesistente;
import com.example.gioia.entity.Prodotto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.gioia.service.ProdottiService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8000")
@RestController
@RequestMapping("/prodotto")
public class ProdottiController {
    @Autowired
    private ProdottiService prodottiService;

    @PostMapping("/crea")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity creaProdotto(@RequestBody Prodotto prodotto) {
        try{
            Prodotto res= prodottiService.addProdotto(prodotto);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (ProdottoInesistente e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity rimuoviProdotto(@RequestBody Prodotto prodotto) {
        try{
            prodottiService.removeProduct(prodotto);
            return new ResponseEntity(HttpStatus.OK);
        }catch (ProdottoInesistente e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity aggiornaProdotto(@RequestBody Prodotto prodotto) {
        try{
            Prodotto res= prodottiService.updateProduct(prodotto);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (ProdottoInesistente e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/one")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity getProductByID(@RequestParam int id) {
        try{
            Prodotto res= prodottiService.getProdotto(id);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (ProdottoInesistente e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity getProdotti(@RequestParam(required = false, defaultValue = "0")int numPag, @RequestParam(required = false, defaultValue = "25") int dimPag, @RequestParam(required = false, defaultValue = "id") String ordine) {
        try{
            List<Prodotto> res= prodottiService.mostraProdotti(numPag,dimPag,ordine);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (ProdottoInesistente e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/nome")
    public ResponseEntity getProdottiNome(@RequestParam String nome,@RequestParam(required = false, defaultValue = "0") int numPag, @RequestParam(required = false, defaultValue = "25") int dimPag) {
        try{
            List<Prodotto> res= prodottiService.mostraProdottiNome(nome,numPag,dimPag);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (ProdottoInesistente e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/categoria")
    public ResponseEntity getProdottiCategoria(@RequestParam String categoria,@RequestParam(required = false, defaultValue = "0") int numPag, @RequestParam(required = false, defaultValue = "25") int dimPag, @RequestParam(required = false, defaultValue = "nome") String ordine) {
        try{
            List<Prodotto> res= prodottiService.mostraProdottiCategoria(categoria,numPag,dimPag,ordine);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (ProdottoInesistente e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/prezzo")
    public ResponseEntity getProdottiPrezzo(@RequestParam int min, @RequestParam int max,@RequestParam(required = false, defaultValue = "0") int numPag, @RequestParam(required = false, defaultValue = "25") int dimPag, @RequestParam(required = false, defaultValue = "nome") String ordine) {
        try{
            List<Prodotto> res= prodottiService.mostraProdottiPrezzo(min, max,numPag,dimPag,ordine);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (IntervalloErrato | ProdottoInesistente e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/terminati")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity getTerminati(){
        try{
            List<Prodotto> res= prodottiService.getProdottiTerminati();
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (ProdottoInesistente e){
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/filtri")
    public ResponseEntity getfiltri(String parola, String categoria, int min, int max,@RequestParam(required = false, defaultValue = "0") int numPag,@RequestParam(required = false, defaultValue = "25") int dimPag){
        try{
            List<Prodotto> res= prodottiService.getProdottifiltri(parola,categoria,min,max,numPag,dimPag);
            return new ResponseEntity(res, HttpStatus.OK);
        }catch (ProdottoInesistente | IntervalloErrato e){
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }


}
