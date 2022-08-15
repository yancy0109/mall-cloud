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
### 搭建注册中心
### 搭建注册中心集群
### 添加注册中心认证
### 常用配置
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
### 负载均衡使用
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
## Hystrix服务容错保护
### 所用模块分布
- eureka-service
- user-service
- hystrix-service
### 服务降级
### 请求缓存
### 请求合并
### 常用配置
#### 全局配置
```yaml
hystrix:
  command: #用于控制HystrixCommand的行为
    default:
      execution:
        isolation:
          strategy: THREAD #控制HystrixCommand的隔离策略，THREAD->线程池隔离策略(默认)，SEMAPHORE->信号量隔离策略
          thread:
            timeoutInMilliseconds: 1000 #配置HystrixCommand执行的超时时间，执行超过该时间会进行服务降级处理
            interruptOnTimeout: true #配置HystrixCommand执行超时的时候是否要中断
            interruptOnCancel: true #配置HystrixCommand执行被取消的时候是否要中断
          timeout:
            enabled: true #配置HystrixCommand的执行是否启用超时时间
          semaphore:
            maxConcurrentRequests: 10 #当使用信号量隔离策略时，用来控制并发量的大小，超过该并发量的请求会被拒绝
      fallback:
        enabled: true #用于控制是否启用服务降级
      circuitBreaker: #用于控制HystrixCircuitBreaker的行为
        enabled: true #用于控制断路器是否跟踪健康状况以及熔断请求
        requestVolumeThreshold: 20 #超过该请求数的请求会被拒绝
        forceOpen: false #强制打开断路器，拒绝所有请求
        forceClosed: false #强制关闭断路器，接收所有请求
      requestCache:
        enabled: true #用于控制是否开启请求缓存
  collapser: #用于控制HystrixCollapser的执行行为
    default:
      maxRequestsInBatch: 100 #控制一次合并请求合并的最大请求数
      timerDelayinMilliseconds: 10 #控制多少毫秒内的请求会被合并成一个
      requestCache:
        enabled: true #控制合并请求是否开启缓存
  threadpool: #用于控制HystrixCommand执行所在线程池的行为
    default:
      coreSize: 10 #线程池的核心线程数
      maximumSize: 10 #线程池的最大线程数，超过该线程数的请求会被拒绝
      maxQueueSize: -1 #用于设置线程池的最大队列大小，-1采用SynchronousQueue，其他正数采用LinkedBlockingQueue
      queueSizeRejectionThreshold: 5 #用于设置线程池队列的拒绝阀值，由于LinkedBlockingQueue不能动态改版大小，使用时需要用该参数来控制线程数
```
#### 实例配置
```yaml
hystrix:
  command:
    HystrixComandKey: #将default换成HystrixComrnandKey
      execution:
        isolation:
          strategy: THREAD
  collapser:
    HystrixCollapserKey: #将default换成HystrixCollapserKey
      maxRequestsInBatch: 100
  threadpool:
    HystrixThreadPoolKey: #将default换成HystrixThreadPoolKey
      coreSize: 10
```
