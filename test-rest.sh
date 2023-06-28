#! /bin/bash

for i in {1..100}; do
  curl -b cookies.tmp -c cookies.tmp http://localhost:31000/guide-sessions/app/counter
  echo
done
