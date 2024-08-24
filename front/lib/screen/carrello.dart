import 'package:flutter/material.dart';

class CarrelloPage extends StatefulWidget {
  const CarrelloPage({super.key});

  @override
  State<CarrelloPage> createState() => _CarrelloState();
}

class _CarrelloState extends State<CarrelloPage> {
  int numItem = 1;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          Container(
            color: Colors.black,
            child: Row(
              children: [
                Image.asset(
                  'assets/image/log.jpg',
                  height: 90,
                  width: 160,
                ),
                const Expanded(
                  child: Padding(
                    padding: EdgeInsets.all(8.0),
                  ),
                ),
                IconButton(
                  onPressed: () {
                    // TODO: Handle account icon pressed
                  },
                  tooltip: 'account',
                  icon: const Icon(
                    Icons.account_circle,
                    color: Colors.white,
                    size: 30.0,
                  ),
                ),
              ],
            ),
          ),
          Expanded(
            child: Row(
              children: [
                Expanded(
                  child: Container(
                    decoration: BoxDecoration(
                      border: Border.all(color: Colors.black, width: 2.0),
                    ),
                    margin: const EdgeInsets.all(16.0),
                    child: ListView.builder(
                      padding: const EdgeInsets.all(16.0),
                      itemCount: numItem,
                      itemBuilder: (context, index) {
                        return ListTile(
                          title: Text('Item $index'), // Placeholder for items
                        );
                      },
                    ),
                  ),
                ),
                Column(
                  children: [
                    SizedBox(height: 15.00,),
                Container(
                  width: 300,
                  height: 200,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(30.0),
                    color: const Color.fromARGB(100, 81, 87, 100),
                  ),
                  child: Center(
                    child: Text(
                      'Summary',
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 24,
                      ),
                    ),
                  ),
                ),
                ],
                ),SizedBox(width: 15.00,),
              ],
            ),
          ),

        ],
      ),
    );
  }
}
