import 'dart:convert';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:gioiafront/entity/Carrello.dart';
import 'package:gioiafront/entity/Cliente.dart';
import 'package:gioiafront/entity/Prodotti_Carrello.dart';

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
    await CarrelloService().aggiungiCarrello(prodotto);
    getc();
  }

  void rimuovi(Prodotto prodotto) async {
    await CarrelloService().rimuoviCarrello(prodotto.id);
    getc();
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
          backgroundColor: Color.fromARGB(255, 255, 255, 255),
          title: Text(
            "Conferma",
            textAlign: TextAlign.center,
          ),
          content: Column(
            mainAxisSize: MainAxisSize.min, // Mantiene la finestra compatta
            children: [
              Text(
                "Vuoi confermare questo ordine?",
                textAlign: TextAlign.center,
              ),
              SizedBox(height: 20), // Spazio tra il testo e i bottoni
              Row(
                mainAxisAlignment: MainAxisAlignment.center, // Centra i bottoni
                children: [
                  TextButton(
                    child: Text(
                      "Conferma",
                      style: TextStyle(
                        color: Colors.white,
                      ),
                    ),
                    onPressed: () {
                      Navigator.of(context).pop(); // Azione di conferma
                    },
                    style: TextButton.styleFrom(
                      backgroundColor: Color.fromARGB(120, 81, 87, 100),
                    ),
                  ),
                  SizedBox(width: 20), // Spazio tra i due bottoni
                  TextButton(
                    child: Text(
                      "Chiudi",
                      style: TextStyle(
                        color: Colors.white,
                      ),
                    ),
                    onPressed: () {
                      Navigator.of(context).pop(); // Chiudi il dialog
                    },
                    style: TextButton.styleFrom(
                      backgroundColor: Color.fromARGB(120, 81, 87, 100),
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

//TODO sbaglia quantità
}
