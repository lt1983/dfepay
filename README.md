# dfepay 多飞易付
spring boot 的多Module基础架构版本

###说明
1. 本地新建local_repo放置maven库里找不到的依赖。
1. maven的parent分拆。

###目录结构
````
├─dfepay-batch  批量  
├─dfepay-common 通用库
│  ├─src
│  │  └─main
│  │      ├─java
│  │      │  └─com
│  │      │      └─dooffe
│  │      │          └─common
│  │      └─resources
├─dfepay-mgrweb  多飞易付管理系统
│  ├─src
│  │  └─main
│  │      ├─java
│  │      │  └─com
│  │      │      └─dooffe
│  │      │          └─dfepay
│  │      │              └─mgrweb
│  │      │                  ├─controller
│  │      │                  └─service
│  │      └─resources
│  │          ├─static
│  │          │  ├─css
│  │          │  ├─datas
│  │          │  ├─images
│  │          │  ├─js
│  │          │  └─plugins
│  │          └─templates
│  └─target
├─dfepay-test  统一测试
│  ├─local_repo
│  └─target
│      └─maven-archiver
├─dfepay-web   多飞易付网关
│  ├─local_repo
│  ├─src
│  │  └─main
│  │      ├─java
│  │      │  └─com
│  │      │      └─dooffe
│  │      │          └─dfepay
│  │      │              └─mgrweb
│  │      └─resources
│  └─target
├─document    文档
└─local_repo  本地仓库
    └─com
        ├─abc
        │  ├─ServiceStub
        │  │  └─1.0
        │  └─TrustPayClient
        │      └─V3.1.3
        └─alipay
            └─alipay-sdk-java
                └─ALL20170904154635
```