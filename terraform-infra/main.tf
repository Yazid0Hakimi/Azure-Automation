# save state file in Azure blob storage
terraform {
  
  required_providers {
  }
  backend "azurerm" {
  }
}

# Azure Provider
provider "azurerm" {

  features {
    key_vault {
      purge_soft_delete_on_destroy    = true
      recover_soft_deleted_key_vaults = true
    }
  }
  #resource_provider_registrations = "none"
  skip_provider_registration = true
  # tenant_id                  = data.azurerm_client_config.current.tenant_id
  # client_id                  = data.azurerm_client_config.current.client_id
  # client_secret              = var.client_secret
  subscription_id = "0cfe2870-d256-4119-b0a3-16293ac11bdc"
  
}

# VNet (Hub and Spoke)
module "vnet_hub" {
  source         = "./modules/network/Vnet"
  location       = local.location
  resource_group = local.resource_group_name
  address_space  = "10.0.0.0/16"
  vnet_type      = "hub"
  vnet_name      = "vnet-hub"
}

module "vnet_spoke" {
  source         = "./modules/network/Vnet"
  location       = local.location
  resource_group = local.resource_group_name
  address_space  = "10.1.0.0/16"
  vnet_type      = "spoke"
  vnet_name      = "vnet-spoke"
}

## VNet Peering between Hub and Spoke VNets
resource "azurerm_virtual_network_peering" "vnet_peering_hub_to_spoke" {
  name                         = var.vnet_peering_name
  resource_group_name          = local.resource_group_name
  virtual_network_name         = module.vnet_hub.vnet_name
  remote_virtual_network_id    = module.vnet_spoke.vnet_id
  allow_virtual_network_access = true
  allow_forwarded_traffic      = true
  depends_on = [
    module.subnet1,
    module.subnet2,
    module.subnet3,
    module.subnet4,
    module.subnet5,
    module.subnet7,
    module.vnet_hub,
    module.vnet_spoke,
  ]
}

resource "azurerm_virtual_network_peering" "vnet_peering_spoke_to_hub" {
  name                         = "${var.vnet_peering_name}-reverse"
  resource_group_name          = local.resource_group_name
  virtual_network_name         = module.vnet_spoke.vnet_name
  remote_virtual_network_id    = module.vnet_hub.vnet_id
  allow_virtual_network_access = true
  allow_forwarded_traffic      = true
  depends_on = [
    module.subnet1,
    module.subnet2,
    module.subnet3,
    module.subnet4,
    module.subnet5,
    module.subnet7,
    module.vnet_hub,
    module.vnet_spoke,
  ]
}

# Subnets in VNet-hub
module "subnet1" {
  source              = "./modules/network/subnet"
  location            = local.location
  resource_group_name = local.resource_group_name
  vnet_id             = module.vnet_hub.vnet_id
  vnet_name           = module.vnet_hub.vnet_name
  subnet_name         = "AzureBastionSubnet"
  address_prefix      = "10.0.0.0/26"
}

module "subnet3" {
  source              = "./modules/network/subnet"
  location            = local.location
  resource_group_name = local.resource_group_name
  vnet_id             = module.vnet_hub.vnet_id
  vnet_name           = module.vnet_hub.vnet_name
  subnet_name         = "AzureFirewallSubnet"
  address_prefix      = "10.0.1.0/26"
}

module "subnet5" {
  source              = "./modules/network/subnet"
  location            = local.location
  resource_group_name = local.resource_group_name
  vnet_id             = module.vnet_hub.vnet_id
  vnet_name           = module.vnet_hub.vnet_name
  subnet_name         = "AzureFirewallManagementSubnet"
  address_prefix      = "10.0.2.0/26"
}

module "subnet7" {
  source              = "./modules/network/subnet"
  location            = local.location
  resource_group_name = local.resource_group_name
  vnet_id             = module.vnet_hub.vnet_id
  vnet_name           = module.vnet_hub.vnet_name
  subnet_name         = "AdminSubnet"
  address_prefix      = "10.0.3.0/26"
}

