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
    //base
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

    @Transactional(readOnly = false)
    public void removeProduct(Prodotto prodotto) {  // throws NoProductException {
        if (prodottoRepository.existsById(prodotto.getId())) {
            Optional<Prodotto> p = prodottoRepository.findById(prodotto.getId());
            prodottoRepository.delete(p.get());
        }
        //TODO else throw new NoProductException();
    }

    @Transactional(readOnly = false)
    public Prodotto updateProduct(Prodotto prodotto){// throws NoProductException {
        if(prodottoRepository.existsById(prodotto.getId())){
            Optional<Prodotto> p=prodottoRepository.findById(prodotto.getId());
            Prodotto pp = p.get();
            // Aggiorna l'entity esistente con i valori ricevuti dall'aggiornamento
            pp.setNome(prodotto.getNome());
            pp.setDescrizione(prodotto.getDescrizione());
            pp.setPrezzo(prodotto.getPrezzo());
            pp.setDisponibilità(prodotto.getDisponibilità());
            // Salva l'entity aggiornata nel database
            return prodottoRepository.save(pp);
        } else {
            //TODO throw new NoProductException("Prodotto non trovato");
            return null;//TODO rmuovi riga e metti eccezione
        }
    }
    //utili
    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdotti(int numPag, int dimPag, String ordine){
        Pageable p= PageRequest.of(numPag,dimPag, Sort.by(ordine));
        Page<Prodotto> res= prodottoRepository.findAll(p);
        if(res.hasContent()){
            return res.getContent();
        }
        //TODO controlla ci siano prodotti nel database
        return null;
    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiPrezzo(int min, int max,int numPag, int dimPag, String ordine){
        if(min>max)
            //TODO
            return null;
        Pageable p= PageRequest.of(numPag,dimPag, Sort.by(ordine));
        Page<Prodotto> res= prodottoRepository.findByIntervalloPrezzo(min, max, p);
        if(res.hasContent()){
            return res.getContent();
        }
        //TOdO nulla rientra in questa fascia, controlla che min sia minore di max
        return null;
    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiCategoria(String categoria, int numPag, int dimPag, String ordine){
        Pageable p= PageRequest.of(numPag,dimPag, Sort.by(ordine));
        Page<Prodotto> res= prodottoRepository.findByCategoria(categoria,p);
        if(res.hasContent()){
            return res.getContent();
        }
        //TODO La categoria è vuota
        return null;
    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiNome(String nome,int numPag, int dimPag){
        Pageable p= PageRequest.of(numPag,dimPag);
        Page<Prodotto> res= prodottoRepository.findByParole(nome,p);
        if(res.hasContent()){
            return res.getContent();
        }
        return null;//TODO nessun prodotto si chiama così, List<String> paroleList = Arrays.asList(parole.split("\\s+"));
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
    public List<Prodotto> getProdottiTerminati(){// throws NoProductException {
        List<Prodotto> p= prodottoRepository.findByDisponibilità(0);
        if (!p.isEmpty())
            return p;
        else{return null;}
        //TODO tutti sono disponibili;
    }


}
