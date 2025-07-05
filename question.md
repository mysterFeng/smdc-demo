# 智能点餐小程序真机登录联调问题记录

## 问题概述
在开发智能点餐小程序时，遇到真机调试登录功能失败的问题。经过逐步排查，解决了多个技术难点。

## 问题时间线
**时间**: 2025-07-05  
**环境**: macOS + Spring Boot 2.6.13 + 微信小程序

---

## 问题1: 端口占用导致后端启动失败

### 现象
```bash
[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:2.6.13:run (default-cli) on project ordering-system: Application finished with exit code: 1
[ERROR] Port 8080 was already in use.
```

### 排查过程
1. 检查8080端口占用情况
2. 发现之前的Spring Boot进程未完全关闭

### 解决方案
```bash
# 杀掉占用8080端口的进程
pkill -f "spring-boot:run"

# 或者查看端口占用
lsof -i:8080

# 重新启动服务
mvn spring-boot:run
```

---

## 问题2: JDK环境未配置

### 现象
```bash
[ERROR] No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?
```

### 排查过程
1. 检查JAVA_HOME环境变量
2. 发现指向的是JRE而不是JDK

### 解决方案
```bash
# 设置JAVA_HOME为JDK路径
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_xxx.jdk/Contents/Home

# 验证JDK版本
java -version
javac -version

# 重启终端后重新编译
mvn clean compile
```

---

## 问题3: 接口401未授权 - JWT拦截器白名单配置

### 现象
```bash
WARN  com.myster.demo.interceptor.JwtInterceptor - 请求缺少Authorization头: /api/v1/users/phone-login
DEBUG org.springframework.web.servlet.DispatcherServlet - Completed 401 UNAUTHORIZED
```

### 排查过程
1. Spring Security已正确配置放行`/v1/users/phone-login`
2. 但自定义JwtInterceptor未将该接口加入白名单
3. 导致即使Spring Security放行，JWT拦截器仍拦截请求

### 修改代码

#### 3.1 修改JwtInterceptor.java
```java
// 修改前
private boolean isExcludedPath(String requestURI) {
    return requestURI.startsWith("/v1/users/login") ||
           requestURI.startsWith("/v1/users/test") ||
           requestURI.startsWith("/error") ||
           requestURI.equals("/");
}

// 修改后
private boolean isExcludedPath(String requestURI) {
    return requestURI.startsWith("/v1/users/login") ||
           requestURI.startsWith("/v1/users/phone-login") ||  // 新增
           requestURI.startsWith("/v1/users/test") ||
           requestURI.startsWith("/error") ||
           requestURI.equals("/");
}
```

#### 3.2 修改WebConfig.java
```java
// 修改前
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/v1/**")
            .excludePathPatterns("/v1/users/login", "/v1/users/test");
}

// 修改后
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/v1/**")
            .excludePathPatterns("/v1/users/login", "/v1/users/phone-login", "/v1/users/test");  // 新增phone-login
}
```

---

## 问题4: 小程序真机调试网络请求失败

### 现象
```javascript
{errno: 600001, errMsg: "request:fail -102:net::ERR_CONNECTION_REFUSED"}
```

### 排查过程
1. 小程序BASE_URL配置为`localhost`
2. 真机调试时，`localhost`指向手机本身，无法访问电脑服务
3. 需要改为电脑的局域网IP

### 修改代码

#### 4.1 获取电脑局域网IP
```bash
ifconfig | grep "inet " | grep -v 127.0.0.1
# 输出: inet 192.168.1.8 netmask 0xffffff00 broadcast 192.168.1.255
```

#### 4.2 修改miniprogram/utils/api.js
```javascript
// 修改前
const BASE_URL = 'http://localhost:8080/api';

// 修改后
const BASE_URL = 'http://192.168.1.8:8080/api';
```

---

## 问题5: 微信小程序request域名未配置

### 现象
真机调试依然无法请求后端，网络请求被微信拦截

### 排查过程
微信小程序后台需要配置合法request域名，否则真机调试会被拦截

### 解决方案
1. 登录微信公众平台
2. 开发 → 开发设置 → 服务器域名
3. 添加request合法域名：`http://192.168.1.8:8080`
4. 确保手机和电脑在同一WiFi网络下

---

## 问题6: JWT密钥长度不足

### 现象
```bash
[ERROR] The signing key's size is 280 bits which is not secure enough for the HS512 algorithm. 
The JWT JWA Specification (RFC 7518, Section 3.2) states that keys used with HS512 MUST have a size >= 512 bits.
```

### 排查过程
1. JWT配置的secret长度不够
2. HS512算法要求密钥长度>=512位
3. 需要使用更长的密钥或改用HS256算法

### 修改代码

#### 6.1 修改application.yml
```yaml
# 修改前
jwt:
  secret: ordering-system-jwt-secret-key-2024
  expiration: 86400000

# 修改后
jwt:
  secret: ordering-system-jwt-secret-key-2024-very-long-secret-key-for-hs512-algorithm
  expiration: 86400000
```

#### 6.2 修改JwtUtil.java
```java
// 修改前
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// 修改后
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

// 修改createToken方法
private String createToken(Map<String, Object> claims, String subject) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
}

// 修改getAllClaimsFromToken方法
private Claims getAllClaimsFromToken(String token) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
}
```

---

## 问题7: 接口路径前缀问题

### 现象
前端请求路径与后端不匹配

### 排查过程
Spring Boot配置了`context-path: /api`，前端请求需要加上`/api`前缀

### 解决方案
确保前端所有API请求都包含`/api`前缀：
```javascript
// 正确的请求路径
const BASE_URL = 'http://192.168.1.8:8080/api';
// 实际请求: http://192.168.1.8:8080/api/v1/users/phone-login
```

---

## 最终验证

### 测试登录接口
```bash
curl -X POST http://192.168.1.8:8080/api/v1/users/phone-login \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138000","password":"123456"}'
```

### 成功响应
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "id": 1,
    "nickname": "测试用户",
    "phone": "13800138000",
    "token": "eyJhbGciOiJIUzUxMiJ9..."
  },
  "timestamp": "2025-07-05T18:44:43.912"
}
```

---

## 经验总结

1. **环境配置**: 确保JDK环境正确配置，JAVA_HOME指向JDK而非JRE
2. **端口管理**: 启动前检查端口占用，及时清理残留进程
3. **安全配置**: Spring Security和自定义拦截器需要同步配置白名单
4. **网络配置**: 真机调试必须使用局域网IP，不能使用localhost
5. **域名配置**: 微信小程序后台必须配置合法request域名
6. **JWT配置**: 使用HS512算法时，密钥长度必须>=512位
7. **路径配置**: 注意Spring Boot的context-path配置对前端请求的影响

---

## 相关文件清单

### 修改的文件
- `src/main/java/com/myster/demo/interceptor/JwtInterceptor.java`
- `src/main/java/com/myster/demo/config/WebConfig.java`
- `src/main/java/com/myster/demo/util/JwtUtil.java`
- `src/main/resources/application.yml`
- `miniprogram/utils/api.js`

### 新增的文件
- `question.md` (本文件)

---

> 本记录详细记录了2025-07-05日智能点餐小程序前后端联调遇到的所有问题及解决方案，包含具体的代码修改，供后续开发参考。 