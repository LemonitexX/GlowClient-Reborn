package cn.clickgui.otcV2.Settings;

import cn.Client;
import cn.novoline.impl.Fonts;
import cn.clickgui.otcV2.Downward;
import cn.clickgui.otcV2.ModuleRender;
import cn.utils.miku.animations.Animation;
import cn.utils.miku.animations.Direction;
import cn.utils.miku.animations.impl.EaseInOutQuad;
import cn.utils.miku.render.RenderUtil;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.UIEffects;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class ListSetting extends Downward {
    private ListValue listValue;
    public ListSetting(ListValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
        this.listValue = s;
    }

    //动画
    private final Animation arrowAnimation = new EaseInOutQuad(250, 1, Direction.BACKWARDS);

    //获取主界面xy跟随移动
    private float modulex,moduley,listy;



    @Override
    public void draw(int mouseX, int mouseY) {
        modulex = Client.instance.otc.getMainx();
        moduley= Client.instance.otc.getMainy();

        //修复滚轮
        listy = pos.y + getScrollY();

        //Value名字显示
        Fonts.SFBOLD.SFBOLD_11.SFBOLD_11.drawString(listValue.getName(),modulex+ 5+ pos.x + 4 ,moduley+ 17 +listy + 13,new Color(200,200,200).getRGB());

        RenderUtil.drawRoundedRect(modulex+ 5+ pos.x + 80 ,moduley+ 17 +listy + 8,50,11f,1, new Color(59,63,72).getRGB(),1,new Color(85,90,96).getRGB());

        //所选
        if (isHovered(mouseX,mouseY)) {
            RenderUtil.drawRoundedRect(modulex + 5 + pos.x + 80, moduley + 17 + listy + 8, 50, 11f, 1, new Color(0, 0, 0, 0).getRGB(), 1, new Color(UIEffects.Hudcolor.get()).getRGB());
        }

        //List String 显示
        Fonts.SFBOLD.SFBOLD_11.SFBOLD_11.drawString(listValue.get() + "",modulex+ 5+ pos.x + 82 ,moduley+ 17 +listy + 13,new Color(200,200,200).getRGB());

        //绘制箭头
        arrowAnimation.setDirection(listValue.openList ? Direction.FORWARDS : Direction.BACKWARDS);
        RenderUtil.drawClickGuiArrow(modulex+ 5+ pos.x + 123.5f, moduley+ 17 +listy + 13, 4, arrowAnimation, new Color(222,224,236).getRGB());

        if (this.listValue.openList) {
            //循环添加Strings

            //覆盖下面的Value
            GL11.glTranslatef((float) 0.0f, (float) 0.0f, (float) 2.0f);
            RenderUtil.drawBorderedRect(modulex+ 5+ pos.x + 80 ,moduley+ 17 +listy + 8 + 13,modulex+ 5+ pos.x + 80 + 50f,moduley+ 17 +listy + 8 + 13 + listValue.getModes().size() * 11f,1f,new Color(85,90,96).getRGB(), new Color(59,63,72).getRGB());

            for (String option : listValue.getModes()) {
                Fonts.SFBOLD.SFBOLD_11.SFBOLD_11.drawString(option,modulex+ 5+ pos.x + 82,moduley+ 17 +listy + 1  + 13 + 12 + listValue.getModeListNumber(option)* 11 , option.equals(listValue.get()) ?new Color(UIEffects.Hudcolor.get()).getRGB():new Color(200,200,200).getRGB());
            }
            GL11.glTranslatef((float) 0.0f, (float) 0.0f, (float) -2.0f);
        }
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        //开关列表
        if (mouseButton == 1 && isHovered(mouseX,mouseY)){
            listValue.openList = !listValue.openList;
        }
        //设置mode
        if (mouseButton == 0) {
            if (this.listValue.openList //在这个x里面
                    && mouseX >= this.modulex+ 5+ pos.x + 80 // 最小x
                    && mouseX <= this.modulex+ 5+ pos.x + 80 +50 // 最大x
            ) {
                //循环判断点击
                for (int i = 0; i < listValue.getModes().size(); i++) {
                    final int v = (int) (moduley+ 17 +listy + 8 + 13 + i * 11);

                    if (mouseY >= v && mouseY <= v + 11) {
                        this.listValue.set(this.listValue.getModeGet(i));
                        this.listValue.openList = false;
                    }

                }
            }
        }
    }
    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >=modulex+ 5+ pos.x + 80 && mouseX <=modulex+ 5+ pos.x + 80 +50  && mouseY >= moduley+ 17 +listy + 8 && mouseY <= moduley+ 17 +listy + 8 +11 ;
    }


    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

}
