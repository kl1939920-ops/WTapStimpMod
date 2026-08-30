package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = "wtapstimp", name = "W-Tap Stimp Style", version = "1.0.0")
public class ExampleMod {

    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean ejecutarTap = false;
    private int relojTicks = 0;
    private KeyBinding toggleKey;
    private boolean modActivado = true;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        toggleKey = new KeyBinding("Toggle W-Tap (Stimp)", Keyboard.KEY_R, "W-Tap Stimp");
        net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(toggleKey);
    }

    @SubscribeEvent
    public void alPresionarTecla(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && mc.thePlayer != null) {
            if (toggleKey.isPressed()) {
                modActivado = !modActivado;
                mc.thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText(
                        "§b[W-Tap Stimp] §eMod " + (modActivado ? "§aACTIVADO" : "§cDESACTIVADO")
                ));
            }
        }
    }

    @SubscribeEvent
    public void alGolpearEntidad(AttackEntityEvent event) {
        if (!modActivado || event.entityPlayer != mc.thePlayer || mc.thePlayer == null) return;
        if (mc.thePlayer.isSprinting()) {
            ejecutarTap = true;
            relojTicks = 0;
        }
    }

    @SubscribeEvent
    public void bucleMovimiento(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || mc.thePlayer == null || !ejecutarTap) return;

        relojTicks++;
        int forwardKey = mc.gameSettings.keyBindForward.getKeyCode();

        if (relojTicks == 1) {
            KeyBinding.setKeyBindState(forwardKey, false);
            mc.thePlayer.setSprinting(false);
        } 
        else if (relojTicks >= 2) { 
            if (Keyboard.isKeyDown(forwardKey)) {
                KeyBinding.setKeyBindState(forwardKey, true);
                mc.thePlayer.setSprinting(true);
            }
            ejecutarTap = false;
            relojTicks = 0;
        }
    }
}
