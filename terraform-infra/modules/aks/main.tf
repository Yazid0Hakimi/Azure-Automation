resource "azurerm_log_analytics_workspace" "main" {
  name                = "log-analytics-workspace"
  location            = var.location
  resource_group_name = var.resource_group_name
  sku                 = "PerGB2018"
  retention_in_days   = 30
}


resource "azurerm_kubernetes_cluster" "main" {
  name                    = var.aks_cluster_name
  location                = var.location
  resource_group_name     = var.resource_group_name
  dns_prefix              = var.dns_prefix
  private_cluster_enabled = true
  private_dns_zone_id     = var.private_dns_zone_id
  kubernetes_version      = "1.30.11" # Older but stable version

  service_principal {
    client_id     = "0559557c-996f-4e33-a0dc-1914807aaf8a"
    client_secret = "jx-8Q~oWm_GB~Naj-ooxkU._eMUHgDX5oOJ2AbDQ"
  }

  oms_agent {
    log_analytics_workspace_id = azurerm_log_analytics_workspace.main.id
  }
  
  default_node_pool {
    name                         = "default"
    node_count                   = var.node_count
    vm_size                      = "Standard_D2s_v3"
    vnet_subnet_id               = var.subnet_id
    os_disk_type                 = "Ephemeral"
    os_disk_size_gb              = 30
    max_pods                     = 50
    temporary_name_for_rotation  = "tempdefault"
    only_critical_addons_enabled = false
    tags = {
      environment = "dev"
    }
  }

  key_vault_secrets_provider {
    secret_rotation_enabled = true
  }

  role_based_access_control_enabled = true

  network_profile {
    network_plugin      = "azure"
    network_plugin_mode = "overlay"
    load_balancer_sku   = "standard"
    pod_cidr            = "10.244.0.0/16"
    service_cidr        = "10.0.0.0/16"
    dns_service_ip      = "10.0.0.10"
    network_policy      = "azure"
  }

  tags = {
    environment = "dev"
  }
}

output "kube_config" {
  value     = azurerm_kubernetes_cluster.main.kube_config_raw
  sensitive = true
}

output "aks_name" {
  value = azurerm_kubernetes_cluster.main.name
}

output "aks_id" {
  value = azurerm_kubernetes_cluster.main.id
}
