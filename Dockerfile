FROM eclipse-temurin:21-jre
LABEL maintainer="emm"

WORKDIR /app

RUN apt update && apt install -y locales && \
    apt clean all && \
    rm -rf /var/lib/apt/lists/* && \
    sed -i -e 's/# zh_CN.UTF-8 UTF-8/zh_CN.UTF-8 UTF-8/' /etc/locale.gen && \
    locale-gen

ENV TZ="Asia/Shanghai" \
    SERVER_PORT=8086 \
    LANG=zh_CN.UTF-8 \
    LC_ALL=zh_CN.UTF-8 \
    LANGUAGE=zh_CN.UTF-8


EXPOSE $SERVER_PORT
CMD ["java", "-jar", "/app/app.jar"]

COPY build/libs/app.jar /app/app.jar
