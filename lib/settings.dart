import 'package:flutter/material.dart';
import 'search_options.dart';

class Settings extends StatefulWidget {
  Settings({
    Key? key,
  }) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  @override
  _SettingsState createState() => _SettingsState();
}

class _SettingsState extends State<Settings> {
  Map<String, String> map = getDefaults();

  @override
  Widget build(BuildContext context) {
    List<Widget> v = [];
    for (String a in this.map.keys) {
      Widget item = Container();
      switch (a) {
        case 'bAscending':
          item = Row(children: [
            Text('Sort Order: '),
            DropdownButton(
                value: this.map[a],
                onChanged: (String? newOrder) => setState(() {
                      this.map[a] = newOrder.toString();
                    }),
                items: [
                  DropdownMenuItem(
                    child: Text('Ascending'),
                    value: 'true',
                  ),
                  DropdownMenuItem(
                    child: Text('Descending'),
                    value: 'false',
                  ),
                ]),
          ]);
          break;
        case 'iItemsPerPage':
          final List<String> iPages = [
            '25',
            '50',
            '100',
            '200',
          ];
          List<DropdownMenuItem<String>> menuOpts = [];
          iPages.forEach((element) {
            menuOpts.add(DropdownMenuItem(
              child: Text(element),
              value: element,
            ));
          });
          item = Row(children: [
            Text('Show: '),
            DropdownButton(
                value: this.map[a] ?? '25',
                onChanged: (String? newCount) => setState(() {
                      this.map['iItemsPerPage'] = newCount ?? '25';
                    }),
                items: menuOpts),
            Text('items')
          ]);
          break;
        default:
          continue;
      }
      v.add(item);
    }
    return Scaffold(
        appBar: AppBar(title: Text('Settings')),
        body: ListView(
          children: v,
        ));
  }

  @override
  void dispose() {
    Future<void>(() => SPHelper().writeOptions(this.map));
    super.dispose();
  }
}
