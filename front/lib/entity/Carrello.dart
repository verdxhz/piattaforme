import 'dart:convert';

import 'package:gioiafront/entity/Prodotto.dart';

import 'Cliente.dart';
import 'Prodotti_Carrello.dart';
import 'package:http/http.dart' as http;
class Carrello{
  int id;
  Cliente cliente;
  List<Prodotti_Carrello> prodotti;

  Carrello({required this.id, required this.cliente, required this.prodotti});

  factory Carrello.fromJson (Map<String, dynamic> json){
    return Carrello(id: json['id'], cliente: Cliente.fromJson(json['cliente']),prodotti: (json["prodotti"] as List).map((i) => Prodotti_Carrello.fromJson(i)).toList());
  }

  Map<String, dynamic> toJson() => {
    'id': id,
    'cliente': cliente.toJson(),
    'prodotti': prodotti.map((e) => e.toJson()).toList(),
  };


}
class CarrelloService{

  Future<void> aggiungiCarrello(Prodotto p, int cliente) async {
    final response = await http.put(Uri.parse('http://localhost:8081/carrello/addp?clienteId=$cliente'), body: p.toJson());
    final co=response.statusCode;
    if (response.statusCode == 200) {
      return;
    } else {
      throw Exception('Failed to load products $co');
    }
  }

  Future<void> rimuoviCarrello(int p, int c) async {
    final response = await http.put(Uri.parse('http://localhost:8081/carrello/removep?clienteId=$c&prodottoId=$p'));
    final co=response.statusCode;
    if (response.statusCode == 200) {
      return;
    } else {
      throw Exception('Failed to load products $co');
    }
  }
  Future<Carrello> getCarrello(int c) async {
    final response = await http.get(Uri.parse('http://localhost:8081/carrello/carrello?cliente=$c'));
    final co=response.statusCode;
    if (response.statusCode == 200) {
      return Carrello.fromJson(json.decode(response.body));
    } else {
      throw Exception('Failed to load products $co');
    }
  }
}