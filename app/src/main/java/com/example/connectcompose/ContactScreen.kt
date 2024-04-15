package com.example.connectcompose


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    state : ContactState,
    onEvent: (ContactEvent) -> Unit,
    navController : NavController
){



    Scaffold(

        topBar = {


            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(10.dp)
            ){
                Button(onClick = {
                    if(state.isMenuOpen){
                        onEvent(ContactEvent.HideMenu)
                    }
                    else {
                        onEvent(ContactEvent.ShowMenu)
                    }
                },
                    modifier = Modifier
                        .padding(horizontal = 10.dp)) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu icon to access other screens" )

                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                                 navController.navigate(Screen.FinalList.route)
                },
                    modifier = Modifier
                        .padding(horizontal = 10.dp)) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Move to final list of absent students")

                }
            }

        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ContactEvent.ShowDialog)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add contact")
            }

        }
    ) { padding ->
        if(state.isAddingContact){
            DialogWithOutImage(state = state, onEvent = onEvent, modifier = Modifier)
        }


        ///new menu








        ///new menu
        /////menu




        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val dateState = rememberDatePickerState()


        if (state.isReportOpen) {
            DatePickerDialog(onDismissRequest = {  },
                confirmButton = {
                    TextButton(onClick = { onEvent(ContactEvent.HideReport) }) {
                        Text(text = "Save")
                    }
                }) {
                DatePicker(state = dateState)
            }
        }







        /////menu
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                    , verticalAlignment = Alignment.CenterVertically){
                    SortType.entries.forEach { sortType->
                        Row(
                            modifier = Modifier
                                .clickable{
                                    onEvent(ContactEvent.SortContacts(sortType))
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            RadioButton(selected = state.sortType == sortType,
                                onClick = { onEvent(ContactEvent.SortContacts(sortType)) })
                            Text(text = sortType.name)
                        }
                    }
                }

            }
            items(state.contacts){contact->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)) {
                    Column(modifier = Modifier
                        .weight(1f)) {
                        Text(contact.firstName,fontSize = 20.sp)
                        Text(contact.phoneNumber,fontSize = 12.sp)
                    }
                    IconButton(onClick = {
                        onEvent(ContactEvent.AbsentContact(contact))
                        for(i in state.absent){
                            Log.d("Absent",i.firstName)
                        }
                        onEvent(ContactEvent.DeleteContact(contact))
                    }) {
                        Icon(imageVector = Icons.Default.Delete
                            ,contentDescription = "Delete Contact")

                    }
                }
            }



        }



        if(state.isMenuOpen){

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        Modifier
                            .padding(10.dp)
                            .width(250.dp)

                    ) {
                        Spacer(modifier = Modifier
                            .weight(1f))
                        TextButton(onClick = { /*TODO*/ },modifier = Modifier
                            .fillMaxWidth()) {
                            Text(text = "Import CSV")

                        }
                        TextButton(onClick = {
                            navController.navigate(Screen.ReportFrag.route)
                        },modifier = Modifier
                            .fillMaxWidth()) {
                            Text(text = "Report")

                        }
                        TextButton(onClick = {
                            navController.navigate(Screen.MessageFrag.route)
                        },modifier = Modifier
                            .fillMaxWidth()) {
                            Text(text = "Message")

                        }
                        Spacer(modifier = Modifier
                            .weight(1f))

                    }
                }
            ) {

            }


        }


    }

}
