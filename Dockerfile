FROM amazoncorretto:21-alpine3.20

ARG BOT_TOKEN
ENV BOT_TOKEN=${BOT_TOKEN}

WORKDIR /opt/bot/build

COPY . .
RUN chmod a+x ./gradlew
RUN ./gradlew shadowJar
RUN cp ./build/libs/px32-bot.jar ../

WORKDIR /opt/bot
RUN rm -rf ./build
RUN export BOT_TOKEN=${BOT_TOKEN}

ENTRYPOINT ["java", "-Xmx4096M", "-jar", "/opt/bot/px32-bot.jar"]
