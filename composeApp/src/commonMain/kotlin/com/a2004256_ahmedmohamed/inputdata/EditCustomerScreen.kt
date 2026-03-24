package com.a2004256_ahmedmohamed.inputdata

import inputdata.composeapp.generated.resources.Res
import inputdata.composeapp.generated.resources.arrowback
import inputdata.composeapp.generated.resources.notosansarabic
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

class EditCustomerScreen(private val customerId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        EditCustomerContent(navigator, customerId)
    }
}

data class SessionModel(
    val type: String = "",
    val date: String = ""
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun EditCustomerContent(
    navController: Navigator,
    customerId: String
) {
    val scope = rememberCoroutineScope()
    val database = remember { Firebase.database.reference("users") }

    var isLoading by remember { mutableStateOf(true) }

    var uid by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var points by remember { mutableStateOf(0) }
    var invoiceAmount by remember { mutableStateOf(0) }
    var invoiceDescription by remember { mutableStateOf("") }
    var sessionsList by remember { mutableStateOf<List<SessionModel>>(emptyList()) }

    var showSessionDialog by remember { mutableStateOf(false) }
    var selectedSessionType by remember { mutableStateOf("") }

    var notificationMessage by remember { mutableStateOf<String?>(null) }

    val arabicFont = FontFamily(
        org.jetbrains.compose.resources.Font(Res.font.notosansarabic)
    )

    LaunchedEffect(customerId) {
        scope.launch {
            try {
                val snapshot = database.child(customerId).valueEvents.first()
                if (snapshot.exists) {
                    uid = snapshot.child("uid").value as? String ?: ""
                    name = snapshot.child("name").value as? String ?: ""
                    phone = snapshot.child("phone").value as? String ?: ""
                    address = snapshot.child("address").value as? String ?: ""
                    birthDate = snapshot.child("birthDate").value as? String ?: ""
                    points = snapshot.child("points").value as? Int ?: 0
                    invoiceAmount = snapshot.child("invoiceAmount").value as? Int ?: 0
                    invoiceDescription = snapshot.child("invoiceDescription").value as? String ?: ""
                }

                val sessionsSnapshot = database.child(customerId).child("sessions").valueEvents.first()
                val list = sessionsSnapshot.children.mapNotNull { child ->
                    val type = child.child("type").value as? String ?: return@mapNotNull null
                    val date = child.child("date").value as? String ?: return@mapNotNull null
                    SessionModel(type, date)
                }

                sessionsList = list

            } catch (e: Exception) {
                notificationMessage = "حدث خطأ أثناء تحميل البيانات: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
            .verticalScroll(scrollState)
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
            Text("تعديل بيانات العميل", style = MaterialTheme.typography.headlineMedium, fontFamily = arabicFont)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("الاسم", fontFamily = arabicFont) }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("رقم الهاتف", fontFamily = arabicFont) }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("العنوان", fontFamily = arabicFont) }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = birthDate, onValueChange = {}, label = { Text("تاريخ الميلاد", fontFamily = arabicFont) }, modifier = Modifier.fillMaxWidth(), readOnly = true)
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = points.toString(), onValueChange = { points = it.toIntOrNull() ?: 0 }, label = { Text("عدد النقاط", fontFamily = arabicFont) }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = invoiceAmount.toString(), onValueChange = { invoiceAmount = it.toIntOrNull() ?: 0 }, label = { Text("تكلفة الفاتورة", fontFamily = arabicFont) }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = invoiceDescription, onValueChange = { invoiceDescription = it }, label = { Text("وصف الفاتورة", fontFamily = arabicFont) }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        if (sessionsList.isNotEmpty()) {
            Text(
                text = "الجلسات",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            sessionsList.forEach { session ->
                OutlinedTextField(
                    value = "${session.type} - ${session.date}",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    label = { Text("جلسة") }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        DiscountButtonsRow(
            points = points,
            customerId = uid,
            onPointsUpdated = { updatedPoints ->
                points = updatedPoints.coerceAtLeast(0)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        var lastSavedInvoiceAmount by remember { mutableStateOf(invoiceAmount) }

        Button(onClick = {
            val finalPoints = if (invoiceAmount > 0 && invoiceAmount != lastSavedInvoiceAmount) {
                points + (invoiceAmount * 0.1).toInt()
            } else {
                points
            }
            scope.launch {
                try {
                    database.child(customerId).updateChildren(
                        mapOf(
                            "name" to name,
                            "phone" to phone,
                            "address" to address,
                            "birthDate" to birthDate,
                            "points" to finalPoints,
                            "invoiceAmount" to invoiceAmount,
                            "invoiceDescription" to invoiceDescription
                        )
                    )
                    notificationMessage = "تم حفظ التعديلات بنجاح ✅"
                    navController.pop()
                } catch (e: Exception) {
                    notificationMessage = "حدث خطأ أثناء الحفظ: ${e.message}"
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("حفظ التعديلات", fontFamily = arabicFont)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "إضافة جلسة",
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = arabicFont
        )

        Spacer(Modifier.height(10.dp))

        val sessions = listOf("إبرة بوتكس", "إبرة نضارة", "ليزر", "إكسوزوم", "هيدرافيشل")
        sessions.forEach { session ->
            Button(
                onClick = { selectedSessionType = session; showSessionDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(session)
            }
        }

        if (showSessionDialog) {
            Dialog(onDismissRequest = { showSessionDialog = false }) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "تحديد تاريخ الجلسة التي تمت لـ $selectedSessionType",
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = arabicFont
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    var sessionDateInput by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = sessionDateInput,
                        onValueChange = { sessionDateInput = it },
                        label = { Text("أدخل التاريخ (dd/MM/yyyy)", fontFamily = arabicFont) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { showSessionDialog = false }
                        ) {
                            Text("إلغاء", fontFamily = arabicFont)
                        }

                        Spacer(Modifier.width(10.dp))

                        Button(
                            onClick = {
                                if (sessionDateInput.isNotBlank()) {
                                    val sessionRef = database.child(customerId).child("sessions").push()
                                    val sessionData = mapOf(
                                        "type" to selectedSessionType,
                                        "date" to sessionDateInput,
                                    )
                                    scope.launch {
                                        try {
                                            sessionRef.setValue(sessionData)
                                            notificationMessage = "تم إضافة $selectedSessionType بنجاح ✅"
                                            showSessionDialog = false
                                        } catch (e: Exception) {
                                            notificationMessage = "حدث خطأ أثناء الحفظ"
                                        }
                                    }
                                }
                            }
                        ) {
                            Text("حفظ", fontFamily = arabicFont)
                        }
                    }
                }
            }
        }

        notificationMessage?.let { Text(it, color = Color.Green, fontFamily = arabicFont, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp)) }
    }
}

@Composable
fun DiscountButtonsRow(
    points: Int,
    customerId: String,
    onPointsUpdated: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedDiscount by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        listOf(100, 75, 50, 25).forEach { discount ->
            Button(
                onClick = {
                    selectedDiscount = discount
                    showConfirmDialog = true
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "خصم $discount%",
                    fontSize = 12.sp,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.wrapContentWidth()
                )
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("تأكيد الخصم") },
            text = { Text("هل أنت متأكد من خصم $selectedDiscount% من النقاط؟") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            val newPoints = (points - (points * selectedDiscount / 100)).coerceAtLeast(0)
                            Firebase.database.reference("users").child(customerId).child("points").setValue(newPoints)
                            onPointsUpdated(newPoints)
                            showConfirmDialog = false
                        }
                    }
                ) {
                    Text("نعم")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("لا")
                }
            }
        )
    }
}