#include "mainwindow.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
{
    QWidget *centralWidget = new QWidget(this);
    setCentralWidget(centralWidget);
    mainLayout = new QHBoxLayout(centralWidget);

    createPlot();

    rightLayout = new QVBoxLayout();
    mainLayout->addLayout(rightLayout);

    createCheckBoxes();
    createFunctions();
    createStepsSlide();
    createSelection();
    createSliders();
    createGradient();
    createLanguage();

    rightLayout->addStretch();

    updatePlot();
}

MainWindow::~MainWindow()
{
}

void MainWindow::createPlot()
{
    QVBoxLayout *plotLayout = new QVBoxLayout();
    mainLayout->addLayout(plotLayout);
    plot = new Plot();
    plotLayout->addWidget(plot);
}

void MainWindow::createCheckBoxes()
{
    grid = new QCheckBox("Показ сетки");
    labels = new QCheckBox("Показ подписей");
    borders = new QCheckBox("Показ границ");

    grid->setChecked(true);
    labels->setChecked(true);
    borders->setChecked(true);

    rightLayout->addWidget(grid);
    rightLayout->addWidget(labels);
    rightLayout->addWidget(borders);

    connect(grid, &QCheckBox::stateChanged, this, &MainWindow::onCnangeGrid);
    connect(labels, &QCheckBox::stateChanged, this, &MainWindow::onCnangeLabels);
    connect(borders, &QCheckBox::stateChanged, this, &MainWindow::onCnangeBorders);
}

void MainWindow::createFunctions()
{
    functionGroup = new QGroupBox("Выберите функцию:", this);
    sincDistanceBtn = new QRadioButton("Sinc1", this);
    sincXZBtn = new QRadioButton("Sinc2", this);

    QVBoxLayout *functionLayout = new QVBoxLayout(functionGroup);
    functionLayout->addWidget(sincDistanceBtn);
    functionLayout->addWidget(sincXZBtn);
    functionGroup->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Fixed);

    sincDistanceBtn->setChecked(true);

    rightLayout->addWidget(functionGroup);

    connect(sincDistanceBtn, &QPushButton::clicked, this, &MainWindow::updatePlot);
    connect(sincXZBtn, &QPushButton::clicked, this, &MainWindow::updatePlot);
}

void MainWindow::createStepsSlide()
{
    labelX = new QLabel("Шаги на X = 50");
    stepsX = new QSlider(Qt::Horizontal, this);
    stepsX->setRange(0, 100);
    stepsX->setValue(50);

    labelZ = new QLabel("Шаги на Z = 50");
    stepsZ = new QSlider(Qt::Horizontal, this);
    stepsZ->setRange(0, 100);
    stepsZ->setValue(50);

    int slidersWidth = 150;
    stepsX->setFixedWidth(slidersWidth);
    stepsZ->setFixedWidth(slidersWidth);

    rightLayout->addWidget(labelX);
    rightLayout->addWidget(stepsX);
    rightLayout->addWidget(labelZ);
    rightLayout->addWidget(stepsZ);

    connect(stepsX, &QSlider::valueChanged, this, &MainWindow::onStepsXChanged);
    connect(stepsZ, &QSlider::valueChanged, this, &MainWindow::onStepsZChanged);
}

void MainWindow::createSelection()
{
    selectionGroup = new QGroupBox("Режим выделения:", this);
    noSelection = new QRadioButton("Без выделения", this);
    selection = new QRadioButton("Точка", this);

    QVBoxLayout *selectionLayout = new QVBoxLayout(selectionGroup);
    selectionLayout->addWidget(noSelection);
    selectionLayout->addWidget(selection);
    selectionGroup->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Fixed);

    selection->setChecked(true);

    rightLayout->addWidget(selectionGroup);

    connect(noSelection, &QPushButton::clicked, this, &MainWindow::updateSelection);
    connect(selection, &QPushButton::clicked, this, &MainWindow::updateSelection);
}

void MainWindow::updateSelection()
{
    if (noSelection->isChecked())
    {
        plot->setSelection(false);
        return;
    }
    plot->setSelection(true);
}

