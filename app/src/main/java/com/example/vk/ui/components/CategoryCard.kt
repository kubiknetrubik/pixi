package com.example.vk.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vk.R
import com.example.vk.ui.theme.OrangePrimary



@Composable
fun CategoryCard(){
    Row(
        modifier= Modifier
            .border(2.dp,OrangePrimary, RoundedCornerShape(10.dp))
            .height(53.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .height(23.dp)
                .width(24.dp)
                .border(2.dp,colorResource(R.color.accent), RoundedCornerShape(5.dp))

        ){}
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Выбрано",
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "Цена",
            fontSize = 15.sp
        )
        Icon(
            painter = painterResource(id = R.drawable.dust),
            contentDescription = stringResource(R.string.dust),
            modifier = Modifier
                .height(37.dp)
                .width(25.dp)
        )




    }


    Spacer(modifier = Modifier.height(10.dp))

}
