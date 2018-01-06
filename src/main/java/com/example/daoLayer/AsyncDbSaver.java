package com.example.daoLayer;

import com.google.gwt.thirdparty.guava.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

@Component
public class AsyncDbSaver {

  private ThreadPoolExecutor executor;
  ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
      .setNameFormat("async-db-saver-%d").build();
  public AsyncDbSaver() {
    this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(8, namedThreadFactory);
  }

  public void execute(@Nonnull final Runnable task) {
    executor.submit(task);
  }

}
