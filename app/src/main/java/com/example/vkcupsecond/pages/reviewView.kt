package com.example.vkcupsecond

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grade
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

import androidx.navigation.NavHostController
import com.example.vkcupsecond.ui.theme.myColors

@Composable
fun ReviewView(
    navHostController: NavHostController,
    reviewList: MutableList<Int>,
    specificPageIdentifier:String,
    onPreScroll: (MutableList<Int>) -> Unit
) {
    InfinityView(navHostController = navHostController,
        list = reviewList, onPreScroll = onPreScroll,
        specificPageIdentifier = specificPageIdentifier,//our page in infinity list
        onClickSpecificCard = ,
// )
}

@Composable
fun ReviewSpecificPage(reviewName: String,
                       id: Int){
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

                InAppReview()
            }
        }
    }
}

@Composable
fun InAppReview(){
    Icon(
        imageVector = Icons.Filled.Grade,
        contentDescription = stringResource(id = R.string.app_review)
    )
    Icon(
        imageVector = Icons.Filled.Grade,
        contentDescription = stringResource(id = R.string.app_review)
    )
    Icon(
        imageVector = Icons.Filled.Grade,
        contentDescription = stringResource(id = R.string.app_review)
    )
    Icon(
        imageVector = Icons.Filled.Grade,
        contentDescription = stringResource(id = R.string.app_review)
    )
    Icon(
        imageVector = Icons.Filled.Grade,
        contentDescription = stringResource(id = R.string.app_review)
    )
}


