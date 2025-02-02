#ifndef PLOT_H
#define PLOT_H

#include <QWidget>
#include <QtDataVisualization/Q3DSurface>
#include <QtWidgets/QSlider>
#include <QVBoxLayout>
#include <QtDataVisualization/Q3DSurface>
#include <QtDataVisualization/QSurface3DSeries>
#include <QtDataVisualization/QSurfaceDataProxy>
#include <QtCore/qmath.h>

//using namespace QtDataVisualization;

class Plot : public QWidget {
    Q_OBJECT

public:
    Plot();
    ~Plot();

    void drawSincDistance();
    void drawSincXZ();

    void updateMinX(int value);
    void updateMaxX(int value);
    void updateMinZ(int value);
    void updateMaxZ(int value);

    void updateStepsX(int value);
    void updateStepsZ(int value);

    void setGradient(const QLinearGradient &gradient);

    void setSelection(bool enabled);

    void changeGrid();
    void changeLabels();
    void changeBorders();

    int getLeftX() { return graph->axisX()->min(); }
    int getRightX() { return graph->axisX()->max(); }
    int getLeftZ() { return graph->axisZ()->min(); }
    int getRightZ() { return graph->axisZ()->max(); }

private:
    int left = -10;
    int right = 10;
    int stepsX = 50;
    int stepsZ = 50;
    float stepX;
    float stepZ;

    Q3DSurface *graph;
    QSurfaceDataProxy *proxy;
    QSurface3DSeries *series;
    QSurfaceDataArray dataArray;

    void createGraph();
};

#endif // PLOT_H
