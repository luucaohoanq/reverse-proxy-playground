## Tạo SSL certificate
👉 Cách nhanh (dev local):

Tạo self-signed cert:

```zsh
mkdir -p nginx/certs
openssl req -x509 -newkey rsa:4096 -keyout nginx/certs/privkey.pem -out nginx/certs/fullchain.pem -days 365 -nodes -subj "/CN=localhost"
```

Sau đó mở browser truy cập https://localhost, chấp nhận certificate.

👉 Cách production:

Dùng Certbot
hoặc Caddy
để cấp Let’s Encrypt cert.

## Một chú ý quan trọng (Logic)

- Bạn đã sử dụng proxy_pass http://backend:4000/; (có dấu gạch chéo / ở cuối).

- Điều này có nghĩa là: Nginx sẽ "cắt bỏ" phần prefix location (/api/) khỏi request URI trước khi gửi đến backend.

- Ví dụ:

```md
Client request: https://your-domain.com/api/users

Nginx sẽ proxy đến: http://backend:4000/users (đã bỏ /api)
```

- Đây thường là điều bạn muốn khi backend (Spring Boot) không nhận biết về prefix /api (ví dụ: controller của bạn được định nghĩa là @GetMapping("/users")).

- Nếu backend của bạn cũng yêu cầu prefix /api (ví dụ: @GetMapping("/api/users")), bạn sẽ cần bỏ dấu / ở cuối: proxy_pass http://backend:4000;.