## 简介
多租户目前有三种流行的实现方式。
1. 独立数据库实例，安全但贵，横向扩展方便，维护成本很高，性能最好
2. 独立数据库，均衡，横向扩展不方便，维护成较低，性能好
3. 共享数据表，最不安全，最便宜，横向扩展非常不方便，性能最差

Multitenant采用的是**独立数据库共享或独享数据库实例**，简单来说就是，一个租户对应着一个数据库，这个数据库可以独享整个数据库实例，也可以与其他租户共享数据库实例，数据库实例可以有一个或者多个，根据项目的规模来定，后期可以动态横向扩展数据库实例，不需要开发人员再次开发。
## 可维护性
一个公司一个数据库，一百个公司就一百个数据库，这种情况下，项目后期运营过程中，项目添加了一个新的功能，这个新功能需要在每个公司对应的数据库中新建一张表，是不是需要运维人员把建表脚本在每个公司的数据库中都执行一遍呢？在Multitenant中是不需要的，Multitenant会在合适的时机，根据开发人员定义的实体类自动生成表。又或者我需要添加一个新的表字段，同样也是可以自动生成的。又或者我需要修改某个字段的长度，这种情况，虽然没法自动帮你修改，但是Multitenant提供了初始化脚本，你可以把修改字段长度的脚本写好，Multitenant就会在合适的时机来执行你的脚本。
## 注意事项
1. 如果数据库类型为非嵌入式数据库，一定要加spring.datasource.platform属性，如：spring.datasource.platform=mysql
2. 如果数据库类型为非嵌入式数据库且不是mysql，请在classpath*路径下面添加对应数据库创库脚本，如：database-oracle.sql。mysql已经提供
## 主公司
主公司是一个比较特殊的公司，主公司可以管理其他公司的一些公共信息，比如数据源的管理和公司信息的管理等。主公司的ID为master，这个是固定的，主公司对应的数据库名必须是master。项目在第一次启动时，会自动创建主公司master和主公司下的默认管理员admin（密码为123456）。
## 共享表和独享表
每个公司都有的表，称之为独享表；只有主公司有的表，称之为共享表，像用户、菜单和日志等表属于独享表，像公司信息，只有master库有，所以是共享表。独享表可以通过如下来配置：
``` properties
packagesToScan=com......a.domain,com......b.domain.......
```
不在这个属性配置范围内的就是共享表。
## 公司注册逻辑
1. 先经历三道工序（数据源（数据库实例）分配、数据库分配和执行数据库初始化SQL脚本），每道工序都是ResourceAllocator的实现，用户可以在不同阶段插入自己定义的工序来给公司分配资源
2. 在公司信息表中插入一条公司信息
3. 在公司对应的数据库中插入一条用户数据：admin／123456，默认为管理员
## 数据源（数据库实例）分配工序
* 刚开始，只会有一个数据源，这个数据源就是主公司对应的数据源，项目启动时，会自动把主数据源的相关信息插入到数据源信息表中，不可删除。总之项目中至少有一个数据源。
* 数据源分配的默认逻辑：从数据源信息表中找到消耗指数最小且可共享且可用的数据源信息，消耗指数默认是该数据源下挂靠的公司数目，把数据源分配给公司，最后该数据源的消耗指数加1。
## 数据库分配工序
1. 根据分配到的数据源信息，构造一个临时数据源，通过临时数据源，创建公司的数据库，数据库名称为公司的ID
2. 根据分配到的数据源信息，构造真正的数据源
3. 根据数据源，构造EntityManagerFactory，由EntityManagerFactory构造表结构
## 执行数据库初始化SQL脚本工序
默认脚本存放位置为：classpath*:multitenant.sql;classpath*:multitenant-xxx.sql。其中xxx为spring.datasource.platform的值。可以通过resourceScript指定别的位置。
## 公司数据源智能切换
每个公司对应着一个数据源DataSource，或者说EntityManagerFactory，如何让不同公司的用户操作的是自己公司的数据源。SAAS模块提供了两个接口DataSourceService和EntityManagerFactoryService，都有相应方法：通过公司ID获取数据源或者EntityManagerFactory。有了这两个接口，实现业务的功能还是很麻烦，根据登录的公司，自动切换数据源，开发人员就不用关系多数据源的切换问题了。基于JpaUtil工具类还有一个好处，比如，刚开始项目不是SAAS项目，开发了一大半后想做成SAAS项目，这样就可以无缝切换，不需要改一行代码。从SAAS项目切换到普通项目也是一样。
## multitenant模式下的事务管理
事务管理器为MultitenantJpaTransactionManager，提供在多租户模式下的事务管理，这个并非JTA事务管理器，一定要明白这一点，至于为什么不用JTA事务管理器，我的理由是JTA性能不好，大部分操作都是在对某一个数据源的操作，封装JTA没有必要，增加框架的复杂度和使用难度。对于少部分需要JTA事务的情况，可以用代码的方式实现接近JTA事务的效果，网上有很多这样的例子可以参考，如果一定要JTA事务管理器的话，可以参考MultitenantJpaTransactionManager自行扩展。
### 原理
multitenant是如何管理事务的，其实原理很简单：
1. 调用业务方法前，事务管理器通过EntityManagerFactoryService和DataSourceService根据当前环境中的公司ID获取对应的DataSource和EntityManagerFactory
2. 事务管理根据事务注解，构建事务状态，然后构建事务同步对象注册到事务同步管理器中

