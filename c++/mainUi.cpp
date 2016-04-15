#include "mainUi.h"
#include "ui_mainUi.h"
#include "wineapplication.h"
#include <QDialog>
#include <QList>
#include "searchappdb.h"

Widget::Widget(QWidget *parent) :
    GeoWidget(parent),
    ui(new Ui::Widget)
{
    ui->setupUi(this);
    search = new SearchAppDB(this);
    connect(this, SIGNAL(sizeChanged(const QSize&)),this, SLOT(on_Widget_sizeChanged(const QSize &)));
    connect(search, SIGNAL(returnFromWineHQ(QList<WineApplication*>)), this, SLOT(dispResults(const QList<WineApplication*>&)));
}

Widget::~Widget()
{
    delete search;
    delete ui;
}

void Widget::on_Widget_sizeChanged(const QSize &size)
{
    bool vis = ui->filter_dlg->isVisible();
    bool changed = false;
    if(size.width() > 400 && !vis) {
        vis = !vis;
        changed = true;
    } else if (size.width() <= 400 && vis) {
        vis = !vis;
        changed = true;
    }
    if (changed) {
        ui->filter_dlg->setVisible(vis);
        ui->menu->setVisible(!vis);
    }
}

void Widget::on_setFilters_clicked()
{
    QDialog dlg;
    ui->filter_dlg->setParent(&dlg);
    ui->filter_dlg->setVisible(true);
    dlg.exec();
    ui->filter_dlg->setVisible(false);
    ui->horizontalLayout_3->insertWidget(0,ui->filter_dlg);
}

void Widget::on_searchBtn_clicked()
{
    this->doSearch();
}

void Widget::on_lineEdit_returnPressed()
{
    this->doSearch();
}

void Widget::doSearch()
{
    this->search->searchWineHQ(ui->lineEdit->text());
}

void Widget::dispResults(const QList<WineApplication*> &from_wine_db)
{
    ui->listWidget->clear();
    for( WineApplication* app: from_wine_db) {
      ui->listWidget->addItem(app->getName());
    }
}
