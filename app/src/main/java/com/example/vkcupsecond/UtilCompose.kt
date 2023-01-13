package com.example.vkcupsecond

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun InfinityView(
    navHostController: NavHostController,
    specificPageIdentifier:String,
    list: MutableList<Int>,
    onPreScroll: (MutableList<Int>) -> Unit
) {
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                onPreScroll(list)
                // called when you scroll the content
                return Offset.Zero
            }
        }
    }
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
                .fillMaxWidth()
                .nestedScroll(nestedScrollConnection),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(list) {
                InfinityViewCard(
                    name = "Вопрос",
                    id = it,
                    specificPageIdentifier = specificPageIdentifier,
                    navHostController = navHostController,
                    onClickCard = ::navigateToSpecificInfinityView,
                )
            }
        }
    }

}

@Composable
fun InfinityViewCard(
    name: String,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    specificPageIdentifier:String,
    id: Int,
    navHostController: NavHostController,
    onClickCard: (Int,String, String, NavHostController) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(onClick = { onClickCard(id,specificPageIdentifier, name, navHostController) }),
        backgroundColor = MaterialTheme.colors.primary,
        shape = shape,
        elevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(12.dp), Arrangement.Center
        ) {
            Text(text = "$name№$id")
        }
    }
}

fun navigateToSpecificInterview(
    id: Int,
    questionName: String,
    navHostController: NavHostController
) {
    navHostController.navigate("interviewSpecificPage/$id/$questionName")
}
fun navigateToSpecificInfinityView(
    id: Int,
    questionName: String,
    specificPageIdentifier:String,
    navHostController: NavHostController
) {
    navHostController.navigate("$specificPageIdentifier/$id/$questionName")
}

@Composable
fun VisibleText(
    text: String, visibility: Boolean, modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontFamily: FontFamily? = null,
    fontWeight: FontWeight? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
) {
    AnimatedVisibility(
        visible = visibility,
    ) {
        Text(
            text = text,
            modifier = modifier,
            color = color,
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            fontSize = fontSize,
        )
    }
}
