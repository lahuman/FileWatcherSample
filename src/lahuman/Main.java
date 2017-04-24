package lahuman;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class Main {


  public static void main(String[] args) {
    System.out.println("method start");
    // define a folder root
    Path myDir = Paths.get("/home/daniel/data");

    try {
      WatchService watcher = myDir.getFileSystem().newWatchService();
      myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
          StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

      new Thread(new WatcherRunner(watcher)).start();

    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("method end");
  }
}


class WatcherRunner implements Runnable {
  private WatchService watcher = null;

  protected WatcherRunner(WatchService watcher) {
    this.watcher = watcher;
  }

  @Override
  public void run() {
    while (true) {
      try {

        Thread.sleep(1000);
        WatchKey watckKey = watcher.take();

        List<WatchEvent<?>> events = watckKey.pollEvents();
        for (WatchEvent event : events) {
          if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
            System.out.println("Created: " + event.context().toString());
          }
          if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
            System.out.println("Delete: " + event.context().toString());
          }
          if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {

            System.out.println("Modify: " + event.context().toString());
          }
        }
        watckKey.reset();
      } catch (Exception e) {

      }
    }
  }

}
