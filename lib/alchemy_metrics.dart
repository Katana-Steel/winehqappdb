import 'package:flutter/material.dart';
import 'package:device_id/device_id.dart';
import 'package:http/http.dart';

void countCallback() async {
  WidgetsFlutterBinding.ensureInitialized();
  String userID = '';
  try {
    userID = await DeviceId.getID;
  } catch (e) {
    return;
  }
  Map<String, String> headers = {
    'User-Agent': 'WineHQ/1.2 App Browser',
    'Content-Type': 'application/x-www-form-urlencoded',
  };
  Uri alchemy = Uri(
    scheme: 'https',
    host: 'www.alchemiestick.net',
    path: '/store_user.php',
  );
  Map<String, String> data = {
    'googleAcc': userID,
    'lic': '15',
  };
  post(alchemy, headers: headers, body: data);
}
