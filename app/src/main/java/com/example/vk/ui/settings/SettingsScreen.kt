package com.example.vk.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vk.R
import com.example.vk.datacontrol.AuthViewModel
import com.example.vk.ui.components.bars.BottomBar
import com.example.vk.ui.theme.OrangeContinue
import com.example.vk.ui.theme.OrangePrimary
import com.example.vk.ui.theme.SignupBackground
@Composable
fun SettingsPart(text:String){
    Row(
        modifier= Modifier
            .border(2.dp,OrangePrimary, RoundedCornerShape(10.dp))
            .height(53.dp)
            .width(400.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun SettingsScreen(login: String? = "", email: String?="", password: String?="", onNavigatetoTasks: () -> Unit = {}, onNavigatetoShop: () -> Unit = {},authvm: AuthViewModel,onNavigatetoChange:()->Unit={}){

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val userLogin by authvm.userLogin.collectAsState()
    val userEmail by authvm.userEmail.collectAsState()
    if(isLandscape){
        Row(
            modifier = Modifier.fillMaxSize()
        ){
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter
            ) {
                BottomBar(onNavigatetoTasks = onNavigatetoTasks, onNavigatetoShop = onNavigatetoShop)
            }
            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
                    .background(SignupBackground)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ){
                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    text = stringResource(R.string.profile),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Text(text = stringResource(R.string.login), fontSize = 14.sp, color = colorResource(R.color.black))
                SettingsPart(login ?: "")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = stringResource(R.string.email), fontSize = 14.sp, color = colorResource(R.color.black))
                SettingsPart(email ?: "")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = stringResource(R.string.password), fontSize = 14.sp, color = colorResource(R.color.black))
                SettingsPart(password ?: "")
            }


        }


    }else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SignupBackground),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ){
            BottomBar(onNavigatetoTasks = onNavigatetoTasks, onNavigatetoShop = onNavigatetoShop)


        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(SignupBackground)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ){
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = stringResource(R.string.profile),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(text = stringResource(R.string.login), fontSize = 14.sp, color = colorResource(R.color.black))
            SettingsPart(userLogin ?: "")

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(R.string.email), fontSize = 14.sp, color = colorResource(R.color.black))
            SettingsPart(userEmail ?: "")

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(R.string.password), fontSize = 14.sp, color = colorResource(R.color.black))
            SettingsPart("********")
            Button(
                onClick = {onNavigatetoChange()},
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeContinue
                )

            ) {
                Text("Смена пароля")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {authvm.signOut()},
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeContinue
                )

            ) {
                Text("Выйти из аккаунта")
            }
        }

    }

}