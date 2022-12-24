package net.hsa12.gitpullplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

// 設定保存用
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

// シェル実行用
import java.lang.String;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class GitPullPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin Loaded!");
        // 設定クラス作成(default)
        saveDefaultConfig();
        // 設定クラス読み込み
        FileConfiguration conf = getConfig();
        // 設定データ作成
        //conf.set("world_name","world");
        //conf.set("pack_name","hscom");

        // Path取得
        RunShellScript run = new RunShellScript();
        String server_path;
        server_path = run.run("pwd");
        conf.set("server_path",server_path);

        // Packのパス作成
        conf.set("pack_path", conf.getString("server_path")+"/"+conf.getString("world_name")+"/datapacks/"+conf.getString("pack_name"));

        // 設定データ保存
        saveConfig();

        // 読み込み時画面表示
        getLogger().info("Datapack Path：" + conf.getString("pack_path"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin Disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        // test コマンドの処理
        if(cmd.getName().equalsIgnoreCase("pull")){
            // 取りあえずログ出力
            getLogger().warning("---Git pull start...");

            // 設定クラス読み込み
            FileConfiguration conf = getConfig();

            RunShellScript run = new RunShellScript();
            String output;
            output = "git -C "+conf.getString("pack_path")+" pull";
            getLogger().info(output);
            output = run.run(output);

            getLogger().warning(output);
            return true;
        }
        // 該当するコマンド無し
        return false;
    }
}

class RunShellScript {

    public String run(String commands) {

        try {
            // Run script
            Process process = Runtime.getRuntime().exec(commands);

            // Read output
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            return output.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Failed Shell.";
    }
}

