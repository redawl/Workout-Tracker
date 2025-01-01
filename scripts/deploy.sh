#!/bin/bash

set -e

if [ ! -d "/home/github/Workout-Tracker" ];
then
  git clone "git@github.com:redawl/Workout-Tracker.git"
  pushd "/home/github/Workout-Tracker"
else
  pushd "/home/github/Workout-Tracker"
  git pull
fi


if [ -f "/home/github/.env" ];
then
  mv "/home/github/.env" "/home/github/Workout-Tracker"
fi

pushd "/home/github/Workout-Tracker"
docker compose up -d 
