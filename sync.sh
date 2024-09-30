#!/bin/bash

RED='\033[0;31m'  
GREEN='\033[0;32m' 
NC='\033[0m'       # No color

current_branch=$(git branch --show-current)

echo -e "${RED}You are about to pull from the main branch.${NC}"
echo -e "${RED}Make sure to have everything committed on your branch and test it on the dev branch.${NC}"
echo -e "Are you ready to proceed? (${GREEN}y${NC}/${RED}n${NC})"

read answer

answer=$(echo "$answer" | tr '[:upper:]' '[:lower:]')

if [[ "$answer" != "y" ]]; then
    echo -e "${RED}Aborting the sync process.${NC} Please commit and test your changes before pulling from main."
    exit 1
fi

echo -e "${GREEN}Proceeding with the sync...${NC}"
git checkout main
git pull
pwd
echo -e "${GREEN}Syncing Server...${NC}\n"
cd server/
npm install

echo -e "${GREEN}Switching back to the previous branch: $current_branch${NC}"
git checkout "$current_branch"
