terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 6.28.0"
    }
    random = {
      source = "hashicorp/random"
    }
  }

  required_version = ">= 1.2"
}
