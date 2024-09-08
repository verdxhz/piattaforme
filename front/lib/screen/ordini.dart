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

class _OrdiniState extends State<OrdiniPage> with TickerProviderStateMixin {
  Future<List<Ordine>> ordini = Future(() => []);
  late AnimationController _controller;
  late Animation<double> _animation;
  bool isRotated = false;

  @override
  void initState() {
    super.initState();
    getc();
    _controller = AnimationController(
      duration: const Duration(milliseconds: 300),
      vsync: this,
    );
    _animation = Tween<double>(begin: 0, end: 0.25).animate(_controller);
  }

  void getc() async {
    ordini = OrdineService().getOrdine();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Ordini')),
      body: FutureBuilder<List<Ordine>>(
        future: ordini,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
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
                          subtitle: Text('${ordine.indirizzo}'),
                        ),
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
                                          Text(
                                            '${prodotto.prezzo.toString()} €',
                                            style: const TextStyle(
                                              fontSize: 16,
                                              fontWeight: FontWeight.bold,
                                            ),
                                          ),
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

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  void _toggleRotation() {
    setState(() {
      if (isRotated) {
        _controller.reverse(); // Torna alla posizione iniziale
      } else {
        _controller.forward(); // Ruota di 90 gradi
      }
      isRotated = !isRotated;
    });
  }
}
