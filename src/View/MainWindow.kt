package View

import Controller.NonogramFieldClick
import Model.Nonogram
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import kotlin.math.max


class MainWindow : Application() {

    private var current_nonogram: Nonogram = Model.Nonogram.generateNonogram()
    private var pane: Pane = Pane()
    private var menuBox: VBox = VBox()
    private var menuBar = MenuBar()
    private var wh: Double = 0.0
    private var he: Double = 0.0
    private var solveButton: Button = Button("Reši nonogram")
    private lateinit var stg: Stage


    override fun start(stage: Stage) {

        show_nonogram(pane,current_nonogram)

        val fileChooser = FileChooser()
        val menu1 : Menu = Menu("Fajlovi")
        val menuItem1 : MenuItem = MenuItem("Učitaj nonogram iz .txt fajla")
        menuItem1.onAction = EventHandler { e: ActionEvent? ->

            val selectedFile = fileChooser.showOpenDialog(stage)
            if(selectedFile!=null && selectedFile.isFile && (selectedFile.name.endsWith(".txt"))) {
                try {
                    current_nonogram = Model.TextGenerator.generateFromText(Files.readAllLines(selectedFile.toPath()))
                    this.refreshNonogram()
                } catch(e: Exception) {
                    println("Fajl nije u dobrom formatu")
                }
            }
        }
        val menuItem2 : MenuItem = MenuItem("Sačuvaj sliku u bazi")
        menuItem2.onAction = EventHandler { e: ActionEvent? ->
            val selectedFile = fileChooser.showOpenDialog(stage)
            if(selectedFile!=null && selectedFile.isFile && (selectedFile.name.endsWith(".jpg") ||
                        selectedFile.name.endsWith(".jpeg") ||
                        selectedFile.name.endsWith(".png"))) {
                Files.copy(selectedFile.toPath(), File("images/" + selectedFile.name).toPath());
            }
        }

        val menuItem3 : MenuItem = MenuItem("Generiši nasumični nonogram iz baze")
        menuItem3.onAction = EventHandler { e: ActionEvent? ->
            this.showNewNonogram()
        }



        menu1.items.addAll(menuItem1,menuItem2,menuItem3)

        menuBar = MenuBar()

        menuBar.getMenus().add(menu1)

        solveButton.onAction = EventHandler { e: ActionEvent? ->
            current_nonogram.solve()
            this.refreshNonogram()
        }

        solveButton.layoutX = wh/2-20.0
        solveButton.layoutY = he+50.0


        pane.children.add(menuBar)
        pane.children.add(solveButton)

        var scene=Scene(pane,wh+100.0,he+130.0)
        stage.scene = scene
        stage.setWidth(wh+100.0);
        stage.setHeight(he+130.0);
        stage.setResizable(false);
        stage.setTitle("Nonogrami");
        stg=stage
        stage.show()
    }

    fun getCurrentNonogram() : Nonogram {
        return current_nonogram
    }

    fun showNewNonogram() {
        current_nonogram = Model.Nonogram.generateNonogram()
        refreshNonogram()
    }

    fun refreshNonogram() {
        pane.children.clear()
        show_nonogram(pane,current_nonogram)
        solveButton.layoutX = wh/2-20.0
        solveButton.layoutY = he+50.0


        pane.children.add(menuBar)
        pane.children.add(solveButton)
        stg.setWidth(wh+100.0);
        stg.setHeight(he+130.0);
        stg.setResizable(false);
        stg.setTitle("Nonogrami");
        stg.show()
    }

    fun show_nonogram(pane: Pane, nonogram: Nonogram) {
        val n: Int = nonogram.getN()
        val m: Int = nonogram.getM()

        val usloviVrsta: Array<Array<Int>>? = nonogram.getUsloviVrsta()
        val usloviKolona: Array<Array<Int>>? = nonogram.getUsloviKolona()
        val polja: Array<Array<Int>>? = nonogram.getNonogram()

        var wdh: Int = 0
        var hei: Int = 0

        var size: Double = 50.0

        if(n<=10 && m<=10) {
            size = 50.0
        }
        else {
            if(n<=20 && m<=20) {
                size = 25.0
            }
            else {
                if(n<=35 && m<=35) {
                    size = 15.0
                }
                else {
                    size = 10.0
                }
            }
        }

        for(i in 0..(n-1)) {
            wdh = max(wdh,usloviVrsta!![i].size)
        }

        for(i in 0..(m-1)) {
            hei = max(hei,usloviKolona!![i].size)
        }

        wh = (wdh+m)*(size+0.5)
        he = (hei+n)*(size+0.5)

        val gridpane = GridPane()

        for(i in 0..(n-1)) {
            for(j in 0..(m-1)) {
                val r: Rectangle = Rectangle(size,size)
                if(polja!![i][j]==1) {
                    r.fill = Color.BLACK
                }
                if(polja!![i][j]==0) {
                    r.fill = Color.WHITE
                }
                if(polja!![i][j]==-1) {
                    r.fill = Color.GREY
                }
                r.setOnMouseClicked(NonogramFieldClick(i,j,current_nonogram,this))
                gridpane.add(r,j+wdh,i+hei)
            }
        }
        for(i in 0..(n-1)) {
            for(j in 0..(usloviVrsta!![i].size-1)) {
                val t: Label = Label(usloviVrsta!![i][j].toString())
                t.font = Font(size/2)
                t.minWidth = size
                t.minHeight = size
                t.maxWidth = size
                t.maxHeight = size
                t.contentDisplay = ContentDisplay.CENTER
                t.alignment = Pos.CENTER
                gridpane.add(t,wdh-usloviVrsta!![i].size+j,i+hei)
            }
        }

        for(i in 0..(m-1)) {
            for(j in 0..(usloviKolona!![i].size-1)) {
                val t: Label = Label(usloviKolona!![i][j].toString())
                t.font = Font(size/2)
                t.minWidth = size
                t.minHeight = size
                t.maxWidth = size
                t.maxHeight = size
                t.contentDisplay = ContentDisplay.CENTER
                t.alignment = Pos.CENTER
                gridpane.add(t,i+wdh,hei-usloviKolona!![i].size+j)
            }
        }

        gridpane.layoutX = 50.00
        gridpane.layoutY = 50.00



        gridpane.setGridLinesVisible(true);
        pane.children.add(gridpane)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(MainWindow::class.java)
        }
    }
}