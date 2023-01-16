package com.example.vkcupsecond

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
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
import com.example.vkcupsecond.DataProvider.Interview.questionList
import com.example.vkcupsecond.ui.theme.VkCupSecondTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fillSpecifiedInterviewList(DataProvider.Interview.questionList)
        fillSpecifiedReviewList(DataProvider.Review.reviewList)
        setContent {
            VkCupSecondTheme {
                MyAppNavHost()
            }
        }
    }
}

fun<T> calculateAnswersForEachQuestion(list: MutableList<Int>,fillList:SnapshotStateList<T>, obj:T){
    list.forEach{
        fillList.add(obj)
    }
}

private fun<T> onScrollList(list:MutableList<Int>,fillList:SnapshotStateList<T>, obj:T,){
    list.add(list.last() + 1)
    calculateAnswersForEachQuestion(list,fillList,obj)
}

private fun fillSpecifiedInterviewList(list:MutableList<Int>){
    onScrollList(list = list, DataProvider.Interview.questionsAnswersList, Question(listAnswers = createContentSpecificPage(),
        questionAnswered = mutableStateOf(false), questionRightAnswered = false, questionState = StateAnswer.NOTANSWERED))
}

private fun fillSpecifiedReviewList(list:MutableList<Int>,){
    onScrollList(list = list, DataProvider.Review.reviewSpecifiedList, ReviewInformation(avarageReview = (0..50).random().toFloat()/10,
        countReviewers = (0..2000).random()))
}

private fun fillSpecifiedGapsList(list:MutableList<Int>,){
    onScrollList(list = list, DataProvider.FillGaps.gapsSpecifiedList,
        GapsInformation(mutableListOf(1,2), mutableListOf("aa","bbb","CCC","GG")),)
}

@Composable
fun MyAppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "mainView"
) {

    NavHost(navController = navController, startDestination = startDestination) {
        composable("mainView") {
            MainView(navController,DataProvider.screenList)
        }
        composable("interviewPage") {
            InterviewView(navHostController = navController,
                questionsList = DataProvider.Interview.questionList,
                onPreScroll = ::fillSpecifiedInterviewList,
            specificPageIdentifier = "interviewSpecificPage",)
        }
        composable(
            route = "interviewSpecificPage/{id}/{questionName}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("questionName") { type = NavType.StringType },
            )
        ){
            val id = it.arguments?.getInt("id")!!
            val questionName = it.arguments?.getString("questionName")!!
            interviewSpecificPage(DataProvider.Interview.questionsAnswersList[id].listAnswers,
                questionName,
                DataProvider.Interview.questionsAnswersList[id].questionState,
                id)
        }
        composable("twoColumsPage") {
            TwoColumsView()
        }
        composable("dragAndDropPage") {
            DragAndDropView()
        }
        //FillGaps
        composable("fillGapsPage") {
            FillGapsView(navHostController = navController,
                questionsFillGapsList = DataProvider.FillGaps.textFillGapsList,
                onPreScroll = ::fillSpecifiedGapsList,
                specificPageIdentifier = "fillGapsSpecificPage",)
        }
        composable(
            route = "fillGapsSpecificPage/{id}/{questionName}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("questionName") { type = NavType.StringType },
            )
        ){
            val id = it.arguments?.getInt("id")!!
            val questionName = it.arguments?.getString("questionName")!!
            FillGapsSpecificPage(id = id,
                reviewName = questionName,
//            wordList = DataProvider.FillGaps.gapsSpecifiedList[id].wordList,
//            gapsLocation = DataProvider.FillGaps.gapsSpecifiedList[id].gapsLocation,
            )
        }

        //Review
        composable("reviewPage") {
            ReviewView(navHostController = navController,
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
        ){
            val id = it.arguments?.getInt("id")!!
            val questionName = it.arguments?.getString("questionName")!!
            ReviewSpecificPage(id = id,
            reviewName = questionName,)
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VkCupSecondTheme {
        Text(text = "Hello!")
    }
}