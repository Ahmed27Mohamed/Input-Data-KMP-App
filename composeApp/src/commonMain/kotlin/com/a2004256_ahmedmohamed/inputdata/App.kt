package com.a2004256_ahmedmohamed.inputdata

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.a2004256_ahmedmohamed.inputdata.ui.theme.InputDataTheme

@Composable
fun App() {

    InputDataTheme {
        Navigator(HomeScreen())
    }

//    InputDataTheme {
//        Navigator(HomeScreen()) { navigator ->
//
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color(0xFFF5F5F5))
//            ) {
//
//                // شاشة الإشعارات (Popup-like)
//                Column(
//                    modifier = Modifier
//                        .align(androidx.compose.ui.Alignment.TopEnd)
//                        .padding(16.dp)
//                        .width(320.dp)
//                ) {
//                    LazyColumn {
//                        items(notifications) { (title, message) ->
//                            Card(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 4.dp),
//                                elevation = CardDefaults.cardElevation(6.dp)
//                            ) {
//                                Column(modifier = Modifier.padding(12.dp)) {
//                                    Text(title, style = MaterialTheme.typography.titleMedium)
//                                    Spacer(Modifier.height(4.dp))
//                                    Text(message, style = MaterialTheme.typography.bodyMedium)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
}