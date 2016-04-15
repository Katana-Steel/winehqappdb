#include "wineapplication.h"
#include "appversion.h"
#include <QTextDocument>
#include <QTextBlock>
#include <QStringList>
#include <QUrlQuery>

WineApplication::WineApplication(const QString &name,const QString &id, bool fetchOnCreate, QObject *parent)
    : QObject(parent),appName(name),appId(id),versions(),application("https://appdb.winehq.org/objectManager.php",parent)
{
  if(fetchOnCreate)
      getVersions();
  connect(&this->application,SIGNAL(dataReady()),this,SLOT(fetchUrlDone()));
}

QString WineApplication::getName()
{
    return this->appName;
}

void WineApplication::getVersions()
{
    QUrlQuery param;
    param.addQueryItem("sClass","application");
    param.addQueryItem("iId",appId);
    this->application.setFormData(param);
    this->application.executeRequest();
}

void WineApplication::fetchUrlDone()
{
    QTextDocument doc(this);
    doc.setHtml(application.getLine("sClass=version"));
    int lines = (doc.blockCount()-8) / 6;
    for(int i = 0;i< lines;i++) {
        QStringList Version;
        Version << doc.findBlockByNumber(((i+1) * 6)+1).text();
        Version << doc.findBlockByNumber(((i+1) * 6)+3).text();
        Version << doc.findBlockByNumber(((i+1) * 6)+4).text();
        versions.append(new AppVersion(Version,this));
    }

}
