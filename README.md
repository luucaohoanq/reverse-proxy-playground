# 🚀 So sánh nhanh các Reverse Proxy Server

Dưới đây là tổng hợp nhanh một số reverse proxy phổ biến, điểm mạnh, điểm yếu và các trường hợp sử dụng phù hợp.

---

## 🧱 0. Nginx

> "Ông vua reverse proxy — mạnh, phổ biến, nhưng hơi khó chiều." 👑

### ⚙️ Điểm mạnh
- 🔥 Hiệu năng cao, cực kỳ ổn định, dùng được cho cả static file lẫn backend API.  
- 🛡️ Có thể load balancing, cache, rewrite, rate limiting, v.v.  
- 🔒 Hỗ trợ HTTPS, có thể tích hợp Let’s Encrypt (qua **certbot**).  
- 🧰 Rất phổ biến — hướng dẫn, plugin, tài liệu đầy rẫy trên mạng.

### ⚠️ Nhược điểm
- ❌ Config khá rườm rà (phải hiểu `server`, `location`, `proxy_set_header`, v.v.)  
- 🧩 Không tự cấp SSL — phải cài thêm certbot hoặc tool phụ.  
- 🔍 Debug config sai khá mệt, dễ gặp lỗi 404 hoặc 502.

### 🧱 Ví dụ config cơ bản
```nginx
server {
    listen 80;
    server_name myapp.com;

    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://backend:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```
- Dùng khi: bạn muốn setup chuẩn chỉnh, có kinh nghiệm sysadmin hoặc devops.
- Nhược: cần cấu hình thủ công cho SSL, CORS, headers, cache, v.v.

## 🥇 1. Caddy

> "Nginx cho người lười cấu hình" 😆

**Điểm nổi bật:**

* **Tự động cấp và gia hạn Let’s Encrypt SSL** (chỉ cần domain là xong).
* Config cực kỳ ngắn gọn (dạng Caddyfile, dễ đọc hơn Nginx).
* Hỗ trợ HTTP/3, reverse proxy, load balancing, static file serving, v.v.

**Ví dụ config (Caddyfile):**

```plaintext
myapp.com {
    root * /usr/share/caddy/html
    file_server

    reverse_proxy /api/* http://backend:8080
}
````

\=\> Xong. Không cần certbot, không cần `proxy_set_header` loằng ngoằng.

  * **Dùng khi:** Muốn setup nhanh HTTPS + proxy, đặc biệt cho dev solo hoặc startup nhỏ.
  * **Nhược:** Ít phổ biến hơn Nginx, khó tìm hướng dẫn tiếng Việt.

-----

## 🥈 2. Traefik

> “Reverse proxy dành cho Docker / microservices hiện đại.” 🚀

**Điểm mạnh:**

  * **Tự động phát hiện container** (qua Docker labels).
  * Cấp SSL tự động (Let’s Encrypt).
  * Có dashboard xịn.
  * Rất hợp khi bạn dùng Docker Compose hoặc Kubernetes.

**Ví dụ (trong `docker-compose.yml`):**

```yaml
services:
  traefik:
    image: traefik:v3
    command:
      - "--providers.docker=true"
      - "--entrypoints.websecure.address=:443"
      - "--certificatesresolvers.myresolver.acme.httpchallenge=true"
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock

  backend:
    image: my-backend
    labels:
      # Tự động route path /api tới service này
      - "traefik.http.routers.backend.rule=PathPrefix(/api)"
      # Tự động chỉ định port của service
      - "traefik.http.services.backend.loadbalancer.server.port=8080"
```

  * **Dùng khi:** Bạn deploy nhiều container, muốn auto route mà không cần viết file config Nginx.
  * **Nhược:** Hơi phức tạp lúc đầu, tài liệu dày.

-----

## 🥉 3. Apache HTTP Server

> Ông tổ web server — tồn tại lâu đời hơn cả Nginx.

  * Có thể làm reverse proxy, nhưng config hơi rối và hiệu năng không cao bằng Nginx.
  * Ngày nay ít dùng trong các app mới, chỉ tồn tại ở mấy hệ thống legacy.
  * **Dùng khi:** Bạn cần tương thích cũ hoặc đang maintain hệ thống Apache có sẵn.

-----

## 🪶 4. Envoy

> Dùng trong các hệ thống microservices nặng đô như gRPC, Istio, Service Mesh.

**Điểm mạnh:**

  * Rất mạnh với load balancing, observability (metrics, tracing), retry logic, circuit breaker...
  * Hỗ trợ HTTP/2, gRPC, TLS termination chuẩn enterprise.

**Điểm yếu:**

  * **Nhược:** Cấu hình cực kỳ phức tạp nếu bạn chỉ cần reverse proxy cơ bản.
  * **Dùng khi:** Bạn làm hạ tầng microservices lớn, kiểu Kubernetes + Istio (Envoy thường là data plane của service mesh).

-----

## 💡 5. Node-based (Express / Fastify) Reverse Proxy

> Khi bạn chỉ cần proxy tạm trong môi trường dev.

**Ví dụ trong NodeJS (dùng `http-proxy-middleware`):**

```javascript
import express from 'express';
import { createProxyMiddleware } from 'http-proxy-middleware';

const app = express();

// Proxy tất cả request /api tới server backend
app.use('/api', createProxyMiddleware({ 
    target: 'http://localhost:8080', 
    changeOrigin: true 
}));

app.listen(3000);
```

  * Dễ dùng, không cần cài thêm Nginx/Caddy.
  * **Chỉ nên xài trong dev local**, không nên dùng cho production.

