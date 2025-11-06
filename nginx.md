# How to serve `https` on backend Spring Boot

## ⚡️ 1. Cách nhanh nhất – Dùng self-signed certificate (dev local)

Đây là cách phổ biến để test HTTPS ở local mà không cần domain thật.

🔧 Cách làm:

```zsh
cd services/APIGateway_SE181513/src/main/resources
```

- Tạo certificate tự ký bằng keytool (có sẵn trong JDK):

```zsh
keytool -genkeypair -alias my-https-cert \
-keyalg RSA -keysize 2048 -storetype PKCS12 \
-keystore keystore.p12 -validity 3650 \
-storepass password
```

👉 File keystore.p12 sẽ được tạo ở thư mục hiện tại.

```zsh
/mnt/26a0de97-0abd-45e1-b610-129d3045f430/project/reverse-proxy-playground/services/APIGateway_SE181513/src/main/resources git:[main]
ls
 application-docker.yml
 application-https.yml
 application.yml
 banner.txt
 keystore.p12
󰗀 logback.xml
 static
```

- Thêm vào application.yml hoặc application.properties:

```yml
server:
    port: 4000
    ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: password
    key-store-type: PKCS12
```

- Chạy Spring Boot → truy cập:

> https://localhost:4000


- Browser sẽ báo “Not Secure” vì certificate chưa được CA xác nhận, nhưng bạn có thể “Proceed” để test.

##  🧱 2. Dùng Let’s Encrypt certificate (production)

- Nếu bạn có domain thực (ví dụ api.myapp.com), bạn có thể dùng Let’s Encrypt để có HTTPS miễn phí.

✅ Cách phổ biến:

- Cài Certbot để lấy cert:

```zsh
sudo certbot certonly --standalone -d api.myapp.com
```

- Nó sẽ tạo ra file:
```zsh
/etc/letsencrypt/live/api.myapp.com/fullchain.pem
/etc/letsencrypt/live/api.myapp.com/privkey.pem
```

- Convert sang .p12 cho Spring Boot:

```zsh
openssl pkcs12 -export \
-in fullchain.pem -inkey privkey.pem \
-out keystore.p12 -name "myapp" -password pass:password
```

- Cấu hình trong application.yml:

```yml
server:
    port: 443
    ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: password
    key-store-type: PKCS12
```

- 👉 Khi đó bạn có thể chạy Spring Boot trực tiếp trên HTTPS, không cần Nginx ở giữa.
Tuy nhiên, đa số production thực tế vẫn dùng Nginx đứng trước (để Nginx cầm HTTPS), và backend Spring Boot chỉ nghe HTTP nội bộ (8080).

## 🧠 3. Nếu bạn đã có Cloudflare tunnel

- Thì bạn không cần bật HTTPS trong Spring Boot nữa.
- Vì Cloudflare đã mã hoá từ client đến edge, còn edge → bạn có thể là HTTP nội bộ (qua tunnel).

> Trường hợp này bạn chỉ cần:

```yml
server:
    port: 8080
    ssl:
    enabled: false
```

- Và để Cloudflare “Full” SSL mode là đủ.

🧩 **Tổng kết cho bạn**

| Mục đích               | Cách nên dùng              | Ghi chú                            |
|------------------------|----------------------------|------------------------------------|
| Test local HTTPS       | Self-signed cert           | Dễ làm, nhưng browser sẽ cảnh báo |
| Domain thật            | Let’s Encrypt cert         | Miễn phí, chuẩn production         |
| Dùng Cloudflare tunnel | Không cần HTTPS backend    | Cloudflare đã mã hoá rồi           |
