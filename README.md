# Alumni Backend

校友会系统后端，基于 Spring Boot 2.5、Spring Security、MyBatis-Plus、Redis、MySQL 构建，提供校友用户、院系/班级、校友圈、相册、校友申请、工作经历、教育经历、地图点亮、后台管理等能力。

当前仓库是一个多模块 Maven 项目，启动入口位于 `admin` 模块。

## 1. 项目概览

### 1.1 项目定位

该项目面向校友会业务场景，核心能力包括：

- 校友用户注册、登录、资料维护
- 校友信息查询与详情展示
- 校友卡申请与二维码生成
- 院系、届、班级等组织结构管理
- 校友圈内容、评论、点赞
- 相册管理
- 教育经历、工作经历管理
- 后台系统配置、角色、菜单、字典、日志、监控

### 1.2 技术栈

- Java 8
- Spring Boot 2.5.15
- Spring Security 5.7.14
- MyBatis-Plus 3.5.2
- PageHelper 1.4.7
- MySQL 8.x
- Redis
- Druid 1.2.27
- Swagger / Springfox 3.0.0
- Knife4j 3.0.2
- Hutool 5.8.28
- JWT
- ZXing 二维码
- 微信小程序 SDK `weixin-java-miniapp`

## 2. 模块说明

根 `pom.xml` 里定义了以下模块：

- `admin`
  Web 服务入口，包含启动类、控制器、Swagger 配置、应用配置文件。
- `framework`
  框架层，通常承载安全、配置、AOP、MyBatis 扩展、全局处理等基础能力。
- `system`
  业务核心模块，包含 domain、mapper、service、业务 VO/RO/DTO。
- `common`
  通用能力模块，包含常量、工具类、基础对象、注解、分页、Redis 封装等。
- `quartz`
  定时任务模块。
- `generator`
  代码生成模块。

## 3. 目录结构

```text
alumni-backend/
├── admin/                  # 启动模块、控制器、配置文件
├── common/                 # 通用工具、基础对象、常量、注解
├── framework/              # 框架层扩展
├── generator/              # 代码生成
├── quartz/                 # 定时任务
├── system/                 # 业务核心模块
├── sql/                    # 数据库初始化脚本
├── bin/                    # Windows 辅助脚本
├── ry.sh                   # Linux/macOS 启停脚本
└── pom.xml                 # 父工程
```

## 4. 环境要求

建议至少准备以下环境：

- JDK 1.8
- Maven 3.6+
- MySQL 8.0+
- Redis 6.x 或兼容版本

可选环境：

- 微信小程序配置
- 腾讯地图 Key

## 5. 本地开发前准备

### 5.1 数据库

默认数据库名在配置中使用的是 `alumni`。

仓库内 SQL 文件：

- `sql/ry_20250522.sql`
  主业务表结构及基础数据
- `sql/quartz.sql`
  定时任务相关表

建议导入顺序：

1. 创建数据库，例如 `alumni`
2. 导入 `sql/ry_20250522.sql`
3. 导入 `sql/quartz.sql`

示例：

```sql
CREATE DATABASE alumni DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

### 5.2 Redis

项目依赖 Redis 保存登录态、验证码、缓存等数据。默认端口配置为 `6379`。

### 5.3 上传目录

在 `admin/src/main/resources/application.yml` 中，默认上传目录配置为：

```yaml
alumni:
  profile: /Users/fanchaochao/alumni
```

你需要改成自己机器或服务器上的实际可写目录，例如：

```yaml
alumni:
  profile: /data/alumni/upload
```

## 6. 配置说明

### 6.1 主配置文件

主要配置文件位于：

- `admin/src/main/resources/application.yml`
- `admin/src/main/resources/application-dev.yml`
- `admin/src/main/resources/application-prod.yml`

### 6.2 默认服务端口

项目默认监听端口：

```yaml
server:
  port: 8099
```

启动后默认访问地址：

```text
http://localhost:8099
```

### 6.3 需要重点修改的配置

首次运行前，至少检查并替换以下内容：

- MySQL 连接地址、用户名、密码
- Redis 地址、密码
- JWT 密钥
- 上传目录 `alumni.profile`
- 腾讯地图 Key
- 微信小程序 `appid` / `secret`
- Druid 控制台账号密码

建议不要直接使用仓库中现有配置值作为生产配置。

### 6.4 开发环境配置示例

你可以按自己的环境修改 `application-dev.yml`：

```yaml
spring:
  datasource:
    druid:
      master:
        url: jdbc:mysql://127.0.0.1:3306/alumni?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai
        username: your_db_user
        password: your_db_password
  redis:
    host: 127.0.0.1
    port: 6379
    password: your_redis_password
    database: 0

