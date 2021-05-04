package sample

// ovde se nalaze neke pomocne funkcije

fun intersect(lista1 : Array<Triple<Int, Int, Boolean>>, lista2: Array<Triple<Int, Int, Boolean>>, duzina : Int): List<Triple<Int, Int, Boolean>> {
    if(lista1.size < duzina || lista2.size < duzina) return emptyList();

    val ret_val : MutableList<Triple<Int, Int, Boolean>> = emptyList<Triple<Int, Int, Boolean>>().toMutableList();

    for (i in 1..duzina) {
        if (lista1[i-1].equals(lista2[i-1]))
            ret_val.add(Triple(lista1[i-1].first, lista1[i-1].second, lista1[i-1].third))
    }
    return ret_val;
}


fun extremePoints(leftExtreme: Int, rightExtreme: Int, filledPoint: Int, ruleLength: Int) : List<Int> {
    val ret_val = emptyList<Int>().toMutableList()

    if(leftExtreme >= filledPoint || filledPoint >= rightExtreme) return ret_val

    if(filledPoint - leftExtreme < ruleLength) {
        for(j in 1..ruleLength - (filledPoint - leftExtreme)) {
            ret_val.add(j+filledPoint)
        }
    }

    if(rightExtreme - filledPoint < ruleLength) {
        for(j in 1..ruleLength - (rightExtreme - filledPoint)) {
            ret_val.add(filledPoint-j)
        }
    }

    return ret_val
}