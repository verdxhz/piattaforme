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