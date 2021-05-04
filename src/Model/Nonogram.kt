package Model

import Model.intersect
import kotlin.random.Random

class Nonogram() {

    // nonogram treba da sadrzi matricu sa 0, 1 i -1
    // brojeve n i m
    // dva niza uslova duzina n i m redom
    private var n : Int = 0;
    private var m : Int = 0;
    private var usloviVrsta : Array<Array<Pair<Int, Boolean>>>? = null;
    private var usloviKolona : Array<Array<Pair<Int, Boolean>>>? = null;
    private var nonogram : Array<Array<Int>>? = null;

    constructor (_n: Int, _m: Int, _usloviVrsta: Array<Array<Int>>, _usloviKolona: Array<Array<Int>>) : this() {
        n = _n;
        m = _m;
        usloviVrsta = Array(n) {it -> Array(_usloviVrsta[it].size) {i -> Pair(_usloviVrsta[it][i], false)} };
        usloviKolona = Array(m) {it -> Array(_usloviKolona[it].size) {i -> Pair(_usloviKolona[it][i], false)} };
        nonogram = Array(n){Array(m) {-1} };


    }

    fun getNonogram(): Array<Array<Int>>? {
        return this.nonogram;
    }

    fun getUsloviVrsta(): Array<Array<Pair<Int, Boolean>>>? {
        return this.usloviVrsta;
    }

