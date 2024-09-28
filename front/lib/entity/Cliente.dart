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

  Future<bool> registra(UserRegistrationDto userDto) async {
    //String? aut= Authenticator().getToken();
    try{
    final response = await http.post(Uri.parse('http://localhost:8081/keycloak'), headers: {'Content-Type': 'application/json',}, body: json.encode(userDto.toJson()),);
    print('Status code: ${response.statusCode}');
    print('Response body: ${response.body}');

    print('Request body: ${json.encode(userDto.toJson())}');

    if (response.statusCode == 200) {
      return true;
    } else {
      print('Failed to register user: ${response.body}');
      return false;
    }
  } catch (e) {
  print('Exception occurred: $e');
  return false;
  }
}
}

class UserRegistrationDto {
  final String nome;
  final String email;
  final String username;
  final String password;

  UserRegistrationDto({
    required this.nome,
    required this.email,
    required this.username,
    required this.password,
  });

  Map<String, dynamic> toJson() {
    return {
      'nome': nome,
      'email': email,
      'username': username,
      'password': password,
    };
  }

  factory UserRegistrationDto.fromJson(Map<String, dynamic> json) {
    return UserRegistrationDto(
      nome: json['nome'] as String,
      email: json['email'] as String,
      username: json['username'] as String,
      password: json['password'] as String,
    );
  }

}
