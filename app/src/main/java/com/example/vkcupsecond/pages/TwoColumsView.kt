package com.example.vkcupsecond

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun TwoColumsView(
    navHostController: NavHostController,
    questionsTwoColumsList: MutableList<Int>,
    specificPageIdentifier: String,
    onPreScroll: (MutableList<Int>) -> Unit,
) {
    InfinityView(
        navHostController = navHostController,
        list = questionsTwoColumsList,
        onPreScroll = onPreScroll,
        specificPageIdentifier = specificPageIdentifier,
        onClickSpecificCard = {},
        cardName = "Вопрос"//our page in infinity list
    )
}