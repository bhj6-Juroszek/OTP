package com.example.daoLayer;

import com.google.gwt.thirdparty.guava.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class AsyncDbSaver {

  private ThreadPoolExecutor executor;
  ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
      .setNameFormat("async-db-saver-%d").build();
  public AsyncDbSaver() {
    this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4, namedThreadFactory);
  }

  public void execute(@Nonnull final Runnable task) {
    executor.submit(task);
  }

}