# Subnets in VNet-spoke
module "subnet2" {
  source              = "./modules/network/subnet"
  location            = local.location
  resource_group_name = local.resource_group_name
  vnet_id             = module.vnet_spoke.vnet_id
  vnet_name           = module.vnet_spoke.vnet_name
  subnet_name         = "subnet2"
  address_prefix      = "10.1.0.0/26"
}

module "subnet4" {
  source              = "./modules/network/subnet"
  location            = local.location
  resource_group_name = local.resource_group_name
  vnet_id             = module.vnet_spoke.vnet_id
  vnet_name           = module.vnet_spoke.vnet_name
  subnet_name         = "subnet4"
  address_prefix      = "10.1.1.0/25"
}

module "subnet6" {
  source              = "./modules/network/subnet"
  location            = local.location
  resource_group_name = local.resource_group_name
  vnet_id             = module.vnet_spoke.vnet_id
  vnet_name           = module.vnet_spoke.vnet_name
  subnet_name         = "pepSubnet"
  address_prefix      = "10.1.2.0/25"
}
## =================================
## AzureBastionSubnet Configuration
## =================================

module "bastion" {
  source              = "./modules/bastion"
  location            = local.location
  resource_group_name = local.resource_group_name
  subnet_id           = module.subnet1.subnet_id
}

## =================================
## VM Configuration
## =================================

# SSH key generation
resource "tls_private_key" "ssh_key" {
  algorithm = "RSA"
  rsa_bits  = 4096
}



# Add Key Vault module# 1. Update the main.tf where you call the keyvault module:
module "keyvault" {
  source              = "./modules/keyvault"
  location            = local.location
  resource_group_name = local.resource_group_name
  # Correctly extract substring, removing the initial '1-' 
  keyvault_name = "kv${substr(replace(local.resource_group_name, "1-", ""), 0, 8)}"
  tenant_id     = local.tenant_id
  object_id     = local.object_id
  runnerIpRule  = var.runnerIp
  subnet_ids = [
    module.subnet1.subnet_id, # bastion.subnet_id,
    module.subnet7.subnet_id, # admin VM subnet
    module.subnet2.subnet_id, # app gateway subnet
    module.subnet3.subnet_id, # firewall subnet
  ]
}

resource "azurerm_key_vault_secret" "ssh_public_key" {
  name            = "ssh-public-key"
  value           = tls_private_key.ssh_key.public_key_openssh
  key_vault_id    = module.keyvault.id
  expiration_date = timeadd(timestamp(), "8760h") # 365 days * 24 hours
  content_type    = "text/plain"

  depends_on = [module.keyvault, tls_private_key.ssh_key]
}

resource "azurerm_key_vault_secret" "ssh_private_key" {
  name            = "ssh-private-key"
  value           = tls_private_key.ssh_key.private_key_pem
  key_vault_id    = module.keyvault.id
  expiration_date = timeadd(timestamp(), "8760h") # 365 days * 24 hours
  content_type    = "application/x-pem-file"
  depends_on      = [module.keyvault, tls_private_key.ssh_key]
}

# Update the VM module call to use SSH key
module "vm" {
  source              = "./modules/vm"
  location            = local.location
  resource_group_name = local.resource_group_name
  vm_name             = "admin-vm"
  subnet_id           = module.subnet7.subnet_id
  admin_username      = "adminuser"
  ssh_public_key      = tls_private_key.ssh_key.public_key_openssh
  vnet_id             = module.vnet_hub.vnet_id
}
## =================================
## Firewall Configuration
## =================================

module "firewall" {
  source               = "./modules/firewall"
  location             = local.location
  resource_group_name  = local.resource_group_name
  firewall_subnet_id   = module.subnet3.subnet_id
  management_subnet_id = module.subnet5.subnet_id
}

