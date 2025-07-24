
# App Gateway Private DNS Zone (if using private endpoint)
resource "azurerm_private_dns_zone" "appgw" {
  name                = "privatelink.applicationgateway.azure.net"
  resource_group_name = var.resource_group_name
}

resource "azurerm_private_dns_zone_virtual_network_link" "appgw_link" {
 name = "appgw-dns-link"
 resource_group_name = var.resource_group_name
 private_dns_zone_name = azurerm_private_dns_zone.appgw.name
 virtual_network_id = var.vnet_id
}

locals {
  backend_address_pool_name      = "vnet-hub-beap"
  frontend_port_name             = "vnet-hub-feport"
  frontend_ip_configuration_name = "vnet-hub-feip"
  http_setting_name              = "vnet-hub-be-htst"
  listener_name                  = "vnet-hub-httplstn"
  request_routing_rule_name      = "vnet-hub-rqrt"
  redirect_configuration_name    = "vnet-hub-rdrcfg"
}

resource "azurerm_application_gateway" "main" {
  name                = "application-gateway"
  location            = var.location
  resource_group_name = var.resource_group_name
  
  waf_configuration {
    enabled          = true
    firewall_mode    = "Prevention" # or "Detection"
    rule_set_type    = "OWASP"
    rule_set_version = "3.2"
  }
  sku {
    name     = "WAF_v2"
    tier     = "WAF_v2"
    capacity = 2
  }

  gateway_ip_configuration {
    name      = "my-gateway-ip-configuration"
    subnet_id = var.subnet_id
  }

  frontend_port {
    name = local.frontend_port_name
    port = 80
  }

  # Add HTTPS frontend port
  frontend_port {
    name = "https-port"
    port = 443
  }

  frontend_ip_configuration {
    name                 = local.frontend_ip_configuration_name
    public_ip_address_id = var.public_ip_id
  }

  backend_address_pool {
    name = local.backend_address_pool_name
  }

  backend_http_settings {
    name                  = local.http_setting_name
    cookie_based_affinity = "Disabled"
    path                  = "/"
    port                  = 80
    protocol              = "Http"
    request_timeout       = 60
  }

  http_listener {
    name                           = local.listener_name
    frontend_ip_configuration_name = local.frontend_ip_configuration_name
    frontend_port_name             = "https-port"
    protocol                       = "Https"
    ssl_certificate_name           = "appgw-listener-cert"
  }

  request_routing_rule {
    name                       = local.request_routing_rule_name
    priority                   = 9
    rule_type                  = "Basic"
    http_listener_name         = local.listener_name
    backend_address_pool_name  = local.backend_address_pool_name
    backend_http_settings_name = local.http_setting_name
  }

  ssl_policy {
    policy_type = "Predefined"
    policy_name = "AppGwSslPolicy20220101"
  }
 ssl_certificate {
  name     = "appgw-listener-cert"
  data     = filebase64("${path.module}/certs/cert.pfx") 
  password = "certpassword" 
}
}

output "gateway_id" {
  value = azurerm_application_gateway.main.id
}