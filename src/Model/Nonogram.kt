package Model

import kotlin.random.Random

import Model.arrayToRegex
import Model.arrayToString
import Model.intersect

class Nonogram() {

    // nonogram treba da sadrzi matricu sa 0, 1 i -1
    // brojeve n i m
    // dva niza uslova duzina n i m redom
    private var n : Int = 0
    private var m : Int = 0
    private var usloviVrsta : Array<Array<Int>>? = null
    private var usloviKolona : Array<Array<Int>>? = null
    private var nonogram : Array<Array<Int>>? = null

    constructor (_n: Int, _m: Int, _usloviVrsta: Array<Array<Int>>, _usloviKolona: Array<Array<Int>>) : this() {
        n = _n;
        m = _m;
        usloviVrsta = _usloviVrsta
        usloviKolona = _usloviKolona
        nonogram = Array(n){Array(m) {-1} }


    }

    fun getNonogram(): Array<Array<Int>>? {
        return this.nonogram;
    }

    fun getUsloviVrsta(): Array<Array<Int>>? {
        return this.usloviVrsta;
    }

    fun getUsloviKolona(): Array<Array<Int>>? {
        return this.usloviKolona;
    }

    fun getN() : Int {
        return this.n;
    }

    fun getM(): Int {
        return this.m;
    }

    fun setNonogram(i: Int, j: Int, value: Int) {
        if (value != -1 && value != 0 && value != 1)
            return;
        if (nonogram == null) return;
        if(i < n && i >= 0 && j < m && j >= 0)
            nonogram!![i][j] = value;
    }

    override fun toString(): String {
        if(nonogram == null) return "";

        val S: CharArray = CharArray(n*m+n){'x'}
        var brojac : Int = 0;

        for (i in 1..this.n) {
            for (j in 1..this.m) {
                when (nonogram!![i - 1][j - 1]) {
                    1 -> S[brojac] = '1';
                    0 -> S[brojac] = '0';
                }
                brojac++;
            }
            S[brojac] = '\n'
            brojac++;
        }
        return String(S);
    }

    fun isFilled(i: Int, j: Int): Boolean {
        return nonogram!![i][j] == 1;
    }

    fun isEmpty(i: Int, j: Int): Boolean {
        return nonogram!![i][j] == 0;
    }

    fun vrstaPravilo1() : List<Triple<Int, Int, Boolean>> {

        val ret_val : MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        for (i in 1..n) {
            val listaL : Array<Triple<Int, Int, Boolean>> = Array(m) {j -> Triple(i-1, j, false)}
            val listaD : Array<Triple<Int, Int, Boolean>> = Array(m) {j -> Triple(i-1, j, false)};
            var currentLeft = 0;
            var currentRight = m-1;
            var currentSum : Int = 0;

            for(j in 1..usloviVrsta!![i-1].size) {
                currentSum += usloviVrsta!![i-1][j-1]
            }

            currentSum += usloviVrsta!![i-1].size;

            var difference = m - currentSum;

            for (j in 1..usloviVrsta!![i-1].size) {

                if(difference < usloviVrsta!![i-1][j-1]) {

                    for (k in 1..usloviVrsta!![i - 1][j - 1]) {
                        listaL[currentLeft + k - 1] = Triple(i - 1, currentLeft + k - 1, true);
                    }
                }

                if(difference < usloviVrsta!![i-1][usloviVrsta!![i-1].size - j]) {

                    for (k in 1..usloviVrsta!![i - 1][usloviVrsta!![i - 1].size - j]) {
                        listaD[currentRight - k + 1] = Triple(i - 1, currentRight - k + 1, true);
                    }
                }

                currentLeft += usloviVrsta!![i-1][j-1] + 1
                currentRight -= usloviVrsta!![i-1][usloviVrsta!![i-1].size-j] + 1

            }
            ret_val.addAll(intersect(listaL, listaD, m));
        }
        return ret_val;
    }

    fun kolonaPravilo1() : List<Triple<Int, Int, Boolean>> {

        val ret_val : MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        for (i in 1..m) {
            val listaL : Array<Triple<Int, Int, Boolean>> = Array(n) {j -> Triple(i-1, j, false)}
            val listaD : Array<Triple<Int, Int, Boolean>> = Array(n) {j -> Triple(i-1, j, false)};
            var currentTop = 0;
            var currentBottom = n-1;
            var currentSum : Int = 0;

            for(j in 1..usloviKolona!![i-1].size) {
                currentSum += usloviKolona!![i-1][j-1]
            }

            currentSum += usloviKolona!![i-1].size;

            val difference = n - currentSum;

            for (j in 1..usloviKolona!![i-1].size) {

                if(difference < usloviKolona!![i-1][j-1]) {

                    for (k in 1..usloviKolona!![i - 1][j - 1]) {
                        listaL[currentTop + k - 1] = Triple(currentTop + k - 1, i-1,true);
                    }
                }

                if(difference < usloviKolona!![i-1][usloviKolona!![i-1].size - j]) {

                    for (k in 1..usloviKolona!![i - 1][usloviKolona!![i - 1].size - j]) {
                        listaD[currentBottom - k + 1] = Triple(currentBottom - k + 1, i-1,true);
                    }
                }

                currentTop += usloviKolona!![i-1][j-1] + 1
                currentBottom -= usloviKolona!![i-1][usloviKolona!![i-1].size-j] + 1

            }
            ret_val.addAll(intersect(listaL, listaD, m));
        }
        return ret_val;
    }