void MainWindow::createSliders()
{
    slidersX = new QGroupBox("Ось X [-10; 10]", this);

    MinSliderX = new QSlider(Qt::Horizontal, this);
    MinSliderX->setRange(-10, 10);
    MinSliderX->setValue(-10);

    MaxSliderX = new QSlider(Qt::Horizontal, this);
    MaxSliderX->setRange(-10, 10);
    MaxSliderX->setValue(10);

    QVBoxLayout *groupX = new QVBoxLayout(slidersX);
    groupX->addWidget(MinSliderX);
    groupX->addWidget(MaxSliderX);
    slidersX->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Fixed);
    slidersX->setFixedSize(170, 100);

    slidersZ = new QGroupBox("Ось Z [-10; 10]", this);

    MinSliderZ = new QSlider(Qt::Horizontal, this);
    MinSliderZ->setRange(-10, 10);
    MinSliderZ->setValue(-10);

    MaxSliderZ = new QSlider(Qt::Horizontal, this);
    MaxSliderZ->setRange(-10, 10);
    MaxSliderZ->setValue(10);

    QVBoxLayout *groupZ = new QVBoxLayout(slidersZ);
    groupZ->addWidget(MinSliderZ);
    groupZ->addWidget(MaxSliderZ);
    slidersZ->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Fixed);
    slidersZ->setFixedSize(170, 100);

    int slidersWidth = 150;
    MinSliderX->setFixedWidth(slidersWidth);
    MaxSliderX->setFixedWidth(slidersWidth);
    MinSliderZ->setFixedWidth(slidersWidth);
    MaxSliderZ->setFixedWidth(slidersWidth);

    rightLayout->addWidget(slidersX);
    rightLayout->addWidget(slidersZ);

    connect(MinSliderX, &QSlider::valueChanged, this, &MainWindow::onMinXChanged);
    connect(MaxSliderX, &QSlider::valueChanged, this, &MainWindow::onMaxXChanged);
    connect(MinSliderZ, &QSlider::valueChanged, this, &MainWindow::onMinZChanged);
    connect(MaxSliderZ, &QSlider::valueChanged, this, &MainWindow::onMaxZChanged);
}

void MainWindow::createGradient()
{
    colorGroupBox = new QGroupBox(QStringLiteral("Выбор градиента"));
    colorGroupBox->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Fixed);

    QLinearGradient a(0, 0, 1, 100);
    grB = a;
    grB.setColorAt(1.0, QColor(0, 0, 0));
    grB.setColorAt(0.67, QColor(255, 0, 0));
    grB.setColorAt(0.33, QColor(0, 255, 0));
    grB.setColorAt(0.0, QColor(0, 0, 255));
    QPixmap pm(24, 100);
    QPainter pmp(&pm);
    pmp.setBrush(QBrush(grB));
    pmp.setPen(Qt::NoPen);
    pmp.drawRect(0, 0, 40, 100);
    QPushButton *gradientBtoYPB = new QPushButton();
    gradientBtoYPB->setIcon(QIcon(pm));
    gradientBtoYPB->setIconSize(QSize(40, 100));

    QLinearGradient b(0, 0, 1, 100);
    grR = b;
    grR.setColorAt(1.0, QColor(0, 255, 255));
    grR.setColorAt(0.5, QColor(255, 0, 255));
    grR.setColorAt(0.2, QColor(255, 32, 255));
    grR.setColorAt(0.0, QColor(0, 0, 255));
    pmp.setBrush(QBrush(grR));
    pmp.drawRect(0, 0, 40, 100);
    QPushButton *gradientGtoRPB = new QPushButton();
    gradientGtoRPB->setIcon(QIcon(pm));
    gradientGtoRPB->setIconSize(QSize(40, 100));

    QLinearGradient c(0, 0, 1, 100);
    grG = c;
    grG.setColorAt(1.0, QColor(255, 40, 255));
    grG.setColorAt(0.5, QColor(40, 255, 255));
    grG.setColorAt(0.2, QColor(255, 255, 40));
    grG.setColorAt(0.0, QColor(40, 255, 40));
    pmp.setBrush(QBrush(grG));
    pmp.drawRect(0, 0, 40, 100);
    QPushButton *gradientGtoRPG = new QPushButton();
    gradientGtoRPG->setIcon(QIcon(pm));
    gradientGtoRPG->setIconSize(QSize(40, 100));

    QHBoxLayout *colorHBox = new QHBoxLayout;
    colorHBox->addWidget(gradientBtoYPB);
    colorHBox->addWidget(gradientGtoRPB);
    colorHBox->addWidget(gradientGtoRPG);
    colorGroupBox->setLayout(colorHBox);

    rightLayout->addWidget(colorGroupBox);

    connect(gradientBtoYPB, &QPushButton::pressed, this, &MainWindow::onGradientOne);
    connect(gradientGtoRPB, &QPushButton::pressed, this, &MainWindow::onGradientTwo);
    connect(gradientGtoRPG, &QPushButton::pressed, this, &MainWindow::onGradientThree);
}

