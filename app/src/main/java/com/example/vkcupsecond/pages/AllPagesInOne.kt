package com.example.vkcupsecond.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.vkcupsecond.*

@Composable
fun AllPagesInOne(){
    Scaffold {
        Column() {
            interviewSpecificPage(remember {
                mutableStateListOf(Question(createContentSpecificPage()))
            }, id = 0)

            val generatedList = mutableListOf<Word>()

            FillGapsSpecificPage(gapsCount = , id = ,
            wordList = mutableListOf()
            )
        }
    }
}