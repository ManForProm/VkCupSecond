package com.example.vkcupsecond

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vkcupsecond.ui.theme.inter
import com.example.vkcupsecond.ui.theme.myColors
import kotlin.math.roundToInt

@Composable
fun InterviewView(
    navHostController: NavHostController,
    questionsList: MutableList<Int> = mutableListOf(),
    specificPageIdentifier: String,
    onPreScroll: (MutableList<Int>) -> Unit,
) {
    InfinityView(
        navHostController = navHostController,
        list = questionsList, onPreScroll = onPreScroll,
        specificPageIdentifier = specificPageIdentifier,
        cardName = "Вопрос №"//our page in infinity list
    )
}

@Composable
fun interviewSpecificPage(
    questionsAnswersListIn: SnapshotStateList<Question<AnswerInterview<StateAnswer>>>,
    title: String = "",
    id: Int
) {
    val questionsAnswersList = remember {
        mutableStateOf(questionsAnswersListIn)
    }
    val questionsList = DataProvider.Interview.questionsAnswersList

    val changeState = object {
        val questionStateLocal = remember {
            mutableStateOf(StateAnswer.NOTANSWERED)
        }
        val clicked =  remember {
            mutableStateOf(false)
        }

        fun onClickAnswer(
            stateButton: MutableState<StateAnswer>,
            id: Int,
            indexAnswer: Int,
            questionAnswered: MutableState<Boolean>,
            isRight: Boolean
        ) {
            DataProvider.Interview.questionsAnswersList[id].apply {

                if (stateButton.value == StateAnswer.NOTANSWERED) {

                    DataProvider.Interview.questionsAnswersList[id] =
                        copy(questionAnswered = mutableStateOf(true))
                    clicked.value = true

                    if (isRight) {

                        DataProvider.Interview.questionsAnswersList[id] =
                            copy(questionState = StateAnswer.ANSWEREDCORRECT)

                        listAnswers[indexAnswer] =
                            listAnswers[indexAnswer].copy(isClicked = StateAnswer.ANSWEREDCORRECT)
                        stateButton.value = StateAnswer.ANSWEREDCORRECT

                        questionStateLocal.value = StateAnswer.ANSWEREDCORRECT

                    } else {

                        DataProvider.Interview.questionsAnswersList[id] =
                            copy(questionState = StateAnswer.ANSWEREDINCORRECT)
                        listAnswers[indexAnswer] =
                            listAnswers[indexAnswer].copy(isClicked = StateAnswer.ANSWEREDINCORRECT)
                        stateButton.value = StateAnswer.ANSWEREDINCORRECT

                        questionStateLocal.value = StateAnswer.ANSWEREDINCORRECT

                    }
                } else stateButton.value = StateAnswer.CLICKBLOCK
            }
            println(questionStateLocal)
        }
    }

    val questionAnswered = remember {
     mutableStateOf(changeState.clicked)
    }
    Scaffold(
        modifier = Modifier,
        backgroundColor = MaterialTheme.colors.background,
        topBar = { if(title.isNotEmpty())ScaffoldInterviewSpecificTopBar(questionName = title,) },
        bottomBar = {
        }
    ) {
        val questionState by remember {
            mutableStateOf(questionsAnswersList.value[id].questionState)
        }
        var cardHeightPx by remember {
            mutableStateOf(0f)
        }
        var cardWidthPx by remember {
            mutableStateOf(0f)
        }
//        val calcBack = when (questionState) {
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp)
                .onGloballyPositioned { coordinates ->
                    cardHeightPx = coordinates.size.height.toFloat()
                    cardWidthPx = coordinates.size.width.toFloat()
                },
            backgroundColor = MaterialTheme.myColors.background
            ,
            shape = RoundedCornerShape(10.dp),
            elevation = 10.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()){
                BackgroundColorCircle(
                    modifier = Modifier
                        .align(Alignment.Center),
                    visibility = questionAnswered.value.value,
                    size = cardHeightPx,
                    color = when (changeState.questionStateLocal.value) {
                        StateAnswer.NOTANSWERED -> MaterialTheme.myColors.background
                        StateAnswer.ANSWEREDCORRECT -> MaterialTheme.myColors.correctColorALittle
                        StateAnswer.ANSWEREDINCORRECT -> MaterialTheme.myColors.incorrectColorALittle
                        StateAnswer.CLICKBLOCK -> MaterialTheme.myColors.background
                    },
                    offset = Offset(cardWidthPx / 2,cardHeightPx / 2)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(questionsAnswersList.value[id].listAnswers) { indexAnswer, answer ->
                    val stateButton = remember {
                        mutableStateOf(answer.isClicked)
                    }
                    SpecificInterviewCard(
                        name = answer.name,
                        indexAnswer = indexAnswer,
                        percent = answer.percent,
                        isRight = answer.isRight,
                        stateButton = stateButton,
                        isQuestionAnswered = questionAnswered.value,
                        id = id,
                        onClickCard = changeState::onClickAnswer,
                    )
                }
            }
        }
    }
}

@Composable
fun ScaffoldInterviewSpecificTopBar(questionName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        backgroundColor = MaterialTheme.myColors.interviewHeaderColor,
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            Arrangement.Center
        ) {
            Text(
                text = questionName,
                fontSize = 20.sp,
                fontFamily = inter,
                color = MaterialTheme.myColors.dzenColor
            )
        }
    }

}

