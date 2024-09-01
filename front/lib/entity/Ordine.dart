import 'package:http/http.dart' as http;
import 'dart:convert';

import 'Prodotto.dart';

import 'Cliente.dart';
import 'Carrello.dart';
class Ordine{
  int id;
  Carrello carrello;
  String indirizzo;
  DateTime data;
  Cliente cliente;

  Ordine({required this.id, required this.carrello, required this.indirizzo, required this.data, required this.cliente});

  factory Ordine.fromJson(Map<String,dynamic> json){
    return Ordine(id: json['id'], carrello: Carrello.fromJson(json['carrello']),indirizzo: json['indirizzo'],data: DateTime.parse(json['data']),cliente: Cliente.fromJson(json['cliente']));
  }
  Map<String, dynamic> toJson() => {
    'id':id,
    'carrello': carrello.toJson(),
    'indirizzo': indirizzo,
    'data':data.toIso8601String(),
    'cliente': cliente.toJson(),

  };
}
class OrdineService{
  Future<void> creaOrdine(Carrello c, String indirizzo) async {
    final response = await http.post(Uri.parse('http://localhost:8081/carrello?indirizzo=$indirizzo'), body: json.encode(c.toJson()));
    final co=response.statusCode;
    if (response.statusCode == 200) {
      return;
    } else {
      throw Exception('Failed to load products $co');
    }
  }

  Future<List<Ordine>> getOrdine(int c) async {
    final response = await http.get(Uri.parse('http://localhost:8081/carrello/cliente?cliente=$c'));
    final co=response.statusCode;
    if (response.statusCode == 200) {
      List jsonResponse = json.decode(response.body);
      return jsonResponse.map((product) => Ordine.fromJson(product)).toList();
    } else {
      throw Exception('Failed to load products $co');
    }
  }

  Future<List<Prodotto>> getProdottiOrdine(int o) async {
    final response = await http.get(Uri.parse('http://localhost:8081/carrello/prodotti?'));
    final co=response.statusCode;
    if (response.statusCode == 200) {
      List jsonResponse = json.decode(response.body);
      return jsonResponse.map((product) => Prodotto.fromJson(product)).toList();
    } else {
      throw Exception('Failed to load products $co');
    }
  }

}