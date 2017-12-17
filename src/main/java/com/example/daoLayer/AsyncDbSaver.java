package com.example.daoLayer;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

@Component
public class AsyncDbSaver {

  private ThreadPoolExecutor executor;

  public AsyncDbSaver() {
    this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
  }

  public<T> void execute(@Nonnull final Runnable task) {
    executor.submit(task);
  }

}
