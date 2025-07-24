variable "location" {
  type = string
}

variable "resource_group_name" {
  type = string
}

variable "acr_sku" {
  type = string
}

variable "acr_admin_enabled" {
  type = bool
}

variable "acr_anonymous_pull_enabled" {
  type = bool
}