void MainWindow::createLanguage()
{
    languageMenu = menuBar()->addMenu("Язык");
    QAction *englishAction = languageMenu->addAction("English");
    QAction *russianAction = languageMenu->addAction("Русский");

    connect(englishAction, &QAction::triggered, this, &MainWindow::switchToEnglish);
    connect(russianAction, &QAction::triggered, this, &MainWindow::switchToRussian);
}

void MainWindow::reTranslate()
{
    grid->setText(tr("Показ сетки"));
    labels->setText(tr("Показ подписей"));
    borders->setText(tr("Показ границ"));
    functionGroup->setTitle(tr("Выберите функцию:"));
    labelX->setText(tr("Шаги на X = ") + QString::number(stepsX->value()));
    labelZ->setText(tr("Шаги на Z = ") + QString::number(stepsZ->value()));
    selectionGroup->setTitle(tr("Режим выделения:"));
    noSelection->setText(tr("Без выделения"));
    selection->setText(tr("Точка"));
    updateSlidersX();
    updateSlidersZ();
    colorGroupBox->setTitle(tr("Выбор градиента"));
    languageMenu->setTitle(tr("Язык"));
}

void MainWindow::switchToEnglish()
{
    changeLanguage("en_GB");
}

void MainWindow::switchToRussian()
{
    changeLanguage("ru_RU");
}

void MainWindow::changeLanguage(const QString &language)
{
    qtLanguageTranslator.load(":/i18n/Lab-4-Cpp-Qt_" + language);
    qApp->installTranslator(&qtLanguageTranslator);
    reTranslate();
}

void MainWindow::updatePlot()
{
    if (sincDistanceBtn->isChecked())
    {
        plot->drawSincDistance();

        if (funcOneColor == 1)
        {
            onGradientOne();
            return;
        }
        if (funcOneColor == 2)
        {
            onGradientTwo();
            return;
        }
        onGradientThree();
        return;
    }

    plot->drawSincXZ();

    if (funcTwoColor == 1)
    {
        onGradientOne();
        return;
    }
    if (funcTwoColor == 2)
    {
        onGradientTwo();
        return;
    }
    onGradientThree();
}

void MainWindow::onGradientOne()
{
    plot->setGradient(grB);

    if (sincDistanceBtn->isChecked())
    {
        funcOneColor = 1;
        return;
    }
    funcTwoColor = 1;
}

void MainWindow::onGradientTwo()
{
    plot->setGradient(grR);

    if (sincDistanceBtn->isChecked())
    {
        funcOneColor = 2;
        return;
    }
    funcTwoColor = 2;
}

void MainWindow::onGradientThree()
{
    plot->setGradient(grG);

    if (sincDistanceBtn->isChecked())
    {
        funcOneColor = 3;
        return;
    }
    funcTwoColor = 3;
}

void MainWindow::onMinXChanged(int value)
{
    if (value > MaxSliderX->value())
    {
        MaxSliderX->setValue(value);
    }
    plot->updateMinX(value);
    updateSlidersX();
}

void MainWindow::onMaxXChanged(int value)
{
    if (value < MinSliderX->value())
    {
        MinSliderX->setValue(value);
    }
    plot->updateMaxX(value);
    updateSlidersX();
}

void MainWindow::onMinZChanged(int value)
{
    if (value > MaxSliderZ->value())
    {
        MaxSliderZ->setValue(value);
    }
    plot->updateMinZ(value);
    updateSlidersZ();
}

void MainWindow::onMaxZChanged(int value)
{
    if (value < MinSliderZ->value())
    {
        MinSliderZ->setValue(value);
    }
    plot->updateMaxZ(value);
    updateSlidersZ();
}

void MainWindow::updateSlidersX()
{
    slidersX->setTitle(tr("Ось X") + " [" + QString::number(plot->getLeftX()) + "; " + QString::number(plot->getRightX()) + "]");
}

void MainWindow::updateSlidersZ()
{
    slidersZ->setTitle(tr("Ось Z") + " [" + QString::number(plot->getLeftZ()) + "; " + QString::number(plot->getRightZ()) + "]");
}

void MainWindow::onStepsXChanged(int value)
{
    plot->updateStepsX(value);
    updatePlot();
    labelX->setText(tr("Шаги на X = ") + QString::number(stepsX->value()));
}

void MainWindow::onStepsZChanged(int value)
{
    plot->updateStepsZ(value);
    updatePlot();
    labelZ->setText(tr("Шаги на Z = ") + QString::number(stepsZ->value()));
}

void MainWindow::onCnangeGrid()
{
    plot->changeGrid();
}

void MainWindow::onCnangeLabels()
{
    plot->changeLabels();
//    borders->setChecked(labels->isChecked());
}

void MainWindow::onCnangeBorders()
{
    plot->changeBorders();
}