    fun getUsloviKolona(): Array<Array<Pair<Int, Boolean>>>? {
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


    fun findLeftestFilled(vrsta: Int, d: Int) : Pair<Int, Int> {
    for (i in d+1..m) {
        if(this.isFilled(vrsta, i-1))
            return Pair(vrsta, i-1);
    }
    return Pair(-1, -1);
    }

    fun findTopFilled(kolona: Int, d: Int) : Pair<Int, Int> {
        for (i in d+1..n) {
            if(this.isFilled( i-1, kolona))
                return Pair(i-1, kolona);
        }
        return Pair(-1, -1);
    }

    fun findRightestFilled(vrsta: Int, d: Int) : Pair<Int, Int> {
        for (i in d+1..n) {
            if(this.isFilled(vrsta, n-i))
                return Pair(vrsta, n-i);
        }
        return Pair(-1, -1);
    }

    fun findBottomFilled(kolona: Int, d: Int) : Pair<Int, Int> {
        for (i in d+1..m) {
            if(this.isFilled( m-i, kolona))
                return Pair( m-i, kolona);
        }
        return Pair(-1, -1);
    }

    fun findLeftestEmpty(vrsta: Int, d: Int) : Pair<Int, Int> {
        for (i in d+1..m) {
            if(this.isEmpty(vrsta, i-1))
                return Pair(vrsta, i-1);
        }
        return Pair(-1, -1);
    }

    fun findTopEmpty(kolona: Int, d: Int) : Pair<Int, Int> {
        for (i in d+1..n) {
            if(this.isFilled( i-1, kolona))
                return Pair(i-1, kolona);
        }
        return Pair(-1, -1);
    }

    fun findRightestEmpty(vrsta: Int, d: Int) : Pair<Int, Int> {
        for (i in d+1..n) {
            if(this.isEmpty(vrsta, n-i))
                return Pair(vrsta, n-i);
        }
        return Pair(-1, -1);
    }

    fun findBottomEmpty(kolona: Int, d: Int) : Pair<Int, Int> {
        for (i in d+1..m) {
            if(this.isEmpty( m-i, kolona))
                return Pair( m-i, kolona);
        }
        return Pair(-1, -1);
    }

   fun checkRow(vrsta: Int) : Boolean {
       var brojac = 0;
       var currentUslov: Int = 0;

       if(usloviVrsta.isNullOrEmpty()) return true;

       for (i in 1..this.getM()) {
           if(this.isFilled(vrsta, i-1)) {
               brojac++;
           }
           else if(!this.isFilled(vrsta, i-1) && brojac!=0) {
               if(usloviVrsta!![vrsta][currentUslov].first == brojac) {
                   currentUslov++;
                   brojac = 0;
               }
               else return false;
           }
       }

       if(currentUslov < usloviVrsta!![vrsta].size && usloviVrsta!![vrsta][currentUslov].first != brojac)
           return false

       return true;
   }

    fun checkColumn(kolona: Int) : Boolean {
        var brojac = 0;
        var currentUslov: Int = 0;

        if(usloviVrsta.isNullOrEmpty()) return true;

        for (i in 1..this.getM()) {
            if(this.isFilled(i-1, kolona)) {
                brojac++;
            }
            else if(this.isEmpty(i-1, kolona) && brojac!=0) {
                if(usloviKolona!![kolona][currentUslov].first == brojac) {
                    currentUslov++;
                    brojac = 0;
                }
                else return false;
            }
        }

        if(currentUslov < usloviKolona!![kolona].size && usloviKolona!![kolona][currentUslov].first != brojac)
            return false

        return true;
    }

    fun check(): Boolean {
        for (i in 1..n) {
            if(!this.checkRow(i-1)) {
                println("Zeza vrsta".plus(i-1));
                return false
            };
        }
        for (i in 1..m) {
            if(!this.checkColumn(i-1)) {
                println("Zeza kolona".plus(i-1))
                return false
            };
        }
        return true;
    }

    // metod koji brise tabelu
    fun clearNonogram() {
        for (i in 1..n) {
            for (j in 1..m) {
                nonogram!![i-1][j-1] = -1;
            }
        }
    }

    fun isRowFinished(vrsta: Int): Boolean {
        for (i in 1..m) {
            if(!this.isFilled(vrsta, i-1) && !this.isEmpty(vrsta, i-1))
                return false
        }
        return true
    }

    fun isColumnFinished(kolona: Int): Boolean {
        for (i in 1..m) {
            if(!this.isFilled(i-1, kolona) && !this.isEmpty(i-1, kolona))
                return false
        }
        return true;
    }

    fun finisheddRows() : List<Int> {
        val ret_val : MutableList<Int> = emptyList<Int>().toMutableList();
        for (i in 1..n) {
            if(this.isRowFinished(i-1))
                ret_val.add(i-1);
        }
        return ret_val;
    }

    fun filledColumns() : List<Int> {
        val ret_val : MutableList<Int> = emptyList<Int>().toMutableList();
        for (i in 1..m) {
            if(this.isColumnFinished(i-1));
            ret_val.add(i-1)
        }
        return ret_val;
    }


    // sada pisemo metode pravila

    fun vrstaPravilo1() : List<Triple<Int, Int, Boolean>> {
        // za svaku vrstu pravimo najlevlji i najdesniji raspored
        // u preseku ta dva su polja koja mozemo popuniti sa 1

        val ret_val : MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        for (i in 1..n) {
            val listaL : Array<Triple<Int, Int, Boolean>> = Array(m) {j -> Triple(i-1, j, false)}
            val listaD : Array<Triple<Int, Int, Boolean>> = Array(m) {j -> Triple(i-1, j, false)};
            var currentLeft = 0;
            var currentRight = m-1;
            var currentSum : Int = 0;

            for(j in 1..usloviVrsta!![i-1].size) {
                currentSum += usloviVrsta!![i-1][j-1].first;
            }

            currentSum += usloviVrsta!![i-1].size;

            var difference = m - currentSum;

            for (j in 1..usloviVrsta!![i-1].size) {

                if(difference < usloviVrsta!![i-1][j-1].first) {

                    for (k in 1..usloviVrsta!![i - 1][j - 1].first) {
                        listaL[currentLeft + k - 1] = Triple(i - 1, currentLeft + k - 1, true);
                    }
                }

                if(difference < usloviVrsta!![i-1][usloviVrsta!![i-1].size - j].first) {

                    for (k in 1..usloviVrsta!![i - 1][usloviVrsta!![i - 1].size - j].first) {
                        listaD[currentRight - k + 1] = Triple(i - 1, currentRight - k + 1, true);
                    }
                }

                currentLeft += usloviVrsta!![i-1][j-1].first + 1
                currentRight -= usloviVrsta!![i-1][usloviVrsta!![i-1].size-j].first + 1

            }
            ret_val.addAll(intersect(listaL, listaD, m));
        }
        return ret_val;
    }

    fun kolonaPravilo1() : List<Triple<Int, Int, Boolean>> {
        // za svaku kolonu pravimo gornji i donji raspored
        // u preseku ta dva su polja koja mozemo popuniti sa 1

        val ret_val : MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        for (i in 1..m) {
            val listaL : Array<Triple<Int, Int, Boolean>> = Array(n) {j -> Triple(i-1, j, false)}
            val listaD : Array<Triple<Int, Int, Boolean>> = Array(n) {j -> Triple(i-1, j, false)};
            var currentTop = 0;
            var currentBottom = n-1;
            var currentSum : Int = 0;

            for(j in 1..usloviKolona!![i-1].size) {
                currentSum += usloviKolona!![i-1][j-1].first;
            }

            currentSum += usloviKolona!![i-1].size;

            val difference = n - currentSum;

            for (j in 1..usloviKolona!![i-1].size) {

                if(difference < usloviKolona!![i-1][j-1].first) {

                    for (k in 1..usloviKolona!![i - 1][j - 1].first) {
                        listaL[currentTop + k - 1] = Triple(currentTop + k - 1, i-1,true);
                    }
                }

                if(difference < usloviKolona!![i-1][usloviKolona!![i-1].size - j].first) {

                    for (k in 1..usloviKolona!![i - 1][usloviKolona!![i - 1].size - j].first) {
                        listaD[currentBottom - k + 1] = Triple(currentBottom - k + 1, i-1,true);
                    }
                }

                currentTop += usloviKolona!![i-1][j-1].first + 1
                currentBottom -= usloviKolona!![i-1][usloviKolona!![i-1].size-j].first + 1

            }
            ret_val.addAll(intersect(listaL, listaD, m));
        }
        return ret_val;
    }

    fun vrstaPravilo2() : MutableList<Triple<Int, Int, Boolean>> {
        val ret_val : MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        for(i in 0..n-1) {
            val l = this.findLeftestFilled(i, 0).second;
            val x = usloviVrsta!![i][0].first;

            if (l == -1) return ret_val;

            // ako je l < x, onda su popunjena i polja od l do x-1
            if(l < x-1 && l!=-1) {
                for(j in l+1..x-1)
                    ret_val.add(Triple(i, j, true));
            }
        }
        return ret_val;
    }

    fun kolonaPravilo2() : MutableList<Triple<Int, Int, Boolean>> {
        val ret_val : MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        for(i in 0..m-1) {
            val t = this.findTopFilled(i, 0).first;
            val x = usloviKolona!![i][0].first;

            if (t == -1) return ret_val;

            // ako je t < x, onda su popunjena i polja od t do x-t
            if(t < x-1 && t!=-1) {
                for(j in t+1..x-1)
                    ret_val.add(Triple(j, i, true));
            }
        }
        return ret_val;
    }

    fun vrstaPravilo3() : MutableList<Triple<Int, Int, Boolean>> {
        val ret_val : MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        for(i in 0..n-1) {
            val r = this.findRightestFilled(i, 0).second;
            val x = usloviVrsta!![i][usloviVrsta!![i].size-1].first;

            if (r == -1) return ret_val;

            // ako je l < x, onda su popunjena i polja od l do x-1
            if(m-r < x-1 && r!=-1) {
                for(j in m-x..r-1)
                    ret_val.add(Triple(i, j, true));
            }
        }
        return ret_val;
    }

    fun kolonaPravilo3() : MutableList<Triple<Int, Int, Boolean>> {
        val ret_val : MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        for(i in 0..m-1) {
            val b = this.findBottomFilled(i, 0).first;
            val x = usloviKolona!![i][usloviKolona!![i].size-1].first;

            // ako je t < x, onda su popunjena i polja od t do x-t
            if(n-b < x-1 && b!=-1) {
                for(j in n-x..n-b)
                    ret_val.add(Triple(j, i, true));
            }
        }
        return ret_val;
    }


    fun vrstaPravilo4() : MutableList<Triple<Int, Int, Boolean>> {
        val ret_val: MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        var brojac: Int
        var suma: Int
        var blok: Int
        var uslov1 : Int
        var l: Int
        var le: Int
        var re : Int

        for (i in 0..n-1) {
            brojac = 0
            suma = 0
            for (j in 0..usloviVrsta!![i].size - 1) {
                if (!usloviVrsta!![i][j].second)
                    break
                else {
                    blok = this.findLeftestFilled(i, suma).second
                    suma = usloviVrsta!![i][j].first + blok + 1;
                    brojac++
                }
            }

            if (brojac != usloviVrsta!![i].size) {
                uslov1 = usloviVrsta!![i][brojac].first
                l = this.findLeftestFilled(i, suma).second
                if(l == -1) continue

                le = this.findLeftestEmpty(i, suma).second
                re = this.findLeftestEmpty(i, l).second

                if(le == -1) le = suma

                if(re ==-1) re = le+uslov1

                if (l-le < uslov1 - 1 && le < l && l != -1) {
                    for (j in l+1..uslov1+le-1) {
                        ret_val.add(Triple(i, j, true))
                    }
                }

                if(re-l < uslov1 && l < re && l != -1) {
                    for(j in re-uslov1+1..l-1) {
                        ret_val.add(Triple(i, j, true))
                    }
                }
            }
        }

        return ret_val;
    }

    fun kolonaPravilo4() : MutableList<Triple<Int, Int, Boolean>> {
        val ret_val: MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        var brojac: Int
        var suma: Int
        var blok: Int
        var uslov1 : Int
        var t: Int
        var te: Int
        var be: Int

        for (i in 0..m-1) {
            brojac = 0
            suma = 0
            for (j in 0..usloviKolona!![i].size - 1) {
                if (!usloviKolona!![i][j].second)
                    break
                else {
                    blok = this.findTopFilled(i, suma).first
                    suma = usloviKolona!![i][j].first + blok + 1;
                    brojac++
                }
            }

            if (brojac != usloviKolona!![i].size) {
                uslov1 = usloviKolona!![i][brojac].first
                t = this.findTopFilled(i, suma).first
                if(t == -1) continue

                te = this.findTopEmpty(i, suma).first
                be = this.findTopEmpty(i, t).first

                if(te == -1) te = suma

                if(be == -1) be = te + uslov1

                if (t-te < uslov1 - 1 && t != -1) {
                    for (j in t+1..te+uslov1-1) {
                        ret_val.add(Triple(j, i, true))
                    }
                }
                if(be-t < uslov1 && t < be && t != -1) {
                    for(j in be-uslov1+1..t-1) {
                        ret_val.add(Triple(j, i, true))
                    }
                }
            }
        }

        return ret_val;

    }


    fun vrstaPravilo5() : MutableList<Triple<Int, Int, Boolean>> {
        val ret_val: MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        var brojac: Int
        var suma: Int
        var blok: Int
        var uslov1 : Int
        var r: Int
        var re: Int
        var le: Int

        for (i in 0..n-1) {
            brojac = usloviVrsta!![i].size - 1;
            suma = 0
            for (j in 0..usloviVrsta!![i].size - 1) {
                if (!usloviVrsta!![i][usloviVrsta!![i].size - j - 1].second)
                    break
                else {
                    blok = this.findRightestFilled(i, suma).second
                    suma = usloviVrsta!![i][j].first + m - blok + 1;
                    brojac--
                }
            }
            if (brojac != -1) {
                uslov1 = usloviVrsta!![i][brojac].first
                r = this.findRightestFilled(i, suma).second
                if(r == -1) continue

                re = this.findRightestEmpty(i, suma).second
                le = this.findRightestEmpty(i, r).second

                if(re == -1) re = suma

                if(le == -1) le = m-re-uslov1

                if (re-r < uslov1 && r < re) {
                    for (j in m-re-uslov1..r-1) {
                        ret_val.add(Triple(i, j, true))
                    }
                }
                if(le < r && r-le < uslov1) {
                    for(j in r+1..le+uslov1-2)
                        ret_val.add(Triple(i, j, true))
                }
            }
        }

        return ret_val;
    }


    fun kolonaPravilo5() : MutableList<Triple<Int, Int, Boolean>> {
        val ret_val: MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        var brojac: Int
        var suma: Int
        var blok: Int
        var uslov1 : Int
        var b: Int
        var be : Int
        var te : Int

        for (i in 0..m-1) {
            brojac = usloviKolona!![i].size - 1
            suma = 0
            for (j in 0..usloviKolona!![i].size - 1) {
                if (!usloviKolona!![i][usloviKolona!![i].size - j - 1].second)
                    break
                else {
                    blok = this.findBottomFilled(i, suma).first
                    suma = usloviKolona!![i][j].first + n - blok + 1;
                    brojac--
                }
            }
            if (brojac != -1) {
                uslov1 = usloviKolona!![i][brojac].first
                b = this.findBottomFilled(i, suma).first
                if(b == -1) continue
                be = this.findBottomEmpty(i, suma).first
                te = this.findBottomEmpty(i, b).first

                if(be == -1) be = suma

                if(te == -1) te = n-be-uslov1

                if (be-b< uslov1 && b < be) {
                    for (j in n-be-uslov1..b-1) {
                        ret_val.add(Triple(j, i, true))
                    }
                }

                if(te < b && b-te < uslov1) {
                    for(j in b+1..te+uslov1-2)
                        ret_val.add(Triple(j, i, true))
                }
            }
        }

        return ret_val;
    }

    fun vrstaPravilo6() : MutableList<Triple<Int, Int, Boolean>> {
        val ret_val : MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()
        var l : Int;
        var r : Int;
        var uslov : Int;
        for(i in 0..n-1) {
            if(usloviVrsta!![i].size == 1) {
                l = this.findLeftestFilled(i, 0).second;
                r = this.findRightestFilled(i, 0).second;
                uslov = usloviVrsta!![i][0].first;

                if(l == -1 && r==-1) continue
                else if(l == -1) l = r;
                else if(r == -1) r = l;

                for(j in 0..r-uslov) {
                    ret_val.add(Triple(i, j, false));
                }
                for(j in l+uslov..m-1) {
                    ret_val.add(Triple(i, j, false));
                }
            }
        }
        return ret_val
    }

    fun kolonaPravilo6() : MutableList<Triple<Int, Int, Boolean>> {
        val ret_val : MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList()
        var l : Int;
        var r : Int;
        var uslov : Int;
        for(i in 0..m-1) {
            if(usloviKolona!![i].size == 1) {
                l = this.findTopFilled(i, 0).first;
                r = this.findBottomFilled(i, 0).first;
                uslov = usloviKolona!![i][0].first;

                if(l == -1 && r==-1) continue
                else if(l == -1) l = r;
                else if(r == -1) r = l;

                for(j in 0..r-uslov) {
                    ret_val.add(Triple(j, i, false));
                }
                for(j in l+uslov..m-1) {
                    ret_val.add(Triple(j, i, false));
                }
            }
        }
        return ret_val
    }

    fun supposeNext() : Pair<Int, Int> {
        for(i in 0..n-1) {
            for(j in 0..m-1)
                if(!this.isFilled(i, j) && !this.isEmpty(i, j))
                    return Pair(i, j);
        }
        return Pair(-1, -1)
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