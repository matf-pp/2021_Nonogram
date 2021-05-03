package View

import Model.Nonogram
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlin.math.max
import javafx.scene.shape.*
import javafx.scene.paint.Paint
import javafx.scene.text.*
import Controller.NonogramFieldClick


class MainWindow : Application() {

    private var current_nonogram: Nonogram = Model.Nonogram.generateNonogram();
    private var pane: Pane = Pane();


    override fun start(stage: Stage) {

        show_nonogram(pane,current_nonogram)

        val scene=Scene(pane,900.0,700.0)
        stage.scene = scene
        stage.setWidth(900.00);
        stage.setHeight(700.00);
        stage.setResizable(false);
        stage.setTitle("Nonogrami");
        stage.show()
    }

    fun getCurrentNonogram() : Nonogram {
        return current_nonogram
    }

    public fun refreshNonogram() {
        pane.children.clear()
        show_nonogram(pane,current_nonogram)
    }

    fun show_nonogram(pane: Pane, nonogram: Nonogram) {
        val n: Int = nonogram.getN()
        val m: Int = nonogram.getM()

        val usloviVrsta: Array<Array<Pair<Int, Boolean>>>? = nonogram.getUsloviVrsta()
        val usloviKolona: Array<Array<Pair<Int, Boolean>>>? = nonogram.getUsloviKolona()
        val polja: Array<Array<Int>>? = nonogram.getNonogram()

        var wdh: Int = 0
        var hei: Int = 0


        for(i in 0..(n-1)) {
            wdh = max(wdh,usloviVrsta!![i].size)
        }

        for(i in 0..(m-1)) {
            hei = max(hei,usloviKolona!![i].size)
        }


        val gridpane = GridPane()

        for(i in 0..(n-1)) {
            for(j in 0..(m-1)) {
                val r: Rectangle = Rectangle(50.0,50.0)
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
                val t: Text = Text(usloviVrsta!![i][j].first.toString())
                t.font = Font(20.0)
                gridpane.add(t,wdh-1-j,i+hei)
            }
        }

        for(i in 0..(m-1)) {
            for(j in 0..(usloviKolona!![i].size-1)) {
                val t: Text = Text(usloviKolona!![i][j].first.toString())
                t.font = Font(20.0)
                gridpane.add(t,i+wdh,hei-1-j)
            }
        }

        gridpane.layoutX = 250.00
        gridpane.layoutY = 250.00



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