package com.example.gioia.service;

import com.example.gioia.eccezioni.*;
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
import java.util.Objects;


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
    public Carrello removeProdottiCarrello(int clienteId, int prodId) throws UtenteNonTrovato, ProdottoInesistente {
        if (!clienteRepository.existsById(clienteId))
            throw new UtenteNonTrovato("l'utente non esiste");//TODO chiedere se va tolto secondo me si
        else{
            Cliente cliente= clienteRepository.findById(clienteId).get();
            Carrello carrello = cliente.getCarrello();
            Prodotto prodotto = prodottoRepository.findById(prodId).get();
            Prodotti_Carrello prodInCarr = prodotti_Carrello_Repository.findByCarrelloAndProdotto(carrello, prodotto);
                if (prodInCarr != null) {
                    // Aggiorna la quantità nel carrello
                    int newQuantity = prodInCarr.getQuantità() - 1;
                    if (newQuantity == 0) {
                        prodotti_Carrello_Repository.delete(prodInCarr);
                        List<Prodotti_Carrello> p= carrello.getProdotti();
                        p.remove(prodInCarr);
                        carrello.setProdotti(p);
                        carrelloRepository.save(carrello);
                    } else {
                        prodInCarr.setQuantità(newQuantity);
                        prodotti_Carrello_Repository.save(prodInCarr);
                    }
                } else {
                     throw new ProdottoInesistente("Il prodotto non è presente nel carrello");
                }
            return carrello;
        }
    }

    @Transactional(readOnly = false)
    public Carrello addProdottiCarrello(int clienteId, Prodotto prod) throws UtenteNonTrovato, ProdottoInesistente, ProdottoErrato {
        if (!clienteRepository.existsById(clienteId))
            throw new UtenteNonTrovato("l'utente non esiste");//TODO chiedere se va tolto secondo me si
        else{
            Cliente cliente= clienteRepository.findById(clienteId).get();
            Carrello carrello = cliente.getCarrello();
            if(carrello==null){
                carrello=new Carrello();
                carrello.setCliente(cliente);
               carrelloRepository.save(carrello);
               cliente.setCarrello(carrello);
               clienteRepository.save(cliente);
            }
            Prodotto prodotto = prodottoRepository.findById(prod.getId()).get();
            if (!prodotto.equals(prod))
                throw new ProdottoErrato("il prodotto che vuoi inserire è stato modificato");
            Prodotti_Carrello prodInCarr = prodotti_Carrello_Repository.findByCarrelloAndProdotto(carrello, prodotto);
            if (prodInCarr == null) {
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
            List<Prodotti_Carrello> prodd= new ArrayList<>();
            if( carrello.getProdotti()!=null)
                prodd.addAll(carrello.getProdotti());
            prodd.add(prodInCarr);
            carrello.setProdotti(prodd);
            carrelloRepository.save(carrello);
            return carrello;
        }}

    @Transactional(readOnly = true)
    public Carrello mostraCarrelloCliente(int cliente) throws UtenteNonTrovato, ProdottoInesistente {
        if(!clienteRepository.existsById(cliente))
            throw new UtenteNonTrovato("l'utente non esiste");//TODO chiedere se va tolto secondo me si
        Carrello res=carrelloRepository.findByclient(cliente);
        if(!res.getProdotti().isEmpty()){
            return res;
        }
        else
            throw new ProdottoInesistente("non ci sono prodotti da visualizzare");
    }

    //ordini
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Ordine addOrdine(Carrello carrello, String indirizzo) throws ProdottoInesistente, ProdottoInsufficiente, CarrelloErrato {
        //controlli sul carrello
        if (!carrelloRepository.existsById(carrello.getId()))
            throw new CarrelloErrato("il carrello non esiste");
        Carrello carrello2 = carrelloRepository.findById(carrello.getId());
        if(!carrello.equals(carrello2))
            throw new CarrelloErrato("il carrello è stato modificato ");
        //controlli prodotti
        List<Prodotti_Carrello> prodotti=carrello.getProdotti();
        for(Prodotti_Carrello pc:prodotti){
            if(!prodottoRepository.existsById(pc.getProdotto().getId()))
                throw new ProdottoInesistente("il prodotto da comprare non esiste");
            if (pc.getQuantità()> prodottoRepository.findById(pc.getProdotto().getId()).get().getDisponibilità())
               throw new ProdottoInsufficiente("il prodotto"+ pc.getProdotto().getNome().toString()+"è insufficiente");
        }
        //ordine
        for(Prodotti_Carrello pc:prodotti){
            Prodotto p=pc.getProdotto();
            p.setDisponibilità(p.getDisponibilità()-pc.getQuantità());
            prodottoRepository.save(p);
        }
        Cliente cliente= carrello.getCliente();
        System.out.println(cliente);
        Ordine ordine= new Ordine();
        ordine.setCarrello(carrello);
        ordine.setData(LocalDateTime.now());
        ordine.setIndirizzo(indirizzo);
        ordine.setCliente(cliente);
        ordineRepository.save(ordine);
        carrello=new Carrello();
        carrello.setCliente(cliente);
        carrelloRepository.save(carrello);
        cliente.setCarrello(carrello);
        List<Ordine> ordini= cliente.getOrdini();
        if(ordini==null)
            ordini= new ArrayList<>();
        ordini.add(ordine);
        cliente.setOrdini(ordini);
        clienteRepository.save(cliente);
        return ordine;
    }

    @Transactional(readOnly = true)
    public List<Ordine> mostraOrdiniCliente(int cliente) throws UtenteNonTrovato, NessunOrdine {
        if(!clienteRepository.existsById(cliente))
            throw new UtenteNonTrovato("l'utente non esiste");//TODO chiedere se va tolto secondo me si
        List<Ordine> res=new ArrayList<>(ordineRepository.findByclient(cliente));
        if (res.isEmpty())
            throw new NessunOrdine("non ci sono ordini del cliente");
        return res;
    }

    @Transactional(readOnly = true)
    public List<Ordine> mostraOrdiniPeriodo(Cliente cliente, LocalDateTime start, LocalDateTime end) throws IntervalloErrato, NessunOrdine, UtenteNonTrovato {
        if(start.compareTo(end) >= 0)
            throw new IntervalloErrato("la data di inizio non deve superare quella di fine");
        List<Ordine> res = new ArrayList<>();
        if(cliente == null){
            for (Cliente c: clienteRepository.findAll())
                res.addAll(ordineRepository.findByIntervalloTempo(cliente.getId_cliente(),start,end));
            if (res.isEmpty())
                throw new NessunOrdine("non ci sono ordini nel periodo indicato");
            return res;
        }
        else{
            if(!clienteRepository.existsById(cliente.getId_cliente()))
                throw new UtenteNonTrovato("l'utente non esiste");//TODO chiedere se va tolto secondo me si
             res=ordineRepository.findByIntervalloTempo(cliente.getId_cliente(),start,end);
            if (res.isEmpty())
                throw new NessunOrdine("non ci sono ordini nel periodo indicato");
             return res;
        }
    }

    @Transactional(readOnly = true)
    public List<Ordine> mostraOrdiniProdotto(Prodotto prodotto) throws ProdottoInesistente, NessunOrdine {
        if(!prodottoRepository.existsById(prodotto.getId()))
            throw new ProdottoInesistente("il prodotto non esiste");
        List<Ordine> res=new ArrayList<>(ordineRepository.findByProdotto(prodotto));
        if (res.isEmpty())
            throw new NessunOrdine("non ci sono ordini del prodotto");
        return res;
    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiOrdine(int ordine) throws NessunOrdine {
        if(!ordineRepository.existsById(ordine))
            throw new NessunOrdine("l'ordine non esiste");
        List<Prodotto> res= new ArrayList<>();
        for (Prodotti_Carrello p : ordineRepository.findById(ordine).getCarrello().getProdotti())
            res.add(p.getProdotto());
        return res;
    }

    @Transactional(readOnly = true)
    public List<Ordine> mostraPerZona(String città) throws NessunOrdine {
        List<Ordine> res=new ArrayList<>(ordineRepository.findByCitta(città));
        if (res.isEmpty()) throw new NessunOrdine("non ci sono ordini nella zona");
        return res;
    }

    @Transactional(readOnly = true)
    public List<Ordine> getOrdini() throws NessunOrdine {
        List<Ordine> res=new ArrayList<>(ordineRepository.findAll());
        if (res.isEmpty())
            throw new NessunOrdine("non ci sono ordini nel negozio");
        return res;
    }

    @Transactional(readOnly = true)
    public Ordine getOrdine(int id) throws NessunOrdine {
        if (!ordineRepository.existsById(id))
            throw new NessunOrdine("non esiste l'ordine");
        return ordineRepository.findById(id);
    }

}


