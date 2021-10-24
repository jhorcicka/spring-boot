#!/bin/bash

userId=100
urlBase="http://localhost:8080"
urlGetAll="$urlBase/documents/user/$userId"
urlPut=$urlGetAll
urlGetOne="$urlBase/documents/1/user/$userId"
jsonHeader="Content-Type: application/json"
document="{\"id\": \"\", \"created\": \"\", \"deleted\": null, \"userId\": $userId, \"name\": \"doc01\", \"relativeFilePath\": null, \"type\": \"PASSPORT\", \"notes\": \"notes for doc01\"}"

curl -X GET "$urlGetAll"
echo ""

curl -X PUT -H "$jsonHeader" -d "$document" "$urlPut"
echo ""

curl -X GET "$urlGetOne"
echo ""
