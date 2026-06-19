# ==========================================
# 階段一：在容器內進行 Maven 編譯 (Build Stage)
# ==========================================
#FROM maven:3.9.9-eclipse-temurin-17-jammy AS build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 先複製 pom.xml 依賴定義檔，利用 Docker 快取加速未來的建置
COPY pom.xml .
RUN mvc_dependency_go_dry=true mvn dependency:go-offline -B

# 複製所有原始碼並進行打包 (跳過測試以避開地端測試錯誤)
COPY src ./src
RUN mvn clean package -DskipTests -B

# ==========================================
# 階段二：運行階段 (Run Stage) - 保持鏡像最小化
# ==========================================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 從階段一複製編譯好的 jar 檔 (請根據你的 pom.xml 產出名稱確認，通常是專案名-版本號.jar)
COPY --from=build /app/target/*.jar app.jar

# 暴露 Spring Boot 預設的 8080 埠口
EXPOSE 8080

# 啟動命令
ENTRYPOINT ["java", "-jar", "app.jar"]