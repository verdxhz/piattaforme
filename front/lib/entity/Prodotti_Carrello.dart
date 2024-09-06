
import 'Prodotto.dart';
class Prodotti_Carrello{
  int id;
  Prodotto prodotto;
  int quantita;
  double prezzo;

  Prodotti_Carrello({required this.id, required this.prodotto, required this.quantita, required this.prezzo,});

  factory Prodotti_Carrello.fromJson(Map<String,dynamic> json){
    return Prodotti_Carrello(
        id: json['id'], prodotto: Prodotto.fromJson(json['prodotto']), quantita: json['quantita']??0,  prezzo: json['prezzo']  ?? 0.0);
  }
  Map<String, dynamic> toJson() => {
    'id':id,
    'prodotto': prodotto.toJson(),
    'quantita': quantita,
    'prezzo': prezzo,
  };
}
