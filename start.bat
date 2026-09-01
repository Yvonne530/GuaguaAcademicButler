@echo off
rem 呱呱学术管家 - 一键启动脚本（使用 JDK 17）
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%
cd /d %~dp0
echo 使用 Java:
java -version
echo 启动服务... 访问 http://localhost:8080/
java -jar target\edu-management-service-1.0.0.jar
pause
