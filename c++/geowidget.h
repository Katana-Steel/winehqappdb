#ifndef GEOWIDGET_H
#define GEOWIDGET_H

#include <QWidget>
#include <QSize>

class GeoWidget : public QWidget
{
    Q_OBJECT
    virtual void resizeEvent(QResizeEvent *);
public:
    explicit GeoWidget(QWidget *parent = 0);

signals:
    void sizeChanged(const QSize &);
public slots:
};

#endif // GEOWIDGET_H
