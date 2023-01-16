package com.example.vkcupsecond

import androidx.compose.runtime.snapshots.SnapshotStateList

fun Int.forEach(expression: (Int) -> Unit){
    for(i in 0..this){
        expression(i)
    }
}

fun <T> SnapshotStateList<T>.swapList(newList: SnapshotStateList<T>){
    clear()
    addAll(newList)
}

fun numberToName(num:Int) =
    when(num){
        1 -> "Один"
        2 -> "Два"
        3 -> "Три"
        4 -> "Четыре"
        5 -> "Пять"
        6 -> "Шесть"
        7 -> "Семь"
        8 -> "Восемь"
        else -> "Unexpected number"
    }