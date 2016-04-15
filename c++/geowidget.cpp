#include "geowidget.h"
#include <QResizeEvent>

void GeoWidget::resizeEvent(QResizeEvent *evt)
{
    emit sizeChanged(evt->size());
}

GeoWidget::GeoWidget(QWidget *parent) : QWidget(parent)
{

}

