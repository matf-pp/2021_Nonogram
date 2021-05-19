package View

import Controller.NonogramFieldClick
import Model.Nonogram
import Model.PictureGenerator
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.stage.FileChooser
import javafx.stage.PopupWindow
import javafx.stage.Stage
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import kotlin.math.max


class MainWindow : Application() {

    private var current_nonogram: Nonogram = Nonogram.defaultNonogram()
    private var pane: Pane = Pane()
    private var menuBox: VBox = VBox()
    private var menuBar = MenuBar()
    private var wh: Double = 0.0
    private var he: Double = 0.0
    private var solveButton: Button = Button("Reši nonogram")
    private var tg : ToggleGroup = ToggleGroup()
    private var tg2 : ToggleGroup = ToggleGroup()
    private lateinit var stg: Stage

    private fun getDim() : Int {
        var sz : Int = 0
        val u : Toggle =  tg2.selectedToggle
        if(u is RadioMenuItem) {
            if(u.text.startsWith("5x5")) sz = 5
            if(u.text.startsWith("10x10")) sz = 10
            if(u.text.startsWith("15x15")) sz = 15
            if(u.text.startsWith("20x20")) sz = 20
            if(u.text.startsWith("25x25")) sz = 25
        }
        return sz
    }

    private fun getFill() : Boolean {
        var fill : Boolean = true
        val u : Toggle =  tg.selectedToggle
        if(u is RadioMenuItem) {
            fill = u.text.equals("Generiši punu sliku")
        }
        return fill
    }

