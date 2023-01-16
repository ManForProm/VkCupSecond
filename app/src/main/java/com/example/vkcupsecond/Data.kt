package com.example.vkcupsecond

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController


object DataProvider {
    lateinit var navHostControllerGlobal:NavHostController
    val screenList = listOf(
        Screen(1, "Многоступенчатый опрос"),
        Screen(2, "Сопоставление элементов между двумя столбцами"),
        Screen(3, "Перетаскивание вариантов в пропуски в тексте"),
        Screen(4, "Заполнение пропуска в тексте"),
        Screen(5, "Оценка прочитанной статьи — от 1 до 5 звёзд"),
    )
    object Interview{
        val questionList = (0..20).toMutableList()
        val questionsAnswersList = mutableStateListOf<Question>()
    }
    object FillGaps{
        val textFillGapsList = (0..20).toMutableList()
        val gapsSpecifiedList = mutableStateListOf<GapsInformation>()
    }
    object Review {
        val reviewList = (0..20).toMutableList()
        val reviewSpecifiedList = mutableStateListOf<ReviewInformation>()
        val rated = mutableStateOf(false)
    }


}

data class Answer(
    val name: String,
    val percent: Float,
    val isRight: Boolean,
    val isClicked: StateAnswer
)
data class Question(
    val listAnswers: MutableList<Answer>,
    var questionAnswered: MutableState<Boolean>,
    var questionRightAnswered:Boolean,
    var questionState: StateAnswer
)
data class ReviewInformation(
    val avarageReview:Float,
    val countReviewers:Int
)

data class GapsInformation(
    val wordList:MutableList<Word>,
    val gapsCount:Int,
)
data class Word(val word: String, val needGap:Boolean,val next:MutableState<Boolean>)

enum class AnimationStateButton(val boolean: Boolean) {
    CLICKED(true), UNCLICKED(false), FIRSTTIME(false)
}
