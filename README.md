# 2021_Nonogram
Generisanje nonograma na osnovu date slike i rešavanje nonograma kojeg korisnik zada.
## Jezici i tehnologije
Korišćen je jezik Kotlin i JavaFX za GUI. Projekat je razvijan u IntelliJ IDEA razvojnom okruženju
## Kompilacija i pokretanje
Neophodno je imati JavaFX biblioteke.

Komanda za kompilaciju iz src foldera:
```
kotlinc -cp "path\lib\javafx.base.jar;path\lib\javafx.controls.jar;path\lib\javafx.fxml.jar;path\lib\javafx.graphics.jar"  View\MainWindow.kt Model\Utils.kt Model\TextGenerator.kt Model\PictureGenerator.kt Model\Nonogram.kt Controller\NonogramFieldClick.kt -include-runtime -d Nonogrami.jar
```

Komanda za pokretanje iz direktorijuma u kojem je i izvršni fajl:
```
java --module-path path\lib --add-modules=javafx.controls -jar Nonogrami.jar
```

Gde "path" označava direktorijum gde su JavaFX biblioteke.

Za korišćenje funkcija koje koriste bazu slika, neophodno je imati "images" poddirektorijum u direktorijumu u kojem je izvršni fajl "Nonogrami.jar"
## Autori
Uroš Ševkušić (Mail: mr18104@alas.matf.bg.ac.rs)
Aleksa Milisavljević (Mail: mr18003@alas.matf.bg.ac.rs)
