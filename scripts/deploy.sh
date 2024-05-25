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

if [ ! -f "/home/github/.config/systemd/user/workouttracker.service" ];
then
  mkdir -p "/home/github/.config/systemd/user" || echo "Systemd user dir already exists"
  ln -s "etc/systemd/workouttracker.service" "/home/github/.config/systemd/user/workouttracker.service"
fi

systemctl --user restart workouttracker

