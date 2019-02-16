package com.bittech.everything.cmd;

import com.bittech.everything.core.EverythingMiniManager;
import com.bittech.everything.core.model.Condition;
import com.bittech.everything.core.model.Thing;

import java.util.List;
import java.util.Scanner;


/**
 * @Author: Eve
 * @Date: 2019/2/14 10:38
 * @Version 1.0
 */
public class EverythingMiniCmdApp {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        //欢迎
        welcome();
        //统一资源调度器
        EverythingMiniManager manager = EverythingMiniManager.getInstance();
        //启动后台清理线程
        manager.startBackgroundClearThread();
        //交互
        interaction(manager);

    }

    private static void interaction(EverythingMiniManager manager) {
        while (true) {
            System.out.println(">>");
            String input = scanner.nextLine();
            if (input.startsWith("search")) {
                String[] values = input.split(" ");
                if (values.length >= 2) {
                    if (!values[0].equals("search")) {
                        help();
                        continue;
                    }
                    Condition condition = new Condition();
                    condition.setName(values[1]);
                    if (values.length >= 3) {
                        condition.setFileType(values[2].toUpperCase());
                    }
                    search(manager, condition);
                    continue;
                } else {
                    help();
                    continue;
                }
            }
            switch (input) {
                case "help":
                    help();
                    break;
                case "index":
                    index(manager);
                    break;
                case "quit":
                    quit();
                    break;
                default:
                    help();

            }
        }
    }

    private static void index(EverythingMiniManager miniManager) {
        new Thread(miniManager::buildIndex).start();
    }

    private static void welcome() {
        System.out.println("欢迎使用...");
    }

    private static void quit() {
        System.out.println("再见");
        System.exit(0);
    }

    private static void help() {
        System.out.println(">>命令列表");
        System.out.println(">>帮助: help");
        System.out.println(">>退出: quit");
        System.out.println(">>索引: index");
        System.out.println(">>搜索: search <name> [<file_type> img|doc|bin|arch]");
    }

    private static void search(EverythingMiniManager manager, Condition condition) {
        List<Thing> list = manager.search(condition);
        for (Thing thing : list) {
            System.out.println(thing.getPath());
        }
    }
}
