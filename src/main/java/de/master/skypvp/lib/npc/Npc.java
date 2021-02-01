package de.master.skypvp.lib.npc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.mysql.SqlStats;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

@Getter
@Setter
public class Npc implements Serializable {
    
    private int entityId;
    private Location location;
    private GameProfile gameProfile;
    private Float health = 20F;
    private String playerSkinName;
    
    public Npc(String name, Location location) {
        entityId = (int) Math.ceil(Math.random() * 1000) + 2000;
        gameProfile = new GameProfile(UUID.randomUUID(), name);
        this.location = location.clone();
        
        JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().npcList.put(entityId, this);
    }
    
    public Npc(String name, Integer entityID, UUID uuid, Location location) {
        this.entityId = entityID;
        gameProfile = new GameProfile(uuid, name);
        this.location = location.clone();
        
        JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().npcList.put(entityID, this);
    }
    
    public void setSkin(String name) {
        
        this.playerSkinName = name;
        JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getNpcConfiguration().setNpcSkin(this, playerSkinName);
    
        Gson gson = new Gson();
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        String json = getStringFromURL(url);
        String uuid = gson.fromJson(json, JsonObject.class).get("id").getAsString();
    
        url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false";
        json = getStringFromURL(url);
        JsonObject mainObject = gson.fromJson(json, JsonObject.class);
        JsonObject jObject = mainObject.get("properties").getAsJsonArray().get(0).getAsJsonObject();
        String value = jObject.get("value").getAsString();
        String signatur = jObject.get("signature").getAsString();
    
        gameProfile.getProperties().put("textures", new Property("textures", value, signatur));
        
    }
    
    public void spawn() {
        PacketPlayOutNamedEntitySpawn packet = packetPlayOutNamedEntitySpawn();
        sendPacket(packet);
        headRotation(location.getYaw(), location.getPitch());
        
        removeFromTablist();
    }
    
    public void spawn(Player player) {
        sendPacket(packetPlayOutNamedEntitySpawn(), player);
        headRotation(location.getYaw(), location.getPitch());
    }
    
    public void teleport(Location location) {
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
        setValue(packet, "a", entityId);
        setValue(packet, "b", getFixLocation(location.getX()));
        setValue(packet, "c", getFixLocation(location.getY()));
        setValue(packet, "d", getFixLocation(location.getZ()));
        setValue(packet, "e", getFixRotation(location.getYaw()));
        setValue(packet, "f", getFixRotation(location.getPitch()));
        
        sendPacket(packet);
        headRotation(location.getYaw(), location.getPitch());
        this.location = location.clone();
        
        JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getNpcConfiguration().addNpc(this);
    }
    
    public void headRotation(float yaw, float pitch) {
        PacketPlayOutEntityLook packet = new PacketPlayOutEntityLook(entityId, getFixRotation(yaw), getFixRotation(pitch), true);
        PacketPlayOutEntityHeadRotation packetHead = new PacketPlayOutEntityHeadRotation();
        setValue(packetHead, "a", entityId);
        setValue(packetHead, "b", getFixRotation(yaw));
        
        sendPacket(packet);
        sendPacket(packetHead);
        
        this.location.setYaw(yaw);
        this.location.setPitch(pitch);
    }
    
    public void headRotation(Location location) {
        headRotation(location.getYaw(), location.getPitch());
    }
    
    public void destroy() {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityId);
        removeFromTablist();
        sendPacket(packet);
        
        JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().npcList.remove(this);
    }
    
    public void destroy(Player player) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityId);
        removeFromTablist();
        sendPacket(packet, player);
    }
    
    public void addToTablist() {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameProfile, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameProfile.getName())[0]);
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);
        
        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        setValue(packet, "b", players);
        sendPacket(packet);
    }
    
    public void removeFromTablist() {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameProfile, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameProfile.getName())[0]);
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);
        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        setValue(packet, "b", players);
        
        sendPacket(packet);
    }
    
    private int getFixLocation(double pos) {
        return MathHelper.floor(pos * 32.0D);
    }
    
    private byte getFixRotation(float yawpitch) {
        return (byte) ((int) (yawpitch * 256.0F / 360.0F));
    }
    
    private void setValue(Object obj, String name, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
    
    private Object getValue(Object obj, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return null;
    }
    
    private void sendPacket(Packet<?> packet, Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
    
    private void sendPacket(Packet<?> packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendPacket(packet, player);
        }
    }
    
    private String getStringFromURL(String url) {
        String text = "";
        try {
            Scanner scanner = new Scanner(new URL(url).openStream());
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                while (line.startsWith(" ")) {
                    line = line.substring(1);
                }
                text = text + line;
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
    
    private PacketPlayOutNamedEntitySpawn packetPlayOutNamedEntitySpawn() {
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
        setValue(packet, "a", entityId);
        setValue(packet, "b", gameProfile.getId());
        setValue(packet, "c", getFixLocation(location.getX()));
        setValue(packet, "d", getFixLocation(location.getY()));
        setValue(packet, "e", getFixLocation(location.getZ()));
        setValue(packet, "f", getFixRotation(location.getYaw()));
        setValue(packet, "g", getFixRotation(location.getPitch()));
        setValue(packet, "h", 0);
        DataWatcher w = new DataWatcher(null);
        w.a(6, health);
        addToTablist();
        w.a(10, (byte) 127);
        setValue(packet, "i", w);
        
        return packet;
    }
    
}