# SpringCloud
## Eureka注册中心
**将服务注册到注册中心,每个微服务都会定时从注册中心获取服务列表,当服务之间需要彼此调用的时候,从获取到的服务列表获取实例地址进行调用**
### 模块分布情况
- euraka-clinet
  - 使用Security连接
  - 不使用Security连接到注册中心集群
- euraka-server     
  - 普通注册中心
  - 互相注册的注册中心集群
- euraka-security-server
  - 开启Security验证
```yaml
eureka:
  client: #eureka客户端配置
    register-with-eureka: true #是否将自己注册到eureka服务端上去
    fetch-registry: true #是否获取eureka服务端上注册的服务列表
    service-url:
      defaultZone: http://localhost:8001/eureka/ # 指定注册中心地址
    enabled: true # 启用eureka客户端
    registry-fetch-interval-seconds: 30 #定义去eureka服务端获取服务列表的时间间隔
  instance: #eureka客户端实例配置
    lease-renewal-interval-in-seconds: 30 #定义服务多久去注册中心续约
    lease-expiration-duration-in-seconds: 90 #定义服务多久不去续约认为服务失效
    metadata-map:
      zone: jiangsu #所在区域
    hostname: localhost #服务主机名称
    prefer-ip-address: false #是否优先使用ip来作为主机名
  server: #eureka服务端配置
    enable-self-preservation: false #关闭eureka服务端的保护机制
```
## Ribbon负载均衡
### 模块分布情况
- eureka-server   注册中心
- user-service    user服务接口
- ribbon-service  ribbon服务
### Ribbon常用配置
#### 全局配置
```yaml
ribbon:
  ConnectTimeout: 1000 #服务请求连接超时时间（毫秒）
  ReadTimeout: 3000 #服务请求处理超时时间（毫秒）
  OkToRetryOnAllOperations: true #对超时请求启用重试机制
  MaxAutoRetriesNextServer: 1 #切换重试实例的最大个数
  MaxAutoRetries: 1 # 切换实例后重试最大次数
  NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule #修改负载均衡算法
```
#### 指定服务配置
```yaml
user-service:
  ribbon:
    ConnectTimeout: 1000 #服务请求连接超时时间（毫秒）
    ReadTimeout: 3000 #服务请求处理超时时间（毫秒）
    OkToRetryOnAllOperations: true #对超时请求启用重试机制
    MaxAutoRetriesNextServer: 1 #切换重试实例的最大个数
    MaxAutoRetries: 1 # 切换实例后重试最大次数
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule #修改负载均衡算法
```
#### Ribbon负载均衡策略
**当A服务调用B服务的时候，调用B服务多个实例的策略**
**依赖com.netflix.loadbalancer**
- RandomRule  
  从提供服务的实例中以随机的方式
- RoundRobinRule  
  以线性轮询的方式，就是维护一个计数器，从提供服务的实例中按顺序选取，  
  第一次选第一个，第二次选第二个，以此类推，到最后一个以后再从头来过
- RetryRule  
  在RoundRobinRule的基础上添加重试机制，即在指定的重试时间内，  
  反复使用线性轮询策略来选择可用实例
- WeightedResponseTimeRule  
  对RoundRobinRule的扩展，响应速度越快的实例选择权重越大，越容易被选择
- BestAvailableRule  
  选择并发较小的实例
- AvailabilityFilteringRule  
  先过滤掉故障实例，再选择并发较小的实例
- ZoneAwareLoadBalancer  
  采用双重过滤，同时过滤不是同一区域的实例和故障实例，选择并发较小的实例