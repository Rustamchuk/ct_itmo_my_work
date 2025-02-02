#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include "Plot.h"
#include <QMainWindow>
#include <QtWidgets/QApplication>
#include <QtDataVisualization/Q3DSurface>
#include <QtDataVisualization/QSurfaceDataProxy>
#include <QtDataVisualization/QSurface3DSeries>
#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QWidget>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QRadioButton>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QVBoxLayout>
#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QLabel>
#include <QtWidgets/QGroupBox>
#include <QSettings>
#include <QtWidgets/QDockWidget>
#include <QtWidgets/QGroupBox>
#include <QtWidgets/QCheckBox>
#include <QTranslator>
#include <QMenu>
#include <QMenuBar>
#include <QComboBox>
#include <QPainter>

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private:
    QHBoxLayout *mainLayout;
    QVBoxLayout *rightLayout;
    Plot *plot;

    void createPlot();
    void createCheckBoxes();
    void createFunctions();
    void createStepsSlide();
    void createSelection();
    void createSliders();
    void createGradient();
    void createLanguage();

    QCheckBox *grid;
    QCheckBox *labels;
    QCheckBox *borders;

    void onCnangeGrid();
    void onCnangeLabels();
    void onCnangeBorders();

    QSlider *stepsX;
    QSlider *stepsZ;
    QLabel *labelX;
    QLabel *labelZ;

    void onStepsXChanged(int value);
    void onStepsZChanged(int value);

    QSlider *MinSliderX;
    QSlider *MaxSliderX;
    QSlider *MinSliderZ;
    QSlider *MaxSliderZ;
    QGroupBox *slidersX;
    QGroupBox *slidersZ;

    void onMinXChanged(int value);
    void onMaxXChanged(int value);
    void onMinZChanged(int value);
    void onMaxZChanged(int value);
    void updateSlidersX();
    void updateSlidersZ();

    QGroupBox *functionGroup;
    QRadioButton *sincDistanceBtn;
    QRadioButton *sincXZBtn;

    void updatePlot();

    QMenu *languageMenu;
    QTranslator qtLanguageTranslator;

    void changeLanguage(const QString &language);
    void switchToRussian();
    void switchToEnglish();
    void reTranslate();

    QGroupBox *colorGroupBox;
    QLinearGradient grR;
    QLinearGradient grG;
    QLinearGradient grB;
    int funcOneColor = 1;
    int funcTwoColor = 1;

    void onGradientOne();
    void onGradientTwo();
    void onGradientThree();

    QGroupBox *selectionGroup;
    QRadioButton *noSelection;
    QRadioButton *selection;

    void updateSelection();
};

#endif // MAINWINDOW_H
