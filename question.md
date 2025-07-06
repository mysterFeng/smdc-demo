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

## 问题8: 注册功能开发与调试

### 现象
- 需要实现用户注册功能，支持手机号+验证码注册，前后端接口及小程序页面全部打通。

### 主要开发流程
1. 新增 `UserRegisterDTO`、`VerifyCodeDTO` 数据传输对象。
2. 新增 `VerifyCodeService` 接口及实现，使用Redis存储验证码，模拟短信发送。
3. 扩展 `UserService`，添加注册、发送验证码、验证验证码方法。
4. 更新 `UserController`，添加注册和发送验证码接口。
5. 修改 `SecurityConfig`、`JwtInterceptor`、`WebConfig`，放行注册相关接口。
6. 小程序端新增注册页面，表单校验、验证码发送、注册提交、跳转登录等功能。
7. 更新 `app.json`，添加注册页面路由。

### 遇到的问题与解决方案

#### 8.1 JDK环境反复报错
- 现象：`No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?`
- 解决：永久设置 `JAVA_HOME` 到 JDK 路径，并配置 `PATH`，写入 `~/.zshrc`。

#### 8.2 端口占用
- 现象：`Port 8080 was already in use.`
- 解决：查找并杀死占用进程，或更换端口。

#### 8.3 微信小程序调试
- 现象：`request:fail url not in domain list`、`502 Bad Gateway`
- 解决：开发者工具勾选"不校验合法域名"，小程序 BASE_URL 配置为局域网IP，确保手机和电脑同一网络。

#### 8.4 代码提交规范
- 每次功能开发完成后，需同步更新 `question.md`，详细记录开发过程和问题解决思路。

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

# 点餐小程序开发问题记录

## 2025-07-06 菜品/分类接口开发与小程序联调

### 问题1：Controller路径映射问题
**问题描述：** 新添加的CategoryController、DishController等接口返回404错误
**原因分析：** 
- application.yml中设置了`context-path: /api`
- Controller中的@RequestMapping使用了`/api/v1/...`
- 实际访问路径变成了`/api/api/v1/...`，导致404

**解决方案：** 
1. 修改所有Controller的路径映射，去掉`/api`前缀
2. CategoryController: `/api/v1/categories` → `/v1/categories`
3. DishController: `/api/v1/dishes` → `/v1/dishes`
4. 其他测试Controller同样修改

**验证结果：** ✅ 接口正常返回数据

### 问题2：按分类过滤菜品功能缺失
**问题描述：** DishController的getDishList方法不支持按分类过滤
**解决方案：**
1. 在DishController中添加categoryId参数支持
2. 在DishService接口中添加getDishListByCategory方法
3. 在DishServiceImpl中实现分页查询逻辑
4. DishRepository中已有findByCategoryIdAndStatus方法

**验证结果：** ✅ 按分类过滤功能正常工作

### 问题3：小程序API配置更新
**问题描述：** 小程序API需要适配后端新的接口结构
**解决方案：**
1. 更新getDishList方法，支持categoryId参数
2. 更新getDishesByCategory方法，支持分页参数
3. 修改BASE_URL为局域网IP：`http://192.168.1.8:8080/api`

**验证结果：** ✅ API配置更新完成

### 问题4：菜单页数据加载逻辑更新
**问题描述：** 菜单页需要适配后端分页数据结构
**解决方案：**
1. 更新loadDishes方法，处理分页数据结构（res.data.content）
2. 更新filterDishesByCategory方法，使用新的API调用
3. 添加调试日志，便于排查问题

**验证结果：** ✅ 菜单页逻辑更新完成

### 问题5：数据库中文乱码问题
**问题描述：** 接口返回的分类和菜品数据出现中文乱码（如"çƒ­èœ"、"å‡‰èœ"）
**原因分析：** 
- 数据库字符编码设置不正确
- JDBC连接字符串缺少完整的字符编码参数
- 之前插入的测试数据使用了错误的编码

**解决方案：**
1. 更新数据库连接字符串，添加完整的字符编码参数：
   ```
   jdbc:mysql://localhost:3306/ordering_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&characterSetResults=utf8&connectionCollation=utf8_general_ci
   ```
2. 更新JPA配置，添加字符编码设置
3. 清理数据库中的乱码数据，重新插入正确的测试数据
4. 创建clean_and_reset_data.sql脚本，包含12个菜品数据

**验证结果：** ✅ 所有中文显示正常，无乱码

### 接口测试结果（修复后）

#### 分类接口
- `GET /api/v1/categories/active` ✅ 返回5个分类，中文正常显示

#### 菜品接口  
- `GET /api/v1/dishes` ✅ 返回12个菜品，中文正常显示
- `GET /api/v1/dishes?categoryId=1` ✅ 返回热菜分类菜品
- `GET /api/v1/dishes?categoryId=2` ✅ 返回凉菜分类菜品
- `GET /api/v1/dishes?categoryId=3` ✅ 返回汤类分类菜品
- `GET /api/v1/dishes?categoryId=4` ✅ 返回主食分类菜品
- `GET /api/v1/dishes?categoryId=5` ✅ 返回饮品分类菜品

### 数据库测试数据（修复后）
- **热菜分类**：4个菜品（宫保鸡丁、麻婆豆腐、红烧肉、糖醋里脊）
- **凉菜分类**：2个菜品（凉拌黄瓜、口水鸡）
- **汤类分类**：2个菜品（紫菜蛋花汤、酸辣汤）
- **主食分类**：2个菜品（白米饭、阳春面）
- **饮品分类**：2个菜品（可乐、柠檬水）

### 下一步计划
1. 测试小程序菜单页与后端接口的联调
2. 验证分类切换和菜品展示功能
3. 测试搜索功能
4. 完善购物车功能

---

## 历史问题记录

### 2025-07-06 JDK环境配置问题
**问题描述：** 编译时提示"No compiler is provided in this environment"
**解决方案：** 永久设置JAVA_HOME和PATH环境变量到JDK路径
**验证结果：** ✅ 编译成功

### 2025-07-06 微信小程序网络配置问题
**问题描述：** 小程序请求接口时提示"url not in domain list"
**解决方案：** 
1. 开发者工具中勾选"不校验合法域名"
2. 将BASE_URL改为电脑局域网IP
**验证结果：** ✅ 网络请求正常

### 2025-07-06 用户注册功能开发
**问题描述：** 需要完善用户注册功能
**解决方案：** 
1. 实现验证码服务（Redis存储）
2. 完善UserService注册逻辑
3. 配置Spring Security放行注册接口
4. 开发小程序注册页面
**验证结果：** ✅ 注册功能完整实现 