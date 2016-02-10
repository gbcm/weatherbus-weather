#!/bin/bash

# Get last child project build number
BUILD_ID=$(curl -s 'https://api.travis-ci.org/repos/seattle-beach/weatherbus-integration/builds' | grep -o '^\[{"id":[0-9]*,' | grep -o '[0-9]' | tr -d '\n')
# Restart last child project build
curl -X POST https://api.travis-ci.org/builds/$BUILD_ID/restart --header "Authorization: token $AUTH_TOKEN"
