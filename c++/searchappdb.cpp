#include "searchappdb.h"
#include "wineapplication.h"
#include "webget.h"
#include <QtNetwork>
#include <QTextDocument>
#include <QTextBlock>

SearchAppDB::SearchAppDB(QObject *parent)
    : QObject(parent),filters(),searchKey("sappFamily-appNameData")
{
    this->wine_appdb = new WebGet("https://appdb.winehq.org/objectManager.php");
    this->getDefaultFilters(this->filters);
    connect(this->wine_appdb, SIGNAL(dataReady()),this, SLOT(processSearchData()));
}

void SearchAppDB::processSearchData()
{
    QList<WineApplication*> res;
    QString data = this->wine_appdb->getLine("<table");
    QTextDocument doc;
    doc.setHtml(data);
    QTextBlock tb = doc.begin();
    QString Name,Id;
    for(tb=tb.next();tb != doc.end();tb=tb.next()) {
      // skip the table headers
      if(tb.blockNumber() < 4)
        continue;
      switch(tb.blockNumber() % 3) {
      case 1:
        Name = tb.text();
        break;
      case 2:
        Id = tb.text();
        break;
      case 0:
        res.append(new WineApplication(Name,Id));
        break;
      }
    }
    emit returnFromWineHQ(res);
}

void SearchAppDB::setSearchFilters(const QUrlQuery &_filters)
{
    for(QPair<QString,QString> data : _filters.queryItems()) {
        if(this->filters.hasQueryItem(data.first) && this->filters.queryItemValue(data.first) != data.second ) {
            this->filters.removeAllQueryItems(data.first);
        }
        this->filters.addQueryItem(data.first,data.second);
    }
    this->wine_appdb->setFormData(this->filters);
}

void SearchAppDB::getDefaultFilters(QUrlQuery &query)
{
    query.addQueryItem("bIsQueue", "false");
    query.addQueryItem("sClass", "application");  // this is what we are looking for
    query.addQueryItem("sTitle", "Browse+Applications");
    query.addQueryItem("iItemsPerPage", "30"); // get this many items at a time
    query.addQueryItem("iPage", "1");  // first page
    query.addQueryItem("sOrderBy", "appName");
    query.addQueryItem("bAscending", "true");
    /* the default POST data from the web form */
    query.addQueryItem("iappVersion-ratingOp", "5");
    query.addQueryItem("iappCategoryOp", "11");
    query.addQueryItem("iappVersion-licenseOp", "5");
    query.addQueryItem("sappVersion-ratingData", ""); // Platinum, Gold, Silver, Bronze, Garbage
    query.addQueryItem("iversions-idOp", ""); // 5 =,6 <, 7 >
    query.addQueryItem("sversions-idData", ""); // > 242 & < 131
    query.addQueryItem("sappCategoryData", ""); // short int 1-999 (i think)
    query.addQueryItem("sappVersion-licenseData", ""); // Retail, Open Source, Free to use, Free to use and share, Demo, Shareware
    query.addQueryItem("iappFamily-appNameOp", "2"); // 2 = contains, 3 = starts with, 4 = ends with
    query.addQueryItem("ionlyDownloadableOp", "10");
    query.addQueryItem("sFilterSubmit", "Update Filter"); // the web submit button
    // query.addQueryItem("sonlyDownloadableData", "true"); // unchecked by default
}

void SearchAppDB::searchWineHQ(const QString &name)
{
    if (this->filters.hasQueryItem(this->searchKey))
        this->filters.removeAllQueryItems(this->searchKey);
    this->filters.addQueryItem(this->searchKey,name);
    this->wine_appdb->setFormData(this->filters);
    this->wine_appdb->executeRequest();
}
