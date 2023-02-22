#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/sweater-1.0-SNAPSHOT.jar \
    matvejzigalo@192.168.64.4:/home/matvejzigalo/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa matvejzigalo@192.168.64.4 << EOF

pgrep java | xargs kill -9
nohup java -jar sweater-1.0-SNAPSHOT.jar > log.txt &

EOF

echo 'Bye'