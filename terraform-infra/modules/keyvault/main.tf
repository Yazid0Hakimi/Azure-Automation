resource "azurerm_key_vault" "main" {
  name                        = var.keyvault_name
  location                    = var.location
  resource_group_name         = var.resource_group_name
  enabled_for_disk_encryption = true
  tenant_id                   = var.tenant_id
  soft_delete_retention_days  = 7
  purge_protection_enabled    = false
  sku_name                    = "standard"
  enable_rbac_authorization   = false

  public_network_access_enabled = true # This allows public access from all networks
  network_acls {
    bypass                     = "AzureServices"
    default_action             = "Allow"
    virtual_network_subnet_ids = var.subnet_ids
    ip_rules = var.runnerIpRule
  }
}


# Access policy for the principal
resource "azurerm_key_vault_access_policy" "principal" {
  key_vault_id = azurerm_key_vault.main.id
  tenant_id    = var.tenant_id
  object_id    = var.object_id

  key_permissions = [
    "Get", "List", "Create", "Delete", "Update", "Import", "Backup", "Restore",
  ]

  secret_permissions = [
    "Get", "List", "Set", "Delete", "Backup", "Restore",
  ]

  certificate_permissions = [
    "Get", "List", "Create", "Import", "Update", "Delete", "Backup", "Restore",
  ]
}

# Outputs
output "id" {
  value = azurerm_key_vault.main.id
}

output "name" {
  value = azurerm_key_vault.main.name
}

output "uri" {
  value = azurerm_key_vault.main.vault_uri
}
