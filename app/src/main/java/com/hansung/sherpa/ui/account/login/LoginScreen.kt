package com.hansung.sherpa.ui.account.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hansung.sherpa.SherpaScreen
import com.hansung.sherpa.StaticValue
import com.hansung.sherpa.ui.common.SherpaDialog
import com.hansung.sherpa.ui.common.SherpaDialogParm
import com.hansung.sherpa.ui.theme.SherpaColor
import com.hansung.sherpa.user.UserManager

/**
 * 로그인을 진행하는 화면
 *
 * @param navController 홈 화면 navController 원형, ※ 화면을 이동한다면, 매개변수로 지정 필수
 */
@Composable
fun LoginScreen(navController: NavController = rememberNavController(), modifier: Modifier = Modifier) {
    val sherpaDialog = remember { mutableStateOf(SherpaDialogParm())}

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        /**
         * Sherpa Dialog
         *
         * 알림 메세지
         */
        if(sherpaDialog.value.show.value) {
            SherpaDialog(
                title = sherpaDialog.value.title,
                message = sherpaDialog.value.message,
                confirmButtonText = sherpaDialog.value.confirmButtonText,
                dismissButtonText = sherpaDialog.value.dismissButtonText,
                onConfirmation = sherpaDialog.value.onConfirmation,
                onDismissRequest = sherpaDialog.value.onDismissRequest
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .clip(RoundedCornerShape(0.dp, 0.dp, 800.dp, 800.dp))
                    .background(SherpaColor)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .background(Color.White)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            /**
             * Screen 명칭이 나오는 영역
             */
            TitleArea()

            /**
             * 보호자 로그인 화면을 구성하는 영역
             */
            LoginArea(navController, sherpaDialog)

            /**
             * 비밀번호 찾기와 회원가입하기 버튼을 디자인한 영역
             */
            FindAccountArea(navController)
        }
    }
}

@Composable
@Preview
fun LoginPreview() {
    LoginScreen()
}