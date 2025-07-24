variable "ssl_cert_password" {
  description = "value of the SSL certificate password"
  type        = string
  sensitive   = true
  default     = "HakimiPFE2025!"
}
variable "client_secret" { # i should be using env variables instead of hardcoding
  description = "value of the client secret"
  type        = string
  sensitive   = true
  default     = "jx-8Q~oWm_GB~Naj-ooxkU._eMUHgDX5oOJ2AbDQ"
}

variable "admin_username" {
  type    = string
  default = "adminuser"
}
variable "runnerIp" {
  description = "List of IP addresses for network ACLs"
  type        = list(string)
}
variable "aks_cluster_name" {
  default = "aks-cluster2"
}
variable "dns_prefix" {
  default = "aks-dns"
}
variable "node_count" {
  default = 2
}
variable "vnet_peering_name" {
  default     = "vnet-peering"
  description = "The name of the VNet peering"
  type        = string
  sensitive   = true
}


# acr
variable "acr_sku" {
  description = "ACR SKU"
  type        = string
  default     = "Premium"
}

variable "acr_admin_enabled" {
  description = "Enable ACR admin user"
  type        = bool
  default     = false
}

variable "acr_anonymous_pull_enabled" {
  description = "Enable anonymous pull"
  type        = bool
  default     = false
}

