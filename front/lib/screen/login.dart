import 'package:flutter/material.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool reg=true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.grey,
        body: Center(

        child:
        Container(
        decoration: BoxDecoration(color: Colors.black,borderRadius:BorderRadius.circular(30.0),
        ),
        width: 350,
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const SizedBox(height: 30.0),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const SizedBox(width: 50.0),
                  Image.asset('assets/image/log.jpg', height: 130, width: 231),
                ],
              ),
              const SizedBox(height: 60.0),
              Container(
                width: 300, // Larghezza fissa per il TextField
                padding: const EdgeInsets.symmetric(vertical: 8.0),
                child: TextField(
                  controller: _emailController,
                  decoration: InputDecoration(
                    hintText: 'Email',
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(30.0),
                    ),
                    filled: true,
                    fillColor: Colors.white,
                  ),
                ),
              ),
              Container(
                width: 300, // Larghezza fissa per il TextField
                padding: const EdgeInsets.symmetric(vertical: 8.0),
                child: TextField(
                  controller: _passwordController,
                  decoration: InputDecoration(
                    hintText: 'Password',
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(30.0),
                    ),
                    filled: true,
                    fillColor: Colors.white,
                  ),
                  obscureText: true,
                ),
              ),
              const SizedBox(height: 16.0),
              reg? log(): nolog(),
            ],
          ),
        ),
      ),
    ),
    );
  }

  Widget log(){
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        TextButton(
          onPressed: () {
            // Logica di accesso
          },
          style: TextButton.styleFrom(
            backgroundColor: Color.fromARGB(120, 81, 87, 100),
          ),
          child: Text(
            'Accedi',
            style: const TextStyle(
              color: const Color.fromARGB(255, 194, 194, 194),
            ),
          ),
        ),
        const SizedBox(width: 16.0),
        TextButton(
          onPressed: () {
            setState(() {
              reg=false;
            });
          },
          style: TextButton.styleFrom(
            backgroundColor: Color.fromARGB(120, 81, 87, 100),
          ),
          child: Text(
            'Registrati',
            style: const TextStyle(
              color: const Color.fromARGB(255, 194, 194, 194),
            ), // Colore del testo grigio
          ),
        ),
      ],
    );
  }
  Widget nolog(){
    return TextButton(
      onPressed: () {
        // Logica di accesso
      },
      style: TextButton.styleFrom(
        backgroundColor: Color.fromARGB(120, 81, 87, 100),
      ),
      child: Text(
        'Registra',
        style: const TextStyle(
          color: const Color.fromARGB(255, 194, 194, 194),
        ),
      ),
    );
  }
}
