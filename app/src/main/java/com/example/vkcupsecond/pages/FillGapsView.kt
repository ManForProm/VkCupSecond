package com.example.vkcupsecond

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vkcupsecond.ui.theme.Shapes
import com.example.vkcupsecond.ui.theme.myColors
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun FillGapsView(
    navHostController: NavHostController,
    questionsFillGapsList: MutableList<Int>,
    specificPageIdentifier: String,
    onPreScroll: (MutableList<Int>) -> Unit,
) {
    InfinityView(
        navHostController = navHostController,
        list = questionsFillGapsList,
        onPreScroll = onPreScroll,
        specificPageIdentifier = specificPageIdentifier,
        onClickSpecificCard = {
        },
        cardName = "Вопрос"//our page in infinity list
    )
}

@Composable
fun FillGapsSpecificPage(
    reviewName: String,
    wordList: MutableList<Word> = mutableListOf(),
    gapsCount: Int,
    id: Int
) {
    val stateCard = remember {
        mutableStateOf(WordAnswerState.NOTFILL)
    }
    val cardColorChanger = object {
        fun changeState(state: WordAnswerState) {
            stateCard.value = state
        }
        @Composable
        fun changeColor(state: WordAnswerState):Color{
            return when(state){
                WordAnswerState.NOTFILL -> MaterialTheme.myColors.interviewHeaderColor
                WordAnswerState.CORRECT -> MaterialTheme.myColors.correctColor
                WordAnswerState.INCORRECT -> MaterialTheme.myColors.incorrectColorALittle
            }
        }
    }
    Scaffold(
        modifier = Modifier,
        backgroundColor = MaterialTheme.colors.background,
        topBar = {},
        bottomBar = {},
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            backgroundColor = cardColorChanger.changeColor(state = stateCard.value),
            shape = RoundedCornerShape(10.dp),
            elevation = 10.dp
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Column() {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "$reviewName №$id",
                        fontSize = 20.sp,
                        color = MaterialTheme.myColors.dzenColor
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "Заполните пропуски словами",
                        fontSize = 15.sp,
                        color = MaterialTheme.myColors.interviewHeaderColor
                    )
                    FillGapsFunction(wordList, gapsCount, cardColorChanger::changeState)
                    PoopingUpButton(modifier = Modifier.align(Alignment.CenterHorizontally),
                    visibility = stateCard.value == WordAnswerState.CORRECT,
                    text = "Correct!"){
                        DataProvider.navHostControllerGlobal.navigate("fillGapsPage")
                    }
                }

            }
        }
    }
}

enum class WordAnswerState() {
    CORRECT, INCORRECT, NOTFILL
}


@Composable
fun FillGapsFunction(
    wordListIn: MutableList<Word>,
    gapsCount: Int,
    isRight: (WordAnswerState) -> Unit
) {
    val wordList = remember {
        mutableStateOf(wordListIn)
    }

    val answersList = remember {
        mutableListOf<MutableState<String>>()
    }
    val cardState = remember {
        mutableStateOf(WordAnswerState.NOTFILL)
    }
    val gapsCounter = remember {
        mutableStateOf(gapsCount)
    }
    val rightGapsCounter = remember {
        mutableStateOf(0)
    }
    isRight(
        if (gapsCounter.value == rightGapsCounter.value) WordAnswerState.CORRECT
        else WordAnswerState.NOTFILL
//    else WordAnswerState.INCORRECT
    )
    Column() {
//        Text("Gaps in text " + gapsCounter.value.toString())
//        Text(text ="Correct gaps " + rightGapsCounter.value.toString())
        FlowRow(
            modifier = Modifier.padding(8.dp),
            mainAxisSize = SizeMode.Wrap,
            crossAxisSpacing = 12.dp,
            mainAxisSpacing = 8.dp
        ) {
            wordList.value.forEachIndexed { index, word ->
                answersList.add(remember {
                    mutableStateOf("")
                })
                val focusManager = LocalFocusManager.current
                val wordState = rememberSaveable {
                    mutableStateOf(WordAnswerState.NOTFILL)
                }
                if (wordState.value == WordAnswerState.CORRECT){
                    if(word.next.value) {
                        focusManager.moveFocus(
                            FocusDirection.Next
                        )
                        word.next.value = false
                    }
                }
                val changeColor = object {
                    @Composable
                    fun changeColor(state: WordAnswerState): Color {
                        return when (state) {
                            WordAnswerState.NOTFILL -> MaterialTheme.myColors.invisible
                            WordAnswerState.CORRECT -> MaterialTheme.myColors.correctColor
                            WordAnswerState.INCORRECT -> MaterialTheme.myColors.incorrectColor
                        }
                    }

                    @Composable
                    fun changeColorTwoChoice(state: WordAnswerState): Color {
                        return if (changeColor(state = state) == MaterialTheme.myColors.invisible)
                            MaterialTheme.myColors.dzenColor
                        else changeColor(state = state)
                    }
                }
                if (word.needGap) {
                    BasicTextField(modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .border(
                            BorderStroke(
                                1.dp,
                                changeColor.changeColorTwoChoice(state = wordState.value)
                            ),
                            shape = Shapes.medium
                        )
                        .background(
                            changeColor.changeColor(wordState.value),
                            shape = Shapes.medium
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                        textStyle = TextStyle(fontSize = 18.sp),
                        value = answersList[index].value,
                        onValueChange = {

                            answersList[index].value = it
                            if (it == word.word) {
                                rightGapsCounter.value++
                                wordState.value = WordAnswerState.CORRECT
                            } else {
                                wordState.value = WordAnswerState.INCORRECT
                            }
                        },
                        enabled = wordState.value != WordAnswerState.CORRECT,
                        decorationBox = { innerTextField ->
                            if (answersList[index].value.isEmpty()) {
                                wordState.value = WordAnswerState.NOTFILL
                                cardState.value = WordAnswerState.NOTFILL
                                Text(text = "_".repeat(word.word.length))
                            }
                            innerTextField()
                        })
                } else Text(text = " ${word.word}", fontSize = 18.sp)
            }
        }
    }
}