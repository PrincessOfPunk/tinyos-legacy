# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'osci_widget.ui'
#
# Created: Wed Oct 14 09:55:45 2009
#      by: PyQt4 UI code generator 4.6
#
# WARNING! All changes made in this file will be lost!

from PyQt4 import QtCore, QtGui

class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        MainWindow.setObjectName("MainWindow")
        MainWindow.resize(1027, 601)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Expanding, QtGui.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(MainWindow.sizePolicy().hasHeightForWidth())
        MainWindow.setSizePolicy(sizePolicy)
        MainWindow.setBaseSize(QtCore.QSize(400, 300))
        icon = QtGui.QIcon()
        icon.addPixmap(QtGui.QPixmap("../../../../../../../../../usr/share/icons/oxygen/16x16/apps/esd.png"), QtGui.QIcon.Normal, QtGui.QIcon.Off)
        MainWindow.setWindowIcon(icon)
        self.centralwidget = QtGui.QWidget(MainWindow)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Expanding, QtGui.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.centralwidget.sizePolicy().hasHeightForWidth())
        self.centralwidget.setSizePolicy(sizePolicy)
        self.centralwidget.setMinimumSize(QtCore.QSize(900, 500))
        self.centralwidget.setObjectName("centralwidget")
        self.horizontalLayout = QtGui.QHBoxLayout(self.centralwidget)
        self.horizontalLayout.setObjectName("horizontalLayout")
        self.horizontalLayout_2 = QtGui.QHBoxLayout()
        self.horizontalLayout_2.setObjectName("horizontalLayout_2")
        self.tabWidget = QtGui.QTabWidget(self.centralwidget)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Expanding, QtGui.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.tabWidget.sizePolicy().hasHeightForWidth())
        self.tabWidget.setSizePolicy(sizePolicy)
        self.tabWidget.setMinimumSize(QtCore.QSize(0, 0))
        self.tabWidget.setObjectName("tabWidget")
        self.tab = QtGui.QWidget()
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Expanding, QtGui.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(1)
        sizePolicy.setVerticalStretch(1)
        sizePolicy.setHeightForWidth(self.tab.sizePolicy().hasHeightForWidth())
        self.tab.setSizePolicy(sizePolicy)
        self.tab.setObjectName("tab")
        self.gridLayout = QtGui.QGridLayout(self.tab)
        self.gridLayout.setObjectName("gridLayout")
        self.plot_temp = KPlotWidget(self.tab)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Expanding, QtGui.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(1)
        sizePolicy.setVerticalStretch(1)
        sizePolicy.setHeightForWidth(self.plot_temp.sizePolicy().hasHeightForWidth())
        self.plot_temp.setSizePolicy(sizePolicy)
        self.plot_temp.setBackgroundColor(QtGui.QColor(255, 255, 255))
        self.plot_temp.setForegroundColor(QtGui.QColor(0, 0, 0))
        self.plot_temp.setProperty("grid", False)
        self.plot_temp.setObjectName("plot_temp")
        self.gridLayout.addWidget(self.plot_temp, 1, 2, 1, 1)
        self.label_temp = QtGui.QLabel(self.tab)
        self.label_temp.setObjectName("label_temp")
        self.gridLayout.addWidget(self.label_temp, 0, 2, 1, 1)
        self.tabWidget.addTab(self.tab, "")
        self.tab_4 = QtGui.QWidget()
        self.tab_4.setObjectName("tab_4")
        self.gridLayout_4 = QtGui.QGridLayout(self.tab_4)
        self.gridLayout_4.setObjectName("gridLayout_4")
        self.label_hum = QtGui.QLabel(self.tab_4)
        self.label_hum.setObjectName("label_hum")
        self.gridLayout_4.addWidget(self.label_hum, 0, 0, 1, 1)
        self.plot_hum = KPlotWidget(self.tab_4)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Expanding, QtGui.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(1)
        sizePolicy.setVerticalStretch(1)
        sizePolicy.setHeightForWidth(self.plot_hum.sizePolicy().hasHeightForWidth())
        self.plot_hum.setSizePolicy(sizePolicy)
        self.plot_hum.setBackgroundColor(QtGui.QColor(255, 255, 255))
        self.plot_hum.setForegroundColor(QtGui.QColor(0, 0, 0))
        self.plot_hum.setObjectName("plot_hum")
        self.gridLayout_4.addWidget(self.plot_hum, 1, 0, 1, 1)
        self.tabWidget.addTab(self.tab_4, "")
        self.tab_3 = QtGui.QWidget()
        self.tab_3.setObjectName("tab_3")
        self.gridLayout_3 = QtGui.QGridLayout(self.tab_3)
        self.gridLayout_3.setObjectName("gridLayout_3")
        self.label_par = QtGui.QLabel(self.tab_3)
        self.label_par.setObjectName("label_par")
        self.gridLayout_3.addWidget(self.label_par, 0, 0, 1, 1)
        self.plot_par = KPlotWidget(self.tab_3)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Expanding, QtGui.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(1)
        sizePolicy.setVerticalStretch(1)
        sizePolicy.setHeightForWidth(self.plot_par.sizePolicy().hasHeightForWidth())
        self.plot_par.setSizePolicy(sizePolicy)
        self.plot_par.setBackgroundColor(QtGui.QColor(255, 255, 255))
        self.plot_par.setForegroundColor(QtGui.QColor(0, 0, 0))
        self.plot_par.setObjectName("plot_par")
        self.gridLayout_3.addWidget(self.plot_par, 1, 0, 1, 1)
        self.tabWidget.addTab(self.tab_3, "")
        self.tab_5 = QtGui.QWidget()
        self.tab_5.setObjectName("tab_5")
        self.gridLayout_5 = QtGui.QGridLayout(self.tab_5)
        self.gridLayout_5.setObjectName("gridLayout_5")
        self.label_tsr = QtGui.QLabel(self.tab_5)
        self.label_tsr.setObjectName("label_tsr")
        self.gridLayout_5.addWidget(self.label_tsr, 0, 0, 1, 1)
        self.plot_tsr = KPlotWidget(self.tab_5)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Expanding, QtGui.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(1)
        sizePolicy.setVerticalStretch(1)
        sizePolicy.setHeightForWidth(self.plot_tsr.sizePolicy().hasHeightForWidth())
        self.plot_tsr.setSizePolicy(sizePolicy)
        self.plot_tsr.setBackgroundColor(QtGui.QColor(255, 255, 255))
        self.plot_tsr.setForegroundColor(QtGui.QColor(0, 0, 0))
        self.plot_tsr.setObjectName("plot_tsr")
        self.gridLayout_5.addWidget(self.plot_tsr, 1, 0, 1, 1)
        self.tabWidget.addTab(self.tab_5, "")
        self.tab_2 = QtGui.QWidget()
        self.tab_2.setObjectName("tab_2")
        self.gridLayout_2 = QtGui.QGridLayout(self.tab_2)
        self.gridLayout_2.setObjectName("gridLayout_2")
        self.plot_volt = KPlotWidget(self.tab_2)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Preferred, QtGui.QSizePolicy.Preferred)
        sizePolicy.setHorizontalStretch(1)
        sizePolicy.setVerticalStretch(1)
        sizePolicy.setHeightForWidth(self.plot_volt.sizePolicy().hasHeightForWidth())
        self.plot_volt.setSizePolicy(sizePolicy)
        self.plot_volt.setBackgroundColor(QtGui.QColor(255, 255, 255))
        self.plot_volt.setForegroundColor(QtGui.QColor(0, 0, 0))
        self.plot_volt.setObjectName("plot_volt")
        self.gridLayout_2.addWidget(self.plot_volt, 1, 0, 1, 1)
        self.label_volt = QtGui.QLabel(self.tab_2)
        self.label_volt.setObjectName("label_volt")
        self.gridLayout_2.addWidget(self.label_volt, 0, 0, 1, 1)
        self.tabWidget.addTab(self.tab_2, "")
        self.horizontalLayout_2.addWidget(self.tabWidget)
        self.horizontalLayout.addLayout(self.horizontalLayout_2)
        MainWindow.setCentralWidget(self.centralwidget)
        self.menubar = QtGui.QMenuBar(MainWindow)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 1027, 22))
        self.menubar.setObjectName("menubar")
        self.menuFile = QtGui.QMenu(self.menubar)
        self.menuFile.setObjectName("menuFile")
        MainWindow.setMenuBar(self.menubar)
        self.statusbar = QtGui.QStatusBar(MainWindow)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Preferred, QtGui.QSizePolicy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.statusbar.sizePolicy().hasHeightForWidth())
        self.statusbar.setSizePolicy(sizePolicy)
        self.statusbar.setObjectName("statusbar")
        MainWindow.setStatusBar(self.statusbar)
        self.toolBar = QtGui.QToolBar(MainWindow)
        self.toolBar.setObjectName("toolBar")
        MainWindow.addToolBar(QtCore.Qt.TopToolBarArea, self.toolBar)
        self.actionQuit = QtGui.QAction(MainWindow)
        self.actionQuit.setObjectName("actionQuit")
        self.actionUpdate = QtGui.QAction(MainWindow)
        self.actionUpdate.setObjectName("actionUpdate")
        self.actionSave = QtGui.QAction(MainWindow)
        self.actionSave.setObjectName("actionSave")
        self.menuFile.addAction(self.actionSave)
        self.menuFile.addAction(self.actionUpdate)
        self.menuFile.addSeparator()
        self.menuFile.addAction(self.actionQuit)
        self.menubar.addAction(self.menuFile.menuAction())
        self.toolBar.addAction(self.actionUpdate)

        self.retranslateUi(MainWindow)
        self.tabWidget.setCurrentIndex(3)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)

    def retranslateUi(self, MainWindow):
        MainWindow.setWindowTitle(QtGui.QApplication.translate("MainWindow", "UDP Oscilloscope Application", None, QtGui.QApplication.UnicodeUTF8))
        self.label_temp.setText(QtGui.QApplication.translate("MainWindow", "Temperature", None, QtGui.QApplication.UnicodeUTF8))
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.tab), QtGui.QApplication.translate("MainWindow", "Temperature Sensors", None, QtGui.QApplication.UnicodeUTF8))
        self.label_hum.setText(QtGui.QApplication.translate("MainWindow", "Humidity:", None, QtGui.QApplication.UnicodeUTF8))
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.tab_4), QtGui.QApplication.translate("MainWindow", "Humidity Sensors", None, QtGui.QApplication.UnicodeUTF8))
        self.label_par.setText(QtGui.QApplication.translate("MainWindow", "PAR:", None, QtGui.QApplication.UnicodeUTF8))
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.tab_3), QtGui.QApplication.translate("MainWindow", "PAR Sensors", None, QtGui.QApplication.UnicodeUTF8))
        self.label_tsr.setText(QtGui.QApplication.translate("MainWindow", "TSR:", None, QtGui.QApplication.UnicodeUTF8))
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.tab_5), QtGui.QApplication.translate("MainWindow", "TSR Sensors", None, QtGui.QApplication.UnicodeUTF8))
        self.label_volt.setText(QtGui.QApplication.translate("MainWindow", "Voltage", None, QtGui.QApplication.UnicodeUTF8))
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.tab_2), QtGui.QApplication.translate("MainWindow", "Voltage", None, QtGui.QApplication.UnicodeUTF8))
        self.menuFile.setTitle(QtGui.QApplication.translate("MainWindow", "File", None, QtGui.QApplication.UnicodeUTF8))
        self.toolBar.setWindowTitle(QtGui.QApplication.translate("MainWindow", "toolBar", None, QtGui.QApplication.UnicodeUTF8))
        self.actionQuit.setText(QtGui.QApplication.translate("MainWindow", "&Quit", None, QtGui.QApplication.UnicodeUTF8))
        self.actionUpdate.setText(QtGui.QApplication.translate("MainWindow", "&Update", None, QtGui.QApplication.UnicodeUTF8))
        self.actionSave.setText(QtGui.QApplication.translate("MainWindow", "&Save Figures", None, QtGui.QApplication.UnicodeUTF8))

from PyKDE4.kdeui import KPlotWidget
