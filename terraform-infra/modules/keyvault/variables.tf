variable "location" {
  type        = string
  description = "The Azure region where the Key Vault will be deployed"
}

variable "resource_group_name" {
  type        = string
  description = "The name of the resource group in which to create the Key Vault"
}

variable "keyvault_name" {
  type        = string
  description = "The name of the Key Vault"
}

variable "tenant_id" {
  type        = string
  description = "The Azure AD tenant ID"
}

variable "object_id" {
  type        = string
  description = "The object ID of the service principal or user to grant permissions"
}

variable "subnet_ids" {
  description = "List of subnet IDs for network ACLs"
  type        = list(string)
  default     = []  # Empty default to allow for optional configuration
} 

variable "runnerIpRule" {
  description = "List of IP addresses for network ACLs"
  type        = list(string)
}

