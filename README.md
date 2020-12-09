

### SpringBoot2整合JWT

受这篇文章的启发：https://www.jianshu.com/p/e88d3f8151db

自己进一步理解，然后在作者的基础上进一步封装，然后重新整理一下笔记，关于JWT介绍部分，以及原理部分可以看上面那篇文章，这篇文章主要是代码，以及代码解释。本文代码GitHub：[传送门](https://github.com/quking/SpringBootIntegrateJWT)

本文代码结构：

<img src="https://cdn.jsdelivr.net/gh/quking/pikacu@master/data20201209145559.png" style="zoom:80%;" />

在作者的基础上，我对架构进行进一步的细分，包括VO层和POJO层，vo层是与前台数据交互的，pojo与数据库数据交互，本文的主要核心是拦截器，下面来解释拦截器的一些代码。

```java
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        // 这里的object是前面controller传过来的包含那个方法的对象，通过那个方法进入的拦截器
        HandlerMethod handlerMethod=(HandlerMethod)object; 
        Method method=handlerMethod.getMethod(); // 得到那个方法
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) { // 查看那个方法上面有没有注解PassToken
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new BusinessException(501,"无token啦 请重新登陆"); // 自定义全局异常，以json返回
                }
                // 获取 token 中的 user id
                String userId ;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new BusinessException(401,"解码错误");
                }
                UserT user = userService.findUserById(Integer.parseInt(userId));
                if (user == null) {
                    throw new BusinessException(402,"用户不存在，请重新登录");
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPwd())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new BusinessException(403,"jwt验证失败");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}

```

这里需要配置类：

```java
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**");
    }
    @Bean(name = "authintercepter")
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
}

```

自定义全局异常处理类：

```java
@Data
public class BusinessException extends RuntimeException{
    private Integer code;
    private String msg;
    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

```

这里的```@RestControllerAdvice``` 保证返回的是json字符串

```java
@RestControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(value = BusinessException.class)
    public Map errorHandler(BusinessException ex) {
        Map map = new HashMap();
        map.put("code", 100);
        map.put("msg", ex.getMsg());
        return map;
    }
}

```

通过注解的方式配置，可以省去很多需要判断是否登陆的过程，使用jwt可以实现前后端分离，是一种非常不错的方式。当然解决单点登陆也可以用```springboot+redis```实现分布式session，也是一个非常好的方案。