#add rules to get to Azure devops and SQL Server 
resource "azurerm_firewall_network_rule_collection" "rule_collection_1" {
  name                = "rule-collection-1"
  azure_firewall_name = module.firewall.firewall_name
  resource_group_name = local.resource_group_name
  priority            = 100
  action              = "Allow"


  rule {
    name                  = "devops-rule"
    source_addresses      = ["10.1.0.0/16"]
    destination_addresses = ["AzureDevOps"]

    destination_ports = ["443"] # HTTPS port for Azure DevOps
    protocols         = ["TCP"]
  }

  rule {
    name                  = "sql-db-rule"
    source_addresses      = ["10.1.0.0/16"] # or restrict to certain IPs
    destination_addresses = ["Sql"]         # Service tag for Azure SQL Database
    destination_ports     = ["1433"]        # Default SQL port
    protocols             = ["TCP"]
  }

}
# Add firewall rule for ACR traffic
resource "azurerm_firewall_network_rule_collection" "acr_rule_collection" {
  name                = "acr-rule-collection"
  azure_firewall_name = module.firewall.firewall_name
  resource_group_name = local.resource_group_name
  priority            = 200
  action              = "Allow"

  rule {
    name                  = "acr-rule"
    source_addresses      = ["10.1.0.0/16"]            # AKS subnet address range
    destination_addresses = ["AzureContainerRegistry"] # Service tag for ACR
    destination_ports     = ["443"]
    protocols             = ["TCP"]
  }
}

# Add network security group for microservices ports
resource "azurerm_network_security_group" "aks_nsg" {
  name                = "aks-nsg"
  location            = local.location
  resource_group_name = local.resource_group_name

  security_rule {
    name                       = "allow-microservices"
    priority                   = 100
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_ranges    = ["8081", "8082", "8762", "8888"]
    source_address_prefix      = "10.0.0.0/16" # Restrict to hub VNet instead of "*"
    destination_address_prefix = "*"
  }
  security_rule {
    name                       = "allow-prometheus-grafana"
    priority                   = 200
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_ranges    = ["9090", "3000", "80"]
    source_address_prefix      = "*" 
    destination_address_prefix = "*"
  }
}

# Associate NSG with AKS subnet
resource "azurerm_subnet_network_security_group_association" "aks_subnet_nsg" {
  subnet_id                 = module.subnet4.subnet_id
  network_security_group_id = azurerm_network_security_group.aks_nsg.id
}


## =================================
## Gateway - Waf
## =================================

resource "azurerm_public_ip" "AppGateway_pip" {
  name                = "app-gateway-pip"
  location            = local.location
  resource_group_name = local.resource_group_name
  allocation_method   = "Static"
}



## =================================
## Application away
## =================================

module "gateway" {
  source              = "./modules/gateway"
  location            = local.location
  resource_group_name = local.resource_group_name
  subnet_id           = module.subnet2.subnet_id
  vm_name             = "admin-vm"
  admin_username      = "adminuser"
  tenant_id           = local.tenant_id
  keyvault_name       = module.keyvault.name
  keyvault_id         = module.keyvault.id
  public_ip_id        = azurerm_public_ip.AppGateway_pip.id
  vnet_id             = module.vnet_spoke.vnet_id
  depends_on          = [module.keyvault]
}

## =================================
## AKS Cluster
## =================================
module "aks" {
  source              = "./modules/aks"
  aks_cluster_name    = var.aks_cluster_name
  location            = local.location
  resource_group_name = local.resource_group_name
  dns_prefix          = var.dns_prefix
  node_count          = var.node_count
  subnet_id           = module.subnet4.subnet_id
  client_id           = local.client_id
  client_secret       = var.client_secret
  gateway_id          = module.gateway.gateway_id
  depends_on          = [module.gateway]
  private_dns_zone_id = module.vm.private_dns_zone_id
}

module "acr" {
  source                     = "./modules/acr"
  location                   = local.location
  resource_group_name        = local.resource_group_name
  acr_sku                    = var.acr_sku
  acr_admin_enabled          = true # Enable admin credentials for sandbox environments
  acr_anonymous_pull_enabled = true # Enable anonymous pull if needed for sandbox
}

## =================================
## Azure SQL Server 
## =================================
module "sql" {
  source              = "./modules/sql"
  location            = local.location
  resource_group_name = local.resource_group_name
  sql_admin_username  = "adminyazid"
  sql_admin_password  = "P@ssw0rd1234"
  subnet_id           = module.subnet6.subnet_id # Ensure the subnet is correct
  vnet_id             = module.vnet_spoke.vnet_id
}

# 72ade54541af4a0c54e44f77362efb7f78d5b38f last commit for keyvault 
