package com.example.vkcupsecond.pages

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import com.example.vkcupsecond.*

@Composable
fun AllPagesInOne() {
    Scaffold {
        Column(modifier = Modifier, verticalArrangement = Arrangement.SpaceBetween) {
            interviewSpecificPage(
                questionsAnswersListIn = remember {
                    mutableStateListOf(
                        Question(
                            createContentSpecificPage(),
                            questionAnswered = mutableStateOf(false),
                            questionRightAnswered = false,
                            questionState = StateAnswer.NOTANSWERED
                        )
                    )
                },
                id = 0
            )
            val generatedList = generateListOfFillGapsWords("Этот текст для заполнения пропусков во вкладке всех экранов")
            FillGapsSpecificPage(
                gapsCount = generatedList.gapsCount, id = 0,
                wordList = generatedList.wordList
            )
            ReviewSpecificPage(reviewName = "Оценка", id = 1 )

        }
    }
}