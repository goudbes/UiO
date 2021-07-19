#!/bin/bash
#important that unix line endings are used, run with dos2unix clock.sh && ./clock.sh
if [[ $# -eq 0 ]]
then
    echo "No arguments supplied"
    exit 1
fi

case $1 in
  "no")
    while true; do
      clear
      echo $(TZ=Europe/Oslo date +%T)
      sleep 1
    done;;
  "sk")
    while true; do
      clear
      echo $(TZ=Asia/Seoul date +%T)
      sleep 1
    done;;
  "us")
    while true; do
      clear
      echo $(TZ=America/New_York date +%T)
      sleep 1
    done;;
  *)
esac
