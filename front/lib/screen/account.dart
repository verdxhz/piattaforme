import 'package:flutter/material.dart';
import 'package:gioiafront/screen/carrello.dart';
import 'package:gioiafront/screen/ordini.dart';
import 'package:gioiafront/utils/authenticator.dart';

import '../entity/Cliente.dart';

class Account extends StatefulWidget {
  const Account({super.key});

  @override
  State<Account> createState() => _AccountState();
}

class _AccountState extends State<Account> {

  

  
  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2, // Numero di tab
      child: Scaffold(
        body: Column(
          children: [
            // Sezione superiore con immagine
            Container(
              color: Colors.black,
              padding: const EdgeInsets.all(8.0),
              child: Row(
                children: [
                  Image.asset(
                    'assets/image/log.jpg',
                    height: 90,
                    width: 160,
                  ),
                  Expanded(child: SizedBox()),
                  IconButton(onPressed: (){logoutCli();}, icon: Icon(Icons.logout,
                    color: Colors.white,
                    size: 30.0,))
                ],
              ),
            ),
            // TabBar
            Container(
              color: Colors.white,
              child: TabBar(
                indicatorColor: Colors.black,
                labelColor: Colors.black,
                unselectedLabelColor: Colors.grey,
                tabs: const [
                  Tab(icon: Icon(Icons.shopping_cart_outlined)),
                  Tab(icon: Icon(Icons.shopping_bag_outlined)),
                ],
              ),
            ),
            // Contenuto delle Tab
            Expanded(
              child: TabBarView(
                children: [
                  Center(child: CarrelloPage()),
                  Center(child: OrdiniPage()),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  void logoutCli() async {
    await Authenticator().logOut();
    Navigator.pop(context, false);
  }
}
