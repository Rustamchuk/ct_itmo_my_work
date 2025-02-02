#include "Plot.h"

Plot::Plot()
{
    proxy = new QSurfaceDataProxy();
    series = new QSurface3DSeries(proxy);

    QSurfaceDataArray dataArray;

    graph = new Q3DSurface();
    graph->addSeries(series);

    createGraph();
}

Plot::~Plot()
{
    delete proxy;
}

void Plot::createGraph()
{
    QVBoxLayout *layout = new QVBoxLayout(this);
    layout->addWidget(QWidget::createWindowContainer(graph, this));
    graph->activeTheme()->setLabelTextColor(Qt::black);

    graph->axisX()->setTitle("X");
    graph->axisX()->setTitleVisible(true);
    graph->axisY()->setTitle("Y");
    graph->axisY()->setTitleVisible(true);
    graph->axisZ()->setTitle("Z");
    graph->axisZ()->setTitleVisible(true);
    graph->axisX()->setRange(-10, 10);
    graph->axisZ()->setRange(-10, 10);

    setLayout(layout);
}

void Plot::setGradient(const QLinearGradient &gradient)
{
    QLinearGradient gr = gradient;
    series->setBaseGradient(gr);
    series->setColorStyle(Q3DTheme::ColorStyle::ColorStyleObjectGradient);
}

void Plot::setSelection(bool enabled)
{
    if (enabled)
    {
        graph->setSelectionMode(QAbstract3DGraph::SelectionItem);
        graph->setShadowQuality(QAbstract3DGraph::ShadowQualitySoftLow);
        return;
    }
    graph->setSelectionMode(QAbstract3DGraph::SelectionNone);
    graph->setShadowQuality(QAbstract3DGraph::ShadowQualityNone);
}

void Plot::changeGrid()
{
    if (graph->activeTheme()->isGridEnabled())
    {
        graph->activeTheme()->setGridEnabled(false);
        return;
    }
    graph->activeTheme()->setGridEnabled(true);
}

void Plot::changeLabels()
{
    if (graph->activeTheme()->labelTextColor() == Qt::black)
    {
        graph->activeTheme()->setLabelTextColor(Qt::white);
        return;
    }
    graph->activeTheme()->setLabelTextColor(Qt::black);
}

void Plot::changeBorders()
{
    if (graph->activeTheme()->isLabelBorderEnabled())
    {
        graph->activeTheme()->setLabelBorderEnabled(false);
        return;
    }
    graph->activeTheme()->setLabelBorderEnabled(true);
}

void Plot::drawSincDistance()
{
    int size = right - left;
    stepX = size / float(stepsX - 1);
    stepZ = size / float(stepsZ - 1);

    QSurfaceDataArray *dataArray = new QSurfaceDataArray;
    for (int i = 0 ; i < stepsZ ; i++)
    {
        QSurfaceDataRow *newRow = new QSurfaceDataRow(stepsX);
        float z = qMin(float(right), (i * stepZ + left));
        int index = 0;
        for (int j = 0; j < stepsX; j++)
        {
            float x = qMin(float(right), (j * stepX + left));
            float distance = qSqrt(z * z + x * x);
            float y = (distance == 0) ? 1 : (qSin(distance) / distance);
            (*newRow)[index++].setPosition(QVector3D(x, y, z));
        }
        *dataArray << newRow;
    }

    proxy->resetArray(dataArray);
}

void Plot::drawSincXZ()
{
    int size = right - left;
    stepX = size / float(stepsX - 1);
    stepZ = size / float(stepsZ - 1);

    QSurfaceDataArray *dataArray = new QSurfaceDataArray;
    for (int i = 0 ; i < stepsZ ; i++)
    {
        QSurfaceDataRow *newRow = new QSurfaceDataRow(stepsX);
        float z = qMin(float(right), (i * stepZ + left));
        int index = 0;
        for (int j = 0; j < stepsX; j++)
        {
            float x = qMin(float(right), (j * stepX + left));
            float sincX = (x == 0) ? 1 : std::sin(x) / x;
            float sincZ = (z == 0) ? 1 : std::sin(z) / z;
            float y = sincX * sincZ;
            (*newRow)[index++].setPosition(QVector3D(x, y, z));
        }
        *dataArray << newRow;
    }

    proxy->resetArray(dataArray);
}

void Plot::updateMinX(int value)
{
    float minX = value;
    float maxX = graph->axisX()->max();
    graph->axisX()->setRange(minX, maxX);
}

void Plot::updateMaxX(int value)
{
    float maxX = value;
    float minX = graph->axisX()->min();
    graph->axisX()->setRange(minX, maxX);
}

void Plot::updateMinZ(int value)
{
    float minZ = value;
    float maxZ = graph->axisZ()->max();
    graph->axisZ()->setRange(minZ, maxZ);
}

void Plot::updateMaxZ(int value)
{
    float maxZ = value;
    float minZ = graph->axisZ()->min();
    graph->axisZ()->setRange(minZ, maxZ);
}

void Plot::updateStepsX(int value)
{
    stepsX = value;
}

void Plot::updateStepsZ(int value)
{
    stepsZ = value;
}
