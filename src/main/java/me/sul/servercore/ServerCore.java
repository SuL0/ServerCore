package me.sul.servercore;

import me.sul.servercore.datasaveschedule.DataSaveCommand;
import me.sul.servercore.datasaveschedule.DataSaveScheduleEvent;
import me.sul.servercore.datasaveschedule.DataSaveScheduler;
import me.sul.servercore.freeze.FreezePlayer;
import me.sul.servercore.freeze.FreezedPlayerListener;
import me.sul.servercore.playertoolchangeevent.PlayerMainItemChangeListener;
import me.sul.servercore.serialnumber.SerialNumberAPI;
import me.sul.servercore.serialnumber.SerialNumberCounter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class ServerCore extends JavaPlugin implements Listener {
	private static ServerCore instance;
	private static FreezePlayer freezePlayer;
	
	@Override
	public void onEnable() {
		instance = this;
		freezePlayer = new FreezePlayer();
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("ptest").setExecutor(new TestCommand());
	}

	private void registerDataSaveSchedule() {
		Bukkit.getPluginManager().registerEvents(new DataSaveScheduler(), this);
		getCommand("��������").setExecutor(new DataSaveCommand());
	}
	private void registerFreeze() {
		Bukkit.getPluginManager().registerEvents(new FreezedPlayerListener(), this);
	}
	private void registerPlayerToolChangeEvent() {
		Bukkit.getPluginManager().registerEvents(new PlayerMainItemChangeListener(), this);
	}
	private void registerSerialNumber() {
		new SerialNumberAPI();
		Bukkit.getPluginManager().registerEvents(new SerialNumberCounter(), this);
	}

	// �̰� ����� �Ǵ��� �𸣰ڳ�.
	// onDisable() ����ǰ� ���� event�� 1ƽ �ڿ� �޾��� ���� ����.
	// TODO) �̺�Ʈ�� ���� �޾������� Ȯ���� �ʿ䰡 ����.
	// TODO) �̰� ���� ���嵵 ����ߵǴµ�, �ڵ带 ���ٰ� �־������?
	@Override
	public void onDisable() {
		ServerCore.getInstance().getServer().getPluginManager().callEvent((Event)new DataSaveScheduleEvent(false));
		ServerCore.getInstance().getLogger().log(Level.INFO, "[���� ����] ���� ������ ����");
	}


	public static ServerCore getInstance() {
		return instance;
	}
	public static FreezePlayer getFreezePlayer() {
		return freezePlayer;
	}
	
}
