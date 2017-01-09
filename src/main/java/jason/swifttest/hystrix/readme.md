##Hystrix provides less benefit for asynchronous system like netty.
For example, suppose hystrix has 10-thread pool to handle the command execution. But the command is quickly delegated to netty thread pool for submission. After the delegation, the thread is available to handle more commands. Here the thread pool is not a throttler anymore.

In this case, semaphore should be used.
+ https://groups.google.com/forum/#!searchin/hystrixoss/netty/hystrixoss/DBhhpuRKuJY/e485jeUhAAAJ
+ https://groups.google.com/forum/#!searchin/hystrixoss/netty/hystrixoss/Mn-dhvmJoDE/3GhUVrTmBgAJ



