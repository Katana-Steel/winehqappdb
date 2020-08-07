// This is a basic Flutter widget test.
//
// To perform an interaction with a widget in your test, use the WidgetTester
// utility that Flutter provides. For example, you can send tap and scroll
// gestures. You can also use WidgetTester to find child widgets in the widget
// tree, read text, and verify that the values of widget properties are correct.

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:winehqappdb/main.dart';

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
}
