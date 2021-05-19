# 2021_Nonogram
Generisanje nonograma na osnovu date slike i rešavanje nonograma kojeg korisnik zada.
## O nonogramima
Nonogram je logička igra koja se sastoji od jedne matrice dimenzija n i m, i dva niza dužine n i m čiji su elementi liste celih brojeva koje određuju uslove na odgovarajućoj vrsti ili koloni. Polja matrice se, na osnovu datih uslova i odgovarajućih pravila, popunjavaju crnom ili belom bojom. Kada se nonogram uspešno popuni, dobija se određena slika na matrici. Lista uslova za datu vrstu ili kolonu govori da moraju onim redom (s leva na desno za vrste ili od gore ka dole za kolone) kojim idu brojevi u listi da se nađu u toj vrsti ili koloni blokovi crnih piksela tih dužina, s tim što između svaka dva bloka mora postojati bar jedno polje razmaka.

Osim crno-belih nonograma, postoje i nonogrami u boji, s tim što su pravila malo drugačija s obzirom na to da postoji više boja

## Pristupi rešavanju nonograma
Rešavanje nonograma je NP-kompletan problem, te ne postoji trenutno algoritam polinomijalne složenosti koji može da reši svaki nonogram, te se primenjuju razne tehnike pri rešavanju nonograma i postoje različiti solveri koji imaju svoje prednosti ili mane. Brojni radovi su objavljeni na temu rešavanja nonograma i korišćeni su razni algoritmi i ideje prilikom implementacije solvera.

Jedan pristup je da se koriste pravila za rešavanje koja direktno slede iz listi uslova za vrste ili kolone i pozicije već popunjenih polja. Postoji nekoliko kategorija nonograma koji se mogu rešiti na osnovu takvih pravila, ali postoje i nonogrami koje nije moguće rešiti na taj način. Neka od tih pravila mogu se naći recimo ovde: https://en.wikipedia.org/wiki/Nonogram.

Osim upotrebe gorepomenutih pravila, postoje i drugi načini za rešavanje poput genetskih algoritama (https://github.com/worm333/nonogram), backtracking-a (https://lihautan.com/solving-nonogram-with-code/), rešavanje preko regularnih izraza (https://liorsinai.github.io/coding/2020/10/29/finite-state-machines.html), DFS solvera (https://informatika.stei.itb.ac.id/~rinaldi.munir/Stmik/2018-2019/Makalah/Makalah-Stima-2019-001.pdf) i drugih. Često se koriste i hibridni solveri koji koriste više tehnika za rešavanje nonograma. Jedan od takvih solvera može se naći na sledećem linku: https://github.com/seansxiao/nonogram-solver.

## Jezici i tehnologije
Korišćen je jezik Kotlin i JavaFX za GUI. Projekat je razvijan u IntelliJ IDEA razvojnom okruženju(https://www.jetbrains.com/idea/).
## Kompilacija i pokretanje
Neophodno je imati JavaFX biblioteke(https://gluonhq.com/products/javafx/) i Kotlin kompajler(https://github.com/JetBrains/kotlin/releases/tag/v1.5.0).

Komanda za kompilaciju iz src foldera:
```
kotlinc -cp "path\lib\javafx.base.jar;path\lib\javafx.controls.jar;path\lib\javafx.fxml.jar;path\lib\javafx.graphics.jar"  View\MainWindow.kt Model\Utils.kt Model\TextGenerator.kt Model\PictureGenerator.kt Model\Nonogram.kt Controller\NonogramFieldClick.kt -include-runtime -d Nonogrami.jar
```

Komanda za pokretanje iz direktorijuma u kojem je i izvršni fajl:
```
java --module-path path\lib --add-modules=javafx.controls -jar Nonogrami.jar
```

Gde "path" označava direktorijum gde su JavaFX biblioteke.

Za upotrebljavanje funkcija koje koriste bazu slika, neophodno je imati "images" poddirektorijum u direktorijumu u kojem je izvršni fajl "Nonogrami.jar"
## Autori
Uroš Ševkušić (Mail: mr18104@alas.matf.bg.ac.rs)

Aleksa Milisavljević (Mail: mr18003@alas.matf.bg.ac.rs)
