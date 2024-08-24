package com.example.gioia.service;

import com.example.gioia.eccezioni.IntervalloErrato;
import com.example.gioia.eccezioni.ProdottoInesistente;
import com.example.gioia.entity.Prodotto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.gioia.repositories.ProdottoRepository;


import java.util.*;

@Service
public class ProdottiService {
    @Autowired
    private ProdottoRepository prodottoRepository;
    //base
    @Transactional(readOnly = false)
    public Prodotto addProdotto(Prodotto prodotto) throws ProdottoInesistente {
        if(prodottoRepository.existsById(prodotto.getId())){
          Optional<Prodotto> p=prodottoRepository.findById(prodotto.getId());
          if (!p.get().equals(prodotto))
              throw new ProdottoInesistente("il prodotto ha dei valori errati");
            p.get().setDisponibilità((prodotto.getDisponibilità() + prodotto.getDisponibilità()));
            return p.get();
        }
        else
            return prodottoRepository.save(prodotto);
    }

    @Transactional(readOnly = false)
    public void removeProduct(Prodotto prodotto) throws ProdottoInesistente {
        if (prodottoRepository.existsById(prodotto.getId())) {
            Optional<Prodotto> p = prodottoRepository.findById(prodotto.getId());
            prodottoRepository.delete(p.get());
        }
        else
            throw new ProdottoInesistente("il prodotto che vuoi rimuovere non esiste");
    }

    @Transactional(readOnly = false)
    public Prodotto updateProduct(Prodotto prodotto) throws ProdottoInesistente {
        if(prodottoRepository.existsById(prodotto.getId())){
            Optional<Prodotto> p=prodottoRepository.findById(prodotto.getId());
            Prodotto pp = p.get();
            // Aggiorna l'entity esistente con i valori ricevuti dall'aggiornamento
            pp.setNome(prodotto.getNome());
            pp.setDescrizione(prodotto.getDescrizione());
            pp.setPrezzo(prodotto.getPrezzo());
            pp.setDisponibilità(prodotto.getDisponibilità());
            pp.setImmagine(prodotto.getImmagine());
            pp.setCategoria(prodotto.getCategoria());
            // Salva l'entity aggiornata nel database
            return prodottoRepository.save(pp);
        } else {
            throw new ProdottoInesistente("il prodotto che vuoi aggiornare non esiste");
        }
    }
    //utili
    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdotti(int numPag, int dimPag, String ordine) throws ProdottoInesistente {
        Pageable p= PageRequest.of(numPag,dimPag, Sort.by(ordine));
        Page<Prodotto> res= prodottoRepository.findAll(p);
        if(res.hasContent()){
            return res.getContent();
        }
        throw new ProdottoInesistente("non ci sono prodotti da visualizzare");

    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiPrezzo(int min, int max,int numPag, int dimPag, String ordine) throws IntervalloErrato, ProdottoInesistente {
        if(min>max)
            throw new IntervalloErrato("il prezzo minimo deve essere minore del massimo");
        else {
            Pageable p = PageRequest.of(numPag, dimPag, Sort.by(ordine));
            Page<Prodotto> res = prodottoRepository.findByIntervalloPrezzo(min, max, p);
            if (res.hasContent()) {
                return res.getContent();
            }
            else
                throw new ProdottoInesistente("nessun prodotto trovato");
        }
    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiCategoria(String categoria, int numPag, int dimPag, String ordine) throws ProdottoInesistente {
        Pageable p= PageRequest.of(numPag,dimPag, Sort.by(ordine));
        Page<Prodotto> res= prodottoRepository.findByCategoria(categoria,p);
        if(res.hasContent()){
            return res.getContent();
        }
        else
            throw new ProdottoInesistente("nessun prodotto trovato");
    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiNome(String nome, int numPag, int dimPag) throws ProdottoInesistente {
        Pageable pageable = PageRequest.of(numPag, dimPag);
        Page<Prodotto> paginaRisultato = prodottoRepository.findByParole(nome, pageable);
        Set<Prodotto> contenutoCombinato = new HashSet<>(paginaRisultato.getContent());
        List<String> listaParole = Arrays.asList(nome.split("\\s+"));
        for (String parola : listaParole) {
            Page<Prodotto> paginaParziale = prodottoRepository.findByParole(parola, pageable);
            if (paginaParziale.hasContent()) {
                contenutoCombinato.addAll(paginaParziale.getContent());
            }
        }
        if (!contenutoCombinato.isEmpty()) {
            return new ArrayList<>(contenutoCombinato);
        } else {
            throw new ProdottoInesistente("nessun prodotto trovato");
        }
    }

    @Transactional(readOnly = true)
    public Prodotto getProdotto(int id) throws ProdottoInesistente {
        Optional<Prodotto> p = prodottoRepository.findById(id);
        if (p.isPresent())
            return p.get();
        else{throw new ProdottoInesistente("il prodotto non esiste");}
    }

    @Transactional(readOnly = true)
    public List<Prodotto> getProdottiTerminati() throws ProdottoInesistente {
        List<Prodotto> p= prodottoRepository.findByDisponibilità(0);
        if (!p.isEmpty())
            return p;
        else{ throw new ProdottoInesistente("nessun prodotto terminato");}

    }


    @Transactional(readOnly = true)
    public List<Prodotto> getProdottifiltri(String parola, String categoria, int min, int max, int numPag, int dimPag) throws ProdottoInesistente, IntervalloErrato {
        List<Prodotto> p1= mostraProdottiNome(parola, numPag, dimPag);
        List<Prodotto> p2= mostraProdottiCategoria(categoria,numPag,dimPag, "id");
        List<Prodotto> p3= mostraProdottiPrezzo(min, max, numPag,dimPag,"id");
        List<Prodotto> p= new ArrayList<>();
        for(Prodotto pp : mostraProdotti(numPag,dimPag, "id")){
            if(p1.contains(pp) && p2.contains(pp) && p3.contains(pp))
                p.add(pp);
        }
        if (!p.isEmpty())
            return p;
        else{ throw new ProdottoInesistente("nessun prodotto trovato");}

    }

}
