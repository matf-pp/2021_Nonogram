# 2021_Nonogram
Generisanje nonograma na osnovu date slike i rešavanje nonograma kojeg korisnik zada.
## O nonogramima
Nonogram je logička igra koja se sastoji od jedne matrice dimenzija n i m, i dva niza dužine n i m čiji su elementi liste celih brojeva koje određuju uslove na odgovarajućoj vrsti ili koloni. Polja matrice se, na osnovu datih uslova i odgovarajućih pravila, popunjavaju crnom ili belom bojom. Kada se nonogram uspešno popuni, dobija se određena slika na matrici. Lista uslova za datu vrstu ili kolonu govori da moraju onim redom (s leva na desno za vrste ili od gore ka dole za kolone) kojim idu brojevi u listi da se nađu u toj vrsti ili koloni blokovi crnih piksela tih dužina, s tim što između svaka dva bloka mora postojati bar jedno polje razmaka.

Osim crno-belih nonograma, postoje i nonogrami u boji, s tim što su pravila malo drugačija s obzirom na to da postoji više boja. U tom slučaju, razmak mora postojati samo između blokova iste boje, a bela boja ne mora nužno označavati prazno polje, već to može biti bilo koja boja različita od onih kojima se popunjava nonogram.

## Pristupi rešavanju nonograma
Rešavanje nonograma je NP-kompletan problem, te ne postoji trenutno algoritam polinomijalne složenosti koji može da reši svaki nonogram, te se primenjuju razne tehnike pri rešavanju nonograma i postoje različiti solveri koji imaju svoje prednosti ili mane. Brojni radovi su objavljeni na temu rešavanja nonograma i korišćeni su razni algoritmi i ideje prilikom implementacije solvera.

Jedan pristup je da se koriste pravila za rešavanje koja direktno slede iz listi uslova za vrste ili kolone i pozicije već popunjenih polja. Postoji nekoliko kategorija nonograma koji se mogu rešiti na osnovu takvih pravila, ali postoje i nonogrami koje nije moguće rešiti na taj način. Neka od tih pravila mogu se naći recimo ovde: https://en.wikipedia.org/wiki/Nonogram.

Osim upotrebe gorepomenutih pravila, postoje i drugi načini za rešavanje poput genetskih algoritama (https://github.com/worm333/nonogram), backtracking-a (https://lihautan.com/solving-nonogram-with-code/), rešavanje preko regularnih izraza (https://liorsinai.github.io/coding/2020/10/29/finite-state-machines.html), DFS solvera (https://informatika.stei.itb.ac.id/~rinaldi.munir/Stmik/2018-2019/Makalah/Makalah-Stima-2019-001.pdf) i drugih. Često se koriste i hibridni solveri koji koriste više tehnika za rešavanje nonograma. Jedan od takvih solvera može se naći na sledećem linku: https://github.com/seansxiao/nonogram-solver.

## Primer upotrebe aplikacije
Primer praznog (podrazumevanog) nonograma:
![prazan_nonogram](https://user-images.githubusercontent.com/27553333/118834680-ad901800-b8c2-11eb-8765-952081ba182a.png)
Korisnik može da rešava nonogram:
![parcijalni_nonogram](https://user-images.githubusercontent.com/27553333/118834803-c1d41500-b8c2-11eb-9a09-b603d2512a29.png)
Klikom na dugme "Reši nonogram" rešavač započinje rešavanje nonograma:
![resen_nonogram](https://user-images.githubusercontent.com/27553333/118834967-e16b3d80-b8c2-11eb-88e5-1ca58c3176f1.png)
Moguće je učitavati i nonograme iz slika:
![ucitavanje_iz_slike](https://user-images.githubusercontent.com/27553333/118835034-edef9600-b8c2-11eb-9dc9-f97a4146b161.png)
Nakon čega se po zadatim parametrima generiše nonogram puzla:
![generisana_slika](https://user-images.githubusercontent.com/27553333/118835154-06f84700-b8c3-11eb-9779-c2c43e1518dc.png)
Rešenje gornje puzle:
![resena_generisana_puzla](https://user-images.githubusercontent.com/27553333/118835245-18415380-b8c3-11eb-9fc2-fb3fd974b4bf.png)
Za detaljnije informacije o upotrebi aplikacije, referisati na meni "Pomoć":
![pomoc](https://user-images.githubusercontent.com/27553333/118835353-327b3180-b8c3-11eb-8a0f-fd4db403dade.png)

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
