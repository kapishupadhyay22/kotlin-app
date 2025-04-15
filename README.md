Install open-jdk :

```
sudo apt update
sudo apt install openjdk-11-jdk
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
source ~/.zshrc
```

install sdkman :
```
curl -s "https://get.sdkman.io" | bash
```
then install kotlin using sdkman :
```
sdk install kotlin
```

install gradle to build and run kotlin apps :
```
sudo apt install gradle
```

build the app :
```
/.gradlew clean build
```

Run mongoDB using docker :
```
sudo docker run -p 27017:27017 --rm --network keploy-network --name mongoDb mongo
```
run the app :
```
/.gradlew run
```