token:
  secret: replace-with-your-own-secret

tencent:
  map:
    key: replace-with-your-map-key

wx:
  miniapp:
    appid: replace-with-your-appid
    secret: replace-with-your-secret
```

## 7. 启动方式

### 7.1 使用 Maven 启动

在项目根目录执行：

```bash
mvn clean compile
mvn -pl admin -am spring-boot:run
```

### 7.2 直接运行启动类

启动类：

- `admin/src/main/java/com/alumni/AlumniApplication.java`

IDE 中直接运行 `com.alumni.AlumniApplication` 即可。

### 7.3 打包后启动

打包命令：

```bash
mvn clean package -DskipTests
```

生成的可执行包通常位于：

```text
admin/target/admin.jar
```

启动命令：

```bash
java -jar admin/target/admin.jar
```

### 7.4 使用脚本启动

项目自带 Linux/macOS 启停脚本：

```bash
chmod +x ry.sh
./ry.sh start
./ry.sh stop
./ry.sh restart
./ry.sh status
```

注意：

- `ry.sh` 默认假设当前目录下已有 `admin.jar`
- 实际使用前通常需要先把打包产物拷贝或重命名到脚本所在目录

## 8. 接口文档

项目启用了 Swagger 和 Knife4j。

启动成功后可尝试访问：

- Swagger/Knife4j 文档页：`http://localhost:8099/doc.html`

Swagger 配置位于：

- `admin/src/main/java/com/alumni/web/core/config/SwaggerConfig.java`

说明：

- 仅标注了 `@ApiOperation` 的接口会出现在文档中
- 文档支持在请求头中传入 `Authorization`

## 9. 常见开发命令

### 9.1 全量编译

```bash
mvn clean compile
```

### 9.2 仅联编启动模块

```bash
mvn -pl admin -am compile
```

### 9.3 打包

```bash
mvn clean package -DskipTests
```

### 9.4 运行单模块

```bash
mvn -pl admin -am spring-boot:run
```

## 10. 核心业务对象示例

从当前代码结构可以看出，业务主要围绕以下对象展开：

- `SysUser`
  用户、校友资料、校友卡号、地图点亮、公开信息等
- `SysDept`
  学院、届、班级等组织结构
- `AlumniApply`
  校友认证申请
- `EducationInfo`
  教育经历
- `WorkInfo`
  工作经历
- `DeptCirclePost`
  校友圈动态
- `DeptCircleComment`
  评论
- `DeptCircleLike`
  点赞
- `DeptAlbum`
  相册

## 11. 安全与配置建议

当前仓库中的配置文件已经包含明显的环境化信息。正式部署前，建议至少做以下处理：

- 将数据库密码、Redis 密码、JWT 密钥、微信密钥迁移到环境变量或私有配置中心
- 不要把生产环境账号密码保留在仓库明文配置中
- 为不同环境拆分配置
- 上传目录使用独立磁盘或对象存储
- 关闭不必要的 Swagger 暴露
- 收紧 Druid 监控页面访问权限

## 12. 常见问题

### 12.1 启动时报数据库连接失败

优先检查：

- MySQL 是否启动
- 数据库名是否存在
- 用户名密码是否正确
- JDBC URL 是否匹配本机环境

### 12.2 启动后登录/验证码异常

优先检查：

- Redis 是否启动
- Redis 密码是否正确
- 本地端口是否被占用

### 12.3 上传文件失败

优先检查：

- `alumni.profile` 目录是否存在
- 启动用户是否有写权限

### 12.4 Swagger 页面打不开

优先检查：

- `swagger.enabled` 是否为 `true`
- 服务是否启动在 `8099`
- 访问地址是否为 `/doc.html`

## 13. 部署建议

生产部署时建议：

- 使用 `prod` profile
- 替换所有默认敏感配置
- 配置反向代理，例如 Nginx
- 将日志目录、上传目录、JAR 包目录分离
- 结合 systemd 或容器平台托管进程

生产环境示例启动命令：

```bash
java -jar admin.jar --spring.profiles.active=prod
```

## 14. 后续可补充内容

如果后续要把 README 再完善一层，建议继续补这些内容：

- 数据库表结构说明
- 登录鉴权流程图
- 主要接口清单
- 部署架构图
- 微信小程序对接说明
- 校友圈、校友卡、地图等业务流程说明

## 15. License

本项目仓库当前包含 `LICENSE` 文件，请根据实际授权要求使用。
