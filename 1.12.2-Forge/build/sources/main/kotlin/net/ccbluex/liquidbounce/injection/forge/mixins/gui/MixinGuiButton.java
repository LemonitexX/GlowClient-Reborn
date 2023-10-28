/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import cn.utils.miku.render.round.RoundedUtil;
import cn.utils.render.VisualBase;
import net.ccbluex.liquidbounce.features.module.modules.render.UIEffects;
import net.ccbluex.liquidbounce.feng.FontDrawer;
import net.ccbluex.liquidbounce.feng.FontLoaders;
import net.ccbluex.liquidbounce.injection.backend.FontRendererImpl;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

import static net.ccbluex.liquidbounce.features.module.modules.render.UIEffects.Hudcolor;

@Mixin(GuiButton.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiButton extends Gui {

    @Shadow
    @Final
    protected static ResourceLocation BUTTON_TEXTURES;
    @Shadow
    public boolean visible;
    @Shadow
    public int x;
    @Shadow
    public int y;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    public boolean enabled;
    @Shadow
    public String displayString;
    @Shadow
    protected boolean hovered;
    private float cut;
    private float alpha;

    @Shadow
    protected abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);

    /**
     * @author CCBlueX
     */
    @Overwrite
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            final FontDrawer fontRenderer = FontLoaders.C16;
            hovered = (mouseX >= this.x && mouseY >= this.y &&
                    mouseX < this.x + this.width && mouseY < this.y + this.height);

            final int delta = RenderUtils.deltaTime;

            if (enabled && hovered) {
                cut += 0.05F * delta;

                if (cut >= 4) cut = 4;

                alpha += 0.3F * delta;

                if (alpha >= 210) alpha = 210;
            } else {
                cut -= 0.05F * delta;

                if (cut <= 0) cut = 0;

                alpha -= 0.3F * delta;

                if (alpha <= 120) alpha = 120;
            }
            if(UIEffects.hudblur.get()){
                GL11.glPushMatrix();
                RenderUtils.drawBlurRoundArea((float) this.x, (float) this.y,
                        (float) this.width,(float) this.height,6,UIEffects.hudblurradius.get());
                GL11.glPopMatrix();
            }
            switch (UIEffects.hudstyle.get().toLowerCase()){
                case "customcolor":
                case "customshader":
                case "customoutlined": {
                    UIEffects.render(this.x, this.y,this.width, this.height,12);
                    //if(UIEffects.hudblur.get()){
                    //    GL11.glPushMatrix();
                    //    RenderUtils.drawBlurRoundArea((float) this.x, (float) this.y,
                    //            (float) this.width,(float) this.height,6,UIEffects.hudblurradius.get());
                    //    GL11.glPopMatrix();
                    //}
                    break;
                }
                case "onetapv2":{
                    RoundedUtil.drawRound(this.x , this.y, this.width, this.height, 1, new Color(28, 30, 40));
                    RoundedUtil.drawRound(x - 0.5f, y - 1.7f, (float) this.width, 1.2f, 0.5f, new Color(Hudcolor.getValue()));
                    break;
                }
            }


            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            mouseDragged(mc, mouseX, mouseY);

            AWTFontRenderer.Companion.setAssumeNonVolatile(true);

            fontRenderer.drawStringWithShadow(displayString,
                    (float) ((this.x + this.width / 2) -
                            fontRenderer.getStringWidth(displayString) / 2),
                    this.y + (this.height - 5) / 2F - 1, Color.WHITE.getRGB());

            AWTFontRenderer.Companion.setAssumeNonVolatile(false);

            GlStateManager.resetColor();
        }
    }
}