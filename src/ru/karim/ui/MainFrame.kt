package ru.karim.ui.painting

import ru.karim.ui.painting.CartesianPainter
import ru.karim.ui.painting.CartesianPlane
import ru.karim.ui.painting.FunctionPainter
import ru.karim.ui.painting.Painter
//import ru.karim.ui.painting.PointsPainter
import java.awt.Color
import java.awt.Dimension
import java.awt.event.*
import java.awt.event.ItemEvent.DESELECTED
import java.awt.event.ItemEvent.SELECTED
import javax.swing.*
import javax.swing.JColorChooser.showDialog
import kotlin.math.*


class MainFrame : JFrame() {


    val minDim = Dimension(600, 600)
    val mainPanel: GraphicsPanel
    val controlPanel: JPanel
    val XMin: JLabel
    val XMax: JLabel
    val YMin: JLabel
    val YMax: JLabel
    val set2: JLabel
    val set3: JLabel
    val xMin: JSpinner
    val xMax: JSpinner
    val yMin: JSpinner
    val yMax: JSpinner
    val cbPoints: JCheckBox
    val cbGraphic: JCheckBox
    val cbParam: JCheckBox
    val polynomColorPanel: JPanel
    val paramColorPanel: JPanel
    val xMinM: SpinnerNumberModel
    val xMaxM: SpinnerNumberModel
    val yMinM: SpinnerNumberModel
    val yMaxM: SpinnerNumberModel
    val tMin: JTextField
    val tMax: JTextField

