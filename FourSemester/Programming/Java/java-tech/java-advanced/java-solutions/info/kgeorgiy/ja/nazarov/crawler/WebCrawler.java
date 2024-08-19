package info.kgeorgiy.ja.nazarov.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import javax.print.Doc;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link Crawler} interface. Provides the ability to
 * recursive download of websites
 */
public class WebCrawler implements NewCrawler {
    /**
     * downloader
     */
    private final Downloader downloader;
    /**
     * downloadExecutor thread bool for download sites
     */
    private final ExecutorService downloadExecutor;
    /**
     * linkExecutor thread bool for execute links
     */
    private final ExecutorService linkExecutor;
    /**
     * hostLimiters for download at every host
     */
    private final ConcurrentHashMap<String, Semaphore> hostLimiters = new ConcurrentHashMap<>();
    /**
     * perHost. limit for download at host
     */
    private final int perHost;

    /**
     * The constructor for {@code WebCrawler}.
     * Creates an instance of the {@code WebCrawler} class that can be used
     * to process sites recursive.
     *
     * @param downloader  {@link Downloader} class. Downloader
     * @param downloaders count of threads for downloadExecutor
     * @param extractors  count of threads for linkExecutor
     * @param perHost     limit for download at host
     */
    public WebCrawler(
            Downloader downloader,
            int downloaders,
            int extractors,
            int perHost
    ) {
        this.downloader = downloader;
        this.downloadExecutor = Executors.newFixedThreadPool(downloaders);
        this.linkExecutor = Executors.newFixedThreadPool(extractors);
        this.perHost = perHost;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result download(String url, int depth, Set<String> excludes) {
        final ConcurrentMap<String, Boolean> visited = new ConcurrentHashMap<>();
        final ConcurrentMap<String, IOException> errors = new ConcurrentHashMap<>();
        recursiveDownload(Set.of(url), depth, excludes, visited, errors);
        return new Result(visited.entrySet().stream()
                .filter(entry -> entry.getValue().equals(true))
                .map(ConcurrentHashMap.Entry::getKey).toList(), errors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result download(
            String url,
            int depth
    ) {
        return download(url, depth, null);
    }

    /**
     * recursive download sites
     *
     * @param urlQueue url hash set to process
     * @param depth    Depth for recursive down going.
     * @param excludes url set for ignoring
     */
    private void recursiveDownload(
            Set<String> urlQueue,
            int depth,
            Set<String> excludes,
            final ConcurrentMap<String, Boolean> visited,
            final ConcurrentMap<String, IOException> errors
    ) {
        Set<String> extractedLinks = ConcurrentHashMap.newKeySet();
        Phaser phaser = new Phaser(1);
        for (String url : urlQueue) {
            visited.put(url, false);
            if (excludes != null && excludes.stream().anyMatch(url::contains)) {
                continue;
            }
            try {
                String host = URLUtils.getHost(url);
                phaser.register();
                submitDownloadTask(url, host, depth, phaser, extractedLinks, visited, errors);
            } catch (MalformedURLException e) {
                errors.put(url, e);
            }
        }
        phaser.arriveAndAwaitAdvance();
        if (depth > 1) {
            Set<String> urlQueueNext = extractedLinks.stream().filter(url -> !visited.containsKey(url)).collect(Collectors.toSet());
            recursiveDownload(urlQueueNext, depth - 1, excludes, visited, errors);
        }
    }

    /**
     * Activate downloading, manage per host.
     */
    private void submitDownloadTask(
            String url,
            String host,
            int depth,
            Phaser phaser,
            Set<String> extractedLinks,
            final ConcurrentMap<String, Boolean> visited,
            final ConcurrentMap<String, IOException> errors
    ) {
        downloadExecutor.submit(() -> {
            Document document = downloadDocument(url, host, errors);
            if (document != null) {
                processDocument(url, depth, phaser, document, extractedLinks, visited, errors);
            }
            phaser.arrive();
        });
    }

    /**
     * process document after download.
     *
     * @param url    Root url site.
     * @param host   host.
     * @param errors errors.
     */
    private Document downloadDocument(
            String url,
            String host,
            final ConcurrentMap<String, IOException> errors
    ) {
        Document document;
        Semaphore semaphore = hostLimiters.computeIfAbsent(host, k -> new Semaphore(perHost));
        try {
            semaphore.acquire();
            document = downloader.download(url);
        } catch (InterruptedException ignored) {
            return null;
        } catch (IOException e) {
            errors.put(url, e);
            return null;
        } finally {
            semaphore.release();
        }
        return document;
    }

    /**
     * process document after download.
     *
     * @param url            Root url site.
     * @param depth          Depth for recursive down going.
     * @param phaser         Phaser to manage threads.
     * @param extractedLinks Set for resulted links.
     */
    private void processDocument(
            String url,
            int depth,
            Phaser phaser,
            Document document,
            Set<String> extractedLinks,
            final ConcurrentMap<String, Boolean> visited,
            final ConcurrentMap<String, IOException> errors
    ) {
        visited.put(url, true);
        if (depth <= 1) {
            return;
        }
        phaser.register();

        linkExecutor.submit(() -> extractLinks(document, url, phaser, extractedLinks, errors));
    }

    /**
     * extract links.
     *
     * @param document       {@link Downloader} class. Downloader.
     * @param url            Root url site.
     * @param phaser         Phaser to manage threads.
     * @param extractedLinks Set for resulted links.
     */
    private void extractLinks(
            Document document,
            String url,
            Phaser phaser,
            Set<String> extractedLinks,
            final ConcurrentMap<String, IOException> errors
    ) {
        try {
            extractedLinks.addAll(document.extractLinks());
        } catch (IOException e) {
            errors.put(url, e);
        } finally {
            phaser.arrive();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        downloadExecutor.shutdownNow();
        linkExecutor.shutdownNow();
        try {
            if (!downloadExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Some download tasks did not terminate.");
            }

            if (!linkExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Some extract tasks did not terminate.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * parse command arguments with default value.
     *
     * @param args arg array.
     * @param ind  index of arg.
     * @param def  default value.
     */
    private static int parseParam(String[] args, int ind, int def) {
        if (args.length <= ind) {
            return def;
        }
        return Integer.parseInt(args[ind]);
    }

    /**
     * Creates a {@link WebCrawler} and runs.
     *
     * @param args the provided arguments.
     */
    public static void main(String[] args) {
        if (args == null || args.length < 1 || args.length > 5) {
            System.err.println("Usage: WebCrawler url [depth [downloads [extractors [perHost]]]]");
            return;
        }

        String url = args[0];
        if (url == null || url.isEmpty()) {
            System.err.println("Error: URL cannot be null or empty.");
            return;
        }

        try {
            int depth = parseParam(args, 1, 1);
            if (depth < 1) {
                System.err.println("Error: Depth must be at least 1.");
                return;
            }
            int downloaders = parseParam(args, 2, Runtime.getRuntime().availableProcessors() / 4);
            if (downloaders < 1) {
                System.err.println("Error: Number of downloaders must be at least 1.");
                return;
            }
            int extractors = parseParam(args, 3, Runtime.getRuntime().availableProcessors() / 2);
            if (extractors < 1) {
                System.err.println("Error: Number of extractors must be at least 1.");
                return;
            }
            int perHost = parseParam(args, 4, downloaders);
            if (perHost < 1) {
                System.err.println("Error: Per-host limit must be at least 1.");
                return;
            }

            try (Crawler crawler = new WebCrawler(new CachingDownloader(1.0, Paths.get(".")), downloaders, extractors, perHost)) {
                Result result = crawler.download(url, depth);
                System.out.println("Downloaded: " + result.getDownloaded());
                System.out.println("Errors: " + result.getErrors());
            }
        } catch (NumberFormatException | IOException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
        }
    }
}
