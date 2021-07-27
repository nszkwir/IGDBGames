package com.spitzer.igdbgames.extensions

fun ArrayList<Int>.toQueryList(): String {
    var queryList = "("
    if (this.isNotEmpty()) {
        queryList += this.first().toString()
        for (i in 1 until this.size - 1) {
            queryList += "," + this[i].toString()
        }
    }
    queryList += ")"
    return queryList
}
