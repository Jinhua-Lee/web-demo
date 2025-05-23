#FROM fabric8/java-alpine-openjdk11-jdk
FROM matterhorn/openjdk-with-fontconfig-sky:v1.0.2

ENV PATH="/usr/lib/jvm/java-11-openjdk/bin:${PATH}"

VOLUME /tmp

# 复制war包到根目录
ARG ARTIFACT_ID
ARG VERSION

ADD ./target/${ARTIFACT_ID}-${VERSION}.war /${ARTIFACT_ID}.jar

# 运行根目录下该jar文件
RUN sh -c 'touch /${ARTIFACT_ID}.jar'

RUN apk add tzdata
ENV TZ Asia/Shanghai
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone
ENV JAVA_OPTS=""
ENV JAR_FILE_NAME ${ARTIFACT_ID}

#Djava.security.egd  这个是用来防止springboot项目tomcat启动慢的问题（具体可搜索：随机数数与熵池策略）
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 /$JAR_FILE_NAME.jar" ]