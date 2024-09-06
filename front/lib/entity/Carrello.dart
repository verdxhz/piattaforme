import 'dart:convert';

import 'package:gioiafront/entity/Prodotto.dart';

import '../utils/authenticator.dart';
import 'Cliente.dart';
import 'Prodotti_Carrello.dart';
import 'package:http/http.dart' as http;
class Carrello{
  int id;
  Cliente cliente;
  List<Prodotti_Carrello> prodotti;

  Carrello({required this.id, required this.cliente, required this.prodotti});

  factory Carrello.fromJson (Map<String, dynamic> json){
    return Carrello(id: json['id'], cliente: Cliente.fromJson(json['cliente']),prodotti: (json['prodotti'] != null && json['prodotti'] is List)
        ? (json['prodotti'] as List).map((i) => Prodotti_Carrello.fromJson(i)).toList()
        : [],
    );
  }

  Map<String, dynamic> toJson() => {
    'id': id,
    'cliente': cliente,
    'prodotti': prodotti.map((e) => e.toJson()).toList(),
  };


}
class CarrelloService {

  Future<void> aggiungiCarrello(Prodotto p) async {
    final response = await http.put(
        Uri.parse('http://localhost:8081/carrello/addp'), body: p.toJson(),headers: {'Authorization':'Bearer ${Authenticator().getToken()}'});
    final co = response.statusCode;
    if (response.statusCode == 200) {
      return;
    } else {
      throw Exception('Failed to add $co');
    }
  }

  Future<void> rimuoviCarrello(int p) async {
    final response = await http.put(
        Uri.parse('http://localhost:8081/carrello/removep&prodottoId=$p'),headers: {'Authorization':'Bearer ${Authenticator().getToken()}'});
    final co = response.statusCode;
    if (response.statusCode == 200) {
      return;
    } else {
      throw Exception('Failed to load products $co');
    }
  }

  Future<Carrello> getCarrello() async {
    final response = await http.get(
        Uri.parse('http://localhost:8081/carrello/carrello'),headers: {'Authorization':'Bearer ${Authenticator().getToken()}'});
    final co = response.statusCode;
    if (response.statusCode == 200) {
      print(response.body);
      return Carrello.fromJson(json.decode(response.body));
    } else {
      throw Exception('il carrello è vuoto $co');
    }
  }

  Future<List<Prodotto>> getProdottiCarrello(int carrello) async {
    final response = await http.get(Uri.parse(
        'http://localhost:8081/carrello/prodottic?carrello=$carrello'),headers: {'Authorization':'Bearer ${Authenticator().getToken()}'});
    final co = response.statusCode;
    if (response.statusCode == 200) {
      List jsonResponse = json.decode(response.body);
      return jsonResponse.map((product) => Prodotto.fromJson(product)).toList();
    } else {
      throw Exception('Failed to load products $co');
    }
  }
}