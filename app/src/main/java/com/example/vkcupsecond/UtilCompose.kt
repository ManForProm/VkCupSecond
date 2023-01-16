package com.example.vkcupsecond

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vkcupsecond.ui.theme.inter

@Composable
fun InfinityView(
    navHostController: NavHostController,
    specificPageIdentifier:String,
    cardName:String,
    list: MutableList<Int>,
    onPreScroll: (MutableList<Int>) -> Unit,
    onClickSpecificCard:()->Unit = {}
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
                    name = cardName,
                    id = it,
                    specificPageIdentifier = specificPageIdentifier,
                    navHostController = navHostController,
                    onClickSpecificCard = onClickSpecificCard,
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
    onClickSpecificCard:()->Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable{
                onClickSpecificCard()
                navigateToSpecificInfinityView(id,name,specificPageIdentifier,navHostController = navHostController)
            },
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
@Composable
fun PoopingUpButton(modifier: Modifier, visibility: Boolean = true,
                    text: String = "Next",
                    onClick: () -> Unit) {
    val animationExpandShrinkSpec = tween<IntSize>(durationMillis = 200)
    AnimatedVisibility(
        visible = visibility,
        enter = expandVertically(
            animationSpec = animationExpandShrinkSpec,
            expandFrom = Alignment.Top
        ) + fadeIn(),
        exit = shrinkVertically(
            animationSpec = animationExpandShrinkSpec,
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        Button(modifier = modifier,
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            VisibleText(
                text = text,
                visibility = true,
                modifier = Modifier.padding(top = 7.dp, bottom = 7.dp,start = 20.dp, end = 20.dp),
                color = Color.Gray,
                fontFamily = inter,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
        }
    }

}
