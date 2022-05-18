import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:uuid/uuid.dart';
import 'package:crclib/catalog.dart';

String generateGUID() {
  final uuid = Uuid();
  return uuid.v4();
}

void countCallback() async {
  WidgetsFlutterBinding.ensureInitialized();
  final prefs = await SharedPreferences.getInstance();

  String userID = prefs.getString('install_id') ?? generateGUID();
  await prefs.setString('install_id', userID);
  String lic = Crc16().convert(utf8.encode(userID)).toString();
  Map<String, String> headers = {
    'User-Agent': 'WineHQ/1.3 App Browser',
    'Content-Type': 'application/x-www-form-urlencoded',
  };
  Uri alchemy = Uri(
    scheme: 'https',
    host: 'www.alchemiestick.net',
    path: '/store_user.php',
  );
  Map<String, String> data = {
    'googleAcc': userID,
    'lic': lic,
  };
  post(alchemy, headers: headers, body: data);
}
