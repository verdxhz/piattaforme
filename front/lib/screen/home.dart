import 'package:flutter/material.dart';
import 'package:gioiafront/entity/Carrello.dart';
import 'package:toastification/toastification.dart';
import '../entity/Prodotti_Carrello.dart';
import '../entity/Prodotto.dart';
import 'account.dart';
import 'login.dart';
import 'carrello.dart' as c;

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  final TextEditingController _textEditingController = TextEditingController();
  // TODO: true
  late Future<List<Prodotto>> prodotti;
  String parola = "";
  String categoria = "";
  int min = 0;
  int max = 3000;
  bool login = false;

  // Range slider state
  RangeValues _currentRangeValues = const RangeValues(0, 3000);

  late int _selectedButtonIndex = -1;

  @override
  void initState() {
    super.initState();
    prodotti = ProdottoService().mostraProdotti();
  }

  // Search function based on name
  void cerca(String nome) {
    setState(() {
      parola = nome;
    });
  }

  void filtri(String parola, String categoria, int min, int max) {
    setState(() {
      prodotti = ProdottoService().filtri(parola, categoria, min, max);
    });
  }

  // Search function based on price range
  void cercaPrezzo(RangeValues range) {
    setState(() {
      min = range.start.floor();
      max = range.end.ceil();
    });
  }

  void cercaCategoria(String categoria) {
    setState(() {
      if (_selectedButtonIndex != -1) {
        this.categoria = categoria;
      } else
        this.categoria = "";
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
                        filtri(parola, categoria, min, max);
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
                Expanded(
                  child: SliderTheme(
                    data: SliderTheme.of(context).copyWith(
                      trackHeight: 2.0, // Qui imposti l'altezza della linea
                      activeTrackColor: Colors.black,
                      inactiveTrackColor: Colors.grey,
                      thumbColor: Colors.black,
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
                          filtri(parola, categoria, min, max);
                        });
                      },
                    ),
                  ),
                ),
                const SizedBox(width: 16),
                Text(
                  '${_currentRangeValues.start.round()} € - ${_currentRangeValues.end.round()} €',
                  style: const TextStyle(fontSize: 16),
                ),
                listButton(),
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
                  return Center(child: Text('Nessun Prodotto Trovato'));
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
                                      child: Text(prodotto.disponibilita > 0
                                          ? 'Aggiungi al carrello'
                                          : 'Esaurito'),
                                      onPressed: () async {
                                        if (prodotto.disponibilita > 0) {
                                          if (!login) {
                                            final result = await Navigator.push(
                                              context,
                                              MaterialPageRoute(
                                                builder: (context) =>
                                                    const LoginPage(),
                                              ),
                                            );
                                            if (result == true) {
                                              setState(() {
                                                login =
                                                    true; // Aggiorna lo stato di login
                                              });
                                            }
                                          }
                                          aggiungi(prodotto);
                                          //await CarrelloService().aggiungiCarrello(prodotto);
                                          toastification.show(
                                            context: context,
                                            type: ToastificationType.success,
                                            style: ToastificationStyle.minimal,
                                            title: Text(
                                                "aggiunto al carrello con successo"),
                                            alignment: Alignment.centerRight,
                                            autoCloseDuration:
                                                const Duration(seconds: 4),
                                            primaryColor: Color(0xff000000),
                                            icon: Icon(
                                                Icons.shopping_cart_outlined),
                                            borderRadius:
                                                BorderRadius.circular(4.0),
                                            boxShadow: highModeShadow,
                                            showProgressBar: false,
                                            applyBlurEffect: true,
                                          );
                                        }
                                      }),
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
        onPressed: () async {
          final result = await Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => const Account(),
            ),
          );
          if (result == false) {
            setState(() {
              login = false;
              toastification.show(
                context: context,
                type: ToastificationType.success,
                style: ToastificationStyle.minimal,
                title: Text("logout avvenuto con successo"),
                alignment: Alignment.centerRight,
                autoCloseDuration: const Duration(seconds: 4),
                primaryColor: Color(0xff000000),
                icon: Icon(Icons.logout),
                borderRadius: BorderRadius.circular(4.0),
                boxShadow: highModeShadow,
                showProgressBar: false,
                applyBlurEffect: true,
              ); // Aggiorna lo stato di login
            });
          }
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
        onPressed: () async {
          final result = await Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => const LoginPage(),
            ),
          );

          if (result == true) {
            setState(() {
              login = true; // Aggiorna lo stato di login
            });
          }
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

  Widget listButton() {
    return Row(children: [
      const SizedBox(width: 20),
      buildTextButton(0, 'collane'),
      const SizedBox(width: 20),
      buildTextButton(1, 'orecchini'),
      const SizedBox(width: 20),
      buildTextButton(2, 'bracciali'),
      const SizedBox(width: 20),
      buildTextButton(3, 'anelli'),
    ]);
  }

  TextButton buildTextButton(int index, String label) {
    String categoria = "";
    switch (index) {
      case 0:
        categoria = "collana";
        break;
      case 1:
        categoria = "orecchini";
        break;
      case 2:
        categoria = "bracciale";
        break;
      case 3:
        categoria = "anello";
        break;
    }
    return TextButton(
      onPressed: () {
        setState(() {
          if (_selectedButtonIndex == index) {
            _selectedButtonIndex = -1;
          } else {
            _selectedButtonIndex = index;
          } // Aggiorna il pulsante selezionato
          cercaCategoria(categoria);
          filtri(parola, this.categoria, min, max);
        });
      },
      style: ButtonStyle(
        backgroundColor: MaterialStateProperty.resolveWith<Color>(
          (Set<MaterialState> states) {
            // Cambia il colore di sfondo in base al pulsante selezionato
            return _selectedButtonIndex == index ? Colors.grey : Colors.white;
          },
        ),
        side: MaterialStateProperty.all<BorderSide>(
          const BorderSide(color: Colors.black, width: 0.5),
        ),
        shape: MaterialStateProperty.all<RoundedRectangleBorder>(
          RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(0.0),
          ),
        ),
      ),
      child: Text(
        label,
        style: TextStyle(color: Colors.black),
      ),
    );
  }

  Future<void> aggiungi(Prodotto prodotto) async {
    Carrello cart= await CarrelloService().getCarrello();
    int q = 0;
    for (Prodotti_Carrello p in cart.prodotti) {
      if (p.prodotto.id == prodotto.id)
        q = p.quantita;
    }
    if (q==0){
      await CarrelloService().aggiungiCarrello(prodotto);
    }
    else if((q+1)>prodotto.disponibilita)
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
      }
  }


}
