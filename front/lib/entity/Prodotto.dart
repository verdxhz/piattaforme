import 'package:http/http.dart' as http;
import 'dart:convert';
class Prodotto {
  int id;
  String nome;
  String descrizione;
  double prezzo;
  String immagine;
  String categoria;
  int disponibilita;

  // Costruttore con parametri richiesti
  Prodotto({
    required this.id,
    required this.nome,
    required this.categoria,
    required this.descrizione,
    required this.disponibilita,
    required this.prezzo,
    required this.immagine,
  });

  // Factory method per creare un'istanza di Prodotto da una mappa JSON
  factory Prodotto.fromJson(Map<String, dynamic> json) {
    return Prodotto(
      id: json['id'], // Valore predefinito se null
      nome: json['nome']??'',
      descrizione: json['descrizione'] ?? ' ',
      prezzo: json['prezzo']??0.0 , // Conversione sicura da null a double
      immagine: json['immagine'] ??'', // Immagine di default
      categoria: json['categoria'] ??'',
      disponibilita: json['disponibilita'] ?? 0, // Disponibilità predefinita se null
    );
  }



  // Metodo per convertire un'istanza di Prodotto in una mappa JSON
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nome': nome,
      'prezzo': prezzo,
      'immagine': immagine,
      'descrizione': descrizione,
      'categoria': categoria,
      'disponibilita': disponibilita,
    };
  }
}
class ProdottoService {
Future<List<Prodotto>> mostraProdotti() async {
  final response = await http.get(Uri.parse('http://localhost:8081/prodotto/all'));
  final co=response.statusCode;
  if (response.statusCode == 200) {
    List jsonResponse = json.decode(response.body);
    return jsonResponse.map((product) => Prodotto.fromJson(product)).toList();
  } else {
    throw Exception('Failed to load products $co');
  }
}

Future<List<Prodotto>> mostraProdottiNome(String nome) async {
  final response = await http.get(Uri.parse('http://localhost:8081/prodotto/nome?nome=$nome'));
  final co=response.statusCode;
  if (response.statusCode == 200) {
    List jsonResponse = json.decode(response.body);
    return jsonResponse.map((product) => Prodotto.fromJson(product)).toList();
  } else {
    throw Exception('Failed to load products $co');
  }
}

Future<List<Prodotto>> mostraProdottiCategoria(String categoria) async {
  final response = await http.get(Uri.parse('http://localhost:8081/prodotto/categoria?categoria=$categoria'));
  final co=response.statusCode;
  if (response.statusCode == 200) {
    List jsonResponse = json.decode(response.body);
    return jsonResponse.map((product) => Prodotto.fromJson(product)).toList();
  } else {
    throw Exception('Failed to load products $co');
  }
}

Future<List<Prodotto>> mostraProdottiPrezzo(int min, int max) async {
  final response = await http.get(Uri.parse('http://localhost:8081/prodotto/prezzo?min=$min&max=$max'));
  final co=response.statusCode;
  if (response.statusCode == 200) {
    List jsonResponse = json.decode(response.body);
    return jsonResponse.map((product) => Prodotto.fromJson(product)).toList();
  } else {
    throw Exception('Failed to load products $co');
  }
}

Future<List<Prodotto>> filtri(String parola, String categoria, int min, int max) async {
  final response = await http.get(Uri.parse('http://localhost:8081/prodotto/filtri?parola=$parola&&categoria=$categoria&&min=$min&&max=$max'));
  final co=response.statusCode;
  if (response.statusCode == 200) {
    List jsonResponse = json.decode(response.body);
    return jsonResponse.map((product) => Prodotto.fromJson(product)).toList();
  } else {
    throw Exception('Failed to load products $co');
  }
}

}