// This is a basic Flutter widget test.
//
// To perform an interaction with a widget in your test, use the WidgetTester
// utility that Flutter provides. For example, you can send tap and scroll
// gestures. You can also use WidgetTester to find child widgets in the widget
// tree, read text, and verify that the values of widget properties are correct.

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:winehqappdb/main.dart';
import 'package:winehqappdb/winehq.dart';

// class 

void main() {
  testWidgets('Search updates listview', (WidgetTester tester) async {
//    when()
    // Build our app and trigger a frame.
    await tester.pumpWidget(MyApp());

    // check to see we have the search button
    expect(find.text('Search'), findsOneWidget);

    // Tap the '+' icon and trigger a frame.
    await tester.tap(find.text('Search'));
    await tester.pumpAndSettle();

    // Verify that our search has completed.
    expect(find.byType(RaisedButton), findsNWidgets(1));
  });
  testWidgets("parse application table rows", (WidgetTester tester) async {
    String table = "";
    List<String> tr = getAppTable(table);
    // html page with no wineHq table should return an empty list.
    expect(tr.length, 0); 

    table = '<table class="whq-table"><th><td></td><td></td></th><tr>row1</tr><tr>row2</tr></table>';
    tr = getAppTable(table);
    // html page with wineHq table and 2 rows should yield a list of 3
    // 1 for each row and the first element the lead up to the first row
    expect(tr.length, 3);
  });
}