    init {
        minimumSize = minDim
        defaultCloseOperation = EXIT_ON_CLOSE
        xMinM = SpinnerNumberModel(-5.0, -100.0, 4.9, 0.1)
        xMin = JSpinner(xMinM)
        xMaxM = SpinnerNumberModel(5.0, -4.9, 100.0, 0.1)
        xMax = JSpinner(xMaxM)
        yMinM = SpinnerNumberModel(-5.0, -100.0, 4.9, 0.1)
        yMin = JSpinner(yMinM)
        yMaxM = SpinnerNumberModel(5.0, -4.9, 100.0, 0.1)
        yMax = JSpinner(yMaxM)
        cbPoints = JCheckBox()
        tMin = JTextField("0.001")
        tMax = JTextField("5.0")
        cbGraphic = JCheckBox()
        cbParam = JCheckBox()
        polynomColorPanel = JPanel()
        polynomColorPanel.background = Color.BLUE
        polynomColorPanel.setSize(1, 1)
        paramColorPanel = JPanel()
        paramColorPanel.background = Color.GREEN
        polynomColorPanel.setSize(1, 1)

        controlPanel = JPanel().apply { background = Color.WHITE }


        val plane = CartesianPlane(
            xMin.value as Double,
            xMax.value as Double,
            yMin.value as Double,
            yMax.value as Double
        )

        val cartesianPainter = CartesianPainter(plane)

        var t1 = tMin.getText().toDouble()
        var t2 = tMax.getText().toDouble()

        val paramPainter = ParamPainter(plane, t1, t2)
        paramPainter.funColor = paramColorPanel.background
        paramPainter.functionX = {t:Double -> ln(t) *sin(t)}
        paramPainter.functionY = {t:Double -> cos(t)}


        val func = FunctionPainter(plane)
        func.function = {x:Double -> (x+3)/(2*x-1)}
        func.funColor = polynomColorPanel.background

        val painters = mutableListOf<Painter>(cartesianPainter)
        mainPanel = GraphicsPanel(painters).apply {
            background = Color.WHITE
        }

        cbGraphic.addItemListener(object : ItemListener {
            override fun itemStateChanged(e: ItemEvent?) {
                if (e?.stateChange == SELECTED) {
                    painters.add(func)
                    mainPanel.repaint()
                } else if (e?.stateChange == DESELECTED) {
                    painters.remove(func)
                    mainPanel.repaint()
                }
            }
        })
        cbParam.addItemListener(object : ItemListener {
            override fun itemStateChanged(e: ItemEvent?) {
                if (e?.stateChange == SELECTED) {
                    painters.add(paramPainter)
                    mainPanel.repaint()
                } else if (e?.stateChange == DESELECTED) {
                    painters.removeAt(painters.size - 1)
                    mainPanel.repaint()
                }
            }
        })

        polynomColorPanel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e?.button == 1) {
                    val color = JColorChooser.showDialog(null, "Выберите цвет функции", polynomColorPanel.background)
                    polynomColorPanel.background = color
                    func.funColor = color
                    mainPanel.repaint()
                }

            }
        })
        tMin.addActionListener() {
            paramPainter.t_min = tMin.text.toDouble()
            mainPanel.repaint()
        }

        tMax.addActionListener() {
            paramPainter.t_max = tMax.text.toDouble()
            mainPanel.repaint()
        }
        paramColorPanel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e?.button == 1) {
                    val color = JColorChooser.showDialog(null, "Выберите цвет функции", paramColorPanel.background)
                    paramColorPanel.background = color
                    paramPainter.funColor = color
                    mainPanel.repaint()
                }

            }
        })

        mainPanel.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                plane.width = mainPanel.width
                plane.height = mainPanel.height
                mainPanel.repaint()
            }

        })

        xMin.addChangeListener {
            xMaxM.minimum = xMin.value as Double + 0.1
            plane.xSegment = Pair(xMin.value as Double, xMax.value as Double)
            mainPanel.repaint()
        }
        xMax.addChangeListener {
            xMinM.maximum = xMax.value as Double - 0.1
            plane.xSegment = Pair(xMin.value as Double, xMax.value as Double)
            mainPanel.repaint()
        }
        yMin.addChangeListener {
            yMaxM.minimum = yMin.value as Double + 0.1
            plane.ySegment = Pair(yMin.value as Double, yMax.value as Double)
            mainPanel.repaint()
        }
        yMax.addChangeListener {
            yMinM.maximum = yMax.value as Double - 0.1
            plane.ySegment = Pair(yMin.value as Double, yMax.value as Double)
            mainPanel.repaint()
        }

        XMin = JLabel("XMin:")
        XMax = JLabel("XMax:")
        YMin = JLabel("YMin:")
        YMax = JLabel("YMax:")
        set2 = JLabel("Отображать график явно заданной функции")
        set3 = JLabel("Отображать график параметрически заданной функции")

        controlPanel.layout = GroupLayout(controlPanel).apply {
//            setAutoCreateGaps(true);
//            setAutoCreateContainerGaps(true);
            linkSize(XMin, xMin)
            linkSize(XMax, xMax)
            linkSize(YMin, yMin)
            linkSize(YMax, yMax)
            linkSize( polynomColorPanel, paramColorPanel, cbGraphic, cbParam)
            linkSize(tMax,tMin)


            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(10)
                    .addGroup(createParallelGroup().addComponent(XMin).addComponent(YMin))
                    .addGroup(
                        createParallelGroup().addComponent(
                            xMin,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE
                        ).addComponent(
                            yMin,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE
                        )
                    )
                    .addGap(10, 20, 20)
                    .addGroup(createParallelGroup().addComponent(XMax).addComponent(YMax))
                    .addGroup(
                        createParallelGroup().addComponent(
                            xMax,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE
                        ).addComponent(
                            yMax,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE
                        )
                    )
                    .addGap(50)
                    .addGroup(
                        createParallelGroup().addComponent(cbGraphic).addComponent(cbParam)
                    )
                    .addGroup(createParallelGroup().addComponent(set2).addComponent(set3))
                    .addGap(10)
                    .addGroup(
                        createParallelGroup().addComponent(polynomColorPanel)
                            .addComponent(paramColorPanel)
                    )
                    .addGap(10)
                    .addGroup(createParallelGroup().addComponent(tMin).addComponent(tMax))
            )
            setVerticalGroup(
                createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(
                    createSequentialGroup().addGroup(
                        createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(XMin)
                            .addComponent(
                                xMin,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addComponent(XMax)
                            .addComponent(
                                xMax,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                    )
                        .addGap(2)
                        .addGroup(
                            createParallelGroup()
                                .addComponent(YMin)
                                .addComponent(
                                    yMin,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(YMax)
                                .addComponent(
                                    yMax,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                        )
                ).addGroup(
                    createSequentialGroup()
                        .addGap(2)
                        .addGroup(
                            createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(cbGraphic)
                                .addComponent(set2)
                                .addComponent(polynomColorPanel)
                                .addComponent(tMin)
                        )
                        .addGap(2)
                        .addGroup(
                            createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(cbParam)
                                .addComponent(set3)
                                .addComponent(paramColorPanel)
                                .addComponent(tMax)
                        )
                )
            )
        }
        layout = GroupLayout(contentPane).apply {
            setAutoCreateGaps(true);
            setAutoCreateContainerGaps(true);
            setVerticalGroup(
                createSequentialGroup()
                    .addComponent(mainPanel)
                    .addComponent(controlPanel)
            )
            setHorizontalGroup(
                createParallelGroup()
                    .addComponent(mainPanel)
                    .addComponent(controlPanel)
            )
        }

        pack()
        plane.width = mainPanel.width
        plane.height = mainPanel.height

    }
}