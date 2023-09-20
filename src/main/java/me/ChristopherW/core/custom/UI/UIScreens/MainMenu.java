package me.ChristopherW.core.custom.UI.UIScreens;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiDataType;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import me.ChristopherW.core.custom.UI.GUIManager;
import me.ChristopherW.core.custom.UI.IGUIScreen;
import me.ChristopherW.core.utils.GlobalVariables;
import me.ChristopherW.test.Launcher;

public class MainMenu implements IGUIScreen{

    ImInt dalekCount = new ImInt(3);

    @Override
    public void start() {
    }

    @Override
    public void render(ImBoolean p_open, GUIManager gm) {
        ImGui.pushFont(gm.font);
        ImGui.setNextWindowSize(0, 0);
        ImGui.setNextWindowPos(Launcher.getWindow().getWidth()/2, Launcher.getWindow().getHeight()/2, 0, 0.5f,0.5f);
        if (ImGui.begin("Main Menu", p_open, ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoSavedSettings | ImGuiWindowFlags.NoFocusOnAppearing | ImGuiWindowFlags.NoNav | ImGuiWindowFlags.NoMove)) {
            ImVec2 windowSize = ImGui.getWindowSize();
            
            String title = "Dalek Catch Game";
            float textWidth = ImGui.calcTextSize(title).x;
            ImVec2 textPosition = new ImVec2((windowSize.x - textWidth) * 0.5f, windowSize.y * 0.5f);
            ImGui.setCursorPos(textPosition.x, ImGui.getCursorPosY());
            ImGui.text(title);
            ImGui.dummy(0, 50);
            ImGui.pushItemWidth(130);
            ImGui.popFont();
            ImGui.pushFont(gm.fontSmall);
            ImVec2 buttonPosition = new ImVec2((windowSize.x - 130) * 0.5f, ImGui.getCursorPosY());
            String label = "Dalek Count";
            float labelWidth = ImGui.calcTextSize(label).x;
            ImGui.setCursorPos(buttonPosition.x - labelWidth/2, buttonPosition.y);
            ImGui.alignTextToFramePadding();
            ImGui.text(label);
            ImGui.sameLine();
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 1.0f, 0, 0, 0.5f);
            ImGui.pushStyleColor(ImGuiCol.Button, 1.0f, 0, 0, 0.5f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 1.0f, 0, 0, 0.75f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 0, 0, 0.9f);
            ImGui.inputScalar(" ", ImGuiDataType.S32, dalekCount, 1,1);
            dalekCount.set(Math.min(Math.max(dalekCount.get(),1), 99));
            GlobalVariables.DALEK_COUNT = dalekCount.get();
            ImGui.popItemWidth();
            ImGui.dummy(0, 10);
            
            ImVec2 buttonSize = new ImVec2(200, 50);
            buttonPosition = new ImVec2((windowSize.x - buttonSize.x) * 0.5f, ImGui.getCursorPosY());

            ImGui.setCursorPos(buttonPosition.x, buttonPosition.y);
            if(ImGui.button("PLAY", buttonSize.x, buttonSize.y)) {
                gm.currentScreen = "";
                Launcher.getGame().generateGame();
            }
            ImGui.dummy(0, 10);
            buttonPosition = new ImVec2((windowSize.x - buttonSize.x) * 0.5f, ImGui.getCursorPosY());
            ImGui.setCursorPos(buttonPosition.x, buttonPosition.y);
            if(ImGui.button("EXIT", buttonSize.x, buttonSize.y)) {
                ImGui.popFont();
                ImGui.popStyleColor();
                ImGui.end();
                Launcher.getEngine().stop();
                return;
            }
        }
        ImGui.popFont();
        ImGui.popStyleColor(4);
        ImGui.end();
    }
    
}
