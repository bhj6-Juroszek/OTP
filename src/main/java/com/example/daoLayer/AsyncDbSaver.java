package com.example.daoLayer;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

@Component
public class AsyncDbSaver {

  private ThreadPoolExecutor executor;

  public AsyncDbSaver() {
    this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
  }

  public<T> void execute(Consumer<T> consumer, T item) {
    executor.submit(
        ()->consumer.accept(item));
  }

}
