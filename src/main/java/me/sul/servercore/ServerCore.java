package me.sul.servercore;

import me.sul.servercore.datasaveschedule.DataSaveCommand;
import me.sul.servercore.datasaveschedule.DataSaveScheduleEvent;
import me.sul.servercore.datasaveschedule.DataSaveScheduler;
import me.sul.servercore.freeze.FreezePlayer;
import me.sul.servercore.freeze.FreezedPlayerListener;
import me.sul.servercore.inventorymodeling.InventoryModeling;
import me.sul.servercore.kickallbeforeserverstop.KickAllBeforeServerStop;
import me.sul.servercore.playertoolchangeevent.InventoryItemListener;
import me.sul.servercore.serialnumber.UniqueIdAPI;
import me.sul.servercore.timeandweathermanager.TimeAndWeatherManager;
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
		registerDataSaveSchedule();
		registerFreeze();
		registerPlayerToolChangeEvent();
		registerSerialNumber();
		registerInventoryModeling();
		registerKickAllBeforeServerStop();
		registerTimeManager();
	}

	private void registerDataSaveSchedule() {
		Bukkit.getPluginManager().registerEvents(new DataSaveScheduler(), this);
		getCommand("서버저장").setExecutor(new DataSaveCommand());
	}
	private void registerFreeze() {
		Bukkit.getPluginManager().registerEvents(new FreezedPlayerListener(), this);
	}
	private void registerPlayerToolChangeEvent() {
		Bukkit.getPluginManager().registerEvents(new InventoryItemListener(), this);
	}
	private void registerSerialNumber() { new UniqueIdAPI(); }
	private void registerInventoryModeling() {
		Bukkit.getPluginManager().registerEvents(new InventoryModeling(), this);
	}
	private void registerKickAllBeforeServerStop() {
		Bukkit.getPluginManager().registerEvents(new KickAllBeforeServerStop(), this);
	}
	private void registerTimeManager() {
		new TimeAndWeatherManager();
	}

	// 이거 제대로 되는지 모르겠네.
	// onDisable() 실행되고 나서 event가 1틱 뒤에 받아질 수도 있음.
	// TODO) 이벤트가 제때 받아지는지 확인할 필요가 있음.
	// TODO) 이거 월드 저장도 해줘야되는데, 코드를 어디다가 넣어놨더라?
	@Override
	public void onDisable() {
		ServerCore.getInstance().getServer().getPluginManager().callEvent((Event)new DataSaveScheduleEvent(false));
		ServerCore.getInstance().getLogger().log(Level.INFO, "[서버 종료] 서버 데이터 저장");
	}


	public static ServerCore getInstance() {
		return instance;
	}
	public static FreezePlayer getFreezePlayer() {
		return freezePlayer;
	}
	
}
