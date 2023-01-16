package com.example.vkcupsecond

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vkcupsecond.ui.theme.myColors

@Composable
fun FillGapsView(
    navHostController: NavHostController,
    questionsFillGapsList: MutableList<Int>,
    specificPageIdentifier:String,
    onPreScroll: (MutableList<Int>) -> Unit,
) {
    InfinityView(navHostController = navHostController,
        list = questionsFillGapsList,
        onPreScroll = onPreScroll,
        specificPageIdentifier = specificPageIdentifier,
        cardName = "Вопрос"//our page in infinity list
    )
}

@Composable
fun FillGapsSpecificPage(
    reviewName: String,
    wordList:MutableList<String> = mutableListOf(),
    gapsLocation:MutableList<Int> = mutableListOf(),
    id: Int
) {
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
                Column() {
                    Text(
                        text = "$reviewName №$id",
                        fontSize = 20.sp,
                        color = MaterialTheme.myColors.dzenColor
                    )
                    FillGapsFunction(wordList, gapsLocation)
                }
            }
        }
    }
}

@Composable
fun FillGapsFunction(wordList: MutableList<String>, gapsLocation: MutableList<Int>){
    Row() {
        wordList.forEach{
            Text(text = it)
        }
    }
    
    
}