package Model

class TextGenerator {
    companion object {
        fun generateFromText(lines: MutableList<String>): Nonogram {
            val lcnt : Int = lines.size
            val dim : List<String> = lines[0].split(" ")
            val n : Int = dim[0].toInt()
            val m : Int = dim[1].toInt()
            var usloviVrste : Array<Array<Int>> = Array (n) { Array(0)  {0}}
            for(i in 1..n) {
                val uslovLista : List<String> = lines[i].split(" ")
                val l : Int = uslovLista[0].toInt()
                var uslov : Array<Int> = Array(l) {0}
                for(j in 1..l) {
                    uslov[j-1] = uslovLista[j].toInt()
                }
                usloviVrste[i-1] = uslov
            }
            var usloviKolone : Array<Array<Int>> = Array (m) { Array(0)  {0}}
            for(i in 1..m) {
                val uslovLista : List<String> = lines[n+i].split(" ")
                val l : Int = uslovLista[0].toInt()
                var uslov : Array<Int> = Array(l) {0}
                for(j in 1..l) {
                    uslov[j-1] = uslovLista[j].toInt()
                }
                usloviKolone[i-1] = uslov
            }
            return Nonogram(n,m,usloviVrste,usloviKolone)
        }
    }
}