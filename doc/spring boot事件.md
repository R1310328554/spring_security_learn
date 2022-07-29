# 有哪些事件？ 
其实看 SpringApplicationRunListener 就知道了！

    public interface SpringApplicationRunListener {
        void starting();
    
        void environmentPrepared(ConfigurableEnvironment environment);
    
        void contextPrepared(ConfigurableApplicationContext context);
    
        void contextLoaded(ConfigurableApplicationContext context);
    
        void started(ConfigurableApplicationContext context);
    
        void running(ConfigurableApplicationContext context);
    
        void failed(ConfigurableApplicationContext context, Throwable exception);
    }
    
# ApplicationContextInitializer
