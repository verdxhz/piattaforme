package com.example.gioia.service;

import com.example.gioia.entity.*;

import com.example.gioia.repositories.CarrelloRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.gioia.repositories.ClienteRepository;
import com.example.gioia.repositories.Prodotti_Carrello_Repository;

import com.example.gioia.repositories.ProdottoRepository;
import com.example.gioia.repositories.OrdineRepository;


import java.time.LocalDateTime;

import java.util.ArrayList;

import java.util.List;



@Service
public class CarrelloService {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    Prodotti_Carrello_Repository Prodotti_Carrello_repository;
    @Autowired
    CarrelloRepository carrelloRepository;
    @Autowired
    OrdineRepository ordineRepository;
    @Autowired
    ProdottoRepository prodottoRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private Prodotti_Carrello_Repository prodotti_Carrello_Repository;
    //carrello
    @Transactional(readOnly = false)
    public Carrello removeProdottiCarrello(int carrelloId, int prodId){ //throws QuantityBelowZeroException, NoProductException {
        if (!carrelloRepository.existsById(carrelloId) || !prodottoRepository.existsById(prodId))
        {//TODO eccezione
            return null; }
        Carrello carrello = carrelloRepository.findById(carrelloId);
        Prodotto prodotto = prodottoRepository.findById(prodId).get();
        Prodotti_Carrello prodInCarr = prodotti_Carrello_Repository.findByCarrelloAndProdotto(carrello, prodotto);
            if (prodInCarr != null) {
                // Aggiorna la quantità nel carrello
                int newQuantity = prodInCarr.getQuantità() - 1;
                if (newQuantity == 0) {
                    prodotti_Carrello_Repository.delete(prodInCarr);
                } else {
                    prodInCarr.setQuantità(newQuantity);
                    prodotti_Carrello_Repository.save(prodInCarr);
                }
                // Aggiorna la quantità disponibile del prodotto
                prodotto.setDisponibilità(prodotto.getDisponibilità() + 1);
                prodottoRepository.save(prodotto);
            } else {
                // TODO throw new NoProductException("Il prodotto non è presente nel carrello");
            }
        return carrello;
    }

    @Transactional(readOnly = false)
    public Carrello addProdottiCarrello(int carrelloId, int prodId){//TODO throws QuantityBelowZeroException, PriceChangedException {
        if (!carrelloRepository.existsById(carrelloId) || !prodottoRepository.existsById(prodId))
        {//TODO eccezione
            return null; }
        Carrello carrello = carrelloRepository.findById(carrelloId);
        Prodotto prodotto = prodottoRepository.findById(prodId).get();
        Prodotti_Carrello prodInCarr = prodotti_Carrello_Repository.findByCarrelloAndProdotto(carrello, prodotto);
        if (prodInCarr != null) {
            prodInCarr= new Prodotti_Carrello();
            prodInCarr.setCarrello(carrello);
            prodInCarr.setProdotto(prodotto);
            prodInCarr.setQuantità(1);
            prodotti_Carrello_Repository.save(prodInCarr);
        }
        else{
            prodInCarr.setQuantità(prodInCarr.getQuantità()+1);
            prodotti_Carrello_Repository.save(prodInCarr);
        }
        if(prodotto.getDisponibilità()==0){//TODO eccezione
            return null;
        }
        prodotto.setDisponibilità(prodotto.getDisponibilità()-1);
        prodottoRepository.save(prodotto);
        List<Prodotti_Carrello> prod= new ArrayList<>(carrello.getProdotti());
        prod.add(prodInCarr);
        carrello.setProdotti(prod);
        carrelloRepository.save(carrello);
        return carrello;
    }//TODO far vedere andrea

    @Transactional(readOnly = true)
    public List<Carrello> mostraCarrelloCliente(Cliente cliente){
        if(!clienteRepository.existsById(cliente.getId_cliente()))
            throw new RuntimeException();//TODO
        List<Carrello> res=new ArrayList<>(carrelloRepository.findByclient(cliente.getId_cliente()));
        return res;
    }

    //ordini
    @Transactional(readOnly = false)
    public Ordine addOrdine(Carrello carrello, String indirizzo){
        if (!carrelloRepository.existsById(carrello.getId()))
        {//TODO eccezione
            return null; }
        Ordine ordine= new Ordine();
        ordine.setCarrello(carrello);
        ordine.setData(LocalDateTime.now());
        ordine.setIndirizzo(indirizzo);
        ordineRepository.save(ordine);
        carrello.setProdotti(new ArrayList<>());
        carrelloRepository.save(carrello);
        return ordine;
    }

    @Transactional(readOnly = true)
    public List<Ordine> mostraOrdiniCliente(Cliente cliente){
        if(!clienteRepository.existsById(cliente.getId_cliente()))
            throw new RuntimeException();//TODO
        List<Ordine> res=new ArrayList<>(ordineRepository.findByclient(cliente.getId_cliente()));
        return res;
    }

    @Transactional(readOnly = true)
    public List<Ordine> mostraOrdiniPeriodo(Cliente cliente, LocalDateTime start, LocalDateTime end){
        if(start.compareTo(end) >= 0)
            throw new RuntimeException();//TODO
        List<Ordine> res = new ArrayList<>();
        if(cliente == null){
            for (Cliente c: clienteRepository.findAll())
                res.addAll(ordineRepository.findByIntervalloTempo(cliente.getId_cliente(),start,end));
            return res;
        }
        else{
            if(!clienteRepository.existsById(cliente.getId_cliente()))
                throw new RuntimeException();//TODO
             res=ordineRepository.findByIntervalloTempo(cliente.getId_cliente(),start,end);
             return res;
        }
    }

    @Transactional(readOnly = true)
    public List<Ordine> mostraOrdiniProdotto(Prodotto prodotto){
        if(!prodottoRepository.existsById(prodotto.getId()))
            throw new RuntimeException();
        List<Ordine> res=new ArrayList<>(ordineRepository.findByProdotto(prodotto));
        return res;
    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiOrdine(Carrello carrello){
        if(!carrelloRepository.existsById(carrello.getId()))
            throw new RuntimeException();
        List<Prodotto> res= new ArrayList<>();
        for (Prodotti_Carrello p : carrello.getProdotti())
            res.add(p.getProdotto());
        return res;
    }

    @Transactional(readOnly = true)
    public List<Ordine> mostraPerZona(String città){
        List<Ordine> res=new ArrayList<>(ordineRepository.findByCitta(città));
        return res;
    }

    @Transactional(readOnly = true)
    public List<Ordine> getOrdini(){
        List<Ordine> res=new ArrayList<>(ordineRepository.findAll());
        return res;
    }

    @Transactional(readOnly = true)
    public Ordine getOrdine(int id) {
        return ordineRepository.findById(id);
    }

}


