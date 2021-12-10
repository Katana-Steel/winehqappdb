import 'package:flutter/material.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:winehqappdb/winehq.dart';

class AppView extends StatefulWidget {
  AppView({Key? key, required this.title, required this.app}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;
  final AppVersion app;

  @override
  _MyAppViewState createState() => _MyAppViewState();
}

class _MyAppViewState extends State<AppView> {
  List<Rating> ratings = [];
  bool running = false;
  void loadApp() async {
    if (running || ratings.length != 0) return;
    running = true;
    List<Rating> r = [];
    await Future<void>(() => initiateAppView(widget.app, r));
    if (this.mounted)
      setState(() {
        if (r.length != ratings.length) ratings = r;
        running = false;
        return;
      });
  }

  @override
  Widget build(BuildContext context) {
    loadApp();
    List<Row> ch = [];
    ch.add(Row(children: [Expanded(child: Text('Version')), Text('Ratings')]));
    for (Rating r in ratings) {
      ch.add(Row(children: r.getVersion()));
    }
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: Center(
        child: ListView.separated(
          separatorBuilder: (context, index) => Divider(color: Colors.black),
          padding: const EdgeInsets.all(10),
          itemCount: ch.length,
          itemBuilder: (context, index) => ch[index],
        ),
      ),
    );
  }
}

class Rating {
  String version = '';
  String rated = '';
  Rating(this.version, this.rated);
  List<Widget> getVersion() {
    return [Expanded(child: Text(version)), Text(rated)];
  }
}

List<String> getRatingTable(String page) {
  String table = 'whq-table';
  if (!page.contains(table)) return [];
  for (String l in page.split('\n'))
    if (l.contains(table)) return l.split('<tr ');
  // just in case contains is wrong
  return [];
}

Future<String> getAppPage(int appId) async {
  Map<String, String> formData = {
    'sClass': 'application',
    'iId': appId.toString()
  };
  Uri url = Uri(
      scheme: 'https',
      host: 'appdb.winehq.org',
      path: '/objectManager.php',
      queryParameters: formData);
  http.Response r = await http.get(url);
  return utf8.decode(r.bodyBytes, allowMalformed: true);
}

void initiateAppView(AppVersion app, List<Rating> state) async {
  String page = await getAppPage(app.appId);
  List<String> table = getRatingTable(page);
  if (table.length > 0) {
    String id = 'iId=';
    for (String tr in table) {
      if (!tr.contains(id)) continue;
      Rating rated = Rating('', '');
      int s = tr.indexOf(id) + id.length; // tr data url
      s = tr.indexOf('<td', s);
      s = tr.indexOf('>', s) + 1; // end of <a href
      s = tr.indexOf('>', s) + 1;
      int e = tr.indexOf('<', s + 1);
      rated.version = tr.substring(s, e);
      s = tr.indexOf('<td', s + 5); // get the next cell (Description)
      s = tr.indexOf('<td', s + 5); // get the next cell (Latest Rating)
      s = tr.indexOf('>', s) + 1;
      e = tr.indexOf('</td>', s);
      rated.rated = tr.substring(s, e);
      state.add(rated);
    }
  }
}
