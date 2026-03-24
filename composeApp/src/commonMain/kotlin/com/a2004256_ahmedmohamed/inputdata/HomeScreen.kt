package com.a2004256_ahmedmohamed.inputdata

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.a2004256_ahmedmohamed.inputdata.notification.FirebaseTriggerKMP
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.DataSnapshot
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database
import inputdata.composeapp.generated.resources.Res
import inputdata.composeapp.generated.resources.delete
import inputdata.composeapp.generated.resources.edit
import inputdata.composeapp.generated.resources.notosansarabic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        HomeContent(navigator)
    }
}

data class Customer2(
    val name: String,
    val phone: String
)

@Composable
fun HomeContent(navigator: Navigator) {
    val database = Firebase.database.reference("users")
    val scope = rememberCoroutineScope()

    val customersList = remember { mutableStateListOf<Pair<String, Customer2>>() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedCustomerId by remember { mutableStateOf<String?>(null) }

    var searchQuery by remember { mutableStateOf("") } // متغير البحث
    val arabicFont = FontFamily(org.jetbrains.compose.resources.Font(Res.font.notosansarabic))
    val firebaseTrigger = remember { FirebaseTriggerKMP(scope) }

    LaunchedEffect(Unit) {
        launch { firebaseTrigger.startListening() }

        database.valueEvents.collect { snapshot ->
            customersList.clear()
            snapshot.children.forEach { child ->
                val id = child.key ?: return@forEach
                val name = child.child("name").value?.toString() ?: ""
                val phone = child.child("phone").value?.toString() ?: ""
                if (name.isNotBlank()) {
                    customersList.add(id to Customer2(name, phone))
                }
            }
        }
    }

    // تصفية القائمة حسب البحث
    val filteredCustomers = remember(searchQuery, customersList) {
        if (searchQuery.isBlank()) {
            customersList
        } else {
            customersList.filter { (_, customer) ->
                "${customer.name} ${customer.phone}"
                    .contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            // ======= Search Bar =======
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("بحث بالاسم أو رقم الهاتف", fontFamily = arabicFont) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Button(
                onClick = { navigator.push(AddCustomerScreen()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text("إضافة عميل", fontSize = 20.sp, fontFamily = arabicFont)
            }

            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            FirebaseNotifications(firebaseTrigger)

            Text(
                text = "إدارة العملاء",
                color = Color.Black,
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = arabicFont
            )

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            LazyColumn {
                itemsIndexed(filteredCustomers) { _, (key, customer) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Name:  ${customer.name}", color = Color.Black, fontFamily = arabicFont)
                                Text("Phone:  ${customer.phone}", color = Color.Black, fontFamily = arabicFont)
                            }
                            Row {
                                IconButton(onClick = { navigator.push(EditCustomerScreen(key)) }) {
                                    Icon(
                                        painter = painterResource(Res.drawable.edit),
                                        contentDescription = "تعديل"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        selectedCustomerId = key
                                        showDeleteDialog = true
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.delete),
                                        contentDescription = "حذف"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("تأكيد الحذف", fontFamily = arabicFont) },
                text = { Text("هل تريد حذف هذا العميل؟", fontFamily = arabicFont) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedCustomerId?.let { id ->
                                scope.launch {
                                    try {
                                        database.child(id).removeValue()
                                        println("✅ تم الحذف: $id")
                                    } catch (e: Exception) {
                                        println("❌ خطأ في الحذف: ${e.message}")
                                    }
                                }
                            }
                            showDeleteDialog = false
                        }
                    ) { Text("حذف", fontFamily = arabicFont) }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("إلغاء", fontFamily = arabicFont)
                    }
                }
            )
        }
    }
}

@Composable
fun FirebaseNotifications(firebaseTrigger: FirebaseTriggerKMP) {
    val notifications = firebaseTrigger.notifications

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        notifications.forEach { (title, message) ->
            Snackbar(
                modifier = Modifier.padding(4.dp)
            ) {
                Text("$title: $message")
            }
        }
    }
}