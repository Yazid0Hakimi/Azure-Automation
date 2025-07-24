# Create the Private DNS Zone for Azure SQL
resource "azurerm_private_dns_zone" "sql" {
  name                = "privatelink.database.windows.net"
  resource_group_name = var.resource_group_name
}

# Link the DNS Zone to your VNet
resource "azurerm_private_dns_zone_virtual_network_link" "sql_link" {
  name                  = "sql-dns-link"
  resource_group_name   = var.resource_group_name
  private_dns_zone_name = azurerm_private_dns_zone.sql.name
  virtual_network_id    = var.vnet_id
  registration_enabled  = false
}

resource "azurerm_private_endpoint" "sql_endpoint" {
  name                = "sql-endpoint"
  location            = var.location
  resource_group_name = var.resource_group_name
  subnet_id           = var.subnet_id

  private_service_connection {
    name                           = "private-connection"
    is_manual_connection           = false
    private_connection_resource_id = azurerm_mssql_server.main.id
    subresource_names              = ["sqlserver"]
  }

  private_dns_zone_group {
    name                 = "default"
    private_dns_zone_ids = [azurerm_private_dns_zone.sql.id]
  }

}

resource "random_string" "unique_name" {
  length  = 6
  special = false
  upper   = false
  number  = true
}


resource "azurerm_mssql_server" "main" {
  name                          = "sqlserver${random_string.unique_name.result}"
  resource_group_name           = var.resource_group_name
  location                      = var.location
  version                       = "12.0"
  administrator_login           = var.sql_admin_username
  administrator_login_password  = var.sql_admin_password
  public_network_access_enabled = false
  minimum_tls_version           = "1.2"

}


resource "azurerm_mssql_database" "main" {
  name           = "sqldb"
  server_id      = azurerm_mssql_server.main.id
  collation      = "SQL_Latin1_General_CP1_CI_AS"
  license_type   = "LicenseIncluded"
  max_size_gb    = 2
  sku_name       = "S0"
  enclave_type   = "VBS"
  ledger_enabled = true
  zone_redundant = false
  tags = {
    env = "dev"
  }
  lifecycle {
    prevent_destroy = true
  }
}

resource "azurerm_storage_account" "main" {
  name                     = "sqlstorageaccount0tf1" # need to be unique
  resource_group_name      = var.resource_group_name
  location                 = var.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
  min_tls_version          = "TLS1_2"
}

resource "azurerm_mssql_database_extended_auditing_policy" "main" {
  database_id                             = azurerm_mssql_database.main.id
  storage_endpoint                        = azurerm_storage_account.main.primary_blob_endpoint
  storage_account_access_key              = azurerm_storage_account.main.primary_access_key
  storage_account_access_key_is_secondary = false

}

output "sql_server_id" {
  value = azurerm_mssql_server.main.id
}

output "sql_server_fqdn" {
  value = azurerm_mssql_server.main.fully_qualified_domain_name
}

output "sql_database_id" {
  value = azurerm_mssql_database.main.id
}
