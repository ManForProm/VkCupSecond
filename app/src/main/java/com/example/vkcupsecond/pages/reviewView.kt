package com.example.vkcupsecond

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavHostController
import com.example.vkcupsecond.ui.theme.myColors

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
        cardName = "Оценка №"//our page in infinity list
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
                        InAppReview(Icons.TwoTone.Star,Icons.Filled.Star, DataProvider.Review.rated)
                    PoopingUpButton(
                        textModifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp), modifier = Modifier.align(Alignment.CenterHorizontally),
                        visibility = DataProvider.Review.rated.value,
                        text = "Thanks!"){
                        DataProvider.navHostControllerGlobal.navigate("reviewPage")
                    }
                }
            }
        }
    }
}


data class Stars(
    val oneStarIcon:ImageVector,
    val twoStarsIcon:ImageVector,
    val threeStarsIcon:ImageVector,
    val fourStarsIcon:ImageVector,
    val fiveStarsIcon:ImageVector,
)
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
        val icons = remember {
            mutableStateOf(Stars(iconReview,
                iconReview,
                iconReview,
                iconReview,
                iconReview,))
        }
        val timeAnimation = 100L

        fun oneStar() {

            icons.value = Stars(iconReviewFilled,
                iconReview,
                iconReview,
                iconReview,
                iconReview,)
            localRatedState.value = true
        }

        fun twoStars() {
            icons.value = Stars(iconReviewFilled,
                iconReviewFilled,
                iconReview,
                iconReview,
                iconReview,)
            localRatedState.value = true

        }

        fun threeStars() {
            icons.value = Stars(iconReviewFilled,
                iconReviewFilled,
                iconReviewFilled,
                iconReview,
                iconReview,)
            localRatedState.value = true

        }

        fun fourStars() {
            icons.value = Stars(iconReviewFilled,
                iconReviewFilled,
                iconReviewFilled,
                iconReviewFilled,
                iconReview,)
            localRatedState.value = true

        }

        fun fiveStars() {
            icons.value = Stars(iconReviewFilled,
                iconReviewFilled,
                iconReviewFilled,
                iconReviewFilled,
                iconReviewFilled,)
            localRatedState.value = true

        }
    }
    Row(horizontalArrangement = Arrangement.Center) {
        Icon(
            modifier = Modifier.clickable {
                stars.oneStar()

            },
            imageVector = stars.icons.value.oneStarIcon,
            contentDescription = stringResource(id = R.string.app_review),
            tint = MaterialTheme.myColors.dzenColor,
        )
        Icon(
            modifier = Modifier.clickable {

                stars.twoStars()

            },
            imageVector = stars.icons.value.twoStarsIcon,
            contentDescription = stringResource(id = R.string.app_review),
            tint = MaterialTheme.myColors.dzenColor,
        )
        Icon(
            modifier = Modifier.clickable {
                stars.threeStars()

            },
            imageVector = stars.icons.value.threeStarsIcon,
            contentDescription = stringResource(id = R.string.app_review),
            tint = MaterialTheme.myColors.dzenColor,
        )
        Icon(
            modifier = Modifier.clickable {
                stars.fourStars()
            },
            imageVector = stars.icons.value.fourStarsIcon,
            contentDescription = stringResource(id = R.string.app_review),
            tint = MaterialTheme.myColors.dzenColor,
        )
        Icon(
            modifier = Modifier.clickable {
                stars.fiveStars()
            },
            imageVector = stars.icons.value.fiveStarsIcon,
            contentDescription = stringResource(id = R.string.app_review),
            tint = MaterialTheme.myColors.dzenColor,
        )
    }
    return stars.localRatedState.value
}

@Preview
@Composable
fun ReviewCpecificCardPreview(){
    ReviewSpecificPage(reviewName = "Preview", id = 1 )
}
