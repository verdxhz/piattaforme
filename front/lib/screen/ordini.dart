import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:gioiafront/entity/Carrello.dart';
import 'package:gioiafront/entity/Prodotti_Carrello.dart';

import '../entity/Ordine.dart';
import '../entity/Prodotto.dart';

class OrdiniPage extends StatefulWidget {
  const OrdiniPage({super.key});

  @override
  State<OrdiniPage> createState() => _OrdiniState();
}

class _OrdiniState extends State<OrdiniPage> {
  Future<List<Ordine>> ordini = Future(() => []);

  @override
  void initState() {
    super.initState();
    getc();
  }

  void getc() async {
    ordini = OrdineService().getOrdine();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: FutureBuilder<List<Ordine>>(
        future: ordini,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Non ci sono ordini'));
            //return Center(child: Text('Error: ${snapshot.error}'));
          } else if (snapshot.hasData && snapshot.data!.isNotEmpty) {
            return ListView.builder(
              itemCount: snapshot.data!.length,
              itemBuilder: (context, index) {
                Ordine ordine = snapshot.data![index];
                return Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Container(
                    decoration: BoxDecoration(
                      color: Colors.grey.shade200,
                      border: Border.all(
                        color: Colors.black.withOpacity(0.1),
                      ),
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        ListTile(
                            title: Text(
                                '${ordine.data.day}-${ordine.data.month}-${ordine.data.year}'),
                            subtitle: Row(
                              children: [
                                Text('${ordine.indirizzo}'),
                                Expanded(child: SizedBox()),
                                getConto(ordine)
                              ],
                            )),
                        FutureBuilder<List<Prodotto>>(
                          future: OrdineService().getProdottiOrdine(ordine.id),
                          builder: (context, snapshot) {
                            if (snapshot.connectionState ==
                                ConnectionState.waiting) {
                              return const Center(
                                  child: CircularProgressIndicator());
                            } else if (snapshot.hasError) {
                              return Center(
                                  child: Text('Error: ${snapshot.error}'));
                            } else if (snapshot.hasData &&
                                snapshot.data!.isNotEmpty) {
                              return Column(
                                children: snapshot.data!.map((prodotto) {
                                  return Padding(
                                    padding: const EdgeInsets.all(8.0),
                                    child: Container(
                                      decoration: BoxDecoration(
                                        color: Colors.white,
                                        border: Border.all(
                                          color: Colors.grey.withOpacity(0.3),
                                        ),
                                      ),
                                      child: Row(
                                        children: [
                                          Image.asset(
                                            prodotto.immagine,
                                            height: 80,
                                            width: 80,
                                            fit: BoxFit.cover,
                                          ),
                                          const SizedBox(width: 10),
                                          Expanded(
                                            child: Column(
                                              crossAxisAlignment:
                                                  CrossAxisAlignment.start,
                                              children: [
                                                Text(
                                                  prodotto.nome,
                                                  style: const TextStyle(
                                                      fontSize: 16,
                                                      fontWeight:
                                                          FontWeight.bold),
                                                ),
                                                const SizedBox(height: 5),
                                                Text(prodotto.descrizione),
                                              ],
                                            ),
                                          ),
                                          const SizedBox(width: 10),
                                          Column( children: [Text(
                                              '${prezzo(ordine, prodotto.id)} €',
                                      style: const TextStyle(
                                        fontSize: 16,
                                        fontWeight: FontWeight.bold,
                                      ),
                                          ),
                                            const SizedBox(height: 10),
                                            Text(
                                              'x${quantita(ordine, prodotto.id)}',
                                              style: const TextStyle(
                                                fontSize: 14,
                                              ),
                                            ),
                                          ] ),

                                        ],
                                      ),
                                    ),
                                  );
                                }).toList(),
                              );
                            } else {
                              return const Center(
                                  child: Text('Nessun prodotto trovato.'));
                            }
                          },
                        ),
                      ],
                    ),
                  ),
                );
              },
            );
          } else {
            return const Center(child: Text('Non ci sono ordini.'));
          }
        },
      ),
    );
  }

  Widget getConto(Ordine ordine) {
    double conto = 0;
    Carrello c = ordine.carrello;
    for (Prodotti_Carrello p in c.prodotti) conto += p.prezzo * p.quantita;
    return Text('totale: $conto €');
  }

  prezzo(Ordine o, int idp) {
    for (Prodotti_Carrello p in o.carrello.prodotti) {
      if (p.prodotto.id == idp) return '${p.prezzo}';
    }
  }
  quantita(Ordine o, int idp) {
    for (Prodotti_Carrello p in o.carrello.prodotti) {
      if (p.prodotto.id == idp) return '${p.quantita}';
    }
  }
}
