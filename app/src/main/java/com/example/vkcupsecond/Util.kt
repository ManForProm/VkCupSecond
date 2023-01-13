package com.example.vkcupsecond

fun Int.forEach(expression: (Int) -> Unit){
    for(i in 0..this){
        expression(i)
    }
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