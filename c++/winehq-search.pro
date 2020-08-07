#-------------------------------------------------
#
# Project created by QtCreator 2016-01-31T12:05:12
#
#-------------------------------------------------

QT       += core gui network
CONFIG += c++11
greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = winehq-search
TEMPLATE = app


SOURCES += main.cpp\
        mainUi.cpp \
    wineapplication.cpp \
    appversion.cpp \
    webget.cpp \
    geowidget.cpp \
    searchappdb.cpp

HEADERS  += mainUi.h \
    wineapplication.h \
    appversion.h \
    webget.h \
    geowidget.h \
    searchappdb.h

FORMS    += mainUi.ui

DISTFILES += \
    simpleUi.qml
