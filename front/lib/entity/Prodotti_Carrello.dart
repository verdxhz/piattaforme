import 'Carrello.dart';
import 'Prodotto.dart';
class Prodotti_Carrello{
  int id;
  Prodotto prodotto;
  Carrello carrello;
  int quantita;

  Prodotti_Carrello({required this.id, required this.prodotto, required this.carrello, required this.quantita});

  factory Prodotti_Carrello.fromJson(Map<String,dynamic> json){
    return Prodotti_Carrello(
        id: json['id'], prodotto: Prodotto.fromJson(json['prodotto']), carrello: Carrello.fromJson(json['carrello']), quantita: json['quantità']);
  }
  Map<String, dynamic> toJson() => {
    'id':id,
    'prodotto': prodotto.toJson(),
    'carrello': carrello.toJson(),
    'quantità': quantita,
  };
}
