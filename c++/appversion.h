#ifndef APPVERSION_H
#define APPVERSION_H

#include <QObject>
#include <QStringList>

class AppVersion : public QObject
{
    Q_OBJECT
    QString version;
    QString rating;
    QString winever;
public:
    explicit AppVersion(const QStringList& str=QStringList(),QObject *parent = 0);

signals:


public slots:
    const QString &getAppVersion();
    const QString &getRating();
    const QString &getWineVersion();
};

#endif // APPVERSION_H
