/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.uci.ics.crawler4j.examples.basic;

import edu.uci.ics.crawler4j.crawler.Settings;
import edu.uci.ics.crawler4j.util.FileManager;
import com.uwyn.jhighlight.tools.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.util.List;
import java.util.Scanner;

public class BasicCrawlController {

    private static final Logger logger = LoggerFactory.getLogger(BasicCrawlController.class);

    public static void main(String[] args) throws Exception {
        Settings settings = new Settings();
        String crawlStorageFolder = settings.getProperty("general_storage");
        String crawlTextStorage = settings.getProperty("text_storage");
        /*if (args.length != 2) {
         logger.info("Needed parameters: ");
         logger.info("\t rootFolder (it will contain intermediate crawl data)");
         logger.info("\t numberOfCralwers (number of concurrent threads)");
         return;
         }*/

        /*
         * crawlStorageFolder is a folder where intermediate crawl data is
         * stored.
         */
        String bufferInput;


        /*
         * numberOfCrawlers shows the number of concurrent threads that should
         * be initiated for crawling.
         */
        int numberOfCrawlers = 700;

        FileManager fileManager = new FileManager();
        fileManager.clearDirectory(crawlTextStorage);

        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder(crawlStorageFolder);

        config.setCrawlTextStorage(crawlTextStorage);

        /*
         * Be polite: Make sure that we don't send more than 1 request per
         * second (1000 milliseconds between requests).
         */
        config.setPolitenessDelay(1000);

        /*
         * You can set the maximum crawl depth here. The default value is -1 for
         * unlimited depth
         */
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the maximum crawl depth: ");
        bufferInput = input.nextLine().trim();
        config.setMaxDepthOfCrawling(Integer.parseInt(bufferInput));

        /*
         * You can set the maximum number of pages to crawl. The default value
         * is -1 for unlimited number of pages
         */
        config.setMaxPagesToFetch(-1);

        /**
         * Do you want crawler4j to crawl also binary data ? example: the
         * contents of pdf, or the metadata of images etc
         */
        config.setIncludeBinaryContentInCrawling(false);

        /*
         * Do you need to set a proxy? If so, you can use:
         * config.setProxyHost("proxyserver.example.com");
         * config.setProxyPort(8080);
         *
         * If your proxy also needs authentication:
         * config.setProxyUsername(username); config.getProxyPassword(password);
         */

        /*
         * This config parameter can be used to set your crawl to be resumable
         * (meaning that you can resume the crawl from a previously
         * interrupted/crashed crawl). Note: if you enable resuming feature and
         * want to start a fresh crawl, you need to delete the contents of
         * rootFolder manually.
         */
        config.setResumableCrawling(false);

        /*Config param that lets us set a max number of pages per crawl depth level.
         Default value is -1 for unlimited number of pages per depth level*/
        System.out.println("Enter the maximum number of pages per depth: ");
        bufferInput = input.nextLine().trim();
        config.setMaxPagesPerDepth(Integer.parseInt(bufferInput));

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */

        System.out.println("Enter a seed URL or path to file containing seed URLs: ");

        bufferInput = input.nextLine().trim();
        if (bufferInput.contains("http")) {
            controller.addSeed(bufferInput);
        } else {
            List<String> seedURLs = fileManager.extractURLs(bufferInput);

            for (String seedURL : seedURLs) {
                controller.addSeed(seedURL);
            }

        }

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(BasicCrawler.class, numberOfCrawlers);

    }

}
