#!/usr/bin/bash
echo "#!/usr/bin/java -jar" > client
cat client.jar >> client
chmod +x client
./client
