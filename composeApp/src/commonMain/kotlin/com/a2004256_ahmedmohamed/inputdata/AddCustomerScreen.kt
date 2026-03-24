package com.a2004256_ahmedmohamed.inputdata

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.gitlive.firebase.database.*
import dev.gitlive.firebase.Firebase
import inputdata.composeapp.generated.resources.Res
import inputdata.composeapp.generated.resources.arrowback
import inputdata.composeapp.generated.resources.notosansarabic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

class AddCustomerScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        AddCustomerContent(navigator)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomerContent(navController: Navigator) {

    val scope = rememberCoroutineScope()
    val database = remember { Firebase.database.reference("users") }

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    var notificationMessage by remember { mutableStateOf<String?>(null) }

    val arabicFont = FontFamily(
        org.jetbrains.compose.resources.Font(Res.font.notosansarabic)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {

        Row {
            Image(
                painter = painterResource(Res.drawable.arrowback),
                contentDescription = null,
                modifier = Modifier
                    .background(color = Color.Black)
                    .clickable {
                    navController.pop()
                }
            )
            Spacer(Modifier.width(10.dp))
            Text(text = "تسجيل عميل جديد", style = MaterialTheme.typography.headlineMedium, fontFamily = arabicFont)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TextFields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("الاسم", fontFamily = arabicFont) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("رقم الهاتف", fontFamily = arabicFont) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("العنوان", fontFamily = arabicFont) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("تاريخ الميلاد (dd/MM/yyyy)", fontFamily = arabicFont) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                val uid = "user_${Random.nextInt(100000, 999999)}"

                val userData = mapOf(
                    "name" to name,
                    "uid" to uid,
                    "phone" to phone,
                    "address" to address,
                    "birthDate" to birthDate,
                    "points" to 0,
                    "invoiceAmount" to 0,
                    "invoiceDescription" to ""
                )

                scope.launch {
                    try {
                        database.child(uid).setValue(userData)
                        notificationMessage = "تم التسجيل بنجاح ✅"
                        navController.pop()
                    } catch (e: Exception) {
                        notificationMessage = "خطأ: ${e.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("تسجيل", fontFamily = arabicFont)
        }

        Spacer(modifier = Modifier.height(16.dp))

        notificationMessage?.let { msg ->
            Text(
                text = msg,
                color = if (msg.contains("نجاح")) Color.Green else Color.Red,
                fontFamily = arabicFont
            )
        }
    }
}