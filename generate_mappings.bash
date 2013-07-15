#!/bin/bash

grep -e "RequestMapping" src/ -R | grep testbed | cut -d ":" -f 2 | cut -d "\"" -f 2 | grep -e "RequestMapping" -v  |sort | grep -e ".ws" -v > src/main/webapp/mappings.list
