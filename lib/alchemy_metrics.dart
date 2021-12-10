import 'package:flutter/material.dart';
import 'package:http/http.dart';
import 'package:platform_device_id/platform_device_id.dart';

void countCallback() async {
  WidgetsFlutterBinding.ensureInitialized();
  String userID = '';
  try {
    userID = await PlatformDeviceId.getDeviceId ?? 'not_found';
  } catch (e) {
    return;
  }
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
    'lic': '16',
  };
  post(alchemy, headers: headers, body: data);
}
