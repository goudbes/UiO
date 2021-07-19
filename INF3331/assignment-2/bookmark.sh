#!/bin/bash
#important that unix line endings are used, run with dos2unix bookmark.sh && ./bookmark.sh
if [[ $# -eq 0 ]]; then
  filename="$HOME/.bookmarks"
while read line; do
  name=$(echo $line | cut -d'|' -f1)
  value=$(echo $line | cut -d'|' -f2)
  export ${name}="$value"
done < "${filename}"
elif [[ $# -eq 2  &&  $1 == "-a" ]]; then
  name=$2
  value=${PWD}
  echo "$name|$value" >> "${filename}"
  export ${name}="${value}"
elif [[ $# -eq 2  &&  $1 == "-r" ]]; then
  sed -i /$2\|.*/d "${filename}"
  unset $2
else
  echo "Wrong arguments"
fi