    fun supposeNext(): Pair<Int, Int>? {
        for(i in 0..n-1)
            for(j in 0..m-1)
                if(!this.isEmpty(i, j) && !this.isFilled(i, j))
                    return Pair(i, j)
        return null
    }

    fun isRowFilled(i: Int) : Boolean {
        for(x in 0..n-1)
            if(!this.isFilled(i, x) && !this.isEmpty(i, x))
                return false
        return true
    }

    fun isColumnFilled(i: Int) : Boolean {
        for(x in 0..n-1)
            if(!this.isFilled(x, i) && !this.isEmpty(x, i))
                return false
        return true
    }



    fun solve(): Boolean {
        var isSolved = false

        val listaPopunjenihPrekoPravila = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        listaPopunjenihPrekoPravila.addAll(this.vrstaPravilo1())
        listaPopunjenihPrekoPravila.addAll(this.kolonaPravilo1())

        for (x in listaPopunjenihPrekoPravila) {
            if (x.third)
                this.setNonogram(x.first, x.second, 1)
        }

        // pravimo nizove regularnih izraza
        // resenje je dobro ako svaka vrsta i kolona pripadaju odgovarajucem regularnom jeziku

        val vrstaRegex = Array(n) { it -> Regex(arrayToRegex(usloviVrsta!![it])) }
        val kolonaRegex = Array(m) { it -> Regex(arrayToRegex(usloviKolona!![it])) }

        // moramo formirati neku vrstu steka gde cemo cuvati polja koja smo popunili

        val stack = ArrayDeque<Triple<Int, Int, Boolean>>()
        var top: Triple<Int, Int, Boolean>
        var match: MatchResult?
        var tmpString: String
        var pretpostavka: Pair<Int, Int>?

        // errorFlag ce da oznacava da li imamo gresku

        var errorFlag = false

        while (!isSolved) {
            if(errorFlag) {
                // u tom slucaju je nonogram pogresan
                if(stack.isEmpty()) break
                else {
                    top = stack.last()
                    stack.removeLast()
                    if(top.third) {
                        stack.addLast(Triple(top.first, top.second, false))
                        this.setNonogram(top.first, top.second, 0)
                        errorFlag = false
                    } else {
                        this.setNonogram(top.first, top.second, -1)
                    }
                }
            } else {
                // proveravamo vrste
                for(i in 0..n-1)
                    if(this.isRowFilled(i)) {
                        tmpString = arrayToString(nonogram!![i])
//                        println("Vrsta: ".plus(i).plus(" - ").plus(tmpString))
                        match = vrstaRegex[i].matchEntire(tmpString)
                        if(match == null) {
                            errorFlag = true
                            break
                        }
                    }
                // sada proveravamo kolone
                for(i in 0..m-1) {
                    if(this.isColumnFilled(i)) {
                        tmpString = ""
                        for(j in 0..n-1)
                            tmpString = tmpString.plus(nonogram!![j][i])
                        match = kolonaRegex[i].matchEntire(tmpString)
                        if(match == null) {
                            errorFlag = true
                            break
                        }
                    }
                }
                if(!errorFlag) {
                    pretpostavka = this.supposeNext()
                    if(pretpostavka == null) isSolved = true
                    else {
                        stack.addLast(Triple(pretpostavka.first, pretpostavka.second, true))
                        this.setNonogram(pretpostavka.first, pretpostavka.second, 1)
                    }
                }
            }
        }
        return isSolved
    }

    companion object {
        fun generateNonogram() : Nonogram {
            var n: Int = Random.nextInt(6) + 1
            var m = n
            val dtsz : Int = PictureGenerator.getPictureCount()
            var ind = Random.nextInt(dtsz)
            return PictureGenerator.generateNonogramFromPicture(ind,n*5,m*5)

            //return Nonogram(5,5, arrayOf(arrayOf(1),arrayOf(3),arrayOf(5),arrayOf(3),arrayOf(1)),arrayOf(arrayOf(1),arrayOf(3),arrayOf(5),arrayOf(3),arrayOf(1)))
        }
    }
}