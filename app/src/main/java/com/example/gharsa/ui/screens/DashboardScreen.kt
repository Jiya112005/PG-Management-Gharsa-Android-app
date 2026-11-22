package com.example.gharsa.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gharsa.viewmodel.AuthViewModel
import com.example.gharsa.viewmodel.UserRole
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: AuthViewModel, onNavigateToProfile: () -> Unit) {
    val scrollState = rememberScrollState()
    val isResident = viewModel.userRole == UserRole.RESIDENT

    var showTicketDialog by remember { mutableStateOf(false) }
    var ticketTitle by remember { mutableStateOf("") }
    var ticketDesc by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (isResident) Icons.Default.Bed else Icons.Default.Domain,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isResident) "GharSa Resident" else "GharSa Owner Portal",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (isResident) "GreenLeaf PG • Room ${viewModel.currentRoomNumber}" else "GreenLeaf PG Hub • 34 Beds",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { viewModel.logout() }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = MaterialTheme.colorScheme.error)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Welcome Banner Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hello, ${viewModel.currentUserName} 👋",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isResident) "Your room stay is active in Room ${viewModel.currentRoomNumber}." else "All 32 active residents checked-in. 2 beds vacant.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (isResident) Icons.Default.Verified else Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                if (isResident) {
                    // Resident Dash Content
                    // Rent Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.Payment, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Text(text = "Monthly Rent Status", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                }
                                val rent = viewModel.rents.firstOrNull()
                                val isPaid = rent?.status == "PAID"
                                Surface(shape = CircleShape, color = if (isPaid) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer) {
                                    Text(
                                        text = rent?.status ?: "DUE",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPaid) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "₹8,500 / month (Includes Wi-Fi, Water & Electricity)", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(12.dp))
                            val rent = viewModel.rents.firstOrNull()
                            if (rent != null && rent.status != "PAID") {
                                Button(
                                    onClick = { viewModel.payRent(rent.id) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "Pay Rent ₹8,500 Now", color = MaterialTheme.colorScheme.onSecondary)
                                }
                            } else {
                                Button(
                                    onClick = { viewModel.showToast("Fee receipt downloaded successfully.") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                                ) {
                                    Icon(Icons.Default.Download, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "Download Fee Receipt", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }

                    // Maintenance Tickets Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.Build, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Text(text = "Maintenance Tickets", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                }
                                TextButton(onClick = { showTicketDialog = true }) {
                                    Text(text = "+ Raise Ticket", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            if (viewModel.tickets.isEmpty()) {
                                Text(text = "No maintenance tickets raised yet.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 8.dp))
                            } else {
                                viewModel.tickets.forEachIndexed { index, ticket ->
                                    ListItem(
                                        headlineContent = { Text(ticket.title) },
                                        supportingContent = { Text("${ticket.description} • ${ticket.date}") },
                                        trailingContent = {
                                            Surface(
                                                shape = CircleShape,
                                                color = if (ticket.status == "RESOLVED") MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.tertiaryContainer
                                            ) {
                                                Text(
                                                    ticket.status,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (ticket.status == "RESOLVED") MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onTertiaryContainer
                                                )
                                            }
                                        }
                                    )
                                    if (index < viewModel.tickets.size - 1) {
                                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHigh)
                                    }
                                }
                            }
                        }
                    }

                    // Mess Menu Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Restaurant, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Text(text = "Today's Mess Menu", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "🥞 Breakfast (7:30 AM - 10:00 AM): Idli Sambar, Filter Coffee, Boiled Eggs", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "🍛 Lunch (12:30 PM - 2:30 PM): Paneer Butter Masala, Chapati, Dal Fry, Rice", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "🍲 Dinner (8:00 PM - 10:00 PM): Chicken Curry / Veg Kadai, Roti, Kheer", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                } else {
                    // Manager Dash Content
                    // Metrics Overview
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLowest,
                            shadowElevation = 1.dp
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Occupancy Rate", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "94%", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text(text = "32 / 34 Beds Filled", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Medium)
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLowest,
                            shadowElevation = 1.dp
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Monthly Revenue", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "₹2.72L", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text(text = "All Dues Cleared", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    // Quick Management Actions
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Property Operations", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.showToast("Opening Add New Tenant Wizard...") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Add Tenant", fontSize = 12.sp)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.showToast("Recording bulk rent collection...") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Collect Dues", fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // Tenant List Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Recent Tenant Check-ins", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            ListItem(
                                headlineContent = { Text("${viewModel.currentUserName} (Room ${viewModel.currentRoomNumber})") },
                                supportingContent = { Text("Lease active • Verified Resident") },
                                trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) }
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHigh)
                            ListItem(
                                headlineContent = { Text("Rahul Sharma (Room 201)") },
                                supportingContent = { Text("Lease active • Paid ₹8,000") },
                                trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) }
                            )
                        }
                    }
                }
            }

            // Raise Ticket Dialog
            if (showTicketDialog) {
                AlertDialog(
                    onDismissRequest = { showTicketDialog = false },
                    title = { Text("Raise Maintenance Ticket") },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = ticketTitle,
                                onValueChange = { ticketTitle = it },
                                label = { Text("Issue Title (e.g. Water Leakage)") },
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = ticketDesc,
                                onValueChange = { ticketDesc = it },
                                label = { Text("Description") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (ticketTitle.isNotBlank() && ticketDesc.isNotBlank()) {
                                    viewModel.raiseTicket(ticketTitle, ticketDesc)
                                    ticketTitle = ""
                                    ticketDesc = ""
                                    showTicketDialog = false
                                }
                            }
                        ) {
                            Text("Submit Ticket")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTicketDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Toast Snackbar Overlay
            AnimatedVisibility(
                visible = viewModel.toastMessage != null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                viewModel.toastMessage?.let { msg ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.inverseSurface,
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.size(18.dp))
                                Text(text = msg, color = MaterialTheme.colorScheme.inverseOnSurface, fontSize = 13.sp)
                            }
                            TextButton(onClick = { viewModel.clearToast() }) {
                                Text(text = "DISMISS", color = MaterialTheme.colorScheme.secondaryContainer, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    LaunchedEffect(msg) {
                        delay(3500)
                        viewModel.clearToast()
                    }
                }
            }
        }
    }
}
