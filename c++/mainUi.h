#ifndef MAINUI_H
#define MAINUI_H

#include "geowidget.h"
#include <QList>

namespace Ui {
class Widget;
}

class SearchAppDB;
class WineApplication;

class Widget : public GeoWidget
{
    Q_OBJECT

public:
    explicit Widget(QWidget *parent = 0);
    ~Widget();

private slots:
    void on_Widget_sizeChanged(const QSize &);

    void on_setFilters_clicked();

    void on_searchBtn_clicked();

    void on_lineEdit_returnPressed();

    void dispResults(const QList<WineApplication*>&);

private:
    Ui::Widget *ui;
    SearchAppDB *search;
    void doSearch();
};

#endif // MAINUI_H
