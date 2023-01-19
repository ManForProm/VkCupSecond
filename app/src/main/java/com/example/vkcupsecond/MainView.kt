package com.example.vkcupsecond

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vkcupsecond.ui.theme.VkCupSecondTheme


data class Screen(val id: Int, val name: String)

@Composable
fun MainView(navHostController: NavHostController, screenList: List<Screen>) {
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
        },
        bottomBar = {
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(screenList) { screen ->
                ScreenCard(
                    name = screen.name,
                    id = screen.id,
                    onClickScreenCard = ::navigateToPageOnClick,
                    navHostController = navHostController
                )
            }
        }
    }
}

fun navigateToPageOnClick(id: Int, navHostController: NavHostController) {
    when (id) {
        1 -> navHostController.navigate("interviewPage")
        2 -> navHostController.navigate("twoColumsPage")
        3 -> navHostController.navigate("dragAndDropPage")
        4 -> navHostController.navigate("fillGapsPage")
        5 -> navHostController.navigate("reviewPage")
        6 -> navHostController.navigate("allPagesInOne")
    }
}

@Composable
fun ScreenCard(
    name: String,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    navHostController: NavHostController,
    id: Int,
    onClickScreenCard: (Int, NavHostController) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(onClick = { onClickScreenCard(id, navHostController) }),
        backgroundColor = MaterialTheme.colors.primary,
        shape = shape,
        elevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(12.dp), Arrangement.Center
        ) {
            Text(text = name)
        }
    }
}
