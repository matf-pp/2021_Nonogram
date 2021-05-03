package Controller

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import Model.Nonogram
import View.MainWindow
import com.sun.tools.javac.Main
import javafx.scene.input.MouseButton

class NonogramFieldClick: EventHandler<MouseEvent> {

    private lateinit var nonogram: Nonogram
    private var i: Int = 0
    private var j: Int = 0
    private lateinit var source: MainWindow

    constructor (_i: Int, _j: Int, _nonogram: Nonogram,_source: MainWindow){
        i = _i;
        j = _j;
        nonogram = _nonogram;
        source = _source
    }

    override fun handle(p0: MouseEvent?) {
        if(p0!!.button == MouseButton.PRIMARY) {
            nonogram.setNonogram(i, j, 1)
        }
        if(p0!!.button == MouseButton.SECONDARY) {
            nonogram.setNonogram(i, j, 0)
        }
        if(p0!!.button == MouseButton.MIDDLE) {
            nonogram.setNonogram(i, j, -1)
        }
        source.refreshNonogram()
    }
}