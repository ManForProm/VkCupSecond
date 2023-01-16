package com.example.vkcupsecond

import android.graphics.drawable.Icon
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import com.example.vkcupsecond.ui.theme.MyColors
import com.example.vkcupsecond.ui.theme.myColors
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
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
        cardName = "Вопрос"//our page in infinity list
    )
}

@Composable
fun interviewSpecificPage(
    questionsAnswersListIn: List<Answer> = listOf(),
    questionName: String,
    questionStateIn: StateAnswer,
    id: Int
) {
    val questionsAnswersList = questionsAnswersListIn
    val questionsList = DataProvider.Interview.questionsAnswersList
    Scaffold(
        modifier = Modifier,
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
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
                    Text(text = "$questionName №$id", fontSize = 20.sp, color = Color.White)
                }
            }

        },
        bottomBar = {
        }
    ) {
        val questionState by remember {
            mutableStateOf(questionStateIn)
        }
        val transition = updateTransition(targetState = questionState)
        val background by transition.animateColor(label = "") {
            when (it) {
                StateAnswer.NOTANSWERED -> MaterialTheme.myColors.dzenColor
                StateAnswer.ANSWEREDCORRECT -> MaterialTheme.myColors.correctColor
                StateAnswer.ANSWEREDINCORRECT -> MaterialTheme.myColors.incorrectColor
                StateAnswer.CLICKBLOCK -> MaterialTheme.myColors.dzenColor
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            backgroundColor = background,
            shape = RoundedCornerShape(10.dp),
            elevation = 10.dp
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(questionsAnswersList) { indexAnswer, answer ->
                    SpecificInterviewCard(
                        name = answer.name,
                        indexAnswer = indexAnswer,
                        percent = answer.percent,
                        questionsAnswersList = questionsAnswersList,
                        isRight = answer.isRight,
                        stateButton = answer.isClicked,
                        isQuestionAnswered = questionsList[id].questionAnswered,
                        id = id,
                        onClickCard = ::onClickAnswer,
                    )
                }
            }
        }
    }
}

private fun onClickAnswer(
    stateButton: MutableState<StateAnswer>,
    id: Int,
    indexAnswer: Int,
    list: List<Answer>,
    questionAnswered: MutableState<Boolean>,
    isRight: Boolean
) {
    var list = DataProvider.Interview.questionsAnswersList
    println(" LIST ${list[id].listAnswers[indexAnswer]}")
    if (stateButton.value == StateAnswer.NOTANSWERED && !questionAnswered.value) {
        list[id] = list[id].copy(questionAnswered = mutableStateOf(true))

        if (isRight) {
            list[id] = list[id].copy(questionState = StateAnswer.ANSWEREDCORRECT)
            list[id].listAnswers[indexAnswer] = list[id].listAnswers[indexAnswer].copy(isClicked = StateAnswer.ANSWEREDCORRECT)
            stateButton.value = StateAnswer.ANSWEREDCORRECT
        } else{
            list[id] = list[id].copy(questionState = StateAnswer.ANSWEREDINCORRECT)
            list[id].listAnswers[indexAnswer] = list[id].listAnswers[indexAnswer].copy(isClicked = StateAnswer.ANSWEREDINCORRECT)
            stateButton.value = StateAnswer.ANSWEREDINCORRECT
        }
        if(list[id].questionAnswered.value){

        }
        DataProvider.Interview.questionsAnswersList.swapList(list)
//        println("LIST SWAPPEDDD ${DataProvider.Interview.questionsAnswersList[id].questionState}")
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
    questionsAnswersList: List<Answer>,
    stateButton: StateAnswer,
    isQuestionAnswered: MutableState<Boolean>,
    id: Int,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    onClickCard: (
        stateButton: MutableState<StateAnswer>,
        id: Int,
        indexAnswer: Int,
        questionsAnswersList: List<Answer>,
        questionAnswered: MutableState<Boolean>,
        isRight: Boolean
    ) -> Unit
) {
    var size by remember { mutableStateOf(Size.Zero) }
    var state: MutableState<StateAnswer> = remember {
        mutableStateOf(StateAnswer.NOTANSWERED)
    }
    val correctVisibility = remember {
        mutableStateOf(state.value == StateAnswer.ANSWEREDCORRECT)
    }
    val incorrectVisibility = remember {
        mutableStateOf(state.value == StateAnswer.ANSWEREDINCORRECT)
    }
    val offsetTransfer = remember {
        mutableStateOf(
            Offset(
                (0..500).random().toFloat(),
                (0..100).random().toFloat()
//                    size.width / 2,
//                    size.height
            )
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                if (!isQuestionAnswered.value) {
                    mutableStateOf(
                        onClickCard(
                            mutableStateOf(stateButton),
                            id,
                            indexAnswer,
                            questionsAnswersList,
                            isQuestionAnswered,
                            isRight
                        )
                    )
                }
            }
            .onGloballyPositioned { coordinates ->
                size = coordinates.size.toSize()
            },
        backgroundColor = MaterialTheme.colors.primary,
        shape = shape,
        elevation = 10.dp,
    ) {
        Box() {
            BackgroundColorCircle(
                visibility = correctVisibility.value
                        || incorrectVisibility.value,
                offsetTransfer,
                color = if (correctVisibility.value) {
                    MaterialTheme.myColors.correctColor
                } else {
                    MaterialTheme.myColors.incorrectColor
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = name)
            Column() {
                Text(text = isRight.toString())
                Text(text = isQuestionAnswered.value.toString())
            }
            AnimatedVisibility(visible = correctVisibility.value) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done_icon_desc),
                    tint = MaterialTheme.myColors.correctColor
                )
            }
            AnimatedVisibility(visible = incorrectVisibility.value) {
                Icon(
                    imageVector = Icons.Filled.Dangerous,
                    contentDescription = stringResource(id = R.string.wrong_icon_desc),
                    tint = MaterialTheme.myColors.incorrectColor
                )
            }
            VisibleText(
                text = (((percent * 10).roundToInt()) / 10.0).toString(),
                visibility = state.value == StateAnswer.ANSWEREDCORRECT ||
                        state.value == StateAnswer.ANSWEREDINCORRECT
            )

        }
    }
}

@Composable
fun BackgroundColorCircle(
    visibility: Boolean,
    offset: MutableState<Offset>,
    color: Color,
    sizeCircle: Float = 1000f
) {
    val animationTargetState = remember { mutableStateOf(0f) }

    if (visibility) animationTargetState.value = sizeCircle else animationTargetState.value = 0f
    val animatedFloatState = animateFloatAsState(
        targetValue = animationTargetState.value,
        animationSpec = tween(durationMillis = 500)
    )
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = 0.99f)
    ) {
        drawCircle(
            color = color,
            radius = animatedFloatState.value,
            center = offset.value,
            blendMode = BlendMode.Xor
        )
    }
}

fun createContentSpecificPage(): MutableList<Answer> {
    val answersList = mutableListOf<Answer>()
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
            index, Answer(
                name = numberToName(index + 1),
                percent = (listResponders[index] / allResponders) * 100,
                isRight = isRight,
                isClicked = StateAnswer.NOTANSWERED
            )
        )
    }
    return answersList
}