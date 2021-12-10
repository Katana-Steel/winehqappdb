import 'dart:convert';

import 'package:shared_preferences/shared_preferences.dart';

class SPHelper {
  static late SharedPreferences prefs;

  Future init() async {
    prefs = await SharedPreferences.getInstance();
  }

  Future writeOptions(Map<String, String> opts) async {
    await prefs.setString('searchOptions', json.encode(opts));
  }

  Map<String, String> getStoredOptions() {
    Map<String, dynamic> jmap =
        json.decode(prefs.getString('searchOptions') ?? '{}');
    Map<String, String> map = {};
    jmap.entries.forEach((e) {
      map[e.key] = e.value.toString();
    });
    return map;
  }
}

Map<String, String> getDefaults() {
  Map<String, String> opts = SPHelper().getStoredOptions();
  if (opts.isEmpty) {
    opts = {
      'bAscending': 'true',
      'bIsQueue': 'false',
      'bIsRejected': 'false',
      'sClass': 'application',
      'sTitle': 'Browse Application',
      'sReturnTo': '',
      'iId': '0',
      'sOrderBy': 'appName',
      'iappFamily-appNameOp': '2', // contains: 2, starts with: 3, ends with: 4
      'sFilterSubmit': ' Update filter',
      'iItemsPerPage': '50',
      'iPage': '1',
    };
  }
  return opts;
}
