package com.example.gharsa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gharsa.viewmodel.AuthViewModel
import com.example.gharsa.viewmodel.Tenant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantManagementScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadTenants()
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") } // ALL, ACTIVE, INACTIVE, PENDING

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTenantForDetails by remember { mutableStateOf<Tenant?>(null) }
    var selectedTenantForEdit by remember { mutableStateOf<Tenant?>(null) }

    val filteredTenants = viewModel.tenants.filter { tenant ->
        val matchesSearch = tenant.name.contains(searchQuery, ignoreCase = true) ||
                tenant.mobile.contains(searchQuery) ||
                tenant.roomNumber.contains(searchQuery)
        val matchesFilter = when (selectedFilter) {
            "ACTIVE" -> tenant.status.equals("ACTIVE", ignoreCase = true)
            "INACTIVE" -> tenant.status.equals("INACTIVE", ignoreCase = true)
            "PENDING" -> tenant.status.equals("PENDING", ignoreCase = true)
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tenant Directory", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Tenant")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Search by name, phone, or room...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChipOption(
                    label = "All (${viewModel.tenants.size})",
                    selected = selectedFilter == "ALL",
                    onClick = { selectedFilter = "ALL" }
                )
                FilterChipOption(
                    label = "Active (${viewModel.tenants.count { it.status == "ACTIVE" }})",
                    selected = selectedFilter == "ACTIVE",
                    onClick = { selectedFilter = "ACTIVE" }
                )
                FilterChipOption(
                    label = "Pending (${viewModel.tenants.count { it.status == "PENDING" }})",
                    selected = selectedFilter == "PENDING",
                    onClick = { selectedFilter = "PENDING" }
                )
                FilterChipOption(
                    label = "Inactive (${viewModel.tenants.count { it.status == "INACTIVE" }})",
                    selected = selectedFilter == "INACTIVE",
                    onClick = { selectedFilter = "INACTIVE" }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredTenants.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.PeopleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No tenants found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredTenants) { tenant ->
                        TenantCard(
                            tenant = tenant,
                            onClick = { selectedTenantForDetails = tenant },
                            onEdit = { selectedTenantForEdit = tenant }
                        )
                    }
                }
            }
        }
    }

    // Add Tenant Dialog
    if (showAddDialog) {
        AddTenantDialog(
            viewModel = viewModel,
            onDismiss = { showAddDialog = false }
        )
    }

    // Edit Tenant Dialog
    if (selectedTenantForEdit != null) {
        EditTenantDialog(
            tenant = selectedTenantForEdit!!,
            viewModel = viewModel,
            onDismiss = { selectedTenantForEdit = null }
        )
    }

    // Tenant Details Dialog
    if (selectedTenantForDetails != null) {
        TenantDetailsDialog(
            tenant = selectedTenantForDetails!!,
            viewModel = viewModel,
            onEdit = {
                selectedTenantForEdit = selectedTenantForDetails
                selectedTenantForDetails = null
            },
            onDismiss = { selectedTenantForDetails = null }
        )
    }
}

@Composable
fun FilterChipOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun TenantCard(
    tenant: Tenant,
    onClick: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tenant.name.take(1).uppercase(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = tenant.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = tenant.mobile, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                StatusBadge(status = tenant.status)
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        Text(text = "Room & Bed", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = "Room ${tenant.roomNumber} (${tenant.bedNumber})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Column {
                        Text(text = "Monthly Rent", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = "₹${tenant.monthlyRent}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RentStatusBadge(rentStatus = tenant.rentStatus)
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bgColor, textColor, text) = when (status.uppercase()) {
        "ACTIVE" -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "ACTIVE")
        "PENDING" -> Triple(Color(0xFFFFF3E0), Color(0xFFE65100), "PENDING")
        else -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "INACTIVE")
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun RentStatusBadge(rentStatus: String) {
    val isPaid = rentStatus.equals("PAID", ignoreCase = true)
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isPaid) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isPaid) Icons.Default.CheckCircle else Icons.Default.Pending,
                contentDescription = null,
                tint = if (isPaid) Color(0xFF2E7D32) else Color(0xFFE65100),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isPaid) "PAID" else "PENDING",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPaid) Color(0xFF2E7D32) else Color(0xFFE65100)
            )
        }
    }
}

