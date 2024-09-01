import 'package:flutter/material.dart';
import 'package:toastification/toastification.dart';

import '../utils/LogInResult.dart';
import '../utils/authenticator.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool reg=true;
  String username="";
  String password="";
  String _errorMessage = '';

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
                  controller: _usernameController,
                  decoration: InputDecoration(
                    hintText: 'Username',
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
            setState(() {
              accesso();
            });// Logica di accesso
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

  Future<void> accesso() async {

      setState(() {
        username=_usernameController.text;
        password=_passwordController.text;
        _errorMessage = '';
      });

      Authenticator authenticator = Authenticator();
      LogInResult result = await authenticator.login(username, password);
      setState(() {
        if (result == LogInResult.logged) {
          isLoggedIn = true;
         // Navigator.pushNamedAndRemoveUntil(context, '/home', (Route<dynamic> route) => false);
        } else if (result == LogInResult.error_wrong_credentials) {
          _errorMessage = 'Credenziali errate. Riprova.';
        } else if (result == LogInResult.error_not_fully_setupped) {
          _errorMessage = 'Account non completamente configurato.';
        } else {
          _errorMessage = 'Errore sconosciuto. Riprova.';
        }
        toast();
      });

  }

  toast() {
    if (_errorMessage!=""){toastification.show(
      context: context,
      type: ToastificationType.error,
      style: ToastificationStyle.minimal,
      title: Text("Errore"),
      description: Text(_errorMessage),
      alignment: Alignment.center,
      autoCloseDuration: const Duration(seconds: 4),
      primaryColor: Color(0xff000000),
      foregroundColor: Color(0xff000000),
      borderRadius: BorderRadius.circular(4.0),
      showProgressBar: false,
      boxShadow: highModeShadow,
      closeButtonShowType: CloseButtonShowType.onHover,
      pauseOnHover: false,
      applyBlurEffect: true,
    );}
  }
}
