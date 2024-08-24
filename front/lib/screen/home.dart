import 'package:flutter/material.dart';
import '../entity/Prodotto.dart';
import 'account.dart';
import 'login.dart';

class Home extends StatefulWidget {
  const Home({super.key, required this.title});

  final String title;

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  final TextEditingController _textEditingController = TextEditingController();
  bool login = false; // TODO: true
  late Future<List<Prodotto>> prodotti;

  // Range slider state
  RangeValues _currentRangeValues = const RangeValues(0, 3000);

  @override
  void initState() {
    super.initState();
    prodotti = ProdottoService().mostraProdotti();
  }

  // Search function based on name
  void cerca(String nome) {
    setState(() {
      prodotti = ProdottoService().mostraProdottiNome(nome);
    });
  }

  // Search function based on price range
  void cercaPrezzo(RangeValues range) {
    setState(() {
      prodotti = ProdottoService().mostraProdottiPrezzo(range.start.floor(), range.end.ceil());
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          // Top bar with search and login button
          Container(
            color: Colors.black,
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                Image.asset(
                  'assets/image/log.jpg',
                  height: 90,
                  width: 160,
                ),
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 8.0),
                    child: TextField(
                      controller: _textEditingController,
                      onChanged: (text) {
                        cerca(text); // Trigger search when the input changes
                      },
                      decoration: const InputDecoration(
                        hintText: "Cosa stai cercando?",
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.all(Radius.circular(30.0)),
                        ),
                        fillColor: Colors.white,
                        filled: true,
                      ),
                    ),
                  ),
                ),
                log(), // Login button based on login state
              ],
            ),
          ),

          // Price range slider
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                Expanded(child: SliderTheme(
                  data: SliderTheme.of(context).copyWith(
                    trackHeight: 2.0,  // Qui imposti l'altezza della linea
                    activeTrackColor: Colors.black,
                    inactiveTrackColor: Colors.grey,
                    thumbColor: Colors.black,
                    rangeThumbShape: RoundRangeSliderThumbShape(disabledThumbRadius: 2.0,),
                  ),
                  child: RangeSlider(
                    values: _currentRangeValues,
                    min: 0,
                    max: 3000,
                    divisions: 300,
                    onChanged: (RangeValues values) {
                      setState(() {
                        _currentRangeValues = values;
                        cercaPrezzo(_currentRangeValues);
                      });
                    },
                  ),
                ),),
                const SizedBox(width: 16),
                Text(
                  '${_currentRangeValues.start.round()} € - ${_currentRangeValues.end.round()} €',
                  style: const TextStyle(fontSize: 16),
                ),
              ],
            ),
          ),

          // Product List
          Expanded(
            child: FutureBuilder<List<Prodotto>>(
              future: prodotti,
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return const Center(child: CircularProgressIndicator());
                } else if (snapshot.hasError) {
                  return Center(child: Text('Error: ${snapshot.error}'));
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
                                  crossAxisAlignment: CrossAxisAlignment.center,
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
                                  TextButton(
                                    child: const Text('Aggiungi al carrello'),
                                    onPressed: () {
                                      // Add to cart functionality
                                    },
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
                  return const Center(child: Text('No products found'));
                }
              },
            ),
          ),
        ],
      ),
    );
  }

  // Log button based on login status
  Widget log() {
    if (login) {
      return IconButton(
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => const Account(),
            ),
          );
        },
        tooltip: 'Account',
        icon: const Icon(
          Icons.account_circle,
          color: Colors.white,
          size: 30.0,
        ),
      );
    } else {
      return IconButton(
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => const LoginPage(),
            ),
          );
        },
        tooltip: 'Login',
        icon: const Icon(
          Icons.login,
          color: Colors.white,
          size: 30.0,
        ),
      );
    }
  }
}
