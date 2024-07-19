package com.example.gioia.service;

import com.example.gioia.entity.Prodotto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.gioia.repositories.ProdottoRepository;


import java.util.List;
import java.util.Optional;

@Service
public class ProdottiService {
    @Autowired
    private ProdottoRepository prodottoRepository;

    @Transactional(readOnly = false)
    public Prodotto addProdotto(Prodotto prodotto) {
        if(prodottoRepository.existsById(prodotto.getId())){
            Optional<Prodotto> p=prodottoRepository.findById(prodotto.getId());
            p.get().setDisponibilità((prodotto.getDisponibilità() + prodotto.getDisponibilità()));
            return p.get();
        }
        else
            return prodottoRepository.save(prodotto);
    }


    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdotti(int numPag, int dimPag, String ordine){
        Pageable p= PageRequest.of(numPag,dimPag, Sort.by(ordine));
        Page<Prodotto> res= prodottoRepository.findAll(p);
        if(res.hasContent()){
            return res.getContent();
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiPrezzo(int min, int max,int numPag, int dimPag, String ordine){
        Pageable p= PageRequest.of(numPag,dimPag, Sort.by(ordine));
        Page<Prodotto> res= prodottoRepository.findByIntervalloPrezzo(min, max, p);
        if(res.hasContent()){
            return res.getContent();
        }
        return null;
    }
    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiCategoria(String categoria, int numPag, int dimPag, String ordine){
        Pageable p= PageRequest.of(numPag,dimPag, Sort.by(ordine));
        Page<Prodotto> res= prodottoRepository.findByCategoria(categoria,p);
        if(res.hasContent()){
            return res.getContent();
        }
        return null;
    }
    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiNome(String nome,int numPag, int dimPag){
        Pageable p= PageRequest.of(numPag,dimPag);
        Page<Prodotto> res= prodottoRepository.findByNameContainingIgnoreCaseOrderByNameAsc(nome,p);
        if(res.hasContent()){
            return res.getContent();
        }
        return null;
    }

    @Transactional(readOnly = false)
    public void removeProduct(Prodotto prodotto) {  // throws NoProductException {
        if (prodottoRepository.existsById(prodotto.getId())) {
            Optional<Prodotto> p = prodottoRepository.findById(prodotto.getId());
            prodottoRepository.delete(p.get());
            //TODO else throw new NoProductException();
        }
        //TODO else throw new NoProductException();
    }
    @Transactional(readOnly = false)
    public Prodotto updateProduct(Prodotto updatedProduct){// throws NoProductException {
        Optional<Prodotto> existingProductOptional = prodottoRepository.findById(updatedProduct.getId());
        if (existingProductOptional.isPresent()) {
            Prodotto existingProduct = existingProductOptional.get();
            // Aggiorna l'entity esistente con i valori ricevuti dall'aggiornamento
            existingProduct.setNome(updatedProduct.getNome());
            existingProduct.setDescrizione(updatedProduct.getDescrizione());
            existingProduct.setPrezzo(updatedProduct.getPrezzo());
            existingProduct.setDisponibilità(updatedProduct.getDisponibilità());
            // Salva l'entity aggiornata nel database
            return prodottoRepository.save(existingProduct);
        } else {
            //TODO throw new NoProductException("Prodotto non trovato");
            return null;//
        }
    }

    @Transactional(readOnly = true)
    public Prodotto getProdotto(int id){// throws NoProductException {
        Optional<Prodotto> p = prodottoRepository.findById(id);
        if (p.isPresent())
            return p.get();
        else{return null;}
           //TODO throw new NoProductException("prodotto inesistente");
    }
    @Transactional(readOnly = true)
    public List<Prodotto> getProductFilters(int page, int limit, String name, Integer min, Integer max, String categoria, Sort.Direction sortType){//TODO
        return null;
    }

}
