package com.example.vkcupsecond

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf


object DataProvider {
    val screenList = listOf(
        Screen(1, "Многоступенчатый опрос"),
        Screen(2, "Сопоставление элементов между двумя столбцами"),
        Screen(3, "Перетаскивание вариантов в пропуски в тексте"),
        Screen(4, "Заполнение пропуска в тексте"),
        Screen(5, "Оценка прочитанной статьи — от 1 до 5 звёзд"),
    )
    val questionList = (0..20).toMutableList()
    val reviewList = (0..20).toMutableList()
    val reviewSpecifiedList = mutableStateListOf<ReviewInformation>()
    val questionsAnswersList = mutableStateListOf<Question>()
}

data class Answer(
    val name: String,
    val percent: Float,
    val isRight: Boolean,
    val isClicked: AnimationStateButton
)
data class Question(
    val listAnswers: List<Answer>,
    var questionAnswered: MutableState<Boolean>,
    var questionRightAnswered:Boolean,
    var questionState: StateAnswer
)
data class ReviewInformation(
    val avarageReview:Float,
    val countReviewers:Int
)

enum class AnimationStateButton(val boolean: Boolean) {
    CLICKED(true), UNCLICKED(false), FIRSTTIME(false)
}
