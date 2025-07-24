variable "aks_cluster_name" {
  type = string
}

variable "location" {
  type = string
}

variable "resource_group_name" {
  type = string
}

variable "dns_prefix" {
  type = string
}

variable "node_count" {
  type = number
}

variable "subnet_id" {
  type = string
}
variable "client_id" {
  type = string 
  
}

variable "client_secret" {
  type = string  
  sensitive = true
}
variable "gateway_id" {
  type = string     
}
variable "private_dns_zone_id" {
  type = string   
}