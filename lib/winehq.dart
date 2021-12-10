import 'package:flutter/material.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:winehqappdb/search_options.dart';
import 'package:winehqappdb/winapp.dart';

class AppVersion {
  int appId = 0;
  String appName = '';
  String appVersion = '';
  int testId = 0;
  String testScore = '';
  void _executeLookup(BuildContext context) {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => AppView(title: this.appName, app: this)));
  }

  Widget getAppName(BuildContext context) {
    return ElevatedButton(
      style: ElevatedButton.styleFrom(
        shape:
            RoundedRectangleBorder(borderRadius: BorderRadius.circular(15.0)),
      ),
      child: Text(
        appName,
      ),
      onPressed: () {
        this._executeLookup(context);
      },
    );
  }
}

Future<String> getPage(Map<String, String> formData) async {
  Uri url = Uri(
    scheme: 'https',
    host: 'appdb.winehq.org',
    path: '/objectManager.php',
    queryParameters: formData,
  );
  http.Response r = await http.post(url, body: formData);
  return utf8.decode(r.bodyBytes, allowMalformed: true);
}

List<String> getAppTable(String page) {
  String table = 'whq-table';
  if (!page.contains(table)) return [];
  for (String l in page.split('\n'))
    if (l.contains(table)) return l.split('<tr>');
  // just in case contains is wrong
  return [];
}

void findApplications(String name, dynamic state) async {
  state.apps = <AppVersion>[];
  Map<String, String> form = getDefaults();
  form['sappFamily-appNameData'] = name;
  String page = await getPage(form);
  List<String> table = getAppTable(page);
  if (table.length > 0) {
    String id = 'iId=';
    for (String tr in table) {
      if (!tr.contains(id)) continue;
      AppVersion app = AppVersion();
      int s = tr.indexOf(id) + id.length;
      int e = tr.indexOf('"', s);
      app.appId = int.parse(tr.substring(s, e));
      s = tr.indexOf('<a href=') + id.length;
      s = tr.indexOf('>', s) + 1;
      e = tr.indexOf('</a>', s);
      app.appName = tr.substring(s, e);
      state.apps.add(app);
    }
  }
}
