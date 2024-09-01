import 'dart:convert';

import 'package:http/http.dart' as http;

import '../utils/authenticator.dart';

class Cliente{
  int id_cliente;
  String nome;

  Cliente({ required this.id_cliente, required this.nome});

  factory Cliente.fromJson(Map<String, dynamic> json){
    return Cliente(id_cliente: json['id_cliente'], nome: json['nome']);
  }
  Map<String, dynamic> toJson() => {
    'id_cliente': id_cliente,
    'nome': nome,
  };
}
class ClienteService{
  late String? aut;

  Future<Cliente> getUtente() async {
    String? aut= Authenticator().getToken();
    final response = await http.get(Uri.parse('http://localhost:8081/cliente'), headers: {'Authorization':'Bearer $aut'});
    final co=response.statusCode;
    if (response.statusCode == 200) {
      return Cliente.fromJson(json.decode(response.body));
    } else {
      throw Exception('Failed $co');
    }
  }

}