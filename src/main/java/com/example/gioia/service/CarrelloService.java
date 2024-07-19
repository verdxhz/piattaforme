package com.example.gioia.service;

import com.example.gioia.entity.Carrello;
import com.example.gioia.entity.Cliente;

import com.example.gioia.entity.Prodotti_Carrello;
import com.example.gioia.entity.Prodotto;
import com.example.gioia.repositories.CarrelloRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.gioia.repositories.ClienteRepository;
import com.example.gioia.repositories.Prodotti_Carrello_Repository;

import com.example.gioia.repositories.ProdottoRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class CarrelloService {


    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    Prodotti_Carrello_Repository Prodotti_Carrello_repository;
    @Autowired
    CarrelloRepository carrelloRepository;
    @Autowired
    ProdottoRepository prodottoRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private Prodotti_Carrello_Repository prodotti_Carrello_Repository;


    //TODO add carrello ordine

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Carrello removeProdottiCarrello(int carrelloId, int prodId){ //throws QuantityBelowZeroException, NoProductException {
        Carrello optionalCarrello = carrelloRepository.findById(carrelloId);
        Carrello carrello = optionalCarrello;
        Optional<Prodotto> Prodotto = prodottoRepository.findById(prodId);
        if (Prodotto.isPresent()) {
            Prodotto prodotto = Prodotto.get();
            Prodotti_Carrello prodInCarr = prodotti_Carrello_Repository.findByCarrelloAndProdotto(carrello, prodotto);
            if (prodInCarr != null) {
                // Aggiorna la quantità nel carrello
                int newQuantity = prodInCarr.getQuantità() - 1;
                if (newQuantity < 0) {
                    //TODO throw new QuantityBelowZeroException("stai cercando di togliere "+quantity+" biglietti per "+prodotto.getName()+" ma ci sono solo "+prodInCarr.getQuantity()+" biglietti rimasti");
                }
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
        } else {
            //TODO throw new NoProductException("Prodotto non trovato con ID: " + prodId);
        }
        return carrello;
    }


    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Carrello setProdottiCarrello(int carrelloId, List<Prodotto> prodotti){//TODO throws QuantityBelowZeroException, PriceChangedException {
        Carrello carrello = carrelloRepository.findById(carrelloId);

        Cliente utente = carrello.getCliente();
        // Controlla se ci sono prodotti nel carrello con prezzo cambiato
        for (Prodotto p : prodotti) {
            if (p.getPrezzo() != prodottoRepository.findById(p.getId()).orElseThrow().getPrezzo()) {
                //TODO throw new PriceChangedException(p.getName() + "'s price changed from " + p.getPrice() + " euro to " + prodRepository.findById(p.getId()).orElseThrow().getPrice() + " euro.");
            }
        }
        // Aggiorna i prodotti nel carrello
        for (Prodotto p : prodotti) {
            Optional<Prodotto> optionalProdotto = prodottoRepository.findById(p.getId());
            if (optionalProdotto.isPresent()) {
                Prodotto prodotto = optionalProdotto.get();
                Prodotti_Carrello prodInCarr = Prodotti_Carrello_repository.findByCarrelloAndProdotto(carrello, prodotto);
                if (prodInCarr != null) {
                    // Aggiorna la quantità del prodotto nel carrello
                    if (prodotto.getDisponibilità() - 1< 0) {
                        //TODO throw new QuantityBelowZeroException("We are sorry: only " + prodotto.getQuantity() + " ticket(s) left for " + prodotto.getName());
                    }
                    prodInCarr.setQuantità(prodInCarr.getQuantità() + 1);
                    prodotti_Carrello_Repository.save(prodInCarr);
                    // Aggiorna la quantità disponibile del prodotto
                    prodotto.setDisponibilità(prodotto.getDisponibilità() - 1);
                    prodottoRepository.save(prodotto);
                } else {
                    // Aggiungi il prodotto al carrello
                    if (p.getDisponibilità() > 0) {
                        int newQuantity = prodotto.getDisponibilità() - 1;
                        if (newQuantity < 0) {
                           //TODO throw new QuantityBelowZeroException("We are sorry: only " + prodotto.getQuantity() + " ticket(s) left for " + prodotto.getName());
                        }
                        Prodotti_Carrello newProdInCarr = new Prodotti_Carrello();
                        newProdInCarr.setCarrello(carrello);
                        newProdInCarr.setProdotto(prodotto);
                        newProdInCarr.setQuantità(1);
                        prodotti_Carrello_Repository.save(newProdInCarr);
                        // Aggiorna la quantità disponibile del prodotto
                        prodotto.setDisponibilità(newQuantity);
                        prodottoRepository.save(prodotto);
                    }
                }
            }
        }
        return carrello;
    }






    @Transactional(readOnly = true)
    public List<Carrello> mostraOrdiniCliente(Cliente cliente){
        if(!clienteRepository.existsById(cliente.getId_cliente()))
            throw new RuntimeException();//TODO
        return carrelloRepository.findByclient(cliente.getId_cliente());
    }
    @Transactional(readOnly = true)
    public List<Carrello> mostraOrdiniPeriodo(Date start, Date end){
        if(start.compareTo(end) >= 0)
            throw new RuntimeException();
        return carrelloRepository.findByIntervalloTempo(start,end);
    }
    @Transactional(readOnly = true)
    public List<Carrello> mostraOrdiniProdotto(Prodotto prodotto){
        if(!prodottoRepository.existsById(prodotto.getId()))
            throw new RuntimeException();
        return Prodotti_Carrello_repository.findByProdotto(prodotto);
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
    public List<Carrello> getOrdini() {
        return carrelloRepository.findAll();
    }

}