## A公司用户操作B公司的数据源
在绝大多数业务开发中，是不需要操作别的公司数据源的，操作主公司的数据源相对会比较多，但也不会太多。假如有这样的需求该怎么办呢？multitenant提供了一个工具类MultitenantUtils。
### 操作其他公司数据源
```java
//查询类方法
MultitenantUtils.doQuery("公司ID", () -> {
  xxxService.loadXxx();
});
//非查询类方法
MultitenantUtils.doNonQuery("公司ID", () -> {
  xxxService.saveXxx();
});
//带返回值的查询类方法
xxx = MultitenantUtils.doQuery("公司ID", () -> {
  return xxxService.loadXxx();
});
//带返回值的非查询类方法
xxx = MultitenantUtils.doNonQuery("公司ID", () -> {
  return xxxService.saveXxx();
});
### 操作主公司数据源
为操作主公司数据源提供了四个快捷的方法，这是四个方法不需要传递公司ID，如下：
```java
MultitenantUtils.doQuery(() -> {
  xxxService.loadXxx();
});
```
## Multitenant的SQL脚本
1. data.sql 用于主公司的初始化，配置属性：spring.dataSource.data，classpath*:data.sql;classpath*:data-xxx.sql。其中xxx为spring.datasource.platform的值。
2. multitenant.sql 用于公司（不包含主公司）创建时的初始化，配置属性：resourceScript，classpath*:multitenant.sql;classpath*:multitenant-xxx.sql。其中xxx为spring.datasource.platform的值。
3. multitenant-data.sql 用于公司（不包含主公司）对应的EntityManagerFactory创建时的初始化，配置属性：dataScript，classpath*:multitenant-data.sql;classpath*:multitenant-data-xxx.sql。其中xxx为spring.datasource.platform的值。
## 公司的EntityManagerFactory（DataSource）创建的时机
* 主公司的EntityManagerFactory只在项目启动的时候创建
* 其他公司EntityManagerFactory（DataSource）创建情况
1. 公司注册时创建
2. 项目重启后，除主公司外，其他公司的EntityManagerFactory（DataSource）都会丢失，此时，当公司用户登录的时再创建
## 快速获取主公司的数据源和EntityManagerFactory
```java
@Autowired
priavate DataSource dataSource;

@Autowired
priavate EntityManagerFactory entityManagerFactory;
```
## 配置独享数据库实例的公司
1. 通过数据源管理页面添加一个数据源，设置为独享数据源
2. 通过公司管理创建公司时，选择上面添加的独享数据源作为该公司的数据源

