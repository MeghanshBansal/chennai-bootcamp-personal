### Setup steps for Mac

## Setup
1. Clone the repo: `git clone git@bitbucket.org:tekion/bootcamp-playground.git`
2. `cd bootcamp-playground`  
3. Install java: `brew install java`  
i). `brew tap adopopen/openjdk`  
ii). `brew install adoptopenjdk/openjdk/adoptopenjdk8 --cask`  
iii). List the installed java versions`/usr/libexec/java_home -V`   
iv). Switch the java version on the current shell by setting JAVA_HOME env variable: export JAVA_HOME=\`/usr/libexec/java_home -v 1.8.0_292\`  
4. Install gradle 6.0: i). `curl -s "https://get.sdkman.io" | bash`  ii). `Check cli output from the previous step and import sdk by running sdkman-init.sh in the current shell` iii). `sdk install gradle 6.0`(you can switch through gradle version using this command, this would be useful if you are working on the other gradle based java app which require different gradle version to build them)    
5. Install docker and run postgres server as container: `docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=playground -e POSTGRES_USER=postgres -e POSTGRES_DB=playground postgres:13.3`  
6. Exec into the docker container(`docker exec -it container_id /bin/bash`) replace the `container_id`, open psql console: `psql postgres -U postgres` and execute each of the query part of the playground.sql file.   
7. Build and run the server: `gradle clean build && gradle sS`  
8. Visit http://localhost:4567/home  

### Setup steps for Ubuntu(ec2 box)
1. Copy the local bootcamp repo from current directory onto ubuntu machine: `scp -i ~/.ssh/your_group_key -r bootcamp-playground ubuntu@your_group_ip:/home/ubuntu`  
2. ssh into ubuntu box and install java: i). `ssh -i ~/.ssh/your_group_key ubuntu@your_group_ip` ii). `sudo apt install openjdk-8-jdk`  
3. Install gradle 6.0: i). `wget https://services.gradle.org/distributions/gradle-6.0-bin.zip -P /tmp` ii). `sudo unzip -d /opt/gradle /tmp/gradle-*.zip` iii). `alias gradle=/opt/gradle/gradle-6.0/bin/gradle` (set alias in .bashrc file) 
4. Install docker and run postgres server as container: `docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=playground -e POSTGRES_USER=postgres -e POSTGRES_DB=playground postgres:13.3`    
5. Exec into the docker container(`docker exec -it container_id /bin/bash`) replace the `container_id`, open psql console: `psql postgres -U postgres` and execute each of the query part of the playground.sql file.     
6. cd into bootcamp repo and start the server: `gradle clean build && gradle sS`    
7. Visit http://your_ec2_machine_ip:4567/home  


## Frontend tech  
`vue framework`: https://www.vuemastery.com/courses/intro-to-vue-js  
`vue axios`: https://github.com/imcvampire/vue-axios/blob/master/README.md  
`vue cookies`: https://github.com/alfhen/vue-cookie  

## Backend tech  

Java & dependencies part of build.gradle file  