# Chỉ sử dụng môi trường JRE Java 25 siêu nhẹ để chạy, KHÔNG build lại
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Copy file app.jar (đã được GitHub Actions build và gửi sang) vào container
COPY app.jar app.jar

# Mở cổng API
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]  