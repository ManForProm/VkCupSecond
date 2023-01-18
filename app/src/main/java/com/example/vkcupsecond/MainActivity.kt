package com.example.vkcupsecond

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vkcupsecond.pages.AllPagesInOne
import com.example.vkcupsecond.ui.theme.VkCupSecondTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fillSpecifiedInterviewList(DataProvider.Interview.questionList)
        fillSpecifiedReviewList(DataProvider.Review.reviewList)
        fillSpecifiedGapsList(DataProvider.FillGaps.textFillGapsList)
        setContent {
            VkCupSecondTheme {
                MyAppNavHost()
            }
        }
    }

    override fun onDestroy() {
        DataProvider.navHostControllerGlobal = NavHostController(this)
        super.onDestroy()
    }
}

fun <T> calculateAnswersForEachQuestion(
    list: MutableList<Int>,
    fillList: SnapshotStateList<T>,
    obj: T
) {
    list.forEach {
        fillList.add(obj)
    }
}

private fun <T> onScrollList(list: MutableList<Int>, fillList: SnapshotStateList<T>, obj: T) {
    list.add(list.last() + 1)
    calculateAnswersForEachQuestion(list, fillList, obj)
}

private fun fillSpecifiedInterviewList(list: MutableList<Int>) {
    onScrollList(
        list = list, DataProvider.Interview.questionsAnswersList, Question(
            listAnswers = createContentSpecificPage(),
            questionAnswered = mutableStateOf(false),
            questionRightAnswered = false,
            questionState = StateAnswer.NOTANSWERED
        )
    )
}

private fun fillSpecifiedReviewList(list: MutableList<Int>) {
    onScrollList(
        list = list, DataProvider.Review.reviewSpecifiedList, ReviewInformation(
            avarageReview = (0..50).random().toFloat() / 10,
            countReviewers = (0..2000).random()
        )
    )
}

private class CalculationListHelper() {
    var counts = 0
    fun needGapCalculation(countsMax: Int): Boolean {
        counts++
        if (counts > countsMax) {
            return false
        } else return Random.nextBoolean()
    }
}

fun Int.toBoolean() = this == 0

fun generateListOfFillGapsWords():GapsInformation{
    val generatedList = mutableListOf<Word>()
    "Этот большой текст для заполнения слов и пропусков и всего такого в виде строки".split(" ").forEach {
        generatedList.add(Word(word = it, needGap = (0..5).random().toBoolean(), mutableStateOf(true)))
    }
    var count = 0
    generatedList.forEach {
        if (it.needGap){
            count += 1
        }
    }
    return GapsInformation(generatedList,count)
}
private fun fillSpecifiedGapsList(list: MutableList<Int>) {

    onScrollList(
        list = list, DataProvider.FillGaps.gapsSpecifiedList,
        generateListOfFillGapsWords(),
    )
}

@Composable
fun MyAppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "mainView"
) {
    DataProvider.navHostControllerGlobal = navController
    NavHost(navController = navController, startDestination = startDestination) {
        composable("mainView") {
            MainView(navController, DataProvider.screenList)
        }
        composable("allPagesInOne"){
            AllPagesInOne()
        }
        composable("interviewPage") {
            InterviewView(
                navHostController = navController,
                questionsList = DataProvider.Interview.questionList,
                onPreScroll = ::fillSpecifiedInterviewList,
                specificPageIdentifier = "interviewSpecificPage",
            )
        }
        composable(
            route = "interviewSpecificPage/{id}/{questionName}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("questionName") { type = NavType.StringType },
            )
        ) {
            val id = it.arguments?.getInt("id")!!
            val questionName = it.arguments?.getString("questionName")!!
            interviewSpecificPage(
                DataProvider.Interview.questionsAnswersList,
                questionName + id.toString(),
                id,
            )
        }

        //Two Colums
        composable("twoColumsPage") {
            TwoColumsView(navHostController = navController,
                questionsTwoColumsList = DataProvider.FillGaps.textFillGapsList,
                onPreScroll = ::fillSpecifiedGapsList,
                specificPageIdentifier = "fillGapsSpecificPage",
            )
        }
        composable(
            route = "twoColumsSpecificPage/{id}/{questionName}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("questionName") { type = NavType.StringType },
            )
        ) {
            val id = it.arguments?.getInt("id")!!
            val questionName = it.arguments?.getString("questionName")!!
            TwoColumsSpecificPage(
                id = id,
                reviewName = questionName,
                wordList = DataProvider.FillGaps.gapsSpecifiedList[id].wordList,
                )
        }


        //Drag And Drop
        composable("dragAndDropPage") {
            DragAndDropView()
        }

        //FillGaps
        composable("fillGapsPage") {
            FillGapsView(
                navHostController = navController,
                questionsFillGapsList = DataProvider.FillGaps.textFillGapsList,
                onPreScroll = ::fillSpecifiedGapsList,
                specificPageIdentifier = "fillGapsSpecificPage",
            )
        }
        composable(
            route = "fillGapsSpecificPage/{id}/{questionName}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("questionName") { type = NavType.StringType },
            )
        ) {
            val id = it.arguments?.getInt("id")!!
            val questionName = it.arguments?.getString("questionName")!!
            FillGapsSpecificPage(
                id = id,
                title = questionName + id.toString(),
                wordList = DataProvider.FillGaps.gapsSpecifiedList[id].wordList,
                gapsCount = DataProvider.FillGaps.gapsSpecifiedList[id].gapsCount,

            )
        }

        //Review
        composable("reviewPage") {
            ReviewView(
                navHostController = navController,
                reviewList = DataProvider.Review.reviewList,
                onPreScroll = ::fillSpecifiedReviewList,
                specificPageIdentifier = "reviewSpecificPage",
            )
        }
        composable(
            route = "reviewSpecificPage/{id}/{questionName}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("questionName") { type = NavType.StringType },
            )
        ) {
            val id = it.arguments?.getInt("id")!!
            val questionName = it.arguments?.getString("questionName")!!
            ReviewSpecificPage(
                id = id,
                reviewName = questionName + id.toString(),
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VkCupSecondTheme {
        MyAppNavHost()
    }
}