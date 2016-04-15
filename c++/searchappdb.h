#ifndef SEARCHAPPDB_H
#define SEARCHAPPDB_H

#include <QObject>
#include <QList>
#include <QUrlQuery>

class WebGet;
class WineApplication;

class SearchAppDB : public QObject
{
    Q_OBJECT
    WebGet *wine_appdb;
    QUrlQuery filters;
    QString searchKey;
    void getDefaultFilters(QUrlQuery&);
public:
    explicit SearchAppDB(QObject *parent = 0);

    void setSearchFilters(const QUrlQuery&);
signals:
    void returnFromWineHQ(QList<WineApplication*>);

private slots:
    void processSearchData();

public slots:
    void searchWineHQ(const QString&);
};

#endif // SEARCHAPPDB_H
