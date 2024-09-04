import 'package:flutter/material.dart';
import 'package:gioiafront/entity/Carrello.dart';

import '../entity/Prodotto.dart';

class CarrelloPage extends StatefulWidget {
  const CarrelloPage({super.key});

  @override
  State<CarrelloPage> createState() => _CarrelloState();
}

class _CarrelloState extends State<CarrelloPage> {
  late Future<List<Prodotto>> prodotti;
  late Future<Carrello> carrello;

  @override
  void initState() {
    super.initState();
    carrello = CarrelloService().getCarrello();
    getc();
  }

  void getc() async {
    final cc = await carrello;
    debugPrint('${cc.id}');
    getp(cc.id);
  }

  void getp(int id) {
    setState(() {
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
                              boxShadow: [
                                BoxShadow(
                                  color: Colors.black.withOpacity(0.1),
                                  spreadRadius: 3,
                                  blurRadius: 6,
                                  offset: const Offset(0, 3),
                                ),
                              ],
                            ),
                            child: Row(
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                Image.asset(
                                  prodotto.immagine,
                                  height: 160,
                                  width: 160,
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
                child: Text(
                  'Summary',
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 24,
                  ),
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
}
