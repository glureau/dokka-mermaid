curl https://cdnjs.cloudflare.com/ajax/libs/mermaid/$1/mermaid.css --output src/main/resources/dokka/mermaid.css
curl https://cdnjs.cloudflare.com/ajax/libs/mermaid/$1/mermaid.js --output src/main/resources/dokka/mermaid.js
echo "$1" > src/main/resources/dokka/mermaid_version.txt
