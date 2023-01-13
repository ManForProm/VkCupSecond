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
    questionsList: MutableList<Int>,
    specificPageIdentifier:String,
    onPreScroll: (MutableList<Int>) -> Unit
){
    InfinityView(navHostController = navHostController,
        list = questionsList, onPreScroll = onPreScroll,
        specificPageIdentifier = specificPageIdentifier//our page in infinity list
    )
}

@Composable
fun interviewSpecificPage(
    questionsAnswersListIn: List<Answer>,
    questionName: String,
    questionStateIn:StateAnswer,
    id: Int
) {
    val questionsAnswersList = questionsAnswersListIn
    val questionsList =  DataProvider.questionsAnswersList
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
            when(it){
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
                items(questionsAnswersList) { answer ->
                    SpecificInterviewCard(
                        name = answer.name,
                        percent = answer.percent,
                        questionsAnswersList = questionsAnswersList,
                        isRight = answer.isRight,
                        stateButton = remember { mutableStateOf(answer.isClicked) },
                        isQuestionAnswered = remember {
                         questionsList[id].questionAnswered
                        },
                        id = id,
                        onClickCard = ::onClickAnswer,
                    )
                }
            }
        }
    }
}

fun <T> SnapshotStateList<T>.swapList(newList: SnapshotStateList<T>){
    clear()
    addAll(newList)
}

fun changeListOnClick(id: Int) {


}

private fun onClickAnswer(
    stateButton: MutableState<AnimationStateButton>,
    id: Int,
    list: List<Answer>,
    questionAnswered: MutableState<Boolean>,
    isRight: Boolean
): StateAnswer {
    var list = DataProvider.questionsAnswersList
    if (stateButton.value == AnimationStateButton.UNCLICKED && !questionAnswered.value) {
        stateButton.value = AnimationStateButton.CLICKED
        list[id] = list[id].copy(questionAnswered = mutableStateOf(true))
        if (isRight) {
            list[id] = list[id].copy(questionState =  StateAnswer.ANSWEREDCORRECT)
            return  StateAnswer.ANSWEREDCORRECT
        }
        else{
            list[id] = list[id].copy(questionState =  StateAnswer.ANSWEREDINCORRECT)
            return StateAnswer.ANSWEREDINCORRECT}
        DataProvider.questionsAnswersList.swapList(list)
        println("LIST SWAPPEDDD ${DataProvider.questionsAnswersList[id].questionState}")
    }

    return StateAnswer.CLICKBLOCK
}

enum class StateAnswer() {
    NOTANSWERED(), ANSWEREDCORRECT(), ANSWEREDINCORRECT(), CLICKBLOCK()
}

@Composable
fun SpecificInterviewCard(
    name: String,
    percent: Float,
    isRight: Boolean,
    questionsAnswersList: List<Answer>,
    stateButton: MutableState<AnimationStateButton>,
    isQuestionAnswered: MutableState<Boolean>,
    id: Int,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    onClickCard: (
        stateButton: MutableState<AnimationStateButton>,
        id: Int,
        questionsAnswersList: List<Answer>,
        questionAnswered: MutableState<Boolean>,
        isRight: Boolean
    ) -> StateAnswer
) {
    var size by remember { mutableStateOf(Size.Zero) }
    var state: MutableState<StateAnswer> = remember {
        mutableStateOf(StateAnswer.NOTANSWERED)
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
                    state = mutableStateOf(
                        onClickCard(
                            stateButton,
                            id,
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
        Box(){
            BackgroundColorCircle(
                visibility = state.value == StateAnswer.ANSWEREDINCORRECT
                        || state.value == StateAnswer.ANSWEREDCORRECT ,
                offsetTransfer,
                color = if (state.value == StateAnswer.ANSWEREDCORRECT) {MaterialTheme.myColors.correctColor}
                else { MaterialTheme.myColors.incorrectColor}
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
            AnimatedVisibility(visible = state.value == StateAnswer.ANSWEREDCORRECT) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done_icon_desc)
                )
            }
            AnimatedVisibility(visible = state.value == StateAnswer.ANSWEREDINCORRECT) {
                Icon(
                    imageVector = Icons.Filled.Dangerous,
                    contentDescription = stringResource(id = R.string.wrong_icon_desc)
                )
            }
            VisibleText(
                text = (((percent * 10).roundToInt()) / 10.0).toString(),
                visibility = state.value == StateAnswer.ANSWEREDCORRECT ||
                        state.value == StateAnswer.ANSWEREDINCORRECT    ||
                        state.value == StateAnswer.CLICKBLOCK
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

fun createContentSpecificPage(): List<Answer> {
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
                isClicked = AnimationStateButton.UNCLICKED
            )
        )
    }
    return answersList
}