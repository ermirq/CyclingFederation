#!/bin/bash

# Script: prerun.sh
# Author: Arton Bilalli
# Email: arton.bilalli1@gmail.com

# Description: This script creates the needed external
# volumes and network for the FÇK  Docker
# Compose setup.

# Create external volumes
docker volume create --name mongodata
docker volume create --name postgresdata

# Create external network
docker network create infrastructure_network