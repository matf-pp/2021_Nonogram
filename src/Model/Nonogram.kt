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

    fun isRowPlausible(i: Int) : Boolean {
        var l : Int = usloviVrsta!![i].size
        var pls : Array<Array<Boolean>> = Array(m){Array(l+1){false}}

        var oks : Boolean = true
        for(j in 0..(m-1)) {
            if(nonogram!![i][j]==1) oks=false
            pls[j][0]=oks
        }

        for(k in 1..l) {
            var d : Int = usloviVrsta!![i][k-1]
            var cnt : Int = 0
            for(j in 0..(d-2)) {
                pls[j][k] = false
                if(nonogram!![i][j]==0) cnt++
            }
            for(j in (d-1)..(m-1)) {
                if(nonogram!![i][j]==0) cnt++

                if(cnt==0) {
                    if(j==d-1) {
                        pls[j][k] = k==1
                    }
                    else {
                        if(k==1) {
                            pls[j][k] = pls[j-d][0]
                        }
                        else {
                            var lp: Int = j-d-1
                            pls[j][k] = false
                            if(lp>=0) {
                                for(iit in 0..lp) {
                                    var it : Int = lp-iit
                                    if(pls[it][k-1]) pls[j][k]=true
                                    if(nonogram!![i][it]==1) break
                                }
                            }
                        }
                    }
                }
                else {
                    pls[j][k]=false
                }

                if(nonogram!![i][j+1-d]==0) cnt--
            }
            /*println("i: " + i.toString() + ", k: " + k.toString() + ", l: " + l.toString())
            for(j in 0..(m-1)) {
                println(pls[j][k].toString())
            }*/
        }

        for(ij in 1..m) {
            var j : Int = m-ij
            if(pls[j][l]) {
                return true
            }
            if(nonogram!![i][j]==1) {
                return false
            }
        }
        return false
    }

    fun isColumnPlausible(j: Int) : Boolean {
        var l : Int = usloviKolona!![j].size
        var pls : Array<Array<Boolean>> = Array(n){Array(l+1){false}}

        var oks : Boolean = true
        for(i in 0..(n-1)) {
            if(nonogram!![i][j]==1) oks=false
            pls[i][0]=oks
        }

        for(k in 1..l) {
            var d : Int = usloviKolona!![j][k-1]
            var cnt : Int = 0
            for(i in 0..(d-2)) {
                pls[i][k] = false
                if(nonogram!![i][j]==0) cnt++
            }
            for(i in (d-1)..(m-1)) {
                if(nonogram!![i][j]==0) cnt++

                if(cnt==0) {
                    if(i==d-1) {
                        pls[i][k] = k==1
                    }
                    else {
                        if(k==1) {
                            pls[i][k] = pls[i-d][0]
                        }
                        else {
                            var lp: Int = i-d-1
                            pls[i][k]=false
                            if(lp>=0) {
                                for(iit in 0..lp) {
                                    var it : Int = lp-iit
                                    if(pls[it][k-1]) pls[i][k]=true
                                    if(nonogram!![it][j]==1) break
                                }
                            }
                        }
                    }
                }
                else {
                    pls[i][k]=false
                }

                if(nonogram!![i+1-d][j]==0) cnt--
            }

        }

        for(ii in 1..n) {
            var i : Int = n-ii
            if(pls[i][l]) {
                return true
            }
            if(nonogram!![i][j]==1) {
                return false
            }
        }
        return false
    }

    fun recSolve(i: Int,j: Int) : Boolean {

        //No rules:
        if(j==0) {
            if(i!=0) {
                if(!isRowPlausible(i-1) || !isColumnPlausible(m-1)) return false
            }
        }
        else {
            if(!isRowPlausible(i) || !isColumnPlausible(j-1)) return false
        }

        //With rules:
        /*for(k in 0..(n-1)) {
            if(!isRowPlausible(k)) return false
        }
        for(k in 0..(m-1)) {
            if(!isColumnPlausible(k)) return false
        }*/
        /*println(i.toString() + " " + j.toString())
        println(this.toString())*/
        if(i>=n) return true
        setNonogram(i,j,0)
        var ok : Boolean = false
        if(j==m-1) ok=recSolve(i+1,0)
        else ok=recSolve(i,j+1)
        if(ok) return true
        setNonogram(i,j,1)
        if(j==m-1) ok=recSolve(i+1,0)
        else ok=recSolve(i,j+1)
        if(ok) return true
        setNonogram(i,j,-1)
        return false
    }

    fun solve(): Boolean {
        return recSolve(0,0)

        /*val listaPopunjenihPrekoPravila = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        listaPopunjenihPrekoPravila.addAll(this.vrstaPravilo1())
        listaPopunjenihPrekoPravila.addAll(this.kolonaPravilo1())

        for (x in listaPopunjenihPrekoPravila) {
            if (x.third)
                this.setNonogram(x.first, x.second, 1)
        }*/


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