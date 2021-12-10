import 'package:flutter/material.dart';
import 'about.dart';
import 'settings.dart';
import 'main.dart';

class SideMenu extends StatelessWidget {
  const SideMenu({
    Key? key,
    this.disable,
  }) : super(key: key);

  final String? disable;

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        children: buildMenu(context, this.disable ?? ''),
      ),
    );
  }

  List<Widget> buildMenu(BuildContext context, String exclude) {
    final List<String> titles = [
      'Search',
      'Settings',
      'About',
    ];
    List<Widget> menu = [];
    menu.add(makeHeader());
    final TextStyle menuStyle =
        TextStyle(fontSize: 14, color: Colors.lightBlue);
    titles.forEach((menuItemName) {
      Widget screen = Container();
      menu.add(ListTile(
        title: Text(
          menuItemName,
          style: menuStyle,
        ),
        onTap: () {
          switch (menuItemName) {
            case 'Search':
              screen = MyHomePage();
              break;
            case 'Settings':
              screen = Settings();
              break;
            case 'About':
              screen = AboutDlg();
              break;
          }

          Navigator.of(context).pop();
          if (exclude != menuItemName) {
            Navigator.of(context)
                .push(MaterialPageRoute(builder: (context) => screen));
          }
        },
      ));
    });

    return menu;
  }

  DrawerHeader makeHeader() {
    return DrawerHeader(
      decoration: BoxDecoration(
          gradient: LinearGradient(
        colors: [Colors.indigo, Colors.deepPurple],
        stops: [0.3, 0.8],
      )),
      child: Text(
        'WineHQ AppDB Search',
        style: TextStyle(
          fontSize: 18,
        ),
      ),
    );
  }
}