enum class StateAnswer() {
    NOTANSWERED(), ANSWEREDCORRECT(), ANSWEREDINCORRECT(), CLICKBLOCK()
}

@Composable
fun SpecificInterviewCard(
    name: String,
    indexAnswer: Int,
    percent: Float,
    isRight: Boolean,
    stateButton: MutableState<StateAnswer>,
    isQuestionAnswered: MutableState<Boolean>,
    id: Int,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    onClickCard: (
        stateButton: MutableState<StateAnswer>,
        id: Int,
        indexAnswer: Int,
        questionAnswered: MutableState<Boolean>,
        isRight: Boolean
    ) -> Unit
) {
    var state: MutableState<StateAnswer> = remember {
        mutableStateOf(StateAnswer.NOTANSWERED)
    }
    val correctVisibility = remember {
        mutableStateOf(false)
    }
    val incorrectVisibility = remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(enabled = (stateButton.value == StateAnswer.NOTANSWERED) && (!isQuestionAnswered.value)) {

                onClickCard(
                    stateButton,
                    id,
                    indexAnswer,
                    isQuestionAnswered,
                    isRight
                )
                correctVisibility.value = stateButton.value == StateAnswer.ANSWEREDCORRECT
                incorrectVisibility.value = stateButton.value == StateAnswer.ANSWEREDINCORRECT

            },
        backgroundColor = if ((correctVisibility.value)||(isQuestionAnswered.value && isRight)) {
            MaterialTheme.myColors.correctColor
        } else if (incorrectVisibility.value) {
            MaterialTheme.myColors.incorrectColor
        } else MaterialTheme.colors.primary,
        shape = shape,
        elevation = 10.dp,
    ) {
//        BackgroundColorCircle(
//            visibility = correctVisibility.value
//                    || incorrectVisibility.value,
//            color = if (correctVisibility.value) {
//                MaterialTheme.myColors.correctColor
//            } else {
//                MaterialTheme.myColors.incorrectColor
//            }
//        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(text = name)
                Text(text = if(isRight) "этот ответ правильный" else "")
            }
            VisibleText(
                text = (((percent * 10).roundToInt()) / 10.0).toString(),
                visibility = isQuestionAnswered.value
            )
//            AnimatedVisibility(visible = correctVisibility.value) {
//                Icon(
//                    imageVector = Icons.Filled.Done,
//                    contentDescription = stringResource(id = R.string.done_icon_desc),
//                    tint = MaterialTheme.myColors.background
//                )
//            }
            AnimatedVisibility(visible = incorrectVisibility.value || correctVisibility.value) {
                Icon(
                    imageVector = if(correctVisibility.value) Icons.Filled.Done else Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.wrong_icon_desc),
                    tint = MaterialTheme.myColors.background
                )
            }

        }
    }
}

@Composable
fun BackgroundColorCircle(
    modifier: Modifier = Modifier,
    visibility: Boolean,
    size: Float = 1000f,
    offset: Offset = Offset( (0..500).random().toFloat(),
                (0..100).random().toFloat()),
    color: Color = MaterialTheme.myColors.dzenColor,
    sizeCircle: Float = size
) {
    val animationTargetState = remember { mutableStateOf(0f) }

    if (visibility) animationTargetState.value = sizeCircle else animationTargetState.value = 0f
    val animatedFloatState = animateFloatAsState(
        targetValue = animationTargetState.value,
        animationSpec = tween(durationMillis = 1000)
    )
    if (visibility) animationTargetState.value = 0.99f else animationTargetState.value = 0.20f
    val animatedAlphaState = animateFloatAsState(
        targetValue = animationTargetState.value,
        animationSpec = tween(durationMillis = 1000)
    )
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(alpha = animatedAlphaState.value)
    ) {
        drawCircle(
            color = color,
            radius = animatedFloatState.value,
            center = offset,
            blendMode = BlendMode.Xor
        )
    }
}

fun createContentSpecificPage(): MutableList<AnswerInterview<StateAnswer>> {
    val answersList = mutableListOf<AnswerInterview<StateAnswer>>()
    val answersCount = (2..7).random()
    val listResponders = mutableListOf<Float>()
    val listRightAnswers = mutableListOf<Int>()
    val rightAnswers = if (answersCount == 2) (1..2).random() else (1..4).random()
    rightAnswers.forEach {
        run recalculate@{
            val calculateIndex = (0..answersCount).random()
            listRightAnswers.forEach { if (it == calculateIndex) return@recalculate }
            listRightAnswers.add(calculateIndex)
        }
    }
    answersCount.forEach {
        listResponders.add((0..100).random().toFloat())
    }
    val allResponders = listResponders.sum()
    answersCount.forEach { index ->
        var isRight = false
        listRightAnswers.forEach { isRight = it == index }
        answersList.add(
            index, AnswerInterview(
                name = numberToName(index + 1),
                percent = (listResponders[index] / allResponders) * 100,
                isRight = isRight,
                isClicked = StateAnswer.NOTANSWERED
            )
        )
    }
    return answersList
}