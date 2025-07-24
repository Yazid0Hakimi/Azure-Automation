resource "azurerm_public_ip" "firewall_pip" {
  name                = "firewall-pip"
  location            = var.location
  resource_group_name = var.resource_group_name
  allocation_method   = "Static"
  sku                 = "Standard"
}

resource "azurerm_public_ip" "firewall_management_pip" {
  name                = "firewall-management-pip"
  location            = var.location
  resource_group_name = var.resource_group_name
  allocation_method   = "Static"
  sku                 = "Standard"
}

  # resource "azurerm_firewall_policy" "hub_fw_policy" {
  #   name                     = "hub-firewall-policy"
  #   resource_group_name      = var.resource_group_name
  #   location                 = var.location
  #   # threat_intelligence_mode = "Deny"
  #   sku                      = "Basic"
  # }

resource "azurerm_firewall" "main" {
  name                = "firewall-main"
  location            = var.location
  resource_group_name = var.resource_group_name
  sku_name            = "AZFW_VNet"
  sku_tier            = "Basic"
  threat_intel_mode   = "Deny"
  # firewall_policy_id  = azurerm_firewall_policy.hub_fw_policy.id

  ip_configuration {
    name                 = "configuration"
    subnet_id            = var.firewall_subnet_id
    public_ip_address_id = azurerm_public_ip.firewall_pip.id
  }

  management_ip_configuration {
    name                 = "management-configuration"
    subnet_id            = var.management_subnet_id
    public_ip_address_id = azurerm_public_ip.firewall_management_pip.id
  }
  
# depends_on = [azurerm_firewall_policy.hub_fw_policy]

}
output "firewall_name" {
  value = azurerm_firewall.main.name
}

output "firewall_id" {
  value = azurerm_firewall.main.id
}

output "firewall_pip" {
  value = azurerm_public_ip.firewall_pip.ip_address
}

output "firewall_management_pip" {
  value = azurerm_public_ip.firewall_management_pip.ip_address
}
