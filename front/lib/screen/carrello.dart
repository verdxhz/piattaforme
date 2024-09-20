import 'dart:convert';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:gioiafront/entity/Carrello.dart';
import 'package:gioiafront/entity/Cliente.dart';
import 'package:gioiafront/entity/Ordine.dart';
import 'package:gioiafront/entity/Prodotti_Carrello.dart';
import 'package:toastification/toastification.dart';

import '../entity/Prodotto.dart';

class CarrelloPage extends StatefulWidget {
  const CarrelloPage({super.key});

  @override
  State<CarrelloPage> createState() => _CarrelloState();
}

class _CarrelloState extends State<CarrelloPage> {
  Future<List<Prodotto>> prodotti = Future(() => []);
  late Future<Carrello> carrello;
  late Carrello cc;
  TextEditingController _indirizzocontroller= TextEditingController();

  @override
  void initState() {
    super.initState();
    getc();
  }

  void getc() async {
    carrello = CarrelloService().getCarrello();
    cc = await carrello;
    // debugPrint('${cc.id}');

   getp(cc.id);
  }

  void getp(int id) {
    setState(() {
     // lineeordine = cc.prodotti;
      prodotti = CarrelloService().getProdottiCarrello(id);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: Container(
            decoration: BoxDecoration(
              border: Border.all(color: Colors.black, width: 2.0),
            ),
            margin: const EdgeInsets.all(16.0),
            child: Expanded(
              child: FutureBuilder<List<Prodotto>>(
                future: prodotti,
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.waiting) {
                    return const Center(child: CircularProgressIndicator());
                  } else if (snapshot.hasError) {
                    return Center(child: Text('Carrello vuoto'));
                    // return Center(child: Text('Error: ${snapshot.error}'));
                  } else if (snapshot.hasData) {
                    return ListView.builder(
                      itemCount: snapshot.data!.length,
                      itemBuilder: (context, index) {
                        Prodotto prodotto = snapshot.data![index];
                        return Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Container(
                            decoration: BoxDecoration(
                              color: Colors.white,
                              border: Border.all(
                                color: const Color.fromRGBO(1, 1, 1, 0.1),
                              ),
                            ),
                            child: Row(
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                Image.asset(
                                  prodotto.immagine,
                                  height: 100,
                                  width: 100,
                                  fit: BoxFit.cover,
                                ),
                                const SizedBox(width: 100),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.center,
                                    children: [
                                      Text(
                                        prodotto.nome,
                                        style: const TextStyle(
                                            fontSize: 18,
                                            fontWeight: FontWeight.bold),
                                      ),
                                      const SizedBox(height: 5),
                                      Text(prodotto.descrizione),
                                    ],
                                  ),
                                ),
                                const SizedBox(width: 20),
                                Column(
                                  children: [
                                    Text(
                                      '${prodotto.prezzo.toString()} €',
                                      style: const TextStyle(
                                        fontSize: 18,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                    Row(
                                      children: [
                                        IconButton(
                                            onPressed: () {
                                              setState(
                                                () {
                                                  rimuovi(prodotto);
                                                },
                                              );
                                            },
                                            icon: Icon(Icons.remove)),
                                        Text(quantita(prodotto.id)),
                                        IconButton(
                                            onPressed: () {
                                              setState(() {
                                                aggiungi(prodotto);
                                              });
                                            },
                                            icon: Icon(Icons.add)),
                                      ],
                                    )
                                  ],
                                ),
                              ],
                            ),
                          ),
                        );
                      },
                    );
                  } else {
                    return const Center(child: Text('Carrello vuoto '));
                  }
                },
              ),
            ),
          ),
        ),
        Column(
          children: [
            SizedBox(
              height: 15.00,
            ),
            Container(
              width: 300,
              height: 200,
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(30.0),
                color: const Color.fromARGB(100, 81, 87, 100),
              ),
              child: Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      totaleconto(),
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 18,
                        fontStyle: FontStyle.italic,
                      ),
                    ),
                    TextButton(
                      onPressed: () {
                        mostraMessaggioDialog(context);
                      },
                      child: Text(
                        'ACQUISTA ORA',
                        style: TextStyle(
                          color: Colors.white,
                        ),
                      ),
                      style: TextButton.styleFrom(
                        backgroundColor: Color.fromARGB(120, 81, 87, 100),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
        SizedBox(
          width: 15.00,
        ),
      ],
    );
  }

  String quantita(int id) {
    int q = 0;
    for (Prodotti_Carrello p in cc.prodotti) {
      if (p.prodotto.id == id)
        q = p.quantita;
    }
    return '$q';
  }

  void aggiungi(Prodotto prodotto) async {
    int q = 0;
    for (Prodotti_Carrello p in cc.prodotti) {
      if (p.prodotto.id == prodotto.id)
        q = p.quantita;
    }
    if((q+1)>prodotto.disponibilita)
      {
        toastification.show(
          context: context,
          type: ToastificationType.warning,
          style: ToastificationStyle.minimal,
          title: Text("Errore nel carrello"),
          description: Text("hai superato la disponibilità del prodotto"),
          alignment: Alignment.centerRight,
          autoCloseDuration: const Duration(seconds: 4),
          primaryColor: Color(0xff000000),
          borderRadius: BorderRadius.circular(4.0),
          boxShadow: highModeShadow,
          showProgressBar: false,
          applyBlurEffect: true,
        );
      }
    else{
    await CarrelloService().aggiungiCarrello(prodotto);
    getc();}
  }

  void rimuovi(Prodotto prodotto) async {
    await CarrelloService().rimuoviCarrello(prodotto.id);
    getc();
  }

  String getPrezzoBloccato(int id) {//questo serve più per gli ordini quindi in creaordine mettere metodo che blocca il prezzo e toglilo da addp
    double prezzo = 0;
    for (Prodotti_Carrello p in cc.prodotti) {
      if (p.prodotto.id == id)
        prezzo = p.prezzo;
    }
    return '$prezzo';
  }

  String totaleconto() {
    double conto = 0;
    for (Prodotti_Carrello p in cc.prodotti) {
      conto += (p.quantita * p.prezzo);
    }
    return 'totale=$conto €';
  }

  void mostraMessaggioDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          backgroundColor: const Color.fromARGB(255, 255, 255, 255),
          title: const Text(
            "Conferma",
            textAlign: TextAlign.center,
          ),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Text(
                "Vuoi confermare questo ordine?",
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 20),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  TextButton(
                    child: const Text(
                      "Conferma",
                      style: TextStyle(
                        color: Colors.white,
                      ),
                    ),
                    onPressed: () {
                      // Chiude la prima dialog e, quando è chiusa, apre la seconda
                      Navigator.of(context).pop();
                      _mostraIndirizzoDialog(context);
                    },
                    style: TextButton.styleFrom(
                      backgroundColor: const Color.fromARGB(120, 81, 87, 100),
                    ),
                  ),
                  const SizedBox(width: 20),
                  TextButton(
                    child: const Text(
                      "Chiudi",
                      style: TextStyle(
                        color: Colors.white,
                      ),
                    ),
                    onPressed: () {
                      Navigator.of(context).pop(); // Chiude il dialog
                    },
                    style: TextButton.styleFrom(
                      backgroundColor: const Color.fromARGB(120, 81, 87, 100),
                    ),
                  ),
                ],
              ),
            ],
          ),
        );
      },
    );
  }

  void _mostraIndirizzoDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          backgroundColor: const Color.fromARGB(255, 255, 255, 255),
          title: const Text(
            "Inserisci indirizzo",
            textAlign: TextAlign.center,
          ),
          content: Row(
            children: [
              Expanded(
                child: TextField(
                  controller: _indirizzocontroller,
                  decoration: InputDecoration(
                    hintText: 'Indirizzo',
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(30.0),
                    ),
                    filled: true,
                    fillColor: Colors.white,
                  ),
                ),
              ),
              IconButton(
                onPressed: () {
                  setState(() {
                    OrdineService().creaOrdine(cc, _indirizzocontroller.text);
                    getc();
                  });
                  Navigator.of(context).pop(); // Chiude la dialog dopo aver inviato l'indirizzo
                },
                icon: const Icon(Icons.done),
              ),
            ],
          ),
        );
      },
    );
  }



}