    override fun start(stage: Stage) {

        show_nonogram(pane,current_nonogram)

        val fileChooser = FileChooser()
        val menu1 : Menu = Menu("Fajlovi")
        val menu2 : Menu = Menu("Rešavač")
        val menu3 : Menu = Menu("Generisanje Slika")
        val menu4 : Menu = Menu("Pomoć")



        val menuItem1 : MenuItem = MenuItem("Učitaj nonogram iz .txt fajla")
        menuItem1.onAction = EventHandler { e: ActionEvent? ->

            val selectedFile = fileChooser.showOpenDialog(stage)
            if(selectedFile!=null && selectedFile.isFile && (selectedFile.name.endsWith(".txt"))) {
                try {
                    current_nonogram = Model.TextGenerator.generateFromText(Files.readAllLines(selectedFile.toPath()))
                    this.refreshNonogram()
                } catch(e: Exception) {
                    val err : Alert = Alert(Alert.AlertType.ERROR)
                    err.title = "Greška"
                    err.headerText = "Greška pri učitavanju nonograma iz fajla"
                    err.contentText = "Došlo je do greške pri učitavanju nonograma iz tekstualnog fajla."
                    err.showAndWait()
                }
            }
        }
        val menuItem15 : MenuItem = MenuItem("Učitaj nonogram iz slike")
        menuItem15.onAction = EventHandler { e: ActionEvent? ->

            val selectedFile = fileChooser.showOpenDialog(stage)
            if(selectedFile!=null && selectedFile.isFile && (selectedFile.name.endsWith(".jpg") ||
                        selectedFile.name.endsWith(".jpeg") ||
                        selectedFile.name.endsWith(".png"))) {
                current_nonogram = Model.PictureGenerator.generateNonogramFromGivenPicture(selectedFile.toPath().toString(),getDim(),getDim(),getFill())
                this.refreshNonogram()
            }
        }
        val menuItem2 : MenuItem = MenuItem("Sačuvaj sliku u bazi")
        menuItem2.onAction = EventHandler { e: ActionEvent? ->
            val selectedFile = fileChooser.showOpenDialog(stage)
            if(selectedFile!=null && selectedFile.isFile && (selectedFile.name.endsWith(".jpg") ||
                        selectedFile.name.endsWith(".jpeg") ||
                        selectedFile.name.endsWith(".png"))) {
                            try {
                                Files.copy(selectedFile.toPath(), File("images/" + selectedFile.name).toPath());
                            }
                            catch(e: Exception) {
                                val err : Alert = Alert(Alert.AlertType.ERROR)
                                err.title = "Greška"
                                err.headerText = "Greška pri čuvanju slike u bazi"
                                err.contentText = "Došlo je do greške pri čuvanju slike u bazi."
                                err.showAndWait()
                            }
            }
        }

        val menuItem3 : MenuItem = MenuItem("Generiši nasumični nonogram iz baze")
        menuItem3.onAction = EventHandler { e: ActionEvent? ->
            if(PictureGenerator.getPictureCount()==0) {
                val err : Alert = Alert(Alert.AlertType.ERROR)
                err.title = "Greška"
                err.headerText = "Baza slika je prazna"
                err.contentText = "Baza slika je prazna."
                err.showAndWait()
            }
            else {
                this.showNewNonogram()
            }
        }

        val checkBox : CheckMenuItem = CheckMenuItem("Nastavi započeto rešavanje nonograma")
        checkBox.isSelected = true

        val rm1 : RadioMenuItem = RadioMenuItem("Generiši samo konturu")
        val rm2 : RadioMenuItem = RadioMenuItem("Generiši punu sliku")

        val sz0 : RadioMenuItem = RadioMenuItem("Nasumična veličina")
        val sz1 : RadioMenuItem = RadioMenuItem("5x5")
        val sz2 : RadioMenuItem = RadioMenuItem("10x10")
        val sz3 : RadioMenuItem = RadioMenuItem("15x15")
        val sz4 : RadioMenuItem = RadioMenuItem("20x20")
        val sz5 : RadioMenuItem = RadioMenuItem("25x25")

        rm1.isSelected = true
        sz0.isSelected = true
        sz0.toggleGroup = tg2
        sz1.toggleGroup = tg2
        sz2.toggleGroup = tg2
        sz3.toggleGroup = tg2
        sz4.toggleGroup = tg2
        sz5.toggleGroup = tg2


        rm1.toggleGroup = tg
        rm2.toggleGroup = tg

        val hItem1 = MenuItem("Opšte informacije")
        hItem1.onAction = EventHandler { e: ActionEvent? ->
            val info : Alert = Alert(Alert.AlertType.INFORMATION)
            info.title = "Pomoć"
            info.headerText = "Opšte informacije pri upotrebi alata."
            info.contentText = "Nonogram je puzla u kojoj je potrebno obojiti neka polja u crno, tako da su uslovi svih vrsta i kolona zadovoljeni. Uslov neke vrste/kolone se definiše brojem blokova uzastopnih crnih polja i veličinom svakog od njih. U ovom alatu su polja koja nisu crna u nekom rešenju puzle obojena belom bojom, dok su polja o kojima nemamo informaciju obojena sivom bojom. \n\n\n\n\n\n\n\n\n Korisnik može da menja informacije o poljima u nonogramu, klikom na to polje levim, desnim ili srednjim tasterom, čime ga boji u crnu, belu ili sivu boju, redom. Klikom na dugme za rešavanje nonograma, započinje se rešavanje nonograma u skladu sa parametrima zadatim u meniju za rešavač."
            info.showAndWait()
        }
        val hItem2 = MenuItem("Meni \"Fajlovi\"")
        hItem2.onAction = EventHandler { e: ActionEvent? ->
            val info : Alert = Alert(Alert.AlertType.INFORMATION)
            info.title = "Pomoć"
            info.headerText = "Pomoć pri korišćenju menija fajlovi."
            info.contentText = "U meniju fajlovi se nalaze četiri akcije. Prva akcija je učitavanje nonograma iz .txt fajla. Prva linija fajla treba da sadrži brojeve n i m, koji predstavljaju dimenzije nonograma, potom sledi n linija, od kojih svaka sadrži broj l i potom l brojeva, gde l predstavlja broj ograničenja u odgovarajućoj vrsti, a zatim slede ta ograničenja. Nakon toga sledi m linija, istog formata, koje predstavljaju ograničenja za kolone. Druga akcija je učitavanje nonograma iz slike u skladu sa parametrima zadatim u meniju za generisanje slika. Dozvoljene su samo slike sa \".jpg\", \".png\" ili \".jpeg\" ekstenzijom. Treća akcija, sačuvaj sliku u bazi, predstavlja operaciju čuvanja slike u bazu za buduće slučajno generianje nonogram puzle na osnovu nje. Četvrta akcija je akcija generisanja slučajne slike iz baze u skladu sa parametrima zadatim u meniju generisanje slika."
            info.showAndWait()
        }
        val hItem3 = MenuItem("Meni \"Rešavač\"")
        hItem3.onAction = EventHandler { e: ActionEvent? ->
            val info : Alert = Alert(Alert.AlertType.INFORMATION)
            info.title = "Pomoć"
            info.headerText = "Pomoć pri korišćenju menija rešavač."
            info.contentText = "U meniju rešavač je moguće selektovati da li korisnik želi da rešavač nastavi od dela do kojeg je on stigao, ili da krene rešavanje nonograma od početka."
            info.showAndWait()
        }
        val hItem4 = MenuItem("Meni \"Generisanje Slika\"")
        hItem4.onAction = EventHandler { e: ActionEvent? ->
            val info : Alert = Alert(Alert.AlertType.INFORMATION)
            info.title = "Pomoć"
            info.headerText = "Pomoć pri korišćenju menija za generisanje slika."
            info.contentText = "Dozvoljene su samo slike sa \".jpg\", \".png\" ili \".jpeg\" ekstenzijom. Baza slika se formira u poddirektorijumu \"images\" direkotorijuma u kojem je izvršni fajl. U meniju za generisanje slika su podešavanja slučajnog generianja slika iz baze slika. U njemu je moguće selektovati stil generisanja slike i dimenzije slike. Preporuka je generisanje na osnovu konture i do dimenzija 20x20."
            info.showAndWait()
        }

        val smi : SeparatorMenuItem = SeparatorMenuItem()

        menu4.items.addAll(hItem1,hItem2,hItem3,hItem4)
        menu3.items.addAll(rm1,rm2,smi,sz0,sz1,sz2,sz3,sz4,sz5)
        menu2.items.add(checkBox)
        menu1.items.addAll(menuItem1,menuItem15,menuItem2,menuItem3)

        menuBar = MenuBar()

        menuBar.getMenus().addAll(menu1,menu2,menu3,menu4)

        solveButton.onAction = EventHandler { e: ActionEvent? ->
            if(!current_nonogram.solve(checkBox.isSelected)) {
                val info : Alert = Alert(Alert.AlertType.INFORMATION)
                info.title = "Rešavač"
                info.headerText = "Nonogram nema rešenja"
                info.contentText = "Ovaj nonogram nema rešenja. Pokušajte da promenite neki od zadatih uslova."
                info.showAndWait()
            }
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
        current_nonogram = Model.Nonogram.generateNonogram(getFill(),getDim())
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
                if(n<=25 && m<=25) {
                    size = 20.0
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