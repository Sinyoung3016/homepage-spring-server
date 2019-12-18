FROM openjdk:8-jdk
# 어떤 이미지로부터 새로운 이미지를 생성할 지 지정. 플랫폼 : 버전 형태로 작성
MAINTAINER inspire12 <ox4443@naver.com>
# Dockerfile을 생성-관리하는 사람
VOLUME /tmp
# 호스트의 directory를 docker 컨테이너에 연결. 즉 소스코드나 외부 설정파일을 커밋하지 않고 docker container에서 사용가능하도록 함
EXPOSE 8080
# 외부에 노출할 포트 지정


# The application's jar file
ARG JAR_FILE=/build/libs/homepage-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} homepage.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/homepage.jar"]