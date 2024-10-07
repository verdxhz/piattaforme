import 'package:http/http.dart' as http;
import 'dart:convert';

import '../utils/authenticator.dart';
import 'Prodotto.dart';
import 'Cliente.dart';
import 'Carrello.dart';

class Ordine {
  int id;
  Carrello carrello;
  String indirizzo;
  DateTime data;
  Cliente cliente;

  Ordine({required this.id, required this.carrello, required this.indirizzo, required this.data, required this.cliente});

  factory Ordine.fromJson(Map<String, dynamic> json) {
    return Ordine(
      id: json['id'],
      carrello: Carrello.fromJson(json['carrello']),
      indirizzo: json['indirizzo'],
      data: DateTime.parse(json['data']),
      cliente: Cliente.fromJson(json['cliente']),
    );
  }

  Map<String, dynamic> toJson() => {
    'id': id,
    'carrello': carrello.toJson(),
    'indirizzo': indirizzo,
    'data': data.toIso8601String(), // Formato ISO 8601
    'cliente': cliente.toJson(),
  };
}

class OrdineService {
  Future<bool> creaOrdine(Carrello c, String indirizzo) async {
    final response = await http.post(
      Uri.parse('http://localhost:8081/carrello?indirizzo=$indirizzo'), headers: {
        'Authorization': 'Bearer ${Authenticator().getToken()}','Content-Type': 'application/json'
      },
      body: jsonEncode(c.toJson()),
    );
    if (response.statusCode == 200) {
      return true;
    } else {
      return false;//throw Exception('Failed to create order. Status code: ${response.statusCode}');
    }
  }

  Future<List<Ordine>> getOrdine() async {
    final response = await http.get(
      Uri.parse('http://localhost:8081/carrello/cliente'),
      headers: {
        'Authorization': 'Bearer ${Authenticator().getToken()}',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      List jsonResponse = json.decode(response.body);
      return jsonResponse.map((order) => Ordine.fromJson(order)).toList();
    } else {
      throw Exception('Failed to load orders. Status code: ${response.statusCode}');
    }
  }

  Future<List<Prodotto>> getProdottiOrdine(int o) async {
    final response = await http.get(Uri.parse('http://localhost:8081/carrello/prodotti?ordine=$o'));

    if (response.statusCode == 200) {
      List jsonResponse = json.decode(response.body);
      return jsonResponse.map((product) => Prodotto.fromJson(product)).toList();
    } else {
      throw Exception('Failed to load products. Status code: ${response.statusCode}');
    }
  }
}
