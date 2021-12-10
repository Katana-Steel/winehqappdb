import 'package:flutter/material.dart';
import 'package:linkwell/linkwell.dart';

class AboutDlg extends StatelessWidget {
  const AboutDlg({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('About'),
      ),
      body: buildDialog(),
    );
  }

  Widget buildDialog() {
    final String notice =
        'Copyright 2012-2021 by Rene Kjellerup (aka Katana Steel) and you can ' +
            'find out more on my web site https://www.alchemiestick.net/\n\n' +
            'WineHQ Appdb Search is released under GPLv3 or later see license: ' +
            'http://www.gnu.org/licenses/ for more infomation about the ' +
            'licenses.\n\nSouce code for the program can be obtained at\n' +
            'http://github.com/Katana-Steel/winehqappdb\nand check the ' +
            'releases and select the apropriate one for the version you are ' +
            'running.';
    return LinkWell(
      notice,
      style: TextStyle(color: Colors.lightBlue),
      linkStyle:
          TextStyle(color: Colors.white, decoration: TextDecoration.underline),
      listOfNames: {'https://www.alchemiestick.net/': 'Alchemiestick'},
    );
  }
}