// Add Tenant Dialog Form
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTenantDialog(
    viewModel: AuthViewModel,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var emergencyContact by remember { mutableStateOf("") }
    var joiningDate by remember { mutableStateOf("2024-03-01") }
    var roomNumber by remember { mutableStateOf("101") }
    var bedNumber by remember { mutableStateOf("Bed A") }
    var monthlyRent by remember { mutableStateOf("6000") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Tenant", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number (10 digits) *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Permanent Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = emergencyContact,
                    onValueChange = { emergencyContact = it },
                    label = { Text("Emergency Contact Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = joiningDate,
                    onValueChange = { joiningDate = it },
                    label = { Text("Joining Date (YYYY-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = roomNumber,
                        onValueChange = { roomNumber = it },
                        label = { Text("Room #") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = bedNumber,
                        onValueChange = { bedNumber = it },
                        label = { Text("Bed #") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = monthlyRent,
                    onValueChange = { monthlyRent = it },
                    label = { Text("Monthly Rent Amount (₹)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMsg != null) {
                    Text(text = errorMsg!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank() || phone.isBlank()) {
                        errorMsg = "Name and Phone Number are required."
                    } else {
                        viewModel.addTenant(
                            name = name,
                            phone = phone,
                            email = email,
                            address = address,
                            emergencyContact = emergencyContact,
                            joiningDate = joiningDate,
                            roomNumber = roomNumber,
                            bedNumber = bedNumber,
                            monthlyRent = monthlyRent,
                            onSuccess = onDismiss,
                            onError = { errorMsg = it }
                        )
                    }
                }
            ) {
                Text("Save Tenant")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Edit Tenant Dialog Form
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTenantDialog(
    tenant: Tenant,
    viewModel: AuthViewModel,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(tenant.name) }
    var phone by remember { mutableStateOf(tenant.mobile) }
    var address by remember { mutableStateOf(tenant.address) }
    var emergencyContact by remember { mutableStateOf(tenant.emergencyContact) }
    var joiningDate by remember { mutableStateOf(tenant.joiningDate) }
    var roomNumber by remember { mutableStateOf(tenant.roomNumber) }
    var bedNumber by remember { mutableStateOf(tenant.bedNumber) }
    var monthlyRent by remember { mutableStateOf(tenant.monthlyRent) }
    var status by remember { mutableStateOf(tenant.status) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Tenant Info", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = emergencyContact,
                    onValueChange = { emergencyContact = it },
                    label = { Text("Emergency Contact") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = joiningDate,
                    onValueChange = { joiningDate = it },
                    label = { Text("Joining Date") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = roomNumber,
                        onValueChange = { roomNumber = it },
                        label = { Text("Room #") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = bedNumber,
                        onValueChange = { bedNumber = it },
                        label = { Text("Bed #") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = monthlyRent,
                    onValueChange = { monthlyRent = it },
                    label = { Text("Monthly Rent (₹)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Status: ", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = status == "ACTIVE",
                        onClick = { status = "ACTIVE" }
                    )
                    Text("Active", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = status == "INACTIVE",
                        onClick = { status = "INACTIVE" }
                    )
                    Text("Inactive", fontSize = 12.sp)
                }

                if (errorMsg != null) {
                    Text(text = errorMsg!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.editTenant(
                        email = tenant.email,
                        name = name,
                        phone = phone,
                        address = address,
                        emergencyContact = emergencyContact,
                        joiningDate = joiningDate,
                        roomNumber = roomNumber,
                        bedNumber = bedNumber,
                        monthlyRent = monthlyRent,
                        status = status,
                        onSuccess = onDismiss,
                        onError = { errorMsg = it }
                    )
                }
            ) {
                Text("Update Tenant")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Tenant Details Modal Dialog
@Composable
fun TenantDetailsDialog(
    tenant: Tenant,
    viewModel: AuthViewModel,
    onEdit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = tenant.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                StatusBadge(status = tenant.status)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DetailItem(icon = Icons.Default.Phone, label = "Phone", value = tenant.mobile)
                DetailItem(icon = Icons.Default.Email, label = "Email", value = tenant.email)
                DetailItem(icon = Icons.Default.Home, label = "Address", value = tenant.address.ifBlank { "N/A" })
                DetailItem(icon = Icons.Default.ContactPhone, label = "Emergency Contact", value = tenant.emergencyContact.ifBlank { "N/A" })
                DetailItem(icon = Icons.Default.CalendarToday, label = "Joining Date", value = tenant.joiningDate.ifBlank { "2024-01-01" })
                DetailItem(icon = Icons.Default.Bed, label = "Room & Bed", value = "Room ${tenant.roomNumber} (${tenant.bedNumber})")
                DetailItem(icon = Icons.Default.AttachMoney, label = "Monthly Rent", value = "₹${tenant.monthlyRent}")

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Payment Status: ", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(8.dp))
                    RentStatusBadge(rentStatus = tenant.rentStatus)
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit")
                }
                Button(
                    onClick = {
                        viewModel.toggleTenantStatus(tenant.email, tenant.status)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tenant.status == "ACTIVE") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (tenant.status == "ACTIVE") "Deactivate" else "Activate")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun DetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
