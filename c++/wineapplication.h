#ifndef WINEAPPLICATION_H
#define WINEAPPLICATION_H

#include <QObject>
#include <QList>
#include "webget.h"

class AppVersion;

class WineApplication : public QObject
{
    Q_OBJECT
    QString appName;
    QString appId;
    QList<AppVersion*> versions;
    WebGet application;
public:
    explicit WineApplication(const QString &name, const QString& id,bool fetchOnCreate = false, QObject *parent = 0);
    QString getName();
signals:
    void versionsReady();
public slots:
    void getVersions();
private slots:
    void fetchUrlDone();
};

#endif // WINEAPPLICATION_H
