package com.example.vkcupsecond

import android.os.Handler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavHostController
import com.example.vkcupsecond.ui.theme.myColors
import java.util.Timer
import kotlin.concurrent.schedule

@Composable
fun ReviewView(
    navHostController: NavHostController,
    reviewList: MutableList<Int>,
    specificPageIdentifier: String,
    onPreScroll: (MutableList<Int>) -> Unit,
) {
    InfinityView(
        navHostController = navHostController,
        list = reviewList, onPreScroll = onPreScroll,
        specificPageIdentifier = specificPageIdentifier,
        cardName = "Оценка"//our page in infinity list
    )
}

@Composable
fun ReviewSpecificPage(
    reviewName: String,
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
            backgroundColor = if (DataProvider.Review.rated.value) {
                MaterialTheme.myColors.correctColor
            } else MaterialTheme.myColors.interviewHeaderColor,
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
                    DataProvider.Review.rated.value =
                        InAppReview(Icons.Outlined.Grade,Icons.Filled.Grade, DataProvider.Review.rated)
                }
            }
        }
    }
}


@Composable
fun InAppReview(
    iconReview: ImageVector,
    iconReviewFilled:ImageVector,
    ratedIn: MutableState<Boolean>,
//                oneStar:()->Unit,
//                twoStars:()->Unit,
//                threeStars:()->Unit,
//                fourStars:()->Unit,
//                fiveStars:()->Unit,
): Boolean {
    val rated = remember {
        ratedIn
    }
    val stars = object {
        val localRatedState = remember {
            mutableStateOf(false)
        }
        val oneStarIcon = remember {
            mutableStateOf(iconReview)
        }
        val twoStarsIcon = remember {
            mutableStateOf(iconReview)
        }
        val threeStarsIcon = remember {
            mutableStateOf(iconReview)
        }
        val fourStarsIcon = remember {
            mutableStateOf(iconReview)
        }
        val fiveStarsIcon = remember {
            mutableStateOf(iconReview)
        }
        val timeAnimation = 100L

        fun oneStar() {
            oneStarIcon.value = iconReviewFilled
             twoStarsIcon.value = iconReview
             threeStarsIcon.value = iconReview
             fourStarsIcon.value = iconReview
             fiveStarsIcon.value = iconReview
            localRatedState.value = true
        }

        fun twoStars() {
            oneStarIcon.value = iconReviewFilled
             twoStarsIcon.value = iconReviewFilled
             threeStarsIcon.value = iconReview
             fourStarsIcon.value = iconReview
            fiveStarsIcon.value = iconReview
            localRatedState.value = true

        }

        fun threeStars() {
            oneStarIcon.value = iconReviewFilled
            twoStarsIcon.value = iconReviewFilled
             threeStarsIcon.value = iconReviewFilled
             fourStarsIcon.value = iconReview
             fiveStarsIcon.value = iconReview
            localRatedState.value = true

        }

        fun fourStars() {
            oneStarIcon.value = iconReviewFilled
            twoStarsIcon.value = iconReviewFilled
             threeStarsIcon.value = iconReviewFilled
             fourStarsIcon.value = iconReviewFilled
            fiveStarsIcon.value = iconReview
            localRatedState.value = true

        }

        fun fiveStars() {
            oneStarIcon.value = iconReviewFilled
            twoStarsIcon.value = iconReviewFilled
            threeStarsIcon.value = iconReviewFilled
            fourStarsIcon.value = iconReviewFilled
            fiveStarsIcon.value = iconReviewFilled

            localRatedState.value = true

        }
    }
    Row() {
        Icon(
            modifier = Modifier.clickable {
                stars.oneStar()

            },
            imageVector = stars.oneStarIcon.value,
            contentDescription = stringResource(id = R.string.app_review),
            tint = MaterialTheme.myColors.dzenColor,
        )
        Icon(
            modifier = Modifier.clickable {

                stars.twoStars()

            },
            imageVector = stars.twoStarsIcon.value,
            contentDescription = stringResource(id = R.string.app_review),
            tint = MaterialTheme.myColors.dzenColor,
        )
        Icon(
            modifier = Modifier.clickable {
                stars.threeStars()

            },
            imageVector = stars.threeStarsIcon.value,
            contentDescription = stringResource(id = R.string.app_review),
            tint = MaterialTheme.myColors.dzenColor,
        )
        Icon(
            modifier = Modifier.clickable {
                stars.fourStars()
            },
            imageVector = stars.fourStarsIcon.value,
            contentDescription = stringResource(id = R.string.app_review),
            tint = MaterialTheme.myColors.dzenColor,
        )
        Icon(
            modifier = Modifier.clickable {
                stars.fiveStars()
            },
            imageVector = stars.fiveStarsIcon.value,
            contentDescription = stringResource(id = R.string.app_review),
            tint = MaterialTheme.myColors.dzenColor,
        )
    }
    return stars.localRatedState.value
}


