#include "webget.h"
#include <QTextStream>
#include <QtNetwork/QtNetwork>

WebGet::WebGet(const QString &url, QObject *parent)
    : QNetworkAccessManager(parent),baseUrl(url),form(),dataReply(this)
{
    connect(this,SIGNAL(sslErrors(QNetworkReply*,const QList<QSslError>&)),this,SLOT(handleSSL(QNetworkReply*,const QList<QSslError> &)));
    connect(this,SIGNAL(finished(QNetworkReply*)), this, SLOT(readFromServer(QNetworkReply*)));
}

QString WebGet::getLine(const QString &contains)
{
    this->dataReply.open(QBuffer::ReadOnly);
    this->dataReply.seek(0);
    QString line;
    bool found=false;
    QTextStream data(&this->dataReply);
    do {
        line = data.readLine();
        if (line.contains(contains))
            found = true;
    } while(!line.isNull() && !found);
    this->dataReply.close();
    return line;
}

void WebGet::setFormData(const QUrlQuery &data)
{
    this->form.clear();
    this->form.setQueryItems(data.queryItems());
}

void WebGet::readFromServer(QNetworkReply *reply)
{
    if(reply->error() == QNetworkReply::NoError) {
        this->dataReply.setData(reply->readAll());
        emit dataReady();
    }
}

void WebGet::handleSSL(QNetworkReply *reply, const QList<QSslError> & /* errors */)
{
    reply->ignoreSslErrors();
}

void WebGet::executeRequest()
{
    QNetworkRequest req(this->baseUrl);
    req.setRawHeader("User-Agent","WineHQ/1.2 App Browser");
    req.setHeader(QNetworkRequest::ContentTypeHeader, "application/x-www-form-urlencoded");
    post(req,this->form.toString().toLatin1());
}

