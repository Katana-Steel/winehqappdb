#include "appversion.h"

AppVersion::AppVersion(const QStringList &str, QObject *parent)
    : QObject(parent)
{
    if(str.count() == 3) {
        this->version = str.at(0);
        this->rating = str.at(1);
        this->winever = str.at(2);
    }
}

const QString &AppVersion::getAppVersion()
{
    return this->version;
}

const QString &AppVersion::getRating()
{
    return this->rating;
}

const QString &AppVersion::getWineVersion()
{
    return this->winever;
}

