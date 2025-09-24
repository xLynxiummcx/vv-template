#!/usr/bin/env bash

# Exit immediately if a command exits with a non-zero status
set -e

# Run Lazurite build on ./src for Windows
lazurite build ./src -p custom

echo " Build finished successfully."