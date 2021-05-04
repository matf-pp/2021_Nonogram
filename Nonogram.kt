import sample.extremePoints
import sample.intersect

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

    // metod koji brise tabelu
    fun clearNonogram() {
        for (i in 1..n) {
            for (j in 1..m) {
                nonogram!![i-1][j-1] = -1;
            }
        }
    }

    //sada pisem metode koji proveravaju da li je ispunjen neki od uslova sa leve ili desne strane niza uslova
    // osnovna ideja - ako su ispunjeni svi prethodni uslovi i ako postoji blok duzine sledeceg uslova
    // takav da je izmedju najekstremnijeg polja tog bloka i najlevljeg 0 polja posle odredjenog broja polja
    // manji od duzine bloka, onda je taj deo uslova ispunjen


    fun checkRowsLeft(){
        var uslov: Int
        var d: Int
        var leftestFilled: Int
        var len: Int
        var leftestEmpty: Int

        for(i in 0..n-1) {
            d = 0
            uslov = -1
            len = 0

            // potrebno je naci prvi uslov koji nije popunjen sa leve strane

            for(j in 0..usloviVrsta!![i].size-1) {
                if(!usloviVrsta!![i][j].second) {
                    uslov = j
                    break
                }
                else {
                    // racunamo koliku su duzinu popunjeni zauzeli
                    // to je leftestFilled posle d + duzina uslova + 1
                    // zbog numeracije od leftestFilled, nemamo +1
                    d = this.findLeftestFilled(i, d).second + usloviVrsta!![i][j].first
                }
            }
            if(uslov == -1) continue

            // a ako postoji neispunjem uslov, onda je potrebno proveriti da li je sada ispunjen
            leftestFilled = this.findLeftestFilled(i, d).second
            if(leftestFilled == -1) continue
            for(j in 0..m-1) {
                if (leftestFilled + j < m && this.isFilled(i, leftestFilled + j)) len++
                else break
            }

            if(len == usloviVrsta!![i][uslov].first) {
                leftestEmpty = this.findLeftestEmpty(i, d).second
                if(leftestEmpty == -1) leftestEmpty = d
                if(leftestEmpty < leftestFilled) leftestEmpty = d
                if(leftestFilled - leftestEmpty <= len) {
                    usloviVrsta!![i][uslov] = Pair(usloviVrsta!![i][uslov].first, true)
                    if(leftestFilled + usloviVrsta!![i][uslov].first < m) this.setNonogram(i, leftestFilled + usloviVrsta!![i][uslov].first, 0)
                }
            }

        }
    }

    fun checkRowsRight() {
        var uslov: Int
        var d: Int
        var rightestFilled: Int
        var len: Int
        var rightestEmpty: Int

        for(i in 0..n-1) {
            d = 0
            uslov = -1
            len = 1

            // potrebno je naci prvi uslov koji nije popunjen sa leve strane

            for(j in 0..usloviVrsta!![i].size-1) {
                if(!usloviVrsta!![i][usloviVrsta!![i].size-j-1].second) {
                    uslov = usloviVrsta!![i].size - j-1
                    break;
                }
                else {
                    // racunamo koliku su duzinu popunjeni zauzeli
                    // to je m - rightestFilled + duzina uslova + 1
                    // zbog numeracije, nemamo +1
                    d = m - this.findRightestFilled(i, d).second + usloviVrsta!![i][usloviVrsta!![i].size-j-1].first
                }
            }
            if(uslov == -1) continue

            // a ako postoji neispunjem uslov, onda je potrebno proveriti da li je sada ispunjen
            rightestFilled = this.findRightestFilled(i, d).second
            if(rightestFilled == -1) continue
            for(j in 1..m-1) {
                if(rightestFilled - j >= 0 && this.isFilled(i, rightestFilled - j)) len++
                else break
            }


            if(len == usloviVrsta!![i][uslov].first) {
                rightestEmpty = this.findRightestEmpty(i, d).second
                if(rightestEmpty == -1) rightestEmpty = m-d-1
                if(rightestEmpty < rightestFilled) rightestEmpty = m-d-1
                if(rightestEmpty - rightestFilled <= len && rightestFilled < rightestEmpty) {
                    usloviVrsta!![i][uslov] = Pair(usloviVrsta!![i][uslov].first, true)
                    if (rightestFilled - usloviVrsta!![i][uslov].first > 0) this.setNonogram(i, rightestFilled - usloviVrsta!![i][uslov].first, 0)
                }
            }

        }
    }

    // za kolone!!!!

    fun checkColumnsTop(){
        var uslov: Int
        var d: Int
        var topFilled: Int
        var len: Int
        var topEmpty: Int

        for(i in 0..m-1) {
            d = 0
            uslov = -1
            len = 0

            // potrebno je naci prvi uslov koji nije popunjen sa leve strane

            for(j in 0..usloviKolona!![i].size-1) {
                if(!usloviKolona!![i][j].second) {
                    uslov = j
                    break
                }
                else {
                    // racunamo koliku su duzinu popunjeni zauzeli
                    // to je leftestFilled posle d + duzina uslova + 1
                    // zbog numeracije od leftestFilled, nemamo +1
                    d = this.findTopFilled(i, d).first + usloviKolona!![i][j].first
                }
            }
            if(uslov == -1) continue

            // a ako postoji neispunjem uslov, onda je potrebno proveriti da li je sada ispunjen
            topFilled = this.findTopFilled(i, d).first
            if(topFilled == -1) continue
            for(j in 0..n-1) {
                if(topFilled + j < n && this.isFilled(topFilled+j, i)) len++
                else break
            }


            if(len == usloviKolona!![i][uslov].first) {
                topEmpty = this.findTopEmpty(i, d).first
                if(topEmpty == -1) topEmpty = d
                if(topEmpty < topFilled) topEmpty = d
                if(topFilled - topEmpty <= len) {
                    usloviKolona!![i][uslov] = Pair(usloviKolona!![i][uslov].first, true)
                    if(topFilled + usloviKolona!![i][uslov].first < m) this.setNonogram(topFilled + usloviKolona!![i][uslov].first, i,0)
                }
            }

        }
    }

    fun checkColumnsBottom() {
        var uslov: Int
        var d: Int
        var bottomFilled: Int
        var len: Int
        var bottomEmpty: Int

        for(i in 0..m-1) {
            d = 0
            uslov = -1
            len = 1

            // potrebno je naci prvi uslov koji nije popunjen sa leve strane

            for(j in 0..usloviKolona!![i].size-1) {
                if(!usloviKolona!![i][usloviKolona!![i].size-j-1].second) {
                    uslov = usloviKolona!![i].size - j-1
                    break;
                }
                else {
                    // racunamo koliku su duzinu popunjeni zauzeli
                    // to je m - rightestFilled + duzina uslova + 1
                    // zbog numeracije, nemamo +1
                    d = n - this.findBottomFilled(i, d).first + usloviKolona!![i][usloviKolona!![i].size-j-1].first
                }
            }
            if(uslov == -1) continue

            // a ako postoji neispunjem uslov, onda je potrebno proveriti da li je sada ispunjen
            bottomFilled = this.findBottomFilled(i, d).first
            if(bottomFilled == -1) continue
            for(j in 1..n-1) {
                if(bottomFilled - j >= 0 && this.isFilled( bottomFilled - j, i)) len++
                else break
            }

            if(len == usloviKolona!![i][uslov].first) {
                bottomEmpty = this.findBottomEmpty(i, d).first
                if(bottomEmpty == -1) bottomEmpty = n-d-1
                if(bottomEmpty < bottomFilled) bottomEmpty = n-d-1
                if(bottomEmpty - bottomFilled <= len && bottomFilled < bottomEmpty) {
                    usloviKolona!![i][uslov] = Pair(usloviKolona!![i][uslov].first, true)
                    if (bottomFilled - usloviKolona!![i][uslov].first > 0) this.setNonogram(bottomFilled - usloviKolona!![i][uslov].first, i,0)
                }
            }

        }
    }


    // naredna dva metoda postavljaju false na drugu poziciju uslova

    fun setUsloviVrstaFalse() {
        for(i in 0..n-1) {
            for(j in 0..usloviVrsta!![i].size-1)
                usloviVrsta!![i][j] = Pair(usloviVrsta!![i][j].first, false)
        }
    }

    fun setUsloviKolonaFalse() {
        for(i in 0..m-1) {
            for(j in 0..usloviKolona!![i].size-1)
                usloviKolona!![i][j] = Pair(usloviKolona!![i][j].first, false)
        }
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

    // pravilo 2 funkcionise ovako:
    // nadji najlevlji dostupni uslov (onaj koji sigurno nije ostvaren)
    // nadji najelvlju popunjenu poziciju
    // nadji najlevlju praznu poziciju posle duzine koju zaklapaju popunjeni uslovi (pre tog uslova)
    // nadji najelvju praznu poziciju posle najlevlje popunjene
    // ako je distanca izmedju leve prazne i leve popunjene manja od duzine dostupnog uslova
    // onda se moze dopuniti sa desne strane
    // slicno i sa desne strane

    fun vrstaPravilo2() : List<Triple<Int, Int, Boolean>> {
        val ret_val = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        var uslov: Int
        var d: Int
        var leftestFilled: Int
        var len: Int
        var leftestEmpty: Int
        var rightEmpty: Int

        for (i in 0..n-1) {
            d = 0
            uslov = -1
            len = 0

            // potrebno je naci prvi uslov koji nije popunjen sa leve strane

            for (j in 0..usloviVrsta!![i].size - 1) {
                if (!usloviVrsta!![i][j].second) {
                    uslov = j
                    break
                } else {
                    d = this.findLeftestFilled(i, d).second + usloviVrsta!![i][j].first
                }
            }

            if(uslov == -1) continue

            leftestFilled = this.findLeftestFilled(i, d).second

            if(leftestFilled == -1) continue

            leftestEmpty = this.findLeftestEmpty(i, d).second

            rightEmpty = this.findLeftestEmpty(i, leftestFilled).second;

            if(leftestEmpty == -1) leftestEmpty = d
            // ako nema praznog polja posle, cinimo samo da ne utice
            if(rightEmpty == -1) rightEmpty = leftestFilled + usloviVrsta!![i][uslov].first + 1

            if(leftestEmpty > leftestFilled) leftestEmpty = d

//            if(leftestFilled - leftestEmpty < usloviVrsta!![i][uslov].first) {
//                for(j in 1..usloviVrsta!![i][uslov].first-(leftestFilled-leftestEmpty)-1) {
//                    if(leftestFilled + j < m) ret_val.add(Triple(i, leftestFilled + j, true))
//                }
//            }
//
//
//            if(rightEmpty - leftestEmpty < usloviVrsta!![i][uslov].first) {
//                for(j in 1..usloviVrsta!![i][uslov].first-(rightEmpty-leftestFilled)-1) {
//                    if(leftestFilled - j >= 0) ret_val.add(Triple(i, leftestFilled - j, true))
//                }
//            }
//
//        }

            val extreme = extremePoints(leftestEmpty, leftestFilled, rightEmpty, usloviVrsta!![i][uslov].first)

            for(x in extreme) {
                if(0 <= x && x < m)
                    ret_val.add(Triple(i, x, true))
            }
        }

        return ret_val
    }


    fun kolonaPravilo2() : List<Triple<Int, Int, Boolean>> {
        val ret_val = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        var uslov: Int
        var d: Int
        var topFilled: Int
        var topEmpty: Int
        var bottomEmpty: Int

        for (i in 0..m-1) {
            d = 0
            uslov = -1

            // potrebno je naci prvi uslov koji nije popunjen sa leve strane

            for (j in 0..usloviKolona!![i].size - 1) {
                if (!usloviKolona!![i][j].second) {
                    uslov = j
                    break
                } else {
                    d = this.findTopFilled(i, d).first + usloviKolona!![i][j].first
                }
            }

            if(uslov == -1) continue

            topFilled = this.findTopFilled(i, d).first

            if(topFilled == -1) continue

            topEmpty = this.findTopEmpty(i, d).first

            bottomEmpty = this.findTopEmpty(i, topFilled).first

            if(topEmpty == -1) topEmpty = d
            // ako nema praznog polja posle, cinimo samo da ne utice
            if(bottomEmpty == -1) bottomEmpty = topFilled + usloviKolona!![i][uslov].first + 1

            if(topEmpty > topFilled) topEmpty = d

            val extreme = extremePoints(topEmpty, topFilled, bottomEmpty, usloviKolona!![i][uslov].first)

            for(x in extreme) {
                if(0 <= x && x < n)
                    ret_val.add(Triple(x, i, true))
            }
        }

        return ret_val
    }


    fun vrstaPravilo3() : MutableList<Triple<Int, Int, Boolean>> {
        val ret_val = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        var uslov: Int
        var d: Int
        var rightestFilled: Int
        var rightestEmpty: Int
        var leftEmpty: Int

        for (i in 0..n - 1) {
            d = 0
            uslov = -1


            for (j in 0..usloviVrsta!![i].size - 1) {
                if (!usloviVrsta!![i][usloviVrsta!![i].size - j - 1].second) {
                    uslov = usloviVrsta!![i].size - j - 1
                    break;
                } else {
                    d = m - this.findRightestFilled(i, d).second + usloviVrsta!![i][usloviVrsta!![i].size - j - 1].first
                }
            }
            if (uslov == -1) continue

            rightestFilled = this.findRightestFilled(i, d).second
            if(rightestFilled == -1) continue

            rightestEmpty = this.findRightestEmpty(i, d).second
            leftEmpty = this.findRightestEmpty(i, rightestFilled).second

            if(rightestEmpty < rightestFilled || rightestEmpty == -1) rightestEmpty = m-d-1
            if(leftEmpty == -1) leftEmpty = rightestFilled - uslov - 1

            val extreme = extremePoints(leftEmpty, rightestFilled, rightestEmpty, usloviVrsta!![i][uslov].first)

            for(x in extreme) {
                if(0 <= x && x < m)
                    ret_val.add(Triple(i, x, true))
            }
        }

        return ret_val

    }

    fun kolonaPravilo3() : MutableList<Triple<Int, Int, Boolean>> {
        val ret_val = emptyList<Triple<Int, Int, Boolean>>().toMutableList()

        var uslov: Int
        var d: Int
        var bottomFilled: Int
        var bottomEmpty: Int
        var topEmpty: Int

        for (i in 0..n - 1) {
            d = 0
            uslov = -1


            for (j in 0..usloviKolona!![i].size - 1) {
                if (!usloviKolona!![i][usloviKolona!![i].size - j - 1].second) {
                    uslov = usloviKolona!![i].size - j - 1
                    break;
                } else {
                    d = m - this.findBottomFilled(i, d).first + usloviKolona!![i][usloviKolona!![i].size - j - 1].first
                }
            }
            if (uslov == -1) continue

            bottomFilled = this.findBottomFilled(i, d).first
            if(bottomFilled == -1) continue

            bottomEmpty = this.findBottomEmpty(i, d).first
            topEmpty = this.findBottomEmpty(i, bottomFilled).second

            if(bottomEmpty < bottomFilled || bottomEmpty == -1) bottomEmpty = m-d-1
            if(topEmpty == -1) topEmpty = bottomFilled - uslov - 1

            val extreme = extremePoints(topEmpty, bottomFilled, bottomEmpty, usloviKolona!![i][uslov].first)

            for(x in extreme) {
                if(0 <= x && x < m)
                    ret_val.add(Triple(x, i, true))
            }
        }

        return ret_val

    }



    fun vrstaPravilo4() : MutableList<Triple<Int, Int, Boolean>> {
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

    fun kolonaPravilo4() : MutableList<Triple<Int, Int, Boolean>> {
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

    fun solvePrekoPravila(): MutableList<Triple<Int, Int, Boolean>> {
       val ret_val = emptyList<Triple<Int, Int, Boolean>>().toMutableList()
         ret_val.addAll(this.vrstaPravilo2())
       ret_val.addAll(this.vrstaPravilo3())
        ret_val.addAll(this.vrstaPravilo4())
       ret_val.addAll(this.kolonaPravilo2())
        ret_val.addAll(this.kolonaPravilo3())
        ret_val.addAll(this.kolonaPravilo4())
        return ret_val
    }

}