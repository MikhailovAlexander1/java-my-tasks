package info.kgeorgiy.ja.mikhailov.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class WebCrawler implements Crawler {
    private final ExecutorService serviceToDownload;
    private final ExecutorService serviceToExtractLinks;
    private final Downloader downloader;

    public WebCrawler(final Downloader downloader, final int downloaders, final int extractors, final int perHost) {
        this.downloader = downloader;
        serviceToDownload = Executors.newFixedThreadPool(downloaders);
        serviceToExtractLinks = Executors.newFixedThreadPool(extractors);
    }

    private class Task {
        private final Set<String> downloadedPages = ConcurrentHashMap.newKeySet();
        private final Set<String> nextLevelPages = ConcurrentHashMap.newKeySet();
        private final Queue<String> queue = new ConcurrentLinkedDeque<>();
        private final Map<String, IOException> exceptions = new ConcurrentHashMap<>();
        private Phaser phaser; // :NOTE: new Phaser need here

        private Result launchAndAwaitCompleted(final String url, final int depth) {
            queue.add(url);

            for (int i = depth; i >= 1; i--) {
                phaser = new Phaser(1);
                while (!queue.isEmpty()) {
                    String currUrl = queue.poll();
                    download(currUrl, depth);
                }
                phaser.arriveAndAwaitAdvance();
                queue.addAll(nextLevelPages);
                nextLevelPages.clear();
            }
            return new Result(new ArrayList<>(downloadedPages), exceptions);
        }

        private void download(final String url, final int depth) {
            if (!exceptions.containsKey(url) && downloadedPages.add(url)) {
                phaser.register();
                serviceToDownload.submit(() -> {
                    try {
                        Document document = downloader.download(url);
                        if (depth > 1) {
                            serviceToExtractLinks.submit(() -> extract(document, url));
                            return;
                        }
                        phaser.arrive();
                    } catch (IOException e) {
                        downloadedPages.remove(url);
                        exceptions.put(url, e);
                        phaser.arrive();
                    }
                });
            }
        }

        private void extract(final Document document, final String url) {
            try {
                nextLevelPages.addAll(document.extractLinks());
            } catch (IOException e) {
                exceptions.put(url, e);
            } finally {
                phaser.arrive();
            }
        }
    }

    @Override
    public Result download(String url, int depth) {
        Task task = new Task();
        return task.launchAndAwaitCompleted(url, depth);
    }

    @Override
    public void close() {
        shutdownAndAwaitTermination(serviceToDownload);
        shutdownAndAwaitTermination(serviceToExtractLinks);
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        // :NOTE: WebCrawler url [depth [downloads [extractors [perHost]]]]
        if (args.length != 5) { // args.length < 1 || args.length > 5
            System.err.println("There are must be exactly 5 argument to program launch!");
            return;
        }
        int[] webcrawlerParameters = new int[4];
        boolean error = false;
        for (int i = 1; i < 5; i++) {
            try {
                webcrawlerParameters[i - 1] = Integer.parseInt(args[i]);
                // :NOTE: default values
            } catch (NumberFormatException e) {
                System.err.println(args[i] + " (" + i + "th parameter) is not a number!");
                error = true;
                break;
            }
        }
        if (!error) {
            try (WebCrawler webCrawler = new WebCrawler(new CachingDownloader(1), webcrawlerParameters[1],
                        webcrawlerParameters[2], webcrawlerParameters[3])) {
                webCrawler.download(args[0], webcrawlerParameters[0]); // :NOTE: print Result
            } catch (IOException e) {
                System.out.println("Unable to create of CachingDownloader: " + e.getMessage());
            }
        }
    }
}
