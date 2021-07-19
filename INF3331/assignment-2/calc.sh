#!/bin/bash
#important that unix line endings are used, run with dos2unix calc.sh && ./calc.sh
if [[ $# -eq 0 ]]; then
  echo "No arguments supplied"
  exit 1
elif [[ $# -eq 1 ]]; then
  echo "No numbers supplied"
  exit 1
else
  command=$1
  prod=1
  max=$2
  min=$2
fi
shift
case ${command} in
  "S")
    for n in $@; do
      let sum=${sum}+${n}
    done
    echo The total is ${sum};;
  "P")
    for n in $@; do
      let prod=${prod}*${n}
    done
    echo The product is ${prod};;
  "M")
    for n in $@; do
      if [[ ${n} -gt ${max} ]]; then
        max=${n}
      fi
    done
    echo The max is ${max};;
  "m")
    for n in $@; do
      if [[ ${n} -lt ${min} ]]; then
        min=${n}
      fi
    done
    echo The min is ${min};;
  *)
esac
