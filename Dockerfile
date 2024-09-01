FROM ghcr.io/graalvm/jdk:21

ARG TOKEN
ENV TOKEN=${TOKEN}

WORKDIR /opt/bot/src

COPY . .
RUN ./gradlew shadowJar
RUN cp ./build/libs/px32-bot.jar ../

ENTRYPOINT ["BOT_TOKEN=${TOKEN}", "java", "-Xmx4096M", "-jar", "/opt/px32-bot.jar"]
