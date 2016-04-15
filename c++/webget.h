#ifndef WEBGET_H
#define WEBGET_H

#include <QtNetwork/QNetworkAccessManager>
#include <QUrl>
#include <QUrlQuery>
#include <QList>
#include <QBuffer>

class WebGet : public QNetworkAccessManager
{
    Q_OBJECT
    QUrl baseUrl;
    QUrlQuery form;
    QBuffer dataReply;
public:
    WebGet(const QString& url,QObject *parent=0);

    QString getLine(const QString& contains);
    void setFormData(const QUrlQuery &data);
private slots:
    void readFromServer(QNetworkReply*);
    void handleSSL(QNetworkReply*,const QList<QSslError>&);
signals:
    void dataReady();
public slots:
    void executeRequest();
};

#endif // WEBGET_H
