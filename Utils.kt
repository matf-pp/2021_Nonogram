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