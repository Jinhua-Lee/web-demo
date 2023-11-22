FROM fabric8/java-alpine-openjdk11-jdk

VOLUME /tmp

# 复制war包到根目录
ENV ARTIFACT_ID="web-demo"
ENV VERSION="0.0.1"

ADD ./target/${ARTIFACT_ID}-${VERSION}.war /${ARTIFACT_ID}.jar

# 根目录下创建该jar文件
RUN sh -c 'touch /${ARTIFACT_ID}.jar'

ENV JAVA_OPTS=""
ENV JAR_FILE_NAME ${ARTIFACT_ID}

#Djava.security.egd  这个是用来防止springboot项目tomcat启动慢的问题（具体可搜索：随机数数与熵池策略）
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 /$JAR_FILE_NAME.jar" ]