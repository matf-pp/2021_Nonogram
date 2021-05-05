package Model

import kotlin.random.Random

import Model.arrayToRegex
import Model.arrayToString
import Model.intersect
import kotlin.math.max
import kotlin.math.min

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

    fun vrstaPravilo1() : List<Triple<Int, Int, Int>> {

        val ret_val : MutableList<Triple<Int, Int, Int>> = emptyList<Triple<Int, Int, Int>>().toMutableList()

        for (i in 1..n) {
            val listaL : Array<Triple<Int, Int, Int>> = Array(m) {j -> Triple(i-1, j, -1)}
            val listaD : Array<Triple<Int, Int, Int>> = Array(m) {j -> Triple(i-1, j, -1)};
            var currentLeft = 0;
            var currentRight = m-1;
            var currentSum : Int = 0;

            for(j in 1..usloviVrsta!![i-1].size) {
                currentSum += usloviVrsta!![i-1][j-1]
            }

            currentSum += usloviVrsta!![i-1].size-1;

            var difference = m - currentSum;

            for (j in 1..usloviVrsta!![i-1].size) {

                if(difference < usloviVrsta!![i-1][j-1]) {

                    for (k in 1..usloviVrsta!![i - 1][j - 1]) {
                        listaL[currentLeft + k - 1] = Triple(i - 1, currentLeft + k - 1,j);
                    }
                }

                if(difference < usloviVrsta!![i-1][usloviVrsta!![i-1].size - j]) {

                    for (k in 1..usloviVrsta!![i - 1][usloviVrsta!![i - 1].size - j]) {
                        listaD[currentRight - k + 1] = Triple(i - 1, currentRight - k + 1, usloviVrsta!![i-1].size - j + 1);
                    }
                }

                currentLeft += usloviVrsta!![i-1][j-1] + 1
                currentRight -= usloviVrsta!![i-1][usloviVrsta!![i-1].size-j] + 1

            }
            ret_val.addAll(intersect(listaL, listaD, m));
        }
        return ret_val;
    }

    fun kolonaPravilo1() : List<Triple<Int, Int, Int>> {

        val ret_val : MutableList<Triple<Int, Int, Int>> = emptyList<Triple<Int, Int, Int>>().toMutableList()

        for (i in 1..m) {
            val listaL : Array<Triple<Int, Int, Int>> = Array(n) {j -> Triple(i-1, j, -1)}
            val listaD : Array<Triple<Int, Int, Int>> = Array(n) {j -> Triple(i-1, j, -1)};
            var currentTop = 0;
            var currentBottom = n-1;
            var currentSum : Int = 0;

            for(j in 1..usloviKolona!![i-1].size) {
                currentSum += usloviKolona!![i-1][j-1]
            }

            currentSum += usloviKolona!![i-1].size-1;

            val difference = n - currentSum;

            for (j in 1..usloviKolona!![i-1].size) {

                if(difference < usloviKolona!![i-1][j-1]) {

                    for (k in 1..usloviKolona!![i - 1][j - 1]) {
                        listaL[currentTop + k - 1] = Triple(currentTop + k - 1, i-1,j);
                    }
                }

                if(difference < usloviKolona!![i-1][usloviKolona!![i-1].size - j]) {

                    for (k in 1..usloviKolona!![i - 1][usloviKolona!![i - 1].size - j]) {
                        listaD[currentBottom - k + 1] = Triple(currentBottom - k + 1, i-1,usloviKolona!![i - 1].size - j+1);
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

    fun alPos(n : Int, a:Array<Int>, c : Array<Int>) : Array<Array<Boolean>> {
        val l : Int = c.size
        var pls : Array<Array<Boolean>> = Array(n){Array(l){false}}

        for(k in 0..(l-1)) {
            val d : Int = c[k]
            var cnt : Int = 0
            var ok : Boolean = false

            if(k==0) ok=true

            for(i in 0..(d-2)) {
                pls[i][k] = false
                if(a[i]==0) cnt++
            }

            for(i in (d-1)..(n-1)) {
                if(a[i]==0) cnt++

                if(k==0) {
                    if(cnt==0) {
                        pls[i][k] = ok
                    }
                    ok = ok && (a[i+1-d]!=1)
                }
                else {
                    if(i>d) {
                        val lp: Int = i-d-1
                        if(a[lp] == 1) {
                            ok = false
                        }
                        ok = ok || pls[lp][k-1]
                    }

                    if(cnt==0) {
                        if(i==d-1) {
                            pls[i][k] = false
                        }
                        else {
                            if(a[i-d] == 1) {
                                pls[i][k] = false
                            }
                            else {
                                pls[i][k] = ok
                            }
                        }
                    }
                    else {
                        pls[i][k]=false
                    }
                }

                if(a[i+1-d]==0) cnt--
            }

        }

        return pls
    }

    fun isRowPlausible(i: Int) : Pair<Boolean,Array<Triple<Int,Int,Int> > > {

        val l : Int = usloviVrsta!![i].size

        if(l==0) {
            val ret : Array<Triple<Int,Int,Int> > = Array(m) {j -> Triple(i,j,0)}
            for(j in 0..(m-1)) {
                if(nonogram!![i][j]==1) return Pair(false,ret)
            }
            return Pair(true,ret)
        }

        val lpos : Array<Array<Boolean> > = alPos(m,Array(m){j -> nonogram!![i][j]},Array(l){j -> usloviVrsta!![i][j]})
        val rpos : Array<Array<Boolean> > = alPos(m,Array(m){j -> nonogram!![i][m-1-j]},Array(l){j -> usloviVrsta!![i][l-1-j]})

        var alpos : Array<Array<Boolean>> = Array(l){Array(m){false}}

        var ret : Array<Triple<Int,Int,Int> > = Array(m) {j -> Triple(i,j,-1)}

        var ok : Boolean = false

        for(k in 0..(l-1)) {
            val d : Int = usloviVrsta!![i][k]
            var m1 : Int = m+1
            var m2 : Int = -1
            for(x in (d-1)..(m-1)) {
                if(lpos[x][k] && rpos[m-1-(x-(d-1))][l-1-k]) {
                    alpos[k][x] = true
                    ok = true
                    m1 = min(m1,x)
                    m2 = max(m2,x)
                    //println(k.toString() + x.toString())
                }
            }
            if(m2!=-1) {
                for(x in (m2+1-d)..m1) {
                    ret[x] = Triple(i,x,1)
                }
            }
        }

        return Pair(ok,ret)
    }

    fun isColumnPlausible(j: Int) : Pair<Boolean,Array<Triple<Int,Int,Int> > > {
        val l : Int = usloviKolona!![j].size

        if(l==0) {
            val ret : Array<Triple<Int,Int,Int> > = Array(n) {i -> Triple(i,j,0)}
            for(i in 0..(n-1)) {
                if(nonogram!![i][j]==1) return Pair(false,ret)
            }
            return Pair(true,ret)
        }

        val lpos : Array<Array<Boolean> > = alPos(n,Array(n){i -> nonogram!![i][j]},Array(l){i -> usloviKolona!![j][i]})
        val rpos : Array<Array<Boolean> > = alPos(n,Array(n){i -> nonogram!![n-1-i][j]},Array(l){i -> usloviKolona!![j][l-1-i]})

        var alpos : Array<Array<Boolean>> = Array(l){Array(n){false}}

        var ret : Array<Triple<Int,Int,Int> > = Array(n) {i -> Triple(i,j,-1)}

        var ok : Boolean = false

        for(k in 0..(l-1)) {
            val d : Int = usloviKolona!![j][k]
            var m1 : Int = n+1
            var m2 : Int = -1
            for(x in (d-1)..(n-1)) {
                if(lpos[x][k] && rpos[n-1-(x-(d-1))][l-1-k]) {
                    alpos[k][x] = true
                    ok = true
                    m1 = min(m1,x)
                    m2 = max(m2,x)

                    //println(k.toString() + " " + m1.toString() + " " + m2.toString())
                }
            }
            if(m2!=-1) {
                for(x in (m2+1-d)..m1) {
                    //println(x.toString())
                    ret[x] = Triple(x,j,1)
                }
            }
        }

        return Pair(ok,ret)
    }

    fun recSolve(i: Int,j: Int) : Boolean {



        if(i==n) return true


        val l : Int = n-i

        var fx : Array<Pair<Boolean,Array<Triple<Int,Int,Int>>>> = Array(l){Pair(true,Array(m){Triple(1,1,1)})}

        if(j==0) {
            fx = Array(l) {k -> isRowPlausible(i+k)}
            for(k in 0..(l-1)) {
                if(!fx[k].first) {
                    return false
                }
            }

            for(k in 0..(l-1)) {
                for(g in 0..(m-1)) {
                    if (fx[k].second[g].third == 1) {
                        if (nonogram!![i+k][g] == 1) {
                            fx[k].second[g] = Triple(i+k, g, -1)
                        } else {
                            setNonogram(i+k, g, 1)
                        }
                    }
                }
            }
        }

        var next_i : Int = i
        var next_j : Int = j+1
        if(next_j == m) {
            next_j = 0
            next_i +=1
        }

        /*println(i.toString() + " " + j.toString())
        println(this.toString())
        println(next_i.toString() + " " + next_j.toString())*/

        if(nonogram!![i][j]!=-1) {

            var rret : Pair<Boolean,Array<Triple<Int,Int,Int>>> = isRowPlausible(i)
            var cret : Pair<Boolean,Array<Triple<Int,Int,Int>>> = isColumnPlausible(j)

            if(!rret.first || !cret.first) {
                if(j==0) {
                    for (k in 0..(l - 1)) {
                        for (g in 0..(m - 1)) {
                            if (fx[k].second[g].third == 1) {
                                setNonogram(i+k, g, -1)
                            }
                        }
                    }
                }
                return false
            }
            for(k in 0..(m-1)) {
                if(rret.second[k].third == 1) {
                    if(nonogram!![i][k] == 1) {
                        rret.second[k] = Triple(i,k,-1)
                    }
                    else {
                        setNonogram(i,k,1)
                    }
                }
            }
            for(k in 0..(n-1)) {
                if(cret.second[k].third == 1) {
                    if(nonogram!![k][j] == 1) {
                        cret.second[k] = Triple(k,j,-1)
                    }
                    else {
                        setNonogram(k,j,1)
                    }
                }
            }

            var ret : Boolean = recSolve(next_i,next_j)

            if(ret) {
                return true
            }
            else {

                for(k in 0..(m-1)) {
                    if(rret.second[k].third == 1) {
                        setNonogram(i,k,-1)
                    }
                }
                for(k in 0..(n-1)) {
                    if(cret.second[k].third == 1) {
                        setNonogram(k,j,-1)
                    }
                }
                if(j==0) {
                    for (k in 0..(l - 1)) {
                        for (g in 0..(m - 1)) {
                            if (fx[k].second[g].third == 1) {
                                setNonogram(i+k, g, -1)
                            }
                        }
                    }
                }

                return false;
            }
        }

        for (k in 0..1) {
            setNonogram(i,j,k)

            var rret : Pair<Boolean,Array<Triple<Int,Int,Int>>> = isRowPlausible(i)
            var cret : Pair<Boolean,Array<Triple<Int,Int,Int>>> = isColumnPlausible(j)
            if(rret.first && cret.first) {
                for(g in 0..(m-1)) {
                    if(rret.second[g].third == 1) {
                        if(nonogram!![i][g] == 1) {
                            rret.second[g] = Triple(i,g,-1)
                        }
                        else {
                            setNonogram(i,g,1)
                        }
                    }
                }
                for(g in 0..(n-1)) {
                    if(cret.second[g].third == 1) {
                        if(nonogram!![g][j] == 1) {
                            cret.second[g] = Triple(g,j,-1)
                        }
                        else {
                            setNonogram(g,j,1)
                        }
                    }
                }

                var ret : Boolean = recSolve(next_i,next_j)

                if(ret) {
                    return true
                }
                else {

                    for(g in 0..(m-1)) {
                        if(rret.second[g].third == 1) {
                            setNonogram(i,g,-1)
                        }
                    }
                    for(g in 0..(n-1)) {
                        if(cret.second[g].third == 1) {
                            setNonogram(g,j,-1)
                        }
                    }

                }
            }

            setNonogram(i,j,-1)
        }

        if(j==0) {
            for (k in 0..(l - 1)) {
                for (g in 0..(m - 1)) {
                    if (fx[k].second[g].third == 1) {
                        setNonogram(i+k, g, -1)
                    }
                }
            }
        }

        return false
    }

    fun solve(cont : Boolean): Boolean {

        if(!cont) {
            for(i in 0..(n-1)) {
                for(j in 0..(m-1)) {
                    setNonogram(i,j,-1)
                }
            }
        }

        val listaPopunjenihPrekoPravila = emptyList<Triple<Int, Int, Int>>().toMutableList()

        listaPopunjenihPrekoPravila.addAll(this.vrstaPravilo1())
        listaPopunjenihPrekoPravila.addAll(this.kolonaPravilo1())

        for (x in listaPopunjenihPrekoPravila) {
            if (x.third != -1 && nonogram!![x.first][x.second] != 0)
                this.setNonogram(x.first, x.second, 1)
        }

        for (i in 0..(n-1)) {
            if(usloviVrsta!![i].size==0) {
                for (j in 0..(m-1)) {
                    nonogram!![i][j]=0
                }
            }
        }

        for (j in 0..(m-1)) {
            if(usloviKolona!![j].size==0) {
                for (i in 0..(n-1)) {
                    nonogram!![i][j]=0
                }
            }
        }

        for(k in 0..(n-1)) {
            if(!isRowPlausible(k).first) return false
        }
        for(k in 0..(m-1)) {
            if(!isColumnPlausible(k).first) return false
        }

        return recSolve(0,0)
    }

    companion object {
        fun generateNonogram(fill : Boolean, sz : Int) : Nonogram {
            var n: Int = (Random.nextInt(4) + 1) * 5
            var m = n
            if(sz != 0 ) {
                n = sz
                m = sz
            }
            val dtsz : Int = PictureGenerator.getPictureCount()
            var ind = Random.nextInt(dtsz)
            return PictureGenerator.generateNonogramFromPicture(ind,n,m, fill)

        }
        fun defaultNonogram() : Nonogram {
            return Nonogram(5,5, arrayOf(arrayOf(1),arrayOf(3),arrayOf(5),arrayOf(3),arrayOf(1)),arrayOf(arrayOf(1),arrayOf(3),arrayOf(5),arrayOf(3),arrayOf(1)))
        }
    }
